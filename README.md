# 🧠 Sales Backend Application
The Sales Backend Application is a comprehensive Java-based project designed to manage sales operations, including product management, dashboard analytics, and secure authentication using JSON Web Tokens (JWT). This application is built on top of the Spring Boot framework, leveraging its simplicity and flexibility to create a robust and scalable backend system. The core features of this application include product CRUD operations, dashboard analytics, and secure authentication, making it an ideal solution for managing sales data and providing insights to drive business decisions.

## 🚀 Features
- **Product Management**: Create, read, update, and delete (CRUD) products with ease, using the `ProductController` and `ProductService` classes.
- **Dashboard Analytics**: Get insights into sales data and performance metrics through the `DashboardController` and `DashboardService` classes.
- **Secure Authentication**: Authenticate users securely using JSON Web Tokens (JWT) with the `JwtAuthFilter` class.
- **Database Management**: Manage database connections and perform CRUD operations using the `DatabaseConnection` and `DBConfig` classes.
- **Error Handling**: Handle errors and exceptions gracefully, providing informative error messages and ensuring a smooth user experience.

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

## 📸 Screenshots


## 🤝 Contributing
To contribute to the Sales Backend Application, please follow these steps:
1. Fork the repository: `git fork https://github.com/your-repo/sales-backend.git`
2. Create a new branch: `git branch feature/your-feature`
3. Commit your changes: `git commit -m "Your commit message"`
4. Push your changes: `git push origin feature/your-feature`
5. Create a pull request: `git pull-request`



## 📬 Contact
For any questions or concerns, please contact us at (01030761946).

