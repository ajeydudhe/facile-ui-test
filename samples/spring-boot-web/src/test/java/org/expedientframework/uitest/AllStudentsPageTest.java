package org.expedientframework.uitest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.UUID;

import org.expedientframework.uitest.controllers.StudentController;
import org.expedientframework.uitest.pages.AllStudentsPage;
import org.expedientframework.uitest.pages.StudentDetailsPage;
import org.expedientframework.uitest.students.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class AllStudentsPageTest extends AbstractPageTest {
  
  //TODO: Add the test to check no. of times the API is being called.
  
  @Test
  public void students_displaysSingleStudent() {
    
    assertThat(studentController).as("StudentController").isNotNull();
    
    final String studentId = "MyStudent-" + UUID.randomUUID().toString();
    final Student student = StudentController.createStudent(studentId);
    when(studentController.getAllStudents()).thenReturn(Arrays.asList(student));
    
    final AllStudentsPage page = this.getPage("students", AllStudentsPage.class);
    
    assertThat(page.getStudents()).as("Students Size").hasSize(1);
    
    final StudentDetailsPage studentPage = page.getStudents().get(0);
    
    assertThat(studentPage.getStudentId()).as("Student ID").isEqualTo(student.getStudentId());
    assertThat(studentPage.getFirstName()).as("First Name").isEqualTo(student.getFirstName());
    assertThat(studentPage.getLastName()).as("Last Name").isEqualTo(student.getLastName());
    assertThat(studentPage.getAge()).as("Age").isEqualTo(student.getAge());
  }
  
  @Autowired
  private StudentController studentController;
}
