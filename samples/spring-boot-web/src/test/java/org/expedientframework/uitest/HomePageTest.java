package org.expedientframework.uitest;

import static org.assertj.core.api.Assertions.assertThat;

import org.expedientframework.facilemock.http.browsermob.HttpMockContext;
import org.expedientframework.facilemock.http.browsermob.HttpProxyManagerFactory;
import org.expedientframework.uitest.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.expedientframework.facilemock.http.browsermob.HttpMockHelpers.*;

public class HomePageTest {
  
  @Test
  public void applicationName_validName() {
    
    try (HttpMockContext mock = HttpProxyManagerFactory.createMockContext(unitTest())) {
      
      final ChromeOptions chromeOptions = new ChromeOptions();
      
      chromeOptions.setHeadless(true);
      
      this.webDriver = new ChromeDriver(chromeOptions);
      
      mock.when(urlEquals("/")).then(respondWith("<html>\n" + 
          "  <head>\n" + 
          "    <title>Home Page</title>\n" + 
          "  </head>\n" + 
          "  <body>\n" + 
          "    <h1>Hello !</h1>\n" + 
          "    <p>\n" + 
          "      Welcome to <b><span id=\"lblAppName\">Facile UI Test Web Application</span></b>\n" + 
          "    </p>\n" + 
          "  </body>\n" + 
          "</html>"));

      this.webDriver.get(String.format("http://localhost:%d", mock.getHttpProxyManager().getPort()));
      
      final HomePage homePage = PageFactory.initElements(this.webDriver, HomePage.class);
      
      assertThat(homePage.getAppName()).as("Application Name").isEqualTo("Facile UI Test Web Application");    
    }    
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
