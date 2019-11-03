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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Student createStudent(final @RequestBody Student student) {
    
    student.setStudentId("Created-" + student.getStudentId());
    student.setFirstName("Created-" + student.getFirstName());
    student.setLastName("Created-" + student.getLastName());
    student.setAge(1 + student.getAge());
    
    return student;
  }  

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Student updateStudent(final @RequestBody Student student) {
    
    student.setStudentId("Updated-" + student.getStudentId());
    student.setFirstName("Updated-" + student.getFirstName());
    student.setLastName("Updated-" + student.getLastName());
    student.setAge(student.getAge() - 1);
    
    return student;
  }  

  @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Student deleteStudent(final @RequestBody Student student) {
    
    student.setStudentId("Deleted-" + student.getStudentId());
    student.setFirstName("Deleted-" + student.getFirstName());
    student.setLastName("Deleted-" + student.getLastName());
    student.setAge(0);
    
    return student;
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

