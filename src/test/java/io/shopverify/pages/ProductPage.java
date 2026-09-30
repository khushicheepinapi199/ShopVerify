package io.shopverify.pages;
import java.math.BigDecimal;
import org.openqa.selenium.WebDriver;
public class ProductPage extends BasePage {
 public ProductPage(WebDriver d) { super(d); }
 public String name() { return text("inventory-item-name"); }
 public String description() { return text("inventory-item-desc"); }
 public BigDecimal price() { return money(text("inventory-item-price")); }
 public ProductsPage back() { click("back-to-products"); return new ProductsPage(d); }
}