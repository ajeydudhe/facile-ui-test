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

import org.expedientframework.uitest.core.beans.MockInstanceBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

  @Bean
  public MockInstanceBeanFactoryPostProcessor mockInstanceBeanFactoryPostProcessor() {
    
    return new MockInstanceBeanFactoryPostProcessor(StudentController.class);
  }
}

