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
