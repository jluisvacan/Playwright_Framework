# 🧪 Playwright Java E2E Test Automation Framework

[![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Playwright](https://img.shields.io/badge/Playwright_Java-1.62.0-green?logo=playwright)](https://playwright.dev/java/)
![Maven](https://img.shields.io/badge/Build-Maven-blue?style=flat-square&logo=apachemaven)
![QA Automation](https://img.shields.io/badge/Role-QA%20Automation%20%2F%20SDET-red?style=flat-square)

An Enterprise End-to-End Test Automation Framework developed with **Java 17** and **Playwright Java (v1.62.0)**. Built adhering to OOP design principles and software engineering best practices, including the **Page Object Model (POM)** design pattern, thread-safe parallel execution, and automated test lifecycle management.

---

## 🌟 Key Features

- **Page Object Model (POM):** Clean decoupling of page components, locators, and test specifications.
- **Java 17 Core:** Leverages modern Java features for clean, expressive code.
- **Cross-Browser & Multi-Context:** Native support for Chromium, Firefox, and WebKit (Safari) contexts.
- **Thread-Safe Parallel Execution:** Efficient test suite runs powered by JUnit 5 / TestNG integration.
- **Tracing & Artifact Screenshots:** Automatic video, screenshot, and trace captures upon test failure for seamless root cause analysis.

---

## 📁 Standard Project Structure

```text
Playwright_Framework/
├── src/
│   └── test/
│       ├── java/                     # Test suite classes, assertions, and test runners
│       └── resources/                # Configuration files, properties, and test datasets
├── .gitignore                        # Git ignore rules for compiled binaries and reports
├── pom.xml                           # Maven configuration file (Dependencies, plugins, Java version)
└── README.md                         # Project documentation
```

---

## 🚀 Getting Started
### Prerequisites
- Ensure you have the following installed locally:
- JDK 17 (Configured in JAVA_HOME)
- Apache Maven 3.8+
- Git

### Installation
### 1. Clone the repository:

```Bash
git clone https://github.com/jluisvacan/Playwright_Framework.git
cd Playwright_Framework
```
### 2. Install project dependencies:


```Bash
mvn clean install -DskipTests
```

## 🏃 Executing Tests
Run your test suites directly using Maven wrapper or CLI:

* Run all automated tests:
```Bash
mvn test
```

* Run tests in Headed mode (Visual execution):
```Bash
mvn test -Dheadless=false
```

* Run tests targeted for a specific browser:
```Bash
mvn test -Dbrowser=chromium
```

* Generate reports using Allure Reports, 
**this must be run after the tests** 
```Bash
allure serve target/allure-results
```

## 🛠️ Tech Stack & Dependencies
- Programming Language: Java 17
- Automation Core: Playwright Java 1.42.0 
- Build & Dependency Manager: Apache Maven