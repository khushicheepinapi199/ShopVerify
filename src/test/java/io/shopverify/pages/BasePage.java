package io.shopverify.pages;
import java.math.BigDecimal;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
public abstract class BasePage {
 protected final WebDriver d; protected final WebDriverWait wait;
 protected BasePage(WebDriver d) { this.d=d; wait=new WebDriverWait(d,java.time.Duration.ofSeconds(10)); }
 protected By id(String s) { return By.cssSelector("[data-test='"+s+"']"); }
 protected void click(String s) { wait.until(ExpectedConditions.elementToBeClickable(id(s))).click(); }
 protected String text(String s) { return wait.until(ExpectedConditions.visibilityOfElementLocated(id(s))).getText(); }
 protected void fill(String s,String v) { var e=wait.until(ExpectedConditions.visibilityOfElementLocated(id(s))); e.clear(); e.sendKeys(v); }
 protected BigDecimal money(String s) { return new BigDecimal(s.substring(s.indexOf('$')+1).trim()); }
 public String title() { return text("title"); }
 public int count() { var list=d.findElements(id("shopping-cart-badge")); return list.isEmpty()?0:Integer.parseInt(list.get(0).getText()); }
 public CartPage cart() { click("shopping-cart-link"); return new CartPage(d); }
 public LoginPage logout() { d.findElement(By.id("react-burger-menu-btn")).click(); wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))).click(); return new LoginPage(d); }
}