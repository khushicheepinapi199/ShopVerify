# ShopVerify

[![Tests](https://github.com/khushicheepinapi199/ShopVerify/actions/workflows/tests.yml/badge.svg)](https://github.com/khushicheepinapi199/ShopVerify/actions/workflows/tests.yml)

A Java shopping application with user registration and test automation. Create your own credentials, shop a six-product catalog, adjust quantities, and place simulated orders. Accounts and order totals survive restarts using a local H2 database. The original Sauce Demo automation is retained separately in `io.shopverify.tests`.

## Start the shopping app

Install JDK 17 and Maven 3.9+. Clone this repository, open its `pom.xml` in IntelliJ, and run `ShopVerifyApplication`, or use:

```sh
mvn spring-boot:run
```

Open **http://localhost:8080**. Select Create an account, register your own username/password, then log in. There are no pre-created customer accounts. Username: 3–32 lowercase letters, digits or underscores. Password: 8–64 characters, at most 72 UTF-8 bytes. Passwords are hashed with BCrypt; registration never stores the plaintext password. Logout invalidates the session. After restarting, log in again with the same credentials. Keep `data/` to keep your accounts; do not commit it.

## Docker

```sh
docker compose up --build
```

The named volume preserves the database between container restarts. `docker compose down` keeps it; `docker compose down -v` deletes it. Docker builds skip tests; run the test suite separately.

## Tests

Install Google Chrome; Selenium Manager resolves the driver. Internet access is required for initial dependency/driver downloads and the optional external demo suite.

```sh
mvn verify
mvn test -Dtest=ShopIntegrationTest,LocalShopBrowserTest
mvn test -Dtest=LocalShopBrowserTest -Dheadless=false
mvn test -Dtest=LoginTest,ProductsTest,CartTest,CheckoutTest
mvn package -DskipTests
python3 scripts/check_persistence.py
```

44 JUnit cases: 11 backend/security tests, 10 local browser tests, and 23 external Sauce Demo cases. Local browser tests launch the application on a random port against an isolated in-memory test database. They never change the user's saved accounts. The restart check launches the packaged app twice against a temporary file database and verifies the original login.

Reports: `target/surefire-reports`. Failure screenshots: `target/failure-evidence`. GitHub Actions builds the jar, runs the suites and restart check, then uploads evidence. See [test plan](docs/local-test-plan.md).

## Features and architecture

Spring MVC controllers render Thymeleaf pages; Spring Security handles authentication, CSRF tokens and sessions. JPA repositories store BCrypt hashes and per-user order totals in H2. Catalog prices are server-controlled; cart quantities are restricted to 1–10. Demo tax is 8%, rounded half up to cents. Product illustrations are local SVGs associated with product IDs, including after sorting. Carts last only for the current login session; account records and order totals persist. Order pages enforce ownership.

Checkout is simulated and collects no payment details. Use fictional delivery details: they are validated but not saved. Saved orders contain ID, owner, total and timestamp, not item snapshots. This local learning app has no email verification, password reset, inventory system or payment gateway; deployment beyond a local demo would require additional controls. Uploading this repository to GitHub does not host a running website.

## Learning guide

Start with `AccountService` for validation/hashing, `SecurityConfig` for login/logout, `ShopController` for cart and checkout rules, and the test classes for assertions. Explain expected vs actual results, CSRF rejection, data isolation, server-side price calculation, image associations and failure triage before presenting the project in an interview.
