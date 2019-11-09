[![Build Status](https://travis-ci.org/ajeydudhe/facile-ui-test.svg?branch=develop)](https://travis-ci.org/ajeydudhe/facile-ui-test) [![MIT licensed](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
# facile-ui-test - Library to test UI with mock http responses.
**facile-ui-test** can be used to write UI tests especially for Spring MVC projects. It uses [**BrowserMobProxy**](https://github.com/lightbody/browsermob-proxy) to complete the http request flow from the browser to [**MockMvc**](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#spring-mvc-test-framework) and additionally use [**Mockito**](https://site.mockito.org/) to define the mock http responses.

## Adding the library reference
Add the maven dependency to your pom.xml as follows:
```xml
<dependency>
    <groupId>org.expedientframework.uitest</groupId>
    <artifactId>facile-ui-test</artifactId>
    <version>0.0.1-SNAPSHOT</version>
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
In the constructor for the bean provide the list of MVC controller or other bean classes which needs to be mocked. If your MVC controller to be mocked has other dependencies which needs to be mocked then provide those also here.