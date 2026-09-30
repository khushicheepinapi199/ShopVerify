package io.shopverify.pages;
import org.openqa.selenium.WebDriver;
public class LoginPage extends BasePage {
 public LoginPage(WebDriver d) { super(d); }
 public void submit(String u,String p) { fill("username",u); fill("password",p); click("login-button"); }
 public ProductsPage login() { submit("standard_user","secret_sauce"); return new ProductsPage(d); }
 public String error() { return text("error"); }
 public boolean displayed() { return d.findElement(id("login-button")).isDisplayed(); }
}