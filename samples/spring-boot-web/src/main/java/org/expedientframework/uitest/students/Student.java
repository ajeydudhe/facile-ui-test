/********************************************************************
 * File Name:    Student.java
 *
 * Date Created: 26-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.students;
  
public class Student {

  public String getFirstName() {
  
    return this.firstName;
  }
  
  public void setFirstName(final String firstName) {
    
    this.firstName = firstName;
  }
  
  public String getLastName() {
    
    return this.lastName;
  }
  
  public void setLastName(final String lastName) {
    
    this.lastName = lastName;
  }
  
  public int getAge() {
    
    return this.age;
  }
  
  public void setAge(final int age) {
    
    this.age = age;
  }
  
  public String getStudentId() {
    
    return this.studentId;
  }

  public void setStudentId(final String studentId) {
    
    this.studentId = studentId;
  }

  @Override
  public int hashCode() {

    return this.studentId.hashCode();
  }
  
  @Override
  public boolean equals(final Object obj) {

    if(obj instanceof Student) {
      
      final Student target = (Student) obj;
      return this.studentId.equals(target.studentId);
    }
    
    return false;
  }
  
  // Private members
  private String studentId;
  private String firstName;
  private String lastName;
  private int age;
}

