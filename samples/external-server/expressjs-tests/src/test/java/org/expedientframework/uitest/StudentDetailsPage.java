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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class StudentDetailsPage {
  
  public StudentDetailsPage(final WebDriver webDriver) {
    
    this.webDriver = webDriver;
  }
  
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
  
  public String getErrorMessage() {
    
    return this.errorMessageElement.getText();
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
  
  public void createStudent() {

    clickButtonAndWait(this.createStudentButtonElement);
  }
  
  public void updateStudent() {

    clickButtonAndWait(this.updateStudentButtonElement);
  }

  public void deleteStudent() {

    clickButtonAndWait(this.deleteStudentButtonElement);
  }
  
  private void clickButtonAndWait(final WebElement button) {
    
    button.click();
    
    TestUtils.waitForAjaxCalls(this.webDriver);
  }
  
  private static String getValue(final WebElement webElement) {
    
    return webElement.getTagName().equalsIgnoreCase("input") ? webElement.getAttribute("value") : webElement.getText();
  }
  
  // Private members
  private final WebDriver webDriver;
  
  @FindBy(id = "txtStudentId")
  private WebElement studentIdElement;  

  @FindBy(id = "txtFirstName")
  private WebElement firstNameElement;  

  @FindBy(id = "txtLastName")
  private WebElement lastNameElement;  

  @FindBy(id = "txtAge")
  private WebElement ageElement;  
  
  @FindBy(id = "errorMessage")
  private WebElement errorMessageElement;
  
  @FindBy(id = "createStudent")
  private WebElement createStudentButtonElement;  
  
  @FindBy(id = "updateStudent")
  private WebElement updateStudentButtonElement;  

  @FindBy(id = "deleteStudent")
  private WebElement deleteStudentButtonElement;  
}

