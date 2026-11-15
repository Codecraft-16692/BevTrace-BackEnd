# BevTrace API

Generated from the controllers by `tools/generate_docs.py`. Base path `/api/v1`. Errors share one shape: `{code, message, details}`; business-rule failures carry the i18n key of the rule in `details`.

## iam

| Method | Path | Access | Summary |
|---|---|---|---|
| POST | `/api/v1/authentication/tokens` | public | Sign in |
| POST | `/api/v1/authentication/registrations` | public | Register a user |
| GET | `/api/v1/roles` | public | List roles |
| GET | `/api/v1/roles/{id}` | public | Get a role by id |
| GET | `/api/v1/users` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | List users |
| GET | `/api/v1/users/{id}` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Get a user by id |
| POST | `/api/v1/users/{id}/permissions` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Grant a permission (role) to a user and enable the account |
| POST | `/api/v1/users/{id}/revocations` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Revoke the access of a user |

## inventory

| Method | Path | Access | Summary |
|---|---|---|---|
| GET | `/api/v1/discrepancies` | isAuthenticated() | List inventory discrepancies |
| GET | `/api/v1/discrepancies/{id}` | isAuthenticated() | Get a inventory discrepancy by id |
| POST | `/api/v1/discrepancies/{id}/resolutions` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Resolve a discrepancy |
| GET | `/api/v1/reconciliations` | isAuthenticated() | List inventory reconciliations |
| GET | `/api/v1/reconciliations/{id}` | isAuthenticated() | Get a inventory reconciliation by id |
| POST | `/api/v1/reconciliations` | isAuthenticated() | Run a physical reconciliation |
| POST | `/api/v1/reconciliations/{id}/confirmations` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Confirm a reconciliation |
| GET | `/api/v1/batches` | isAuthenticated() | List product batches |
| GET | `/api/v1/batches/{id}` | isAuthenticated() | Get a product batch by id |
| POST | `/api/v1/batches` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Register a batch announced by the plant |
| POST | `/api/v1/batches/receptions` | isAuthenticated() | Receive a batch by scanning its code |
| GET | `/api/v1/products` | isAuthenticated() | List products |
| GET | `/api/v1/products/{id}` | isAuthenticated() | Get a product by id |
| POST | `/api/v1/products` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Add a product to the catalogue |
| GET | `/api/v1/zones` | isAuthenticated() | List warehouse zones |
| GET | `/api/v1/zones/{id}` | isAuthenticated() | Get a warehouse zone by id |
| GET | `/api/v1/waste-records` | isAuthenticated() | List waste records |
| GET | `/api/v1/waste-records/{id}` | isAuthenticated() | Get a waste record by id |
| POST | `/api/v1/waste-records` | isAuthenticated() | Report waste |

## dispatch

| Method | Path | Access | Summary |
|---|---|---|---|
| GET | `/api/v1/cargo-assignments` | isAuthenticated() | List cargo assignments |
| GET | `/api/v1/cargo-assignments/{id}` | isAuthenticated() | Get a cargo assignment by id |
| GET | `/api/v1/destinations` | isAuthenticated() | List delivery destinations |
| GET | `/api/v1/destinations/{id}` | isAuthenticated() | Get a delivery destination by id |
| GET | `/api/v1/dispatch-orders` | isAuthenticated() | List dispatch orders |
| GET | `/api/v1/dispatch-orders/{id}` | isAuthenticated() | Get a dispatch order by id |
| POST | `/api/v1/dispatch-orders` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Schedule a dispatch |
| POST | `/api/v1/dispatch-orders/{id}/vehicle-assignments` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Assign a vehicle |
| POST | `/api/v1/dispatch-orders/{id}/cargo-validations` | ROLE_ADMIN, ROLE_WAREHOUSE_OPERATOR | Validate the scanned pallets |
| PATCH | `/api/v1/dispatch-orders/{id}/priorities` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Change the priority of a pending dispatch |
| POST | `/api/v1/dispatch-orders/{id}/departures` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Register the departure |
| POST | `/api/v1/dispatch-orders/{id}/cancellations` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Cancel a pending dispatch |
| GET | `/api/v1/drivers` | isAuthenticated() | List drivers |
| GET | `/api/v1/drivers/{id}` | isAuthenticated() | Get a driver by id |
| GET | `/api/v1/vehicles` | isAuthenticated() | List transport vehicles |
| GET | `/api/v1/vehicles/{id}` | isAuthenticated() | Get a transport vehicle by id |

## traceability

