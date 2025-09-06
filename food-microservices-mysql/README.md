# Food Ordering – Microservices (MySQL + Postman)

Two Spring Boot services wired to **MySQL** (XAMPP-friendly) with a ready **Postman** setup.

- **order-service** (8081) → DB: `order_service_db`
- **payment-service** (8082) → DB: `payment_service_db`
- JDK 17, Spring Boot 3.3.2, REST communication.

## Prepare MySQL (XAMPP)
Create DBs in phpMyAdmin:
```sql
CREATE DATABASE IF NOT EXISTS order_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS payment_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
> Default uses `root` with no password. Change in each `application.yml` if needed.

## Run
1) Start **payment-service** (8082), then **order-service** (8081).
2) Import Postman files: `FoodOrdering.postman_collection.json` and `Local-Microservices.postman_environment.json`.

## Test (PowerShell quick)
```powershell
$ORD = "http://localhost:8081"
$body = '{
  "userId":"u-123","restaurantId":"r-88",
  "items":[{"menuItemId":"m-1","quantity":2}],
  "paymentMethod":"CARD"
}'
Invoke-RestMethod -Uri "$ORD/api/orders" -Method POST -ContentType "application/json" -Body $body
Invoke-RestMethod -Uri "$ORD/api/orders/o-1" -Method GET
```
