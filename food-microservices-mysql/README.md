# 🍔 Food Ordering System - Microservices

## 📌 Project Overview
This project is a **Microservices-based Food Ordering System** built with **Spring Boot (Java 17)** and **MySQL**.  
It demonstrates how to build an end-to-end distributed system with **independent services**, **database-per-service**, and basic **orchestration** for order flows.

---

## 👨‍🎓 Student Info
- **Name:** Tariq Mahmoud Almadhoon  
- **ID:** 120210624  
- **Supervisor:** Dr. Abd Al Kareem Alashqar  
- **Submission Date:** 3 Oct 2025  

---

## 🔗 Required Links
- **GitHub Repo:** [Food Ordering Microservices](https://github.com/Tariqalmadhoon/foodprogrammin-microservice.git)  
- **Project Video:** *To be added (Google Drive or YouTube link)*  

---

## 🏗️ Services & Ports
| Service              | Port  | Responsibility                                |
|-----------------------|-------|-----------------------------------------------|
| Order Service         | 8081  | Orchestrates order flow, stores orders        |
| Payment Service       | 8082  | Authorizes/Rejects payments                   |
| Inventory Service     | 8083  | Seeds & reserves stock                        |
| Notification Service  | 8084  | Records/sends notifications                   |
| Delivery Service      | 8085  | Schedules deliveries                          |
| Restaurant Service    | 8086  | Provides menu items and prices                |

---

## 🗄️ Database-per-Service
Each service has its **own database**. Example:

- `order_service_db.orders` → order details, status  
- `payment_service_db.payments` → payment records  
- `inventory_service_db.inventory_items` → available stock  
- `delivery_service_db.deliveries` → delivery schedule  
- `notification_service_db.notifications` → notifications log  
- `restaurant_service_db.menu_items` → menu & prices  

---

## 🔌 API Endpoints

### Order Service (`http://localhost:8081`)
- `POST /api/orders` → Create new order  
- `GET /api/orders/{id}` → Get order by ID  

### Payment Service (`http://localhost:8082`)
- `POST /api/payments` → Create payment record  

### Inventory Service (`http://localhost:8083`)
- `POST /api/inventory/item` → Add stock  
- `POST /api/inventory/reserve` → Reserve stock  

### Delivery Service (`http://localhost:8085`)
- `POST /api/deliveries` → Schedule delivery  

### Notification Service (`http://localhost:8084`)
- `POST /api/notifications` → Store/send notification  

### Restaurant Service (`http://localhost:8086`)
- `GET /api/menu/{id}` → Get menu item  
- `POST /api/menu` → Create menu item  

---

## ▶️ Local Setup & Runbook
1. Start **MySQL** and create databases for all services.  
2. Configure `application.properties` for each service (DB URL, username, password, port).  
3. Run services in order:  
   **Payment → Inventory → Delivery → Notification → Restaurant → Order**  
4. Use **Postman** to test endpoints:  
   - Seed menu items  
   - Seed inventory  
   - Create orders and check results  

---

## 🐳 Dockerization
An example **Dockerfile** is provided for **notification-service**:

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests clean package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app/app.jar"]
