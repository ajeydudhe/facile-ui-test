/********************************************************************
 * File Name:    HelloWorldController.java
 *
 * Date Created: 21-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("hello")
public class HelloWorldController {

  @GetMapping
  @ResponseBody
  public String message() {
    
    return "Hello World !!!";
  }

  @GetMapping("/greet/{person}")
  @ResponseBody
  public String greet(final @PathVariable String person) {
    
    return String.format("Hello '%s' !!!", person);
  }

  @GetMapping("/occasion/{person}")
  @ResponseBody
  public String greetOnOccasion(final @PathVariable String person,
                                final @RequestParam String occasion) {
    
    return String.format("Happy %s '%s' !!!", occasion, person);
  }
}

