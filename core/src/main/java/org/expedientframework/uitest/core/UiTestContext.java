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
import org.springframework.test.web.servlet.MockMvc;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;

/**
 * The context class for executing tests using mock http requests and MockMvc.
 * 
 * @author ajey_dudhe
 *
 */
public class UiTestContext implements Closeable {

  public UiTestContext(final MockMvc mockMvc) {

    this(mockMvc, null);
  }
  
  public UiTestContext(final MockMvc mockMvc, final String contextPath) {
    
    this.mockMvc = mockMvc;
    
    this.contextPath = contextPath;
    
    this.httpProxy = new BrowserMobProxyServer();
    
    addHttpRequestFilter();
    
    this.httpProxy.start();
  }

  public void shouldMock(final MockRequestPredicate mockRequestPredicate) {
    
    this.mockRequestPredicate = mockRequestPredicate;
  }
  
  public int getProxyPort() {
  
    return this.httpProxy.getPort();
  }
  
  public void close() {
    
    this.httpProxy.stop();
  }

  private void addHttpRequestFilter() {
    
    this.httpProxy.addRequestFilter((request, contents, messageInfo) -> {
      
      return MockMvcRequestHandler.create(this.contextPath, request, contents, messageInfo, this.mockRequestPredicate)
                                  .execute(this.mockMvc);
    });
  }

  // Private members
  private final MockMvc mockMvc;
  private final BrowserMobProxy httpProxy;
  private final String contextPath;
  private MockRequestPredicate mockRequestPredicate;
}

