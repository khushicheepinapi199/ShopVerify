package io.shopverify.pages;
import java.math.BigDecimal;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
public class ProductsPage extends BasePage {
 public ProductsPage(WebDriver d) { super(d); wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("inventory.html")); text("title"); }
 public void add(String slug) { click("add-to-cart-"+slug); wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(id("remove-"+slug))); }
 public void remove(String slug) { click("remove-"+slug); wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(id("add-to-cart-"+slug))); }
 public void sort(String value) { new Select(d.findElement(id("product-sort-container"))).selectByValue(value); }
 public List<String> names() { return d.findElements(id("inventory-item-name")).stream().map(WebElement::getText).toList(); }
 public List<BigDecimal> prices() { return d.findElements(id("inventory-item-price")).stream().map(e->money(e.getText())).toList(); }
 public ProductPage openFirst() { d.findElements(id("inventory-item-name")).get(0).click(); return new ProductPage(d); }
}