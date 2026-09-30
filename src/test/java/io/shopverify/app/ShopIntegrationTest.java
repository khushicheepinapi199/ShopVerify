package io.shopverify.app;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:integration;DB_CLOSE_DELAY=-1","spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc
class ShopIntegrationTest {
 @Autowired MockMvc mvc; @Autowired AccountService accounts; @Autowired AccountRepository repository; @Autowired PasswordEncoder encoder; @Autowired PurchaseRepository orders;
 String unique() { return "u"+java.util.UUID.randomUUID().toString().replace("-","").substring(0,20); }
 @Test void registerStoresHashAndAuthenticates() throws Exception { String u=unique(); accounts.register(u,"TestPass123!","TestPass123!"); var hash=repository.findByUsername(u).orElseThrow().getPasswordHash(); assertNotEquals("TestPass123!",hash); assertTrue(encoder.matches("TestPass123!",hash)); mvc.perform(formLogin().user(u).password("TestPass123!")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/products")); }
 @Test void duplicateCannotOverwritePassword() { String u=unique(); accounts.register(u,"TestPass123!","TestPass123!"); assertThrows(IllegalArgumentException.class,()->accounts.register(u,"OtherPass123!","OtherPass123!")); assertTrue(encoder.matches("TestPass123!",repository.findByUsername(u).orElseThrow().getPasswordHash())); }
 @Test void invalidRegistrationsRejected() { assertThrows(IllegalArgumentException.class,()->accounts.register("A!","TestPass123!","TestPass123!")); assertThrows(IllegalArgumentException.class,()->accounts.register(unique(),"short","short")); assertThrows(IllegalArgumentException.class,()->accounts.register(unique(),"TestPass123!","different")); assertThrows(IllegalArgumentException.class,()->accounts.register(unique(),"😊".repeat(30),"😊".repeat(30))); }
 @Test void csrfRequiredForRegistration() throws Exception { mvc.perform(post("/register").param("username",unique()).param("password","TestPass123!").param("confirmation","TestPass123!")).andExpect(status().isForbidden()); }
 @Test void anonymousShopperRedirected() throws Exception { mvc.perform(get("/cart")).andExpect(status().is3xxRedirection()); }
 @Test void wrongPasswordRejected() throws Exception { String u=unique(); accounts.register(u,"TestPass123!","TestPass123!"); mvc.perform(formLogin().user(u).password("bad")).andExpect(redirectedUrl("/login?error")); }
 @Test void quantityBounds() { var c=new Cart(); assertThrows(IllegalArgumentException.class,()->c.update(1,0)); assertThrows(IllegalArgumentException.class,()->c.update(1,11)); c.update(1,10); assertEquals(10,c.count()); }
 @Test void checkoutUsesServerPricesAndClearsCart() throws Exception { var session=new org.springframework.mock.web.MockHttpSession(); var cart=new Cart(); cart.update(1,2); session.setAttribute("cart",cart); String u=unique(); mvc.perform(post("/checkout").with(user(u)).with(csrf()).session(session).param("fullName","Demo Shopper").param("address","Test street").param("total","0.01")).andExpect(status().is3xxRedirection()); assertEquals(0,cart.count()); assertEquals(0,new java.math.BigDecimal("84.24").compareTo(orders.findByUsernameOrderByCreatedAtDesc(u).get(0).getTotal())); }
 @Test void checkoutValidationKeepsCart() throws Exception { var session=new org.springframework.mock.web.MockHttpSession(); var cart=new Cart(); cart.add(1); session.setAttribute("cart",cart); mvc.perform(post("/checkout").with(user(unique())).with(csrf()).session(session).param("fullName"," ").param("address","Test")).andExpect(status().isOk()).andExpect(model().attributeExists("error")); assertEquals(1,cart.count()); }
 @Test void anotherUserCannotReadOrder() throws Exception { var order=orders.saveAndFlush(new Purchase("owner",new java.math.BigDecimal("10.00"))); mvc.perform(get("/orders/"+order.getId()).with(user("other"))).andExpect(status().isNotFound()); }
 @Test void logoutInvalidatesSession() throws Exception { var session=new org.springframework.mock.web.MockHttpSession(); mvc.perform(post("/logout").session(session).with(user(unique())).with(csrf())).andExpect(redirectedUrl("/login?logout")); assertTrue(session.isInvalid()); }
}
