/********************************************************************
 * File Name:    TestUtils.java
 *
 * Date Created: 03-Nov-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {

  public static void waitForAjaxCalls(final WebDriver webDriver) {
    
    final JavascriptExecutor executor = (JavascriptExecutor) webDriver;
    
    int nCount = 0;
    for(boolean scriptExecutionResult = false;
        scriptExecutionResult == false && nCount < 10;
        ++nCount) {
      
      scriptExecutionResult = (Boolean) executor.executeScript("return jQuery.active == 0");

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
  
  private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);
}

