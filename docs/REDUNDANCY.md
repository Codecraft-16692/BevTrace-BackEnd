# Redundancy of the reference (Qualitrack) and what BevTrace does instead

The structure, names and `shared` package are Qualitrack's. These are the repetitions of that project that were **not** carried over.

| Qualitrack | Where | BevTrace |
|---|---|---|
| Interface + `Impl` for every service (64 `*Impl`) | `application/internal/*`, adapters, hashing, tokens | concrete `@Service` classes. Interfaces only where there is a real port: `HashingService`, `TokenService`, the repository ports. 2 `Impl` left (`UserDetails*Impl`, Spring Security's own contract) |
| Domain object + persistence entity + **assembler** + Spring Data repo + port + adapter | 6 artifacts per aggregate, mapping written by hand in a separate class | same split (pure aggregate, persistence entity) but the mapping lives **only** in `fromDomain`/`toDomain` of the entity; no assembler classes, 0 converters (23 in Qualitrack: enums use `@Enumerated`) |
| One assembler class per resource/command (61 files in `transform`) | `interfaces/rest/transform` | one `<X>ResourceFromEntityAssembler` per resource and **one** `*CommandFromResourceAssembler` per controller |
| `try/catch` in every command handler (87 catches) | `*CommandServiceImpl` | `Results.attempt` converts exceptions once; handlers only express the use case |
| Validation repeated in resource, assembler, service and aggregate | | format in the resource (Bean Validation), rules in the aggregate, nothing re-checked in the service |
| Domain event -> internal handler -> integration event (42 handlers, 31 `*IntegrationEvent`) | `application/internal/eventhandlers`, `interfaces/events` | the domain event is the contract (`domain/model/events`); a listener only where another context reacts (4 handlers) |
| ACL = interface + `Impl` in two packages | `interfaces/acl` + `application/acl` | one concrete facade per context in `interfaces/acl` |
| Same read endpoints, queries and finders hand-written for each filter | | `FilterSpecifications` gives every collection `?field=value`, `_sort`, `_order`, `_limit` |
| Lombok annotation processor configured twice, Stripe SDK | `pom.xml` | one Lombok configuration, no payment SDK (payments are simulated by the card policy, as in the frontend) |

Measured on the sources (`src/main/java`): Qualitrack 707 files / 30.5k lines for 14 aggregates (~50 files per aggregate);
BevTrace 500 files / 17.4k lines for 30 aggregates (~17 per aggregate, including security, ACL, events and schedulers).

What stays on purpose: the pure-aggregate/persistence-entity separation, the command/query split, the port/adapter for repositories, `Result`/`ApplicationError`
in the application layer, and the ACL between contexts: they are the architecture the reference teaches; the removed items were repetition, not design.
