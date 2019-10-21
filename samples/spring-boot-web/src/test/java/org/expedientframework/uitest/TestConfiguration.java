/********************************************************************
 * File Name:    TestConfiguration.java
 *
 * Date Created: 21-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest;

import org.expedientframework.uitest.controllers.HelloWorldController;
import org.expedientframework.uitest.controllers.HomePageController;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration implements BeanPostProcessor {

  @Bean
  public HelloWorldController helloWorldController() {
    
    return Mockito.mock(HelloWorldController.class);
  }
  
  //@Bean
  public HomePageController homePageController() {
    
    return new HomePageController();
  }
}

