# Connecting the Angular frontend to this backend

The read endpoints keep the **same paths, field names and enum values as `server/db.json`**, so the stores that only read data work by changing the base URL.
What changes is every *write*: the mock server let the client change any row, the backend exposes **domain commands** and applies the business rules itself.

## 1. Environment

```ts
// src/environments/environment.ts
serverBasePath: 'http://localhost:8080/api/v1',
```

CORS allows `http://localhost:4200` by default (`CORS_ORIGINS` to change it).

## 2. Authentication (new)

| Frontend today | Backend |
|---|---|
| `GET /users?email=...&_expand=role` and compare the password in the browser | `POST /authentication/sign-in` `{ email, password }` -> `{ id, name, email, roles, token }` (same shape as `SignInResource`). wrong credentials = 400 with `details: iam.errors.invalid-credentials`; disabled account = 409 with `details: iam.errors.account-disabled` |
| `POST /users` (sign-up) | `POST /authentication/sign-up` `{ roleId, name, email, phone, password }` -> `201 { id, name, email }`. Administrators cannot self-register |
| fake token `btoa(...)` | the JWT. Add an `HttpInterceptorFn` that sets `Authorization: Bearer <token>` on every request to `serverBasePath` and signs the user out on 401 |
| passwords inside `GET /users` | never returned |

## 3. Errors

Errors use the shared (Qualitrack) shape `{ code, message, details }`, localized by `Accept-Language` (`en`/`es`):

| HTTP | `code` | Meaning |
|---|---|---|
| 400 | `VALIDATION_ERROR` | invalid body/argument; `details` explains it |
| 404 | `<ENTITY>_NOT_FOUND` | e.g. `PRODUCTBATCH_NOT_FOUND` |
| 409 | `<ENTITY>_CONFLICT` | duplicate or state conflict; `details` = i18n key (e.g. `inventory.errors.batch-duplicated`) |
| 422 | `BUSINESS_RULE_VIOLATION` | a domain rule failed; **`details` = the frontend i18n key** (e.g. `dispatch.errors.capacity-exceeded`) |
| 401 / 403 | `UNAUTHORIZED` / `FORBIDDEN` | missing token / wrong role |

```ts
const body = error.error as { code?: string; details?: string };
return throwError(() => new BusinessError(body.details ?? fallbackKey));
```

Interpolation parameters (`{available}`, ...) are no longer sent: the message of the rule is already in `details`/`message`.

## 4. Writes: from many calls to one command

| Store action (today) | Backend command | Rules now enforced by the server |
|---|---|---|
| `InventoryStore.registerBatch` | `POST /batches` | duplicate code (409 `inventory.errors.batch-duplicated` in `details`), code format |
| `receiveBatch` | `POST /batches/receptions` `{scanCode}` | `invalid-code`, `already-received` |
| `registerWaste` | `POST /waste-records` `{batchId, quantity, reason}` | stock deduction is done by the server (do not `PATCH` the batch) |
| `runReconciliation` | `POST /reconciliations` `{counts:[{batchId,countedQty}]}` | creates discrepancies and notifies managers |
| `confirmReconciliation` | `POST /reconciliations/{id}/confirmation` | `pending-discrepancies` |
| `resolveDiscrepancy` | `POST /discrepancies/{id}/resolution` `{note}` | adjusts the batch stock |
| `DispatchStore.scheduleDispatch` | `POST /dispatch-orders` `{destinationId, scheduledDate, priority, items:[{batchId,quantity}]}` | past date, free stock, weight/pallets (server computes them) |
| `assignVehicle` | `POST /dispatch-orders/{id}/vehicle-assignment` `{vehicleId}` | vehicle/driver/capacity; reserves the vehicle; notifies operators |
| `validateCargo` | `POST /dispatch-orders/{id}/cargo-validation` `{scannedPallets}` | exact pallet match |
| `changePriority` | `PATCH /dispatch-orders/{id}/priority` `{priority}` | only SCHEDULED/AUTHORIZED |
| `registerDeparture` | `POST /dispatch-orders/{id}/departure` | deducts stock **and starts the traceability** server-side. Delete `startRouteTraceability` and the batch `PATCH`es |
| `cancelOrder` | `POST /dispatch-orders/{id}/cancellation` | releases the vehicle |
| `completeDelivery` | *(removed)* | done when traceability confirms the delivery |
| `TraceabilityStore.finalizeDelivery` | `POST /logs/{id}/delivery` `{receivedBy}` | checkpoint, record, log, order and vehicle are updated atomically |
| `rejectDelivery` | `POST /logs/{id}/rejection` `{reason}` | keeps the shipment open |
| `TelemetryStore.provisionDevice` | `POST /devices` `{vehicleId, modelId, deviceCode}` | duplicate code / vehicle already has a device |
| `IncidentStore.detectAnomalies` | `POST /incidents/detections` -> `{created, reopened}` | rule evaluation, dedupe and 24 h reopen window |
| `acknowledge` | `POST /incidents/{id}/acknowledgement` | user taken from the token |
| `registerCorrectiveAction` | `POST /incidents/{id}/corrective-actions` `{description}` | |
| `resolveIncident` | `POST /incidents/{id}/resolution` | needs an action |
| `createRule` / `setRuleActive` | `POST /rules` / `PATCH /rules/{id}/active` `{active}` | |
| `notify(...)`, `markNotificationRead` | *(remove `notify`)* / `PATCH /notifications/{id}/read` | notifications are created by the server from events |
| `AnalyticsStore.generateReport` | `POST /logistics-reports` `{periodStart, periodEnd}` | KPIs computed on the server. `GET /analytics/indicators?periodStart&periodEnd` returns the same indicators for the dashboard |
| `SubscriptionStore.subscribeToPlan` | `POST /subscriptions` `{companyName, planCode, billingCycle, cardHolder, cardNumber}` | Luhn, declined test card -> 422 (`details: subscription.errors.payment-declined`) (a FAILED payment is recorded) |
| `cancelSubscription` | `POST /subscriptions/{id}/cancellation` | owner or admin |
| `subscribeNewsletter`, `submitContactRequest` | `POST /newsletter-subscribers`, `POST /contact-requests` | public |
| `IamStore.assignRole` / `setUserActive` | `PATCH /users/{id}/role` `{roleId}` / `PATCH /users/{id}/active` `{active}` | admin only, not yourself |

Keep the client-side validations as *UX hints*; the server is the authority. Every `userId`/`managerId` that the client used to send comes from the token now.

## 5. Real data instead of simulations

Position, checkpoints, connectivity and temperature are produced by the platform, not typed by users. In a demo without hardware:

```bash
# an administrator token, then one reading of device 1 (moves the shipment it follows)
curl -X POST localhost:8080/api/v1/location-streams -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"deviceId":1,"latitude":-12.06,"longitude":-76.99,"speed":35,"temperature":8.5}'
```

With `bevtrace.telemetry.monitor.enabled=true` the server marks silent devices as DISCONNECTED and opens a `disconnection-period`; with
`bevtrace.incident.detection.enabled=true` the anomaly detection runs periodically (the button still works).

## 6. Small differences to expect

* `payments` has an extra `userId` field (used for row-level visibility).
* Non-admins only see their own `subscriptions`/`payments` and the `notifications` of their role.
* Every row also carries no `row_version`; audit columns (`created_at`, `updated_at`) are stored but only `createdAt` is exposed where the mock had it.
* `GET /users` and the other admin collections return 403 to other roles.
* `?_expand=` and `?_embed=` are ignored (the frontend already resolves relations by itself).
