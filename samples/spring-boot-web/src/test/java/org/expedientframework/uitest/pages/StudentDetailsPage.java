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

package org.expedientframework.uitest.pages;

import org.openqa.selenium.WebElement;

public class StudentDetailsPage {

  
  public String getStudentId() {
  
    return studentIdElement.getText();
  }
  
  public String getFirstName() {
    
    return firstNameElement.getText();
  }
  
  public String getLastName() {
    
    return lastNameElement.getText();
  }
  
  public int getAge() {
    
    return Integer.parseInt(ageElement.getText());
  }
  
  public void setStudentIdElement(final WebElement studentIdElement) {
    
    this.studentIdElement = studentIdElement;
  }
  
  public void setFirstNameElement(final WebElement firstNameElement) {
    
    this.firstNameElement = firstNameElement;
  }
  
  public void setLastNameElement(final WebElement lastNameElement) {
    
    this.lastNameElement = lastNameElement;
  }
  
  public void setAgeElement(final WebElement ageElement) {
    
    this.ageElement = ageElement;
  }
  
  // Private members
  //@FindBy(id = "lblAppName")
  private WebElement studentIdElement;  
  private WebElement firstNameElement;  
  private WebElement lastNameElement;  
  private WebElement ageElement;  
}

