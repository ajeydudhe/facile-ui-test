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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
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
      
      final MvcResult result = mockMvc.perform(requestBuilder).andReturn();

      LOG.debug("MockMvc response: {}", result.getResponse().getContentAsString());
      
      final HttpResponseStatus httpStatus = HttpResponseStatus.valueOf(result.getResponse().getStatus());
      
      final ByteBuf buffer = Unpooled.wrappedBuffer(result.getResponse().getContentAsByteArray());
      
      final HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpStatus, buffer);
      
      HttpHeaders.setContentLength(response, buffer.readableBytes());
      
      String contentType = result.getResponse().getContentType();
      
      if(contentType == null) {
        
        contentType = MediaType.APPLICATION_JSON_VALUE;

        LOG.warn("### contentType was null and set to [{}]", contentType);        
      }
        
      HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, contentType);
      
      return response;
    } catch (Exception e) {

      LOG.error("An error occurred while processing http request.", e);
      
      throw new RuntimeException(e); //TODO: Ajey - Throw custom exception !!!
    }
  }

  // Private members
  private final MockMvc mockMvc;
  private final BrowserMobProxy httpProxy;
  private final static Logger LOG = LoggerFactory.getLogger(UiTestContext.class);
}

