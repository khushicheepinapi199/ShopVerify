package io.shopverify.tests;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
class CartTest extends BaseTest {
 @Test @Tag("smoke") void addMultipleProducts() { var p=login.login(); p.add("sauce-labs-backpack"); p.add("sauce-labs-bike-light"); assertEquals(2,p.count()); assertEquals(java.util.Set.of("Sauce Labs Backpack","Sauce Labs Bike Light"),java.util.Set.copyOf(p.cart().names())); }
 @Test void removeFromCart() { var p=login.login(); p.add("sauce-labs-backpack"); var c=p.cart(); c.remove("sauce-labs-backpack"); assertTrue(c.names().isEmpty()); assertEquals(0,c.count()); }
 @Test void removeFromProducts() { var p=login.login(); p.add("sauce-labs-backpack"); p.remove("sauce-labs-backpack"); assertEquals(0,p.count()); assertTrue(p.cart().names().isEmpty()); }
 @Test void cartSurvivesNavigationAndRefresh() { var p=login.login(); p.add("sauce-labs-backpack"); p=p.cart().continueShopping(); driver.navigate().refresh(); assertEquals(1,p.count()); assertEquals(java.util.List.of("Sauce Labs Backpack"),p.cart().names()); }
}