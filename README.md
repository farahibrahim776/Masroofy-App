# Masroofy App 💸

[![Language: Java](https://img.shields.io/badge/Language-Java%20100%25-orange?logo=openjdk)](https://www.java.com/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17.0.6-2f6db0?logo=java)](https://openjfx.io/)
[![Build: Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![Database: SQLite](https://img.shields.io/badge/Database-SQLite-003B57?logo=sqlite)](https://www.sqlite.org/)
[![Status: Active Development](https://img.shields.io/badge/Status-Active%20Development-yellow)](https://github.com/farahibrahim776/Masroofy-App)

**Masroofy App** is a Java desktop personal-finance application for recording expenses, organizing spending by category, managing budget cycles, and reviewing financial activity through dashboard and statistics views. The application is designed to provide a focused, local-first experience for users who want practical visibility into their day-to-day spending.

> **Note:** The repository currently does not publish benchmark results, test-coverage percentages, release versions, or a formal open-source license. This README documents the implementation details that are verifiable from the current codebase and avoids presenting unverified performance claims.

---

## Overview

Masroofy App addresses a common personal-finance problem: keeping expense records structured enough to support meaningful budgeting decisions without requiring a remote service or complex infrastructure. It combines a JavaFX graphical interface with a local SQLite database, allowing users to enter and review financial data from a desktop application.

The codebase is organized into dedicated model, controller, service, navigation, utility, and view packages. This separation supports maintainability by keeping user-interface concerns, application workflows, persistence, and domain data conceptually distinct.

## Key Features & Metrics

### Core features

- **Expense management** — Add and manage expenses associated with budget cycles and categories.
- **Budget-cycle organization** — Represent and manage budgeting periods for structured financial tracking.
- **Dashboard and statistics** — Present summaries and financial insights through dedicated dashboard and statistics flows.
- **Expense history** — Review previously recorded expenses through the history interface.
- **PIN-based access flow** — Includes PIN registration and login controllers for a lightweight local authentication experience.
- **Local persistence** — Stores application data in SQLite through the JDBC driver and database helper components.
- **Settings and setup screens** — Provides dedicated UI flows for initial setup and application settings.

### Current engineering metrics

| Metric | Current value |
|---|---:|
| Primary language | Java — 100% of reported repository languages |
| Java target/source level | Java 17 |
| JavaFX version | 17.0.6 |
| Persistence engine | SQLite via `sqlite-jdbc` 3.45.1.0 |
| Test framework | JUnit Jupiter 5.10.2 |
| Published latency/concurrency benchmark | Not available in the repository |
| Published test-coverage percentage | Not available in the repository |
| Published model-accuracy metric | Not applicable to the current application scope |

Performance claims such as latency, throughput, concurrent-user capacity, and coverage should be added only after reproducible benchmarks or CI reports are introduced.

## System Architecture

Masroofy App uses a modular, layered desktop architecture built around JavaFX and SQLite:

```text
┌──────────────────────────────┐
│ JavaFX Views / FXML Screens  │
│ Setup · Dashboard · History  │
│ New Expense · Settings       │
└──────────────┬───────────────┘
               │ user actions / screen navigation
┌──────────────▼───────────────┐
│ Controllers                  │
│ UI event handling, auth,     │
│ budgets, statistics, alerts  │
└──────────────┬───────────────┘
               │ application workflows
┌──────────────▼───────────────┐
│ Services & Navigation        │
│ ExpenseManager,              │
│ DashboardManager, SceneManager│
└──────────────┬───────────────┘
               │ domain operations
┌──────────────▼───────────────┐
│ Models & Persistence         │
│ Expense, Category,           │
│ BudgetCycle, DatabaseHelper  │
└──────────────┬───────────────┘
               │ JDBC
┌──────────────▼───────────────┐
│ Local SQLite Database        │
│ masroofy.db                  │
└──────────────────────────────┘
```

### Design principles

- **Separation of concerns:** Views, controllers, services, models, and persistence utilities are maintained in separate packages.
- **Layered responsibilities:** Controllers coordinate UI events, service classes encapsulate application operations, and model/database classes represent and persist domain data.
- **Modular Java:** The project defines the `com.example.masroofy_app` module and explicitly declares JavaFX and SQL dependencies in `module-info.java`.
- **Local-first data access:** SQLite provides a self-contained persistence layer without requiring a separately deployed database server.
- **Reusable navigation:** Screen transitions are centralized through the navigation package and `SceneManager` flow.

## Tech Stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| User interface | JavaFX Controls 17.0.6 and JavaFX FXML 17.0.6 |
| Build tool | Apache Maven with Maven Wrapper support |
| Persistence | SQLite and Xerial SQLite JDBC 3.45.1.0 |
| Testing | JUnit Jupiter API and Engine 5.10.2 |
| Documentation | Javadoc generated from the Java source packages |
| Application type | Modular Java desktop application |

## Project Structure

```text
Masroofy-App/
├── docs/                         # Generated Javadoc documentation
├── src/
│   └── main/
│       ├── java/
│       │   ├── module-info.java
│       │   └── com/example/masroofy_app/
│       │       ├── Controller/   # UI and workflow controllers
│       │       ├── model/        # Domain models and database helpers
│       │       ├── navigation/   # Scene and screen navigation
│       │       ├── service/      # Application services
│       │       ├── utils/        # Shared utility classes
│       │       └── view/         # JavaFX view controllers
│       └── resources/
│           └── view/             # FXML and UI resources
├── masroofy.db                   # Local SQLite database file
├── pom.xml                       # Maven project configuration
├── mvnw                          # Maven Wrapper for Unix-like systems
├── mvnw.cmd                      # Maven Wrapper for Windows
└── README.md
```

## Getting Started

### Prerequisites

Install the following before building the project:

- **JDK 17** or a compatible Java 17 distribution
- **Git**
- A JavaFX-compatible desktop environment
- No separate SQLite server is required; the application uses a local SQLite database

Maven is optional because the repository includes the Maven Wrapper.

### Clone the repository

```bash
git clone https://github.com/farahibrahim776/Masroofy-App.git
cd Masroofy-App
```

### Build the project

On macOS/Linux:

```bash
./mvnw clean package
```

On Windows:

```powershell
mvnw.cmd clean package
```

If Maven is installed globally, the equivalent command is:

```bash
mvn clean package
```

### Run the application

The Maven configuration provides a JavaFX run goal:

On macOS/Linux:

```bash
./mvnw clean javafx:run
```

On Windows:

```powershell
mvnw.cmd clean javafx:run
```

Using an IDE such as IntelliJ IDEA is also supported. Import the project as a Maven project, ensure the IDE uses **JDK 17**, and run the configured JavaFX application entry point.

### Environment variables

The current repository does not require environment variables for its default local setup. Database access is configured through the application code and the local `masroofy.db` SQLite file.

For production-grade deployments, consider moving database paths and other runtime settings into external configuration rather than hard-coding them in source code.

## Usage

After launching the application:

1. Complete the initial setup flow.
2. Register a local PIN, then use the PIN login flow when prompted.
3. Create or select a budget cycle.
4. Add expenses and assign categories.
5. Review the dashboard, statistics, and history screens to understand spending activity.
6. Adjust application preferences through the settings screen.

### Common development commands

```bash
# Compile the source code
./mvnw compile

# Run tests
./mvnw test

# Create a clean build artifact
./mvnw clean package

# Launch the JavaFX desktop application
./mvnw clean javafx:run
```

### Documentation

Generated Javadoc is available in the [`docs/`](./docs) directory. Open [`docs/index.html`](./docs/index.html) in a browser to browse the documented packages and classes.

## Contributing

Contributions are welcome and should follow the existing project conventions.

1. Fork the repository.
2. Create a focused branch:

   ```bash
   git checkout -b feature/short-description
   ```

3. Make your changes and add or update tests where appropriate.
4. Verify the project builds and tests pass:

   ```bash
   ./mvnw clean test
   ```

5. Commit with a clear message:

   ```bash
   git commit -m "Add short description of change"
   ```

6. Push the branch and open a pull request.

Pull requests should include a concise description of the problem, the implementation approach, testing performed, and any UI screenshots or migration notes relevant to reviewers.

## License

No license file is currently included in this repository. Until an explicit license is added, the project should be treated as **all rights reserved**. If the maintainers intend to distribute the project as open source, add a `LICENSE` file and update this section accordingly—for example, with the MIT License.

## Author and Support

- **Repository owner:** [farahibrahim776](https://github.com/farahibrahim776)
- **Issues and feature requests:** [Open an issue](https://github.com/farahibrahim776/Masroofy-App/issues)

---

**Masroofy App** — a focused JavaFX desktop companion for more organized personal expense tracking.
