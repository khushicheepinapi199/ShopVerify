package io.shopverify.tests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;
class LoginTest extends BaseTest {
 @Test @Tag("smoke") void validLogin() { assertEquals("Products",login.login().title()); }
 @ParameterizedTest @CsvSource(value={"bad|secret_sauce|Username and password do not match","standard_user|bad|Username and password do not match","locked_out_user|secret_sauce|Sorry, this user has been locked out."},delimiter='|')
 void rejectedLogin(String user,String password,String expected) { login.submit(user,password); assertTrue(login.error().contains(expected)); assertTrue(login.displayed()); }
 @ParameterizedTest @CsvSource(value={"''|secret_sauce|Username is required","standard_user|''|Password is required","''|''|Username is required"},delimiter='|')
 void requiredFields(String user,String password,String expected) { login.submit(user,password); assertTrue(login.error().contains(expected)); }
 @Test void logout() { assertTrue(login.login().logout().displayed()); }
}