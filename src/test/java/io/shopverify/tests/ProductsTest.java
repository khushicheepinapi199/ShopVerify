package io.shopverify.tests;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;
class ProductsTest extends BaseTest {
 @ParameterizedTest @ValueSource(strings={"az","za"}) void alphabeticalSorting(String order) {
  var p=login.login(); p.sort(order); var actual=p.names(); var expected=new ArrayList<>(actual); expected.sort(order.equals("az")?Comparator.naturalOrder():Comparator.reverseOrder()); assertFalse(actual.isEmpty()); assertEquals(expected,actual);
 }
 @ParameterizedTest @ValueSource(strings={"lohi","hilo"}) void priceSorting(String order) {
  var p=login.login(); p.sort(order); var actual=p.prices(); var expected=new ArrayList<>(actual); expected.sort(order.equals("lohi")?Comparator.naturalOrder():Comparator.reverseOrder()); assertFalse(actual.isEmpty()); assertEquals(expected,actual);
 }
 @Test void productDetails() { var p=login.login(); var name=p.names().get(0); var price=p.prices().get(0); var detail=p.openFirst(); assertEquals(name,detail.name()); assertEquals(price,detail.price()); assertFalse(detail.description().isBlank()); assertEquals("Products",detail.back().title()); }
}