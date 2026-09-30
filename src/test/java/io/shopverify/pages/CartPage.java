package io.shopverify.pages;
import java.util.List;
import org.openqa.selenium.*;
public class CartPage extends BasePage {
 public CartPage(WebDriver d) { super(d); text("title"); }
 public List<String> names() { return d.findElements(id("inventory-item-name")).stream().map(WebElement::getText).toList(); }
 public void remove(String slug) { click("remove-"+slug); }
 public ProductsPage continueShopping() { click("continue-shopping"); return new ProductsPage(d); }
 public CheckoutPage checkout() { click("checkout"); return new CheckoutPage(d); }
}