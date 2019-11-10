/********************************************************************
 * File Name:    MockMvcRequestHandler.java
 *
 * Date Created: 09-Nov-2019
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

class MockMvcRequestHandler {

  private MockMvcRequestHandler(final MockHttpServletRequestBuilder requestBuilder) {
    
    this.requestBuilder = requestBuilder;
  }

  public static MockMvcRequestHandler create(final String contextPath, 
                                             final HttpRequest request, 
                                             final HttpMessageContents contents, 
                                             final HttpMessageInfo messageInfo) {

    
    try {

      LOG.info("Received request [({}) {}]", request.getMethod().name(), messageInfo.getOriginalUrl());
      
      final URI uri = new URI(messageInfo.getOriginalUrl());
      
      final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.request(request.getMethod().name(), uri);

      if(StringUtils.hasText(contextPath)) {
        
        requestBuilder.contextPath(contextPath);
      }
      //requestBuilder.locale(browserLocale);
      
      // Add headers
      for (Entry<String, String> header : request.headers())
      {
        requestBuilder.header(header.getKey(), header.getValue());
      }
      
      requestBuilder.content(contents.getBinaryContents());
      
      return new MockMvcRequestHandler(requestBuilder);
    } catch (URISyntaxException e) {

      LOG.error("An error occurred.", e);
      
      throw new RuntimeException("An error occured while parsing incoming request uri.", e); //TOD: Ajey - Throw custom exception or a base exception !!!
    }
  }
  
  public HttpResponse execute(final MockMvc mockMvc) {
    
    try {
      
      final MockHttpServletResponse mockMvcResponse = mockMvc.perform(this.requestBuilder).andReturn().getResponse();
      
      LOG.debug("MockMvc response: {}", mockMvcResponse.getContentAsString());
      
      return createResponse(mockMvcResponse);
      
    } catch (Exception e) {

      LOG.error("An error occurred while processing http request.", e);
      
      throw new RuntimeException("An error occurred while performing MockMvc request/response.", e); //TODO: Ajey - Throw custom exception !!!
    }
  }
  
  private static HttpResponse createResponse(final MockHttpServletResponse mockMvcResponse) {
    
    // Create response
    final HttpResponseStatus httpStatus = HttpResponseStatus.valueOf(mockMvcResponse.getStatus());
    
    final ByteBuf buffer = Unpooled.wrappedBuffer(mockMvcResponse.getContentAsByteArray());
    
    final HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpStatus, buffer);
    
    HttpHeaders.setContentLength(response, buffer.readableBytes());
    
    // Add response headers
    LOG.debug("MockMvc response headers: {}", mockMvcResponse.getHeaderNames());
    
    for (String headerName : mockMvcResponse.getHeaderNames()) {
      
      HttpHeaders.setHeader(response, headerName, mockMvcResponse.getHeaderValues(headerName));
    }
    
    return response;
  }
  
  // Private members
  private final MockHttpServletRequestBuilder requestBuilder;
  private static final Logger LOG = LoggerFactory.getLogger(MockMvcRequestHandler.class);
}

