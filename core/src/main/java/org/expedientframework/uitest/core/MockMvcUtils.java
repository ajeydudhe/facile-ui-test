/********************************************************************
 * File Name:    MockMvcUtils.java
 *
 * Date Created: 24-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

class MockMvcUtils {

  public static RequestBuilder createRequestBuilder(final HttpRequest request, final HttpMessageContents contents, final HttpMessageInfo messageInfo) {
      
    try {

      LOG.info("Received request [({}) {}]", request.getMethod().name(), messageInfo.getOriginalUrl());
      
      final URI uri = new URI(messageInfo.getOriginalUrl());
      
      final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.request(request.getMethod().name(), uri);

      //TODO: Ajey - Need to handle application context !!!
      //requestBuilder.contextPath("appPath");            
      //requestBuilder.locale(browserLocale);
      
      // Add headers
      for (Entry<String, String> header : request.headers())
      {
        requestBuilder.header(header.getKey(), header.getValue());
      }
      
      return requestBuilder;        
    } catch (URISyntaxException e) {

      LOG.error("An error occurred.", e);
      
      throw new RuntimeException("An error occured while parsing incoming request uri.", e); //TOD: Ajey - Throw custom exception or a base exception !!!
    }
  }
  
  // Private members
  private final static Logger LOG = LoggerFactory.getLogger(MockMvcUtils.class);
}

