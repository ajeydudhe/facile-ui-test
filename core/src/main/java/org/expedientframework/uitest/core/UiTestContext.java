/********************************************************************
 * File Name:    UiTestContext.java
 *
 * Date Created: 21-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.core;

import java.io.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

/**
 * The context class for executing tests using mock http requests and MockMvc.
 * 
 * @author ajey_dudhe
 *
 */
public class UiTestContext implements Closeable {

  public UiTestContext(final MockMvc mockMvc) {
    
    this.mockMvc = mockMvc;
    
    this.httpProxy = new BrowserMobProxyServer();
    
    addHttpRequestFilter();
    
    this.httpProxy.start();
  }
  
  public int getProxyPort() {
  
    return this.httpProxy.getPort();
  }
  
  public void close() {
    
    this.httpProxy.stop();
  }

  private void addHttpRequestFilter() {
    
    this.httpProxy.addRequestFilter((request, contents, messageInfo) -> {
      
      return processRequest(request, contents, messageInfo);
    });
  }

  private HttpResponse processRequest(final HttpRequest request, final HttpMessageContents contents, final HttpMessageInfo messageInfo) {
    
    try {
      
      LOG.info("Received request [({}) {}]", request.getMethod().name(), messageInfo.getOriginalUrl());
      
      final RequestBuilder requestBuilder = MockMvcUtils.createRequestBuilder(request, contents, messageInfo);
      
      final MockHttpServletResponse mockMvcResponse = mockMvc.perform(requestBuilder).andReturn().getResponse();
      
      LOG.debug("MockMvc response: {}", mockMvcResponse.getContentAsString());
      
      return MockMvcUtils.createResponse(mockMvcResponse);
      
    } catch (Exception e) {

      LOG.error("An error occurred while processing http request.", e);
      
      throw new RuntimeException("An error occurred while performing MockMvc request/response.", e); //TODO: Ajey - Throw custom exception !!!
    }
  }

  // Private members
  private final MockMvc mockMvc;
  private final BrowserMobProxy httpProxy;
  private static final Logger LOG = LoggerFactory.getLogger(UiTestContext.class);
}

