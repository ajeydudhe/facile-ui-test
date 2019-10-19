/********************************************************************
 * File Name:    HomePage.java
 *
 * Date Created: 20-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage {

  public String getAppName() {
    
    return this.appNameElement.getText();
  }
  
  @FindBy(id = "lblAppName")
  protected WebElement appNameElement;
}

