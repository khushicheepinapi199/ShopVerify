package io.shopverify.pages;
import java.math.BigDecimal;
import java.util.List;
import org.openqa.selenium.*;
public class CheckoutPage extends BasePage {
 public CheckoutPage(WebDriver d) { super(d); wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("checkout-step-one.html")); text("title"); }
 public void details(String first,String last,String zip) { fill("firstName",first); fill("lastName",last); fill("postalCode",zip); click("continue"); }
 public String error() { return text("error"); }
 public List<String> names() { return d.findElements(id("inventory-item-name")).stream().map(WebElement::getText).toList(); }
 public BigDecimal subtotal() { return money(text("subtotal-label")); }
 public BigDecimal tax() { return money(text("tax-label")); }
 public BigDecimal total() { return money(text("total-label")); }
 public String finish() { click("finish"); return text("complete-header"); }
 public void cancel() { click("cancel"); }
}