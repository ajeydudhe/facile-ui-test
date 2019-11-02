package org.expedientframework.uitest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.expedientframework.uitest.controllers.StudentController;
import org.expedientframework.uitest.pages.StudentDetailsPage;
import org.expedientframework.uitest.students.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.annotations.Test;

public class StudentDetailsPageTest extends AbstractPageTest {
  
  //TODO: Add the test to check no. of times the API is being called.
  
  @Test
  public void getStudentByID_doesNotExists_displaysErrorMessage() {
    
    assertThat(studentController).as("StudentController").isNotNull();

    final Student student = StudentController.createStudent("NotExists-" + UUID.randomUUID().toString());

    when(studentController.studentPage(student.getStudentId())).thenReturn("student.details");
    when(studentController.student(student.getStudentId())).thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, String.format("Student '%s' not found.", student.getStudentId()), null, null, null));
    
    final StudentDetailsPage studentPage = this.getPage("students/" + student.getStudentId(), StudentDetailsPage.class);

    assertThat(studentPage.getErrorMessage()).as("Error message").isEqualTo(String.format("404 Student '%s' not found.", student.getStudentId()));
  }
  
  @Test
  public void getStudentByID_studentExists_displaysDetails() {
    
    assertThat(studentController).as("StudentController").isNotNull();
    
    final Student student = StudentController.createStudent("MyStudent-" + UUID.randomUUID().toString());
    
    when(studentController.studentPage(student.getStudentId())).thenReturn("student.details");
    when(studentController.student(student.getStudentId())).thenReturn(student);
    
    final StudentDetailsPage studentPage = this.getPage("students/" + student.getStudentId(), StudentDetailsPage.class);
    
    assertThat(studentPage.getStudentId()).as("Student ID").isEqualTo(student.getStudentId());
    assertThat(studentPage.getFirstName()).as("First Name").isEqualTo(student.getFirstName());
    assertThat(studentPage.getLastName()).as("Last Name").isEqualTo(student.getLastName());
    assertThat(studentPage.getAge()).as("Age").isEqualTo(student.getAge());
  }

  @Autowired
  private StudentController studentController;
}
