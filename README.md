# WorldBank Banking System

Modernised JavaFX banking application backed by SQLite and a layered OOP design.

## Features
- Unified login for admins and customers (credentials stored as salted SHA-256 hashes).
- Customers can own multiple accounts (Savings, Investment, Cheque) with enforced business rules:
  - Savings: deposits + interest only (0.05% monthly).
  - Investment: deposits, withdrawals, minimum opening balance of 500 BWP, 5% monthly interest.
  - Cheque: deposits + withdrawals, available only to employed customers with employer details.
- Service-layer orchestration for account, customer, auth, and audit flows.
- DAO layer isolates SQLite persistence; foreign keys and constraints protect integrity.
- Full audit logging of high-level operations and transaction history for every movement.

## Getting Started
```
mvn clean javafx:run
```
The database is auto-initialised in `banking.db` with a default admin user:
- Username: `admin`
- Password: `admin123`

## Running Tests
```
mvn test
```
Unit tests cover each account type, withdrawal and interest interfaces, plus an integration test that exercises the full SQLite-backed service stack via an in-memory database.

## Project Structure
- `com.banking.controller` – JavaFX controllers (Auth, Dashboard, Admin, Customer, reusable account/transaction panels).
- `com.banking.service` – Business logic (AuthService, AccountService, CustomerService, AuditService).
- `com.banking.dao` – SQLite DAOs (users, customers, accounts, transactions, audit log).
- `com.banking.model` – Domain entities, interfaces, and account hierarchy (BigDecimal-based).
- `src/main/resources/com/banking/view` – JavaFX views (single login, dashboards, reusable tables).
- `src/test/java` – Unit + integration tests.

## Development Notes
- Override the DB location (e.g., for tests) via `-Dbanking.db.url=jdbc:sqlite::memory:`.
- Use Maven + Java 21; JavaFX dependencies supplied via `pom.xml`.

