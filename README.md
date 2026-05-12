# Mandoob Backend (Sales Management)

Backend system for managing **mandoob (sales representatives)** operations: authentication, clients, products, invoices, payments, cash requests, and stock tracking.

## Features

- **Admin authentication** (JWT)
- Manage **Clients** (linked to mandoob)
- Manage **Products** (cost, sell price, stock)
- **Invoices** and invoice items
- **Payments** tied to invoices
- **Cash requests** (pending/approved/rejected)
- **Mandoob stock** (add/reduce quantities)
- **Dashboard stats**

## Tech Stack

- Java 17
- Spring Boot 4.0.5
- Spring Data JPA + MySQL
- JWT: `io.jsonwebtoken` (JJWT)
- Lombok

## Prerequisites

- Java 17
- MySQL database

## Project Setup

### 1) Configure MySQL
Update your Spring configuration (typically `application.properties` / `application-*.properties`) with your DB settings and server port.

> This repository expects a MySQL database. Connection details are usually under `spring.datasource.*`.

### 2) Configure JWT
JWT is used for authentication/authorization.

> The JWT secret/token configuration is expected via application properties (commonly a `secret` / `jwt.secret`-like value).

## Database Schema

The SQL schema is provided in:

- [`sql1.md`](./sql1.md)

Tables include:

- `users` (admin + mandoob)
- `clients`
- `products`
- `invoices` + `invoice_items`
- `payments`
- `cash_requests`
- `mandoob_stock`

## Run the Application

### Option A: Maven (recommended)

```bash
./mvnw spring-boot:run
```

### Option B: Package + run

```bash
./mvnw clean package
java -jar target/*.jar
```

## API Base URL

By default:

- `http://localhost:8080`

## Authentication

### Admin login

- **POST** `/auth/login`

Expected body: `User` (model in the project).

Response: `ApiResponse` containing the token (JWT).

### Verify token (if enabled)

- **GET** `/auth/verify-token`

## Endpoints (by module)

### Products

- **GET** `/products`
- **GET** `/products/{id}`
- **GET** `/products/count`
- **GET** `/products/low-stock`
- **POST** `/products`
- **PUT** `/products/{id}`
- **DELETE** `/products/{id}`

### Clients

Note: controller mapping in code uses `clients`.

- **GET** `/clients`
- **GET** `/clients/mandoob/{mandoobId}`
- **GET** `/clients/{id}`
- **POST** `/clients`
- **DELETE** `/clients/{id}`

### Mandoobs

- **POST** `/mandoobs/create`
- **GET** `/mandoobs`
- **GET** `/mandoobs/{id}`
- **POST** `/mandoobs/{id}/toggle-status`

### Invoices

- **GET** `/invoices`
- **GET** `/invoices/{id}`
- **GET** `/invoices/client/{clientId}`
- **GET** `/invoices/mandoob/{mandoobId}`
- **GET** `/invoices/{id}/items`
- **POST** `/invoices`
- **DELETE** `/invoices/{id}`

### Payments

- **GET** `/payments`
- **GET** `/payments/invoice/{invoiceId}`
- **POST** `/payments`

### Cash Requests

- **GET** `/cash-requests`
- **GET** `/cash-requests/pending`
- **GET** `/cash-requests/mandoob/{mandoobId}`
- **POST** `/cash-requests/create`
- **PUT** `/cash-requests/{id}/approve`
- **PUT** `/cash-requests/{id}/reject`

### Mandoob Stock

- **GET** `/mandoob-stock`
- **GET** `/mandoob-stock/mandoob/{mandoobId}`
- **POST** `/mandoob-stock/add`
- **POST** `/mandoob-stock/reduce`

### Dashboard

- **GET** `/dashboard/stats`

## Responses

All controllers use a shared wrapper:

- `ApiResponse`

## Notes

- The repository uses the Java package naming `com.Mahmoud.sales_backend` (instead of `com.Mahmoud.sales-backend`, which is invalid in Java).

## License

Add your project license here (if applicable).

