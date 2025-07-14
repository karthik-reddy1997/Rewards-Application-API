# Customer Rewards Points API

This Spring Boot application provides a RESTful API to calculate customer reward points based on their transaction history. It includes dynamic date-based filtering, per-customer breakdown, and monthly reward summaries.

---
# Problem Statement

For every transaction a customer makes:

- They receive:
    - **2 points** for every dollar spent **over $100**
    - **1 point** for every dollar spent **between $50 and $100**
- No points are awarded for spending **below $50**

### Example:
A purchase of **$120** will earn:
- `(2 × 20) + (1 × 50) = 90 points`

---

##  Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- H2 In-Memory Database

##  Project Structure

src
├── main
│   ├── java/com.example.rewards
│   │   ├── controller
│   │   ├── exception
│   │   ├── model
│   │   ├── repository
│   │   ├── service
│   │   └── RewardsApplication.java
│   └── resources
│       ├── application.properties
│       └── data.sql
├── test/
│   └── java/com.example.rewards
│       ├── controller
│       └── service

---

##  How to Run
mvn spring-boot:run

### Prerequisites
- Java 17+
- Maven 3.6+

### Access H2 Console (for testing)
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:rewarddb

### Running Tests
- ./mvnw test

###  API Endpoints

### Build the App
```bash
mvn clean install

By default the app runs on: http://localhost:8080

GET /api/rewards?customerId=1&startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
Parameters:

customerId: Customer ID (e.g., 1)

start: Start date (e.g., 2025-05-01)

end: End date (e.g., 2025-05-31)

Returns

    {
        "customerId": 1,
        "customerName": "John Doe",
        "startDate": "2025-05-01",
        "endDate": "2025-05-31",
        "monthlyPoints": {
            "MAY": 115
        },
        "totalPoints": 115
    }

