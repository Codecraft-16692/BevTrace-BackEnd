# Architecture

## Bounded contexts

BevTrace's own contexts (see `diagrams/context-map.svg`, generated from the imports):

| Context | Owns | Talks to |
|---|---|---|
| `iam` | users, roles, JWT, authorization | queried by every context through `IamContextFacade` |
| `inventory` | products, zones, batches, waste, reconciliations, discrepancies | publishes `DiscrepanciesDetected` |
| `dispatch` | orders, cargo, vehicles, drivers, destinations | uses `InventoryContextFacade`; publishes `DispatchAuthorized`, `DispatchDeparted`; reacts to `DeliveryConfirmed` |
| `traceability` | route logs, checkpoints, delivery records | uses `TelemetryContextFacade`; reacts to `DispatchDeparted`, `ReadingRecorded`; publishes `DeliveryConfirmed` |
| `telemetry` | devices, models, location stream, disconnections | uses `DispatchContextFacade`; publishes `ReadingRecorded` |
| `incident` | alert rules, incidents, corrective actions, notifications | uses telemetry/traceability facades; reacts to dispatch, inventory and traceability events |
| `analytics` | logistics reports, KPIs | uses dispatch/inventory facades |
| `subscription` | plans, subscriptions, payments, newsletter, contact | self-contained |

Rule: another context may only be reached through its `interfaces.acl.*ContextFacade` or its `domain.model.events.*`.
Domain events are the integration contract as well (no separate integration-event classes).

## Layout of a context (Qualitrack's)

```
domain/model/{aggregates,entities,valueobjects,commands,queries,events}   domain/repositories (ports)   domain/services (stateless policies)
application/commandservices   application/queryservices   application/internal/{eventhandlers,outboundservices,schedulers}
infrastructure/persistence/jpa/{entities,repositories,adapters}
interfaces/acl   interfaces/rest   interfaces/rest/resources   interfaces/rest/transform
```

Per aggregate: the **aggregate** (pure, extends `AbstractDomainAggregateRoot`, owns the rules, `restore(...)` rebuilds it), its
**`<X>PersistenceEntity`** (extends `AuditableAbstractPersistenceEntity`, owns `fromDomain`/`toDomain`: the *only* mapping point), a Spring Data
**`<X>JpaRepository`**, the **port** `<X>Repository` and its **adapter** (maps and publishes the aggregate's domain events after `save`), the two
queries and the **query service**, the **resource** with its **assembler** and the controller.

## Flow of a command (dispatch departure)

`POST /dispatch-orders/{id}/departure` -> `DispatchOrdersController` (validates the body resource, builds a `RegisterDepartureCommand` with the
assembler) -> `DispatchOrderCommandService.handle` (one transaction, `Results.attempt`) -> `DispatchOrder.depart()` checks the state machine and
registers `DispatchDeparted` -> adapter saves and publishes -> `traceability` handler starts the route in the same transaction -> the controller turns
the `Result` into a response with `ResponseEntityAssembler`.

## Errors

Single source: `shared` (`Result`, `ApplicationError`, `ErrorResponseAssembler`, `GlobalExceptionHandler`). Aggregates throw
`IllegalArgumentException` (invalid value, 400) or `IllegalStateException(<i18n key>)` (rule broken in the current state, 422);
`Results.attempt` converts them once. Not found -> `<ENTITY>_NOT_FOUND` 404, duplicate/state conflict -> `<ENTITY>_CONFLICT` 409.
Messages (English / Spanish) come from `shared/infrastructure/i18n/ErrorMessages`.

## Security

Stateless JWT (HS256, jjwt) in `iam/infrastructure/authorization/sfs`: `BearerAuthorizationRequestFilter` loads the user by e-mail on every request
(so a disabled account stops working immediately), role = the single authority (`ROLE_ADMIN`, `ROLE_LOGISTICS_MANAGER`, `ROLE_WAREHOUSE_OPERATOR`).
Public: sign-in/up, role and plan catalogs, the two public forms, Swagger, health. Everything else: token + `@PreAuthorize` per endpoint;
owner scoping (subscriptions, payments) and role scoping (notifications) are applied in the controllers. `ForbiddenRequestAdvice` makes `@PreAuthorize`
rejections answer 403 (the shared handler would report them as 500).

## What differs from Qualitrack's `shared`

Everything is Qualitrack's file, only the package/name changed (`OpenApiConfiguration` also drops Qualitrack's documentation link), plus two **new**
generic files, none of the original ones was edited:

* `shared/application/result/Results.java`: converts domain exceptions into a `Result` once (Qualitrack repeats a `try/catch` in each handler: 87 catches).
* `shared/infrastructure/persistence/jpa/specifications/FilterSpecifications.java`: the `?field=value&_sort&_order&_limit` filters of the collection endpoints.

## Known limits

* Built and tested only with `javac` against hand-written stubs and domain unit tests; **the Maven build and Spring context start-up have not been run** (no access to Maven Central here). Run `mvn test` first.
* The naming strategy pluralizes table names (`pluralize` library); tables are already declared in plural (`@Table(name = "product_batches")`) and the library is expected to keep them, but this was not executed. If a table name changes, set it in `@Table` and in `DemoSeedScript`.
* Optimistic locking (`@Version`) of the previous version was dropped, like in Qualitrack; concurrent stock changes rely on transactions.
* Lombok is needed only by `AuditableAbstractPersistenceEntity` (Qualitrack's file).
