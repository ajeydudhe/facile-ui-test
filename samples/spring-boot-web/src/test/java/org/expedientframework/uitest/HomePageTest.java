package org.expedientframework.uitest;

import static org.assertj.core.api.Assertions.assertThat;

import org.expedientframework.facilemock.http.browsermob.HttpMockContext;
import org.expedientframework.facilemock.http.browsermob.HttpProxyManagerFactory;
import org.expedientframework.uitest.controllers.HelloWorldController;
import org.expedientframework.uitest.core.UiTestContext;
import org.expedientframework.uitest.pages.HomePage;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.Test;

import static org.expedientframework.facilemock.http.browsermob.HttpMockHelpers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ContextConfiguration(classes = {TestConfiguration.class })
public class HomePageTest extends AbstractPageTest {
  
  @Test
  public void applicationName_validName() {
    
    try (HttpMockContext mock = HttpProxyManagerFactory.createMockContext(unitTest())) {
      
      createWebDriver(mock);
      
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
      //final int port = 8080; // mock.getHttpProxyManager().getPort()
      //final String url = String.format("http://localhost:%d", port);
      this.webDriver.get("http://blahblahDoesNotExists.com:1234");
      
      final HomePage homePage = PageFactory.initElements(this.webDriver, HomePage.class);
      
      assertThat(homePage.getAppName()).as("Application Name").isEqualTo("Facile UI Test Web Application");    
    }    
  }
  
  @Test
  public void applicationName_usingMockMvc_validName() throws Exception {
    
    try (HttpMockContext mock = HttpProxyManagerFactory.createMockContext(unitTest())) {

      // Perform request using MockMvc  
      final String homePageContents = getHomePageContents();
      
      assertThat(homePageContents).as("Response").contains("Facile UI Test Web Application");
      
      createWebDriver(mock);
      
      mock.when(urlEquals("/")).then(respondWith(homePageContents));

      this.webDriver.get("http://blahblahDoesNotExists.com:1234");
      
      final HomePage homePage = PageFactory.initElements(this.webDriver, HomePage.class);
      
      assertThat(homePage.getAppName()).as("Application Name").isEqualTo("Facile UI Test Web Application");    
    }    
  }

  @Test
  public void helloWorld_usingMockController_succeeds() throws Exception {
    
    try (HttpMockContext mock = HttpProxyManagerFactory.createMockContext(unitTest())) {

      assertThat(helloWorldController).as("helloWorldController").isNotNull();
      
      // Using Mockitto we mock the response at the controller class level
      when(helloWorldController.message()).thenReturn("My mocked hello world blah blah blah !!!");
      
      // Perform request using MockMvc  
      assertThat(this.mockMvc).as("MockMvc").isNotNull();
      
      // This will invoke the controller but return the mock response
      final MvcResult result = this.mockMvc.perform(get("/hello"))
                                   .andDo(print())
                                   .andExpect(status().isOk()).andReturn();
      
      // Hook into http  request
      createWebDriver(mock);
      
      // Here we are returning the response from MockMvc which calls into the mocked controller
      mock.when(urlEquals("/hello/greet")).then(respondWith(result.getResponse().getContentAsString()));

      this.webDriver.get("http://blahblahDoesNotExists.com:1234/hello/greet");

      // Finally checking the response is mocked one
      assertThat(this.webDriver.getPageSource()).as("Hello message").isEqualTo("<html><head></head><body>My mocked hello world blah blah blah !!!</body></html>");    
    }    
  }

  @Test
  public void helloWorld_usingUiTestContext_succeeds() {
    
    try (UiTestContext uiTestContext = new UiTestContext(this.mockMvc)) {

      assertThat(helloWorldController).as("helloWorldController").isNotNull();
      
      // Using Mockitto we mock the response at the controller class level
      when(helloWorldController.message()).thenReturn("My mocked hello world blah blah blah !!!");
      
      // Perform request using MockMvc  
      assertThat(this.mockMvc).as("MockMvc").isNotNull();
      
      // Hook into http  request
      createWebDriver(uiTestContext.getProxyPort());
      
      this.webDriver.get("http://blahblahDoesNotExists.com:1234/hello");

      // Finally checking the response is mocked one
      assertThat(this.webDriver.getPageSource()).as("Hello message").isEqualTo("<html><head></head><body>My mocked hello world blah blah blah !!!</body></html>");    
    }    
  }

  @Test
  public void greet_usingUiTestContext_succeeds() {
    
    try (UiTestContext uiTestContext = new UiTestContext(this.mockMvc)) {

      // Using Mockitto we mock the response at the controller class level
      when(helloWorldController.greet("John")).thenReturn("Hello 'Blah' !!!");
      
      // Perform request using MockMvc  
      assertThat(this.mockMvc).as("MockMvc").isNotNull();
      
      // Hook into http  request
      createWebDriver(uiTestContext.getProxyPort());
      
      this.webDriver.get("http://blahblahDoesNotExists.com:1234/hello/greet/John");

      // Finally checking the response is mocked one
      assertThat(this.webDriver.getPageSource()).as("Hello message").isEqualTo("<html><head></head><body>Hello 'Blah' !!!</body></html>");    
    }    
  }

  @Test
  public void greetOnOccasion_succeeds() {
    
    try (UiTestContext uiTestContext = new UiTestContext(this.mockMvc)) {

      // Using Mockitto we mock the response at the controller class level
      when(helloWorldController.greetOnOccasion("John", "birthday")).thenReturn("Happy Diwali Doe !!!");
      
      // Perform request using MockMvc  
      assertThat(this.mockMvc).as("MockMvc").isNotNull();
      
      // Hook into http  request
      createWebDriver(uiTestContext.getProxyPort());
      
      this.webDriver.get("http://blahblahDoesNotExists.com:1234/hello/occasion/John?occasion=birthday");

      // Finally checking the response is mocked one
      assertThat(this.webDriver.getPageSource()).as("Hello message").isEqualTo("<html><head></head><body>Happy Diwali Doe !!!</body></html>");    
    }    
  }

  @Test
  public void greetHelloFromHomePage_succeeds() {
    
    try (UiTestContext uiTestContext = new UiTestContext(this.mockMvc, "/myapp")) {

      // Hook into http  request
      createWebDriver(uiTestContext.getProxyPort());
      
      this.webDriver.get("http://blahblahDoesNotExists.com:1234/myapp");

      final HomePage page = PageFactory.initElements(this.webDriver, HomePage.class);
      
      page.setName("John Doe");
      
      page.clickMessageButton();
      
      // Finally checking the response is mocked one
      assertThat(page.getMessage()).as("Hello message").isEqualTo("Hello 'John Doe' !!!");    
    }    
  }
  
  private String getHomePageContents() {
    
    try {
      
      assertThat(this.mockMvc).as("MockMvc").isNotNull();
      
      final MvcResult result = this.mockMvc.perform(get("/"))
                                   .andDo(print())
                                   .andExpect(status().isOk()).andReturn();
      
      return result.getResponse().getContentAsString();
      
    } catch (Exception e) {
      
      //TODO: Log
      throw new RuntimeException(e);
    }
  }

  @Autowired
  private HelloWorldController helloWorldController;  
}
