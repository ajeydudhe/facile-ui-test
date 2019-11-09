/********************************************************************
 * File Name:    MockInstanceBeanFactoryPostProcessor.java
 *
 * Date Created: 21-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.core.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.lang.NonNull;

/**
 * 
 * This class registers bean factory for specified class which returns the Mockito.mock(class) instance for mocking.
 * 
 * <b>Note:</b> If the bean definition already exists then it will be overridden else a new bean definition will be added. 
 * 
 * @author ajey_dudhe
 *
 */
public class MockInstanceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  public MockInstanceBeanFactoryPostProcessor(@NonNull final Class<?> ... classesToHaveMockInstance) {
    
    this.classesToHaveMockInstance = classesToHaveMockInstance;
  }
  
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {

    for (Class<?> classToMock : classesToHaveMockInstance) {
     
      registerMock(classToMock, beanFactory);
    }    
  }

  private void registerMock(final Class<?> classToMock, 
                            final ConfigurableListableBeanFactory beanFactory) {

    final BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MockInstanceFactoryBean.class)
                                                               .addConstructorArgValue(classToMock)
                                                               .getBeanDefinition();
    
    final String[] beanNames = beanFactory.getBeanNamesForType(classToMock);
    
    if(beanNames == null || beanNames.length == 0) {
      
      registerBean("facile.mock." + classToMock.getName(), beanDefinition, classToMock, (BeanDefinitionRegistry) beanFactory);
      
      return;
    }
    
    for (String beanName : beanNames) {

      LOG.info("Overriding bean definition for '{}'.", classToMock.getName());
      
      registerBean(beanName, beanDefinition, classToMock, (BeanDefinitionRegistry) beanFactory);
    }
  }

  private void registerBean(final String beanName,
                            final BeanDefinition beanDefinition,
                            final Class<?> classToMock,
                            final BeanDefinitionRegistry beanRegistry) {
    
    beanRegistry.registerBeanDefinition(beanName, beanDefinition);
    
    LOG.info("Registering '{}' for class '{}' to return a Mockito.mock() instance.", MockInstanceFactoryBean.class.getName(), classToMock.getName());
  }

  // Private members
  private final Class<?>[] classesToHaveMockInstance;
  
  private final static Logger LOG = LoggerFactory.getLogger(MockInstanceBeanFactoryPostProcessor.class);
}

