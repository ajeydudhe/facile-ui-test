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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Closeable;
import java.net.URI;
import java.net.URL;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

/**
 * TODO: Update with a detailed description of the interface/class.
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
      
      final URI uri = new URI(messageInfo.getOriginalUrl());
      
      final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri.getPath());
      
      //TODO: Ajey - Need to handle application context !!!
      //requestBuilder.contextPath("appPath");            
      
      for (Entry<String, String> header : request.headers())
      {
        requestBuilder.header(header.getKey(), header.getValue());
      }

      //requestBuilder.locale(browserLocale);
      
      final MvcResult result = mockMvc.perform(requestBuilder).andReturn();

      final HttpResponseStatus httpStatus = HttpResponseStatus.valueOf(result.getResponse().getStatus());
      
      final ByteBuf buffer = Unpooled.wrappedBuffer(result.getResponse().getContentAsByteArray());
      final HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpStatus, buffer);
      HttpHeaders.setContentLength(response, buffer.readableBytes());
      
      String contentType = result.getResponse().getContentType();
      
      //LOGGER.info("### ContentType [{}] : [{}]", contentType, relativeUrl);
      /*
      if(contentType == null) // Content type is coming as null for somejavascript.min.js Check for file extension
      {
        contentType = "application/javascript";
      }*/
      
      //LOGGER.info("### After: ContentType [{}] : [{}]", contentType, relativeUrl);

      HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, contentType == null ? "text/html" : contentType);
      
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
