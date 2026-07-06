# CSD214 S26 Lab 5 - Pure Repositories & Data Abstraction

This project is part of the CSD214 Programming Concepts II course (Summer 2026). It demonstrates the Repository Pattern, Dependency Injection, polymorphism, and multiple storage implementations while keeping the presentation layer independent from the data layer.

## Features

- Repository Pattern using a generic `IRepository<T>` interface
- CRUD operations
- Dependency Injection
- Multiple storage implementations
- MySQL persistence using Hibernate/JPA
- H2 in-memory database
- ArrayList in-memory repository
- HashMap in-memory repository
- Polymorphic entity hierarchy
- Inventory management
- Sales system
- Java Faker database seeding

## Repository Implementations

### InMemoryListRepository

Stores data inside an `ArrayList`.

- Sequential search
- O(n) lookup
- Temporary storage
- Auto-generated IDs

### InMemoryMapRepository

Stores data inside a `HashMap`.

- Indexed lookup
- Average O(1) lookup
- Temporary storage
- Auto-generated IDs

### H2Repository

Uses an H2 in-memory SQL database.

- Hibernate/JPA
- Data removed when application closes
- Used for testing

### MySqlRepository

Uses a MySQL database.

- Hibernate/JPA
- Persistent storage
- Transactions
- CRUD operations

## Technologies

- Java 25
- Maven
- Hibernate ORM 6.6.1
- Jakarta Persistence
- MySQL
- H2 Database
- JUnit 5
- Java Faker



## Running the Project

Compile the project

```bash
mvn clean compile
```

Run the application

```bash
mvn exec:java -Dexec.mainClass="bookstore.Main"
```

or run `Main.java` directly from IntelliJ.

When the application starts, choose the storage engine:

```
1. Volatile RAM (ArrayList - Sequential)
2. Volatile RAM (HashMap - Indexed)
3. Integration Testing (H2 In-Memory SQL)
4. Production (MySQL Database)
```

## Lab 5 Reflection

### The Power of the Interface

The application uses the `IRepository` interface so the `App` class does not depend on a specific repository implementation. The repository is selected only in `Main.java`, allowing the same application to work with different storage implementations without changing the application logic. This makes the code easier to maintain and extend.

### Algorithmic Complexity

`InMemoryListRepository` stores data in an `ArrayList`. Searching requires checking each item one by one, giving it a time complexity of **O(n)**.

`InMemoryMapRepository` stores data in a `HashMap`. Looking up an item by its key uses hashing and has an average time complexity of **O(1)**. This provides much better performance as the amount of data increases.

### Volatility vs Persistence

The in-memory repositories store data only while the application is running. All data is lost when the application closes.

The H2 repository also stores data temporarily but uses a real SQL database engine.

The MySQL repository stores data permanently and is suitable for production environments because the data remains available after restarting the application.

### Dependency Injection

The `App` class receives an `IRepository<ProductEntity>` through its constructor instead of creating a repository itself. `Main.java` is responsible for creating the repository and passing it to the application. This makes the application easier to test and allows different storage implementations to be used without modifying the application code.