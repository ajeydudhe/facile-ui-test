/********************************************************************
 * File Name:    DefaultExceptionHandler.java
 *
 * Date Created: 03-Nov-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFound.class)
  public final ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
    
    return handleExceptionInternal(ex, ex.getMessage(), null, HttpStatus.NOT_FOUND, request);
  }
}

