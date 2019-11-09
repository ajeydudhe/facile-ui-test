[![Build Status](https://travis-ci.org/ajeydudhe/facile-ui-test.svg?branch=master)](https://travis-ci.org/ajeydudhe/facile-ui-test) [![MIT licensed](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
# facile-ui-test - Library to test UI with mock http responses.
**facile-ui-test** can be used to write UI tests especially for Spring MVC projects. It uses [**BrowserMobProxy**](https://github.com/lightbody/browsermob-proxy) to complete the http request flow from the browser to [**MockMvc**](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#spring-mvc-test-framework) and additionally use [**Mockito**](https://site.mockito.org/) to define the mock http responses.

## Adding the library reference
Add the maven dependency to your pom.xml as follows:
```xml
<dependency>
    <groupId>org.expedientframework.uitest</groupId>
    <artifactId>facile-ui-test</artifactId>
    <version>1.0.0-M1</version>
</dependency>
```

## Usage for Spring MVC project
### Pre-requisite
Make sure that [**MockMvc**](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#spring-mvc-test-framework) setup is complete. 
Refer [**AbstractPageTest.java**](/samples/spring-boot-web/src/test/java/org/expedientframework/uitest/AbstractPageTest.java)
### Create [**UiTestContext**](/core/src/main/java/org/expedientframework/uitest/core/UiTestContext.java)
```java
this.uiTestContext = new UiTestContext(this.mockMvc, "/myapp");
```
### Create WebDriver instance
Use the _**UiTestContext.getProxyPort()**_ method to set the proxy for the WebDriver as below:
```java
protected void createWebDriver(final UiTestContext uiTestContext) {
  
  if(this.webDriver != null) {
   
    this.webDriver.close();
  }
  
  final ChromeOptions chromeOptions = new ChromeOptions();
  
  chromeOptions.setHeadless(true);    
  
  final String proxyAddress = "localhost:" + uiTestContext.getProxyPort();
  final Proxy proxy = new Proxy().setHttpProxy(proxyAddress).setSslProxy(proxyAddress);   
  
  chromeOptions.setProxy(proxy);
  // Required to use proxy for localhost address
  chromeOptions.addArguments("--proxy-bypass-list=" + "<-loopback>");    
  
  this.webDriver = new ChromeDriver(chromeOptions);
}
```
### Mock the MVC controllers or other beans
In your test configuration create a bean for [_**MockInstanceBeanFactoryPostProcessor**_](/core/src/main/java/org/expedientframework/uitest/core/beans/MockInstanceBeanFactoryPostProcessor.java) as follows:
```java
@Configuration
public class TestConfiguration {

  @Bean
  public MockInstanceBeanFactoryPostProcessor mockInstanceBeanFactoryPostProcessor() {
    
    return new MockInstanceBeanFactoryPostProcessor(HelloWorldController.class, 
                                                    StudentController.class);
  }
}
```
In the constructor for the bean provide the list of MVC controller or other bean classes which needs to be mocked. If your MVC controller to be mocked has other dependencies which needs to be mocked then provide those also here. Inject the mocked MVC controllers or other beans into your tests.
### Writing a test with mocked http response
```java
@Test
public void greet_usingUiTestContext_succeeds() {
  
  try (UiTestContext uiTestContext = new UiTestContext(this.mockMvc)) {

    // Using Mockitto we mock the response at the controller class level
    when(helloWorldController.greet("John")).thenReturn("Hello 'Blah' Dummy !!!");
    
    // Hook into http  request
    createWebDriver(uiTestContext.getProxyPort());
    
    this.webDriver.get("http://localhost/hello/greet/John");

    // Finally checking the response is mocked one
    assertThat(this.webDriver.getPageSource()).as("Hello message").isEqualTo("<html><head></head><body>Hello 'Blah' Dummy !!!</body></html>");    
  }    
}
```
* First we are creating the _**UiTestContext**_ instance.
* Next using _**Mockito**_ we define that _**helloWorldController.greet()**_ when called with parameter as _**John**_ should return value as _**"Hello 'Blah' Dummy !!!"**_.
* Next we are creating the _**WebDriver**_ instance using the http proxy port.
* Then we load the web page using _**this.webDriver.get("http://localhost/hello/greet/John");**_
* Finally, we verify that we are getting the mock response.

As seen above the entire web page is getting loaded as is in the browser but only the http response is getting mocked using _**Mockito**_.

## Work in progress
* Ability to mock only specific http requests so that we can have the web server running outside the test project.
* Handle all request types e.g. form upload etc.   