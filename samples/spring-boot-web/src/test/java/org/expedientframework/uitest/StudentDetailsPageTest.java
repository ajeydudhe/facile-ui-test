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
    
    validate(student, studentPage);
  }

  @Test
  public void createStudent_studentCreated() {
    
    assertThat(studentController).as("StudentController").isNotNull();
    
    final Student dummyStudent = StudentController.createStudent("MyDummyStudent-" + UUID.randomUUID().toString());
    final Student createdStudent = StudentController.createStudent("MyCreatedStudent-" + UUID.randomUUID().toString());
    
    when(studentController.studentPage(dummyStudent.getStudentId())).thenReturn("student.details");
    when(studentController.student(dummyStudent.getStudentId())).thenReturn(dummyStudent);
    
    // Load the page with dummy student details and validate it.
    final StudentDetailsPage studentPage = this.getPage("students/" + dummyStudent.getStudentId(), StudentDetailsPage.class);
    validate(dummyStudent, studentPage);
    
    // When create is called we return different student object which we validate.
    when(studentController.createStudent(dummyStudent)).thenReturn(createdStudent);
    studentPage.createStudent();
    
    validate(createdStudent, studentPage);
  }

  @Test
  public void updateStudent_studentUpdated() {
    
    assertThat(studentController).as("StudentController").isNotNull();
    
    final Student dummyStudent = StudentController.createStudent("MyDummyStudent-" + UUID.randomUUID().toString());
    final Student updatedStudent = StudentController.createStudent("MyUpdatedStudent-" + UUID.randomUUID().toString());
    
    when(studentController.studentPage(dummyStudent.getStudentId())).thenReturn("student.details");
    when(studentController.student(dummyStudent.getStudentId())).thenReturn(dummyStudent);
    
    // Load the page with dummy student details and validate it.
    final StudentDetailsPage studentPage = this.getPage("students/" + dummyStudent.getStudentId(), StudentDetailsPage.class);
    validate(dummyStudent, studentPage);
    
    // When update is called we return different student object which we validate.
    when(studentController.updateStudent(dummyStudent)).thenReturn(updatedStudent);
    studentPage.updateStudent();
    
    validate(updatedStudent, studentPage);
  }

  @Test
  public void deleteStudent_studentDeleted() {
    
    assertThat(studentController).as("StudentController").isNotNull();
    
    final Student dummyStudent = StudentController.createStudent("MyDummyStudent-" + UUID.randomUUID().toString());
    final Student deletedStudent = StudentController.createStudent("MyUpdatedStudent-" + UUID.randomUUID().toString());
    
    when(studentController.studentPage(dummyStudent.getStudentId())).thenReturn("student.details");
    when(studentController.student(dummyStudent.getStudentId())).thenReturn(dummyStudent);
    
    // Load the page with dummy student details and validate it.
    final StudentDetailsPage studentPage = this.getPage("students/" + dummyStudent.getStudentId(), StudentDetailsPage.class);
    validate(dummyStudent, studentPage);
    
    // When update is called we return different student object which we validate.
    when(studentController.deleteStudent(dummyStudent)).thenReturn(deletedStudent);
    studentPage.deleteStudent();
    
    validate(deletedStudent, studentPage);
  }
  
  private void validate(final Student expected, final StudentDetailsPage studentPage) {
    
    assertThat(studentPage.getStudentId()).as("Student ID").isEqualTo(expected.getStudentId());
    assertThat(studentPage.getFirstName()).as("First Name").isEqualTo(expected.getFirstName());
    assertThat(studentPage.getLastName()).as("Last Name").isEqualTo(expected.getLastName());
    assertThat(studentPage.getAge()).as("Age").isEqualTo(expected.getAge());
  }

  @Autowired
  private StudentController studentController;
}
