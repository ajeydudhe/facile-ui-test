package org.expedientframework.uitest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.logging.Level;

import org.expedientframework.uitest.core.UiTestContext;
//import org.expedientframework.uitest.core.UiTestContext;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

@ContextConfiguration(classes = TestConfiguration.class)
public class StudentDetailsPageTest extends AbstractTestNGSpringContextTests {
  
  @Test
  public void getStudentByID_studentExists_displaysDetails() {
    
    assertThat(studentController).as("StudentController").isNotNull();
    
    final Student student = createStudent("MyStudent-" + UUID.randomUUID().toString());
    
    when(studentController.student(student.getStudentId())).thenReturn(student);
    
    this.webDriver.get("http://localhost:8080/students/" + student.getStudentId());
    
    System.out.println(this.webDriver.getPageSource());
    
    //final StudentDetailsPage studentPage = this.getPage("students/" + student.getStudentId(), StudentDetailsPage.class);
    
    //validate(student, studentPage);
  }

  @BeforeClass
  public void setUp() {

    WebDriverManager.chromedriver().setup();
    
    this.mockMvc = MockMvcBuilders.standaloneSetup(StudentController.class).build();
    
    this.uiTestContext = new UiTestContext(this.mockMvc);
    
    // Hook into http request
    createWebDriver(this.uiTestContext.getProxyPort());
  }
  
  @AfterClass
  public void tearDown() {
    
    this.webDriver.close();
    
    this.uiTestContext.close();
  }
  
  private void validate(final Student expected, final StudentDetailsPage studentPage) {
    
    assertThat(studentPage.getStudentId()).as("Student ID").isEqualTo(expected.getStudentId());
    assertThat(studentPage.getFirstName()).as("First Name").isEqualTo(expected.getFirstName());
    assertThat(studentPage.getLastName()).as("Last Name").isEqualTo(expected.getLastName());
    assertThat(studentPage.getAge()).as("Age").isEqualTo(expected.getAge());
  }

  private void createWebDriver(final int proxyPort) {
    
    if(this.webDriver != null) {
     
      this.webDriver.close();
    }
    
    final ChromeOptions chromeOptions = new ChromeOptions();
    
    chromeOptions.setHeadless(true);    
    
    final String proxyAddress = "localhost:" + proxyPort;
    final Proxy proxy = new Proxy().setHttpProxy(proxyAddress).setSslProxy(proxyAddress);   
    
    chromeOptions.setProxy(proxy);
    //chromeOptions.addArguments("--proxy-server=" + proxyAddress);
    // Required to use proxy for localhost address
    chromeOptions.addArguments("--proxy-bypass-list=" + "<-loopback>");    
    
    final LoggingPreferences loggingPreferences = new LoggingPreferences();
    loggingPreferences.enable(LogType.BROWSER, Level.ALL);
    
    chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);
    
    this.webDriver = new ChromeDriver(chromeOptions);
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
  
  private WebDriver webDriver;
  private MockMvc mockMvc;
  private UiTestContext uiTestContext;
}
