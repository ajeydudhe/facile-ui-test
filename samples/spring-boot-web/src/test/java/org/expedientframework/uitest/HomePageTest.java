package org.expedientframework.uitest;

import static org.assertj.core.api.Assertions.assertThat;

import org.expedientframework.facilemock.http.browsermob.HttpMockContext;
import org.expedientframework.facilemock.http.browsermob.HttpProxyManagerFactory;
import org.expedientframework.uitest.pages.HomePage;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.expedientframework.facilemock.http.browsermob.HttpMockHelpers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HomePageTest extends AbstractTestNGSpringContextTests {
  
  @Test
  public void applicationName_validName() {
    
    try (HttpMockContext mock = HttpProxyManagerFactory.createMockContext(unitTest())) {
      
      final ChromeOptions chromeOptions = new ChromeOptions();
      
      chromeOptions.setHeadless(true);
      
      final String proxyAddress = "localhost:" + mock.getHttpProxyManager().getPort();
      final Proxy proxy = new Proxy().setHttpProxy(proxyAddress).setSslProxy(proxyAddress);   
      
      chromeOptions.setProxy(proxy);
      //chromeOptions.addArguments("--proxy-server=" + proxyAddress);
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

      // Observation: For localhost address needs to use the proxy port. For DNS name no need to use the proxy port.
      // The proxy setting above takes care of it. So if we have a locahost server which needs to be proxied the consumer will
      // have to use the proxy port in address. We will need a way to forward the request to local server if the request is not to be mocked.
      final int port = 8080; // mock.getHttpProxyManager().getPort()
      final String url = String.format("http://localhost:%d", port);
      this.webDriver.get("http://blahblah.does.not.exists.com:1234");
      
      final HomePage homePage = PageFactory.initElements(this.webDriver, HomePage.class);
      
      assertThat(homePage.getAppName()).as("Application Name").isEqualTo("Facile UI Test Web Application");    
    }    
  }
  
  @Test
  public void applicationName_usingMockMvc_validName() throws Exception {
    
    try (HttpMockContext mock = HttpProxyManagerFactory.createMockContext(unitTest())) {

      // Perform request using MopckMvc  
      assertThat(this.mockMvc).as("MockMvc").isNotNull();
      
      final MvcResult result = this.mockMvc.perform(get("/"))
                                   .andDo(print())
                                   .andExpect(status().isOk()).andReturn();
      
      assertThat(result.getResponse().getContentAsString()).as("Response").contains("Facile UI Test Web Application");
      
      // Hook into http  request
      final ChromeOptions chromeOptions = new ChromeOptions();
      
      chromeOptions.setHeadless(true);
      
      final String proxyAddress = "localhost:" + mock.getHttpProxyManager().getPort();
      final Proxy proxy = new Proxy().setHttpProxy(proxyAddress).setSslProxy(proxyAddress);   
      
      chromeOptions.setProxy(proxy);
      //chromeOptions.addArguments("--proxy-server=" + proxyAddress);
      this.webDriver = new ChromeDriver(chromeOptions);
      
      mock.when(urlEquals("/")).then(respondWith(result.getResponse().getContentAsString()));

      this.webDriver.get("http://blahblah.does.not.exists.com:1234");
      
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
  
  @Autowired
  private MockMvc mockMvc;
}
