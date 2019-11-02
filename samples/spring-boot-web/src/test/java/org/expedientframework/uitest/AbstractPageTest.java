/********************************************************************
 * File Name:    AbstractPageTest.java
 *
 * Date Created: 01-Nov-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest;

import org.expedientframework.facilemock.http.browsermob.HttpMockContext;
import org.expedientframework.uitest.core.UiTestContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true") // To inject mock controller instance.
public abstract class AbstractPageTest extends AbstractTestNGSpringContextTests {

  @BeforeClass
  public void setUp() {

    WebDriverManager.chromedriver().setup();
    
    this.uiTestContext = new UiTestContext(this.mockMvc);
    
    // Hook into http request
    createWebDriver(this.uiTestContext.getProxyPort());
  }
  
  @AfterClass
  public void tearDown() {
    
    this.webDriver.close();
    
    this.uiTestContext.close();
  }
  
  protected void createWebDriver(final HttpMockContext mock) {
    
    createWebDriver(mock.getHttpProxyManager().getPort());
  }
    
  protected void createWebDriver(final int proxyPort) {
    
    if(this.webDriver != null) {
     
      this.webDriver.close();
    }
    
    final ChromeOptions chromeOptions = new ChromeOptions();
    
    chromeOptions.setHeadless(true);
    
    final String proxyAddress = "localhost:" + proxyPort;
    final Proxy proxy = new Proxy().setHttpProxy(proxyAddress).setSslProxy(proxyAddress);   
    
    chromeOptions.setProxy(proxy);
    //chromeOptions.addArguments("--proxy-server=" + proxyAddress);
    this.webDriver = new ChromeDriver(chromeOptions);
  }
  
  protected <T> T getPage(final String relativeUrl, final Class<T> pageClass) {
    
    final String url = getAbsoluteUrl(relativeUrl);
    
    LOG.info("Loading page with relative url [{}] and absolute url [{}]", relativeUrl, url);
    
    this.webDriver.get(url);
    
    waitForAjaxCalls();
    
    return PageFactory.initElements(this.webDriver, pageClass);
  }
  
  protected String getAbsoluteUrl(final String relativeUrl) {
      
    return "http://blahBlahBlahDoesNotExistsReally.dom/" + relativeUrl;
  }
  
  protected void waitForAjaxCalls() {
    
    final JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
    
    int nCount = 0;
    for(boolean scriptExecutionResult = false;
        scriptExecutionResult == false && nCount < 10;
        ++nCount) {
      
      scriptExecutionResult = (boolean) executor.executeScript("return jQuery.active == 0");

      if(scriptExecutionResult) {
        
        return;
      }
      
      LOG.info("Waiting for jQuery.active == 0 for [{}] time.", nCount + 1);
      
      try {
        
        Thread.sleep(100);
        
      } catch (InterruptedException e) {
        
        LOG.error("An error occurred.", e);
      }
    }
  }
  
  // Private members
  protected WebDriver webDriver;
  
  @Autowired
  protected MockMvc mockMvc;
  
  private UiTestContext uiTestContext;
  
  private static final Logger LOG = LoggerFactory.getLogger(AbstractPageTest.class);
}

