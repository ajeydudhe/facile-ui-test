package org.expedientframework.uitest;

import static org.assertj.core.api.Assertions.assertThat;

import org.expedientframework.uitest.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class HomePageTest {
  
  @Test
  public void applicationName_validName() {
    
    this.webDriver = new ChromeDriver();
    
    this.webDriver.get("http://localhost:8080");
    
    final HomePage homePage = PageFactory.initElements(this.webDriver, HomePage.class);
    
    assertThat(homePage.getAppName()).as("Application Name").isEqualTo("Facile UI Test Web Application");    
  }
  
  @BeforeClass
  public void setUp() {
    
    WebDriverManager.chromedriver().setup();
  }
  
  @AfterClass
  public void tearDown() {
    
    if(this.webDriver != null) {
      
      this.webDriver.close();
    }
  }
  
  // Private members
  private WebDriver webDriver;
}
