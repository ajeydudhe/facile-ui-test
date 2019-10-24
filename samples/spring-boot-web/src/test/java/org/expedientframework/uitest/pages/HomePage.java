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
  
  public String getMessage() {
    
    return this.messageElement.getText();
  }
  
  public void setName(final String name) {
    
    this.nameElement.sendKeys(name);
  }
  
  public void clickMessageButton() {
    
    this.btnNameElement.click();
  }
  
  @FindBy(id = "lblAppName")
  protected WebElement appNameElement;

  @FindBy(id = "lblMessage")
  protected WebElement messageElement;

  @FindBy(id = "txtName")
  protected WebElement nameElement;

  @FindBy(id = "btnNameClick")
  protected WebElement btnNameElement;
}

