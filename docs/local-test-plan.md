# Local app test plan

| Layer | Cases |
|---|---|
| Backend and security (11) | Hash and authenticate; duplicate rejection without overwrite; invalid username/password/confirmation; CSRF enforcement; protected route; incorrect password; quantity limits; server-side checkout totals and cart clear; checkout validation; order ownership; logout invalidation |
| Browser (10) | Register/logout/relogin; incorrect password; duplicate username; mismatched confirmation; all four sorting modes and correct loaded images; quantities/tax/totals/remove/refresh; checkout/history; cancel; account session isolation; product details |
| Restart process check | Register on a file DB; stop JVM; restart JVM; login with the same credentials |

External Sauce Demo tests remain documented in `test-plan.md`. A passing suite verifies only these assertions, not every possible behavior. No real payments are exercised.

Local tests create unique usernames and isolated databases. The restart check deletes its temporary DB afterward. Browser failures capture screenshots before driver cleanup. Investigate setup failures separately from assertion failures. A product image check verifies expected asset URL, matching alt text and successful loading; it is not pixel-level visual comparison.
