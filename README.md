# 🚚 Sales Backend — Mr. Mandoob

**Sales Management System** | Spring Boot 3.2 | MySQL | JWT Authentication


---

## 👨‍💻 Contributors

- **Shady Esmat** – Backend Development
- **Mahmoud Essam** – Backend Development
- **Seif Fawzi** – Backend Development
- **Mohamed Ramzi** – Backend Development

---
## ✨ Features

| Module | Status |
|--------|--------|
| 🔐 JWT Auth (Login/Register) | ✅ |
| 📦 Products (CRUD + Stock Management) | ✅ |
| 👥 Client Management | ✅ |
| 🧾 Invoices with Auto Profit Calculation | ✅ |
| 💰 Payment Processing | ✅ |
| 📦 Mandoob (Distributor) Stock Distribution | ✅ |
| 💵 Cash Requests (Approve/Reject Workflow) | ✅ |
| 📊 Role-Based Access Control (RBAC) Dashboard | ✅ |

---

# 🧠 Sales Backend Application
The Sales Backend Application is a comprehensive Java-based project designed to manage sales operations, including product management, dashboard analytics, and secure authentication using JSON Web Tokens (JWT). This application is built on top of the Spring Boot framework, leveraging its simplicity and flexibility to create a robust and scalable backend system. The core features of this application include product CRUD operations, dashboard analytics, and secure authentication, making it an ideal solution for managing sales data and providing insights to drive business decisions.
## 📡 API Endpoints

### 🔐 Authentication (Public)
| Method | Endpoint | Body |
|--------|----------|------|
| `POST` | `/auth/register` | `{name, email, password, role}` |
| `POST` | `/auth/login` | `{email, password}` |

**Response:** JWT Token for subsequent requests

---

### 📊 Dashboard
| Method | Endpoint | Auth Required |
|--------|----------|---|
| `GET` | `/dashboard/stats` | ✅ Bearer Token |

---

### 📦 Products Management
| Method | Endpoint | Auth Required |
|--------|----------|---|
| `GET` | `/products` | ✅ Bearer Token |
| `GET` | `/products/{id}` | ✅ Bearer Token |
| `POST` | `/products` | ✅ Bearer Token |
| `PUT` | `/products/{id}` | ✅ Bearer Token |
| `DELETE` | `/products/{id}` | ✅ Bearer Token |

---

### 👥 Client Management
| Method | Endpoint | Auth Required |
|--------|----------|---|
| `GET` | `/clients` | ✅ Bearer Token |
| `GET` | `/clients/{id}` | ✅ Bearer Token |
| `POST` | `/clients` | ✅ Bearer Token |
| `PUT` | `/clients/{id}` | ✅ Bearer Token |
| `DELETE` | `/clients/{id}` | ✅ Bearer Token |

---

### 🧾 Invoices
| Method | Endpoint | Auth Required |
|--------|----------|---|
| `GET` | `/invoices` | ✅ Bearer Token |
| `GET` | `/invoices/{id}` | ✅ Bearer Token |
| `POST` | `/invoices` | ✅ Bearer Token |
| `PUT` | `/invoices/{id}` | ✅ Bearer Token |

---

### 💰 Payments
| Method | Endpoint | Parameters | Auth Required |
|--------|----------|-----------|---|
| `POST` | `/payments` | `invoiceId`, `amount` | ✅ Bearer Token |
| `GET` | `/payments/invoice/{invoiceId}` | `invoiceId` | ✅ Bearer Token |

---

### 📦 Mandoob Stock Distribution
| Method | Endpoint | Parameters | Auth Required |
|--------|----------|-----------|---|
| `GET` | `/mandoob-stock` | — | ✅ Bearer Token |
| `GET` | `/mandoob-stock/{mandoobId}` | `mandoobId` | ✅ Bearer Token |
| `POST` | `/mandoob-stock/distribute` | `mandoobId`, `productId`, `qty` | ✅ Bearer Token |

---

### 💵 Cash Requests
| Method | Endpoint | Parameters | Auth Required |
|--------|----------|-----------|---|
| `GET` | `/cash-requests` | — | ✅ Bearer Token |
| `POST` | `/cash-requests` | Request body | ✅ Bearer Token |
| `PUT` | `/cash-requests/{id}/approve` | `id` | ✅ Bearer Token |
| `PUT` | `/cash-requests/{id}/reject` | `id`, `reason` | ✅ Bearer Token |

---

## 🛠️ Tech Stack
- **Backend Framework**: Spring Boot
- **Database**: MySQL
- **Authentication**: JSON Web Tokens (JWT)
- **Build Tool**: Maven
- **Dependencies**:
  - `spring-boot-starter-data-jpa`
  - `lombok`
  - `spring-boot-starter-webmvc`
  - `jjwt-api`
  - `mysql-connector-j`
  - `spring-security-crypto`

## 📦 Installation
To install the Sales Backend Application, follow these steps:
1. Clone the repository using Git: `git clone https://github.com/your-repo/sales-backend.git`
2. Navigate to the project directory: `cd sales-backend`
3. Build the project using Maven: `mvn clean install`
4. Run the application using Spring Boot: `mvn spring-boot:run`

## 💻 Usage
To use the Sales Backend Application, follow these steps:
1. Start the application: `mvn spring-boot:run`
2. Use a tool like Postman to send HTTP requests to the application's endpoints:
   - `http://localhost:8080/products` for product management
   - `http://localhost:8080/dashboard` for dashboard analytics
   - `http://localhost:8080/auth` for authentication

## 📂 Project Structure
```markdown
sales-backend
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   ├── sales
│   │   │   │   │   ├── SalesBackendApplication.java
│   │   │   │   │   ├── controller
│   │   │   │   │   │   ├── ProductController.java
│   │   │   │   │   │   ├── DashboardController.java
│   │   │   │   │   ├── service
│   │   │   │   │   │   ├── ProductService.java
│   │   │   │   │   │   ├── DashboardService.java
│   │   │   │   │   ├── repository
│   │   │   │   │   │   ├── ProductRepository.java
│   │   │   │   │   │   ├── DashboardRepository.java
│   │   │   │   │   ├── config
│   │   │   │   │   │   ├── DBConfig.java
│   │   │   │   │   ├── security
│   │   │   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   │   ├── util
│   │   │   │   │   │   ├── DatabaseConnection.java
│   │   ├── resources
│   │   │   ├── application.properties
│   ├── test
│   │   ├── java
│   │   │   ├── com
│   │   │   │   ├── sales
│   │   │   │   │   ├── SalesBackendApplicationTests.java
```

## 📝 License

This project is provided as-is for educational and business use.

---

## 🤝 Support & Contributions

For issues, questions, or feature requests, please open an [Issue](https://github.com/shadyshados651-bot/Final-sales-backend/issues) on GitHub.

---

**Last Updated:** May 10, 2026
