package org.expedientframework.uitest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.expedientframework.uitest.core.UiTestContext;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

@ContextConfiguration(classes = TestConfiguration.class)
public class StudentDetailsPageTest extends AbstractTestNGSpringContextTests {
  
  @Test
  public void getStudentByID_studentExists_displaysDetails() {
    
    final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.studentController).build();

    WebDriver webDriver = null;
    try(UiTestContext uiTestContext = new UiTestContext(mockMvc)) {
      
      webDriver = createWebDriver(uiTestContext.getProxyPort());
      
      final String studentId = "MyStudent" + UUID.randomUUID().toString();
      final Student mockedStudent = createStudent("Mocked-" + UUID.randomUUID().toString());
      
      when(studentController.student(studentId)).thenReturn(mockedStudent);
      
      webDriver.get("http://localhost:8080/students/" + studentId);
      
      TestUtils.waitForAjaxCalls(webDriver);
      
      final StudentDetailsPage studentPage = PageFactory.initElements(webDriver, StudentDetailsPage.class);
      
      System.out.println(webDriver.getPageSource());
      
      assertThat(studentPage.getStudentId()).as("Student ID").isEqualTo(mockedStudent.getStudentId());
      assertThat(studentPage.getFirstName()).as("First Name").isEqualTo(mockedStudent.getFirstName());
      assertThat(studentPage.getLastName()).as("Last Name").isEqualTo(mockedStudent.getLastName());
      assertThat(studentPage.getAge()).as("Age").isEqualTo(mockedStudent.getAge());
    } finally {
      
      if(webDriver != null) {
        
        webDriver.close();
      }
    }
  }

  @BeforeClass
  public void setUp() {

    WebDriverManager.chromedriver().setup();
  }
  
  private WebDriver createWebDriver(final int proxyPort) {
    
    final ChromeOptions chromeOptions = new ChromeOptions();
    
    chromeOptions.setHeadless(true);    
    
    final String proxyAddress = "localhost:" + proxyPort;
    final Proxy proxy = new Proxy().setHttpProxy(proxyAddress).setSslProxy(proxyAddress);   
    
    chromeOptions.setProxy(proxy);
    
    // Required to use proxy for localhost address
    chromeOptions.addArguments("--proxy-bypass-list=" + "<-loopback>");    
    
    return new ChromeDriver(chromeOptions);
  }
  
  private static Student createStudent(final String studentId) {
    
    final Student student = new Student();
    
    student.setStudentId(studentId);
    student.setFirstName(studentId + "-firstName");
    student.setLastName(studentId + "-lastName");
    student.setAge(12);
    
    return student;
  }
   
  @Autowired
  private StudentController studentController;
}
