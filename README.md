# ShopVerify

![Browser tests](https://github.com/khushicheepinapi199/ShopVerify/actions/workflows/tests.yml/badge.svg)

Java UI automation for [Sauce Demo](https://www.saucedemo.com/), a practice shopping application. This learning portfolio tests authentication, catalog sorting, cart operations, checkout validation and order completion.

## Run

Install JDK 17+, Maven 3.9+ and Google Chrome. Open this folder as a Maven project in IntelliJ and reload dependencies. Selenium Manager downloads a matching driver on first run; internet access is required.

```sh
mvn test
mvn test -Dheadless=false
mvn test -Dtest=LoginTest
mvn test -Dgroups=smoke
```

Tests use public demo credentials and synthetic checkout details. Each test gets a fresh browser session. The default is headless Chrome.

## Framework

Page objects contain elements and reusable interactions; tests contain assertions. Explicit waits replace fixed sleeps. JUnit parameterized cases cover login validation, sorting and required checkout fields. BigDecimal avoids floating-point money comparisons. Failure screenshots and URL/error diagnostics are captured before browser cleanup. GitHub Actions executes tests on pushes and pull requests and uploads reports.

## Coverage

23 cases: 8 authentication, 5 catalog, 4 cart and 6 checkout. See [test plan](docs/test-plan.md). Subtotal is compared with selected catalog prices; final total must equal subtotal plus displayed tax. This does not independently verify tax policy or real payment processing. The demo has no editable quantities.

## Results

Results appear under `target/surefire-reports`; failure screenshots under `target/failure-evidence`. On GitHub, open Actions, select a run and download the report artifact. The badge shows CI status; code presence alone does not establish passing tests. No retries hide failures.

## Troubleshooting and interview preparation

Check site access and Chrome/driver installation for setup failures. For assertion failures inspect the report, screenshot and URL, then reproduce visibly. Learn to explain one test end to end, why sessions are isolated, how waits work and how failures are classified. Run and modify the suite before describing it as hands-on experience.
