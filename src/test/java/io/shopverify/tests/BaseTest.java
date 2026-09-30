package io.shopverify.tests;
import java.nio.file.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import io.shopverify.pages.*;
public abstract class BaseTest {
 protected WebDriver driver; protected LoginPage login;
 @RegisterExtension final AfterTestExecutionCallback evidence=context->{
  if(context.getExecutionException().isPresent() && driver!=null) {
   try { var dir=Path.of("target","failure-evidence"); Files.createDirectories(dir); String name=context.getRequiredTestMethod().getName()+"-"+System.nanoTime();
    Files.write(dir.resolve(name+".png"),((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES));
    Files.writeString(dir.resolve(name+".txt"),"URL: "+driver.getCurrentUrl()+"\n"+context.getExecutionException().get());
   } catch(Exception e) { System.err.println("Evidence capture failed: "+e.getMessage()); }
  }
 };
 @BeforeEach void start() {
  var o=new ChromeOptions();
  if(Boolean.parseBoolean(System.getProperty("headless","true"))) o.addArguments("--headless=new");
  o.addArguments("--window-size=1440,1000","--no-sandbox","--disable-dev-shm-usage");
  o.setExperimentalOption("prefs",java.util.Map.of("credentials_enable_service",false,"profile.password_manager_enabled",false));
  driver=new ChromeDriver(o); driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(30));
  driver.get(System.getProperty("baseUrl","https://www.saucedemo.com/")); login=new LoginPage(driver);
 }
 @AfterEach void stop() { if(driver!=null) driver.quit(); }
}