| Method | Path | Access | Summary |
|---|---|---|---|
| GET | `/api/v1/delivery-records` | isAuthenticated() | List delivery records |
| GET | `/api/v1/delivery-records/{id}` | isAuthenticated() | Get a delivery record by id |
| GET | `/api/v1/route-checkpoints` | isAuthenticated() | List route checkpoints |
| GET | `/api/v1/route-checkpoints/{id}` | isAuthenticated() | Get a route checkpoint by id |
| GET | `/api/v1/logs` | isAuthenticated() | List traceability logs |
| GET | `/api/v1/logs/{id}` | isAuthenticated() | Get a traceability log by id |
| POST | `/api/v1/logs/{id}/deliveries` | isAuthenticated() | Confirm the delivery |
| POST | `/api/v1/logs/{id}/rejections` | isAuthenticated() | Register a rejected delivery |

## telemetry

| Method | Path | Access | Summary |
|---|---|---|---|
| GET | `/api/v1/models` | isAuthenticated() | List device models |
| GET | `/api/v1/models/{id}` | isAuthenticated() | Get a device model by id |
| GET | `/api/v1/disconnection-periods` | isAuthenticated() | List disconnection periods |
| GET | `/api/v1/disconnection-periods/{id}` | isAuthenticated() | Get a disconnection period by id |
| GET | `/api/v1/location-streams` | isAuthenticated() | List location streams |
| GET | `/api/v1/location-streams/{id}` | isAuthenticated() | Get a location stream by id |
| POST | `/api/v1/location-streams` | ROLE_ADMIN | Ingest a device reading |
| GET | `/api/v1/devices` | isAuthenticated() | List telemetry devices |
| GET | `/api/v1/devices/{id}` | isAuthenticated() | Get a telemetry device by id |
| POST | `/api/v1/devices` | ROLE_ADMIN,ROLE_LOGISTICS_MANAGER | Provision a device in a vehicle |

## incident

| Method | Path | Access | Summary |
|---|---|---|---|
| GET | `/api/v1/rules` | isAuthenticated() | List alert rules |
| GET | `/api/v1/rules/{id}` | isAuthenticated() | Get a alert rule by id |
| POST | `/api/v1/rules` | ROLE_ADMIN,ROLE_LOGISTICS_MANAGER | Create an alert rule |
| PATCH | `/api/v1/rules/{id}/states` | ROLE_ADMIN,ROLE_LOGISTICS_MANAGER | Enable or disable a rule |
| GET | `/api/v1/corrective-actions` | isAuthenticated() | List corrective actions |
| GET | `/api/v1/corrective-actions/{id}` | isAuthenticated() | Get a corrective action by id |
| GET | `/api/v1/incidents` | isAuthenticated() | List incidents |
| GET | `/api/v1/incidents/{id}` | isAuthenticated() | Get an incident by id |
| POST | `/api/v1/incidents/detections` | ROLE_ADMIN,ROLE_LOGISTICS_MANAGER | Run the anomaly detection now |
| POST | `/api/v1/incidents/{id}/acknowledgments` | isAuthenticated() | Acknowledge an open incident |
| POST | `/api/v1/incidents/{id}/corrective-actions` | isAuthenticated() | Register a corrective action |
| POST | `/api/v1/incidents/{id}/resolutions` | isAuthenticated() | Resolve an incident |
| GET | `/api/v1/notifications` | isAuthenticated() | List notifications |
| GET | `/api/v1/notifications/{id}` | isAuthenticated() | Get a notification by id |
| PATCH | `/api/v1/notifications/{id}/read-receipts` | isAuthenticated() | Mark a notification as read |

## analytics

| Method | Path | Access | Summary |
|---|---|---|---|
| GET | `/api/v1/analytics/indicators` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Compute the indicators of a period |
| GET | `/api/v1/logistics-reports` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | List logistics reports |
| GET | `/api/v1/logistics-reports/{id}` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Get a logistics report by id |
| POST | `/api/v1/logistics-reports` | ROLE_ADMIN, ROLE_LOGISTICS_MANAGER | Generate and save a logistics report |

## subscription

| Method | Path | Access | Summary |
|---|---|---|---|
| GET | `/api/v1/payments` | isAuthenticated() | List payments |
| GET | `/api/v1/payments/{id}` | isAuthenticated() | Get a payment by id |
| GET | `/api/v1/subscription-plans` | public | List subscription plans |
| GET | `/api/v1/subscription-plans/{id}` | public | Get a subscription plan by id |
| GET | `/api/v1/subscriptions` | isAuthenticated() | List subscriptions |
| GET | `/api/v1/subscriptions/{id}` | isAuthenticated() | Get a subscription by id |
| POST | `/api/v1/subscriptions` | isAuthenticated() | Buy a plan |
| POST | `/api/v1/subscriptions/{id}/cancellations` | isAuthenticated() | Cancel a subscription |

