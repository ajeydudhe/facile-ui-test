/********************************************************************
 * File Name:    AllStudentsPage.java
 *
 * Date Created: 01-Nov-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AllStudentsPage {

  public AllStudentsPage(final WebDriver webDriver) {
    
    final List<WebElement> elements = webDriver.findElements(By.xpath("//table[@id='allStudents']/tr"));
    
    for (int nIndex = 1; nIndex < elements.size(); ++nIndex) { // Ignore first row as it is header
      
      final WebElement currentStudentElement = elements.get(nIndex);
      
      final StudentDetailsPage student = new StudentDetailsPage();
      
      student.setStudentIdElement(currentStudentElement.findElement(By.id("studentId")));
      student.setFirstNameElement(currentStudentElement.findElement(By.id("firstName")));
      student.setLastNameElement(currentStudentElement.findElement(By.id("lastName")));
      student.setAgeElement(currentStudentElement.findElement(By.id("age")));
      
      students.add(student);
    }
  }
  
  public List<StudentDetailsPage> getStudents() {
    
    return this.students;
  }
  
  // Private members
  final List<StudentDetailsPage> students = new ArrayList<>();
}

