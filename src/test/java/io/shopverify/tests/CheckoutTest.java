package io.shopverify.tests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;
class CheckoutTest extends BaseTest {
 @ParameterizedTest @CsvSource(value={"''|Tester|12345|First Name is required","Test|''|12345|Last Name is required","Test|Tester|''|Postal Code is required"},delimiter='|')
 void requiredCustomerFields(String first,String last,String zip,String error) { var p=login.login(); p.add("sauce-labs-backpack"); var c=p.cart().checkout(); c.details(first,last,zip); assertTrue(c.error().contains(error)); assertEquals("Checkout: Your Information",c.title()); }
 @Test @Tag("smoke") void completeShoppingJourney() {
  var p=login.login(); var expected=p.prices().get(0).add(p.prices().get(1));
  p.add("sauce-labs-backpack"); p.add("sauce-labs-bike-light"); var c=p.cart().checkout(); c.details("Test","Shopper","12345");
  assertEquals(java.util.Set.of("Sauce Labs Backpack","Sauce Labs Bike Light"),java.util.Set.copyOf(c.names()));
  assertEquals(0,expected.compareTo(c.subtotal())); assertTrue(c.tax().signum()>=0); assertEquals(0,c.subtotal().add(c.tax()).compareTo(c.total()));
  assertEquals("Thank you for your order!",c.finish()); assertEquals(0,c.count());
 }
 @Test void cancelInformationStep() { var p=login.login(); p.add("sauce-labs-backpack"); var c=p.cart().checkout(); c.cancel(); assertEquals("Your Cart",c.title()); assertEquals(1,c.count()); }
 @Test void cancelOverviewStep() { var p=login.login(); p.add("sauce-labs-backpack"); var c=p.cart().checkout(); c.details("Test","Shopper","12345"); c.cancel(); assertEquals("Products",c.title()); assertEquals(1,c.count()); }
}