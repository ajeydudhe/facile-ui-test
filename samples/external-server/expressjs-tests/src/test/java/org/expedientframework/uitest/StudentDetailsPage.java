/********************************************************************
 * File Name:    StudentDetailsPage.java
 *
 * Date Created: 02-Nov-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class StudentDetailsPage {
  
  public String getStudentId() {
  
    return getValue(this.studentIdElement);
  }
  
  public String getFirstName() {
    
    return getValue(this.firstNameElement);
  }
  
  public String getLastName() {
    
    return getValue(this.lastNameElement);
  }
  
  public int getAge() {
    
    return Integer.parseInt(getValue(this.ageElement));
  }
  
  public String getWelcomeMessage() {
    
    return getValue(this.welcomeMessageIdElement);
  }

  private static String getValue(final WebElement webElement) {
    
    return webElement.getTagName().equalsIgnoreCase("input") ? webElement.getAttribute("value") : webElement.getText();
  }
  
  // Private members
  @FindBy(id = "welcomeMessage")
  private WebElement welcomeMessageIdElement;  

  @FindBy(id = "studentId")
  private WebElement studentIdElement;  

  @FindBy(id = "firstName")
  private WebElement firstNameElement;  

  @FindBy(id = "lastName")
  private WebElement lastNameElement;  

  @FindBy(id = "age")
  private WebElement ageElement;  
}

