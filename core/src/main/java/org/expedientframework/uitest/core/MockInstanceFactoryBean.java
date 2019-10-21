/********************************************************************
 * File Name:    MockInstanceFactoryBean.java
 *
 * Date Created: 21-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.core;

import org.mockito.Mockito;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * 
 * Bean factory to create mock instance of the specified class.
 * 
 * @author ajey_dudhe
 *
 */
public class MockInstanceFactoryBean extends AbstractFactoryBean<Object> {

  public MockInstanceFactoryBean(final Class<?> classToMock) {
    
    this.classToMock = classToMock;
  }
  
  @Override
  public Class<?> getObjectType() {

    return this.classToMock;
  }

  @Override
  protected Object createInstance() throws Exception {
    
    return Mockito.mock(this.classToMock);
  }

  // Private members
  private Class<?> classToMock;
}

