/********************************************************************
 * File Name:    StudentController.java
 *
 * Date Created: 25-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.controllers;

import java.util.ArrayList;
import java.util.List;

import org.expedientframework.uitest.students.Student;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequestMapping("students")
public class StudentController {

  @GetMapping(produces = "text/html")
  public String allStudentsPage() {
    
    return "students";
  }

  @GetMapping(produces = "application/json")
  @ResponseBody
  public List<Student> getAllStudents() {
    
    final ArrayList<Student> students = new ArrayList<>();
    
    students.add(createStudent("student-01"));
    students.add(createStudent("student-02"));
    students.add(createStudent("student-03"));
    
    return students;
  }

  @GetMapping(value = "/{studentId}", produces = "text/html")
  public String studentPage(final @PathVariable String studentId) {
    
    return "student.details";
  }

  @GetMapping(value = "/{studentId}", produces = "application/json")
  @ResponseBody
  public Student student(final @PathVariable String studentId) {
      
    if(studentId.startsWith("NotExists")) {
      
      throw HttpClientErrorException.create(HttpStatus.NOT_FOUND, String.format("Student '%s' not found.", studentId), null, null, null);
    }
    
    return createStudent(studentId);
  }

  public static Student createStudent(final String studentId) {
    
    final Student student = new Student();
    
    student.setStudentId(studentId);
    student.setFirstName(studentId + "-firstName");
    student.setLastName(studentId + "-lastName");
    student.setAge(12);
    
    return student;
  }
}

