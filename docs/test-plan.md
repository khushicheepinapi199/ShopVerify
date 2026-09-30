# Test plan

| Area | Cases | Expected behavior |
|---|---|---|
| Login | Valid; wrong username; wrong password; locked user; username missing; password missing; both missing; logout | Products page or specific error; return to login on logout |
| Catalog | Name ascending/descending; price ascending/descending; details | Correct order, matching detail name/price, nonempty description |
| Cart | Add two; remove in cart; remove in catalog; navigate and refresh | Exact products, correct badge count, preserved selections |
| Checkout | First name/last name/postal code missing; full journey; cancel information; cancel overview | Block invalid checkout; matching subtotal and total arithmetic; confirmation; preserved cart on cancel |

Scope: Chrome desktop and standard demo user. No cross-browser, API, payment, load-testing or accessibility claims.

## Defect triage

Read expected and actual values, inspect screenshots and current URL, reproduce with `mvn test -Dtest=ClassName#methodName -Dheadless=false`, and distinguish environment, locator/wait, data and application failures. Record reproduction steps and evidence for confirmed defects. No application defects are claimed before reproduction.
