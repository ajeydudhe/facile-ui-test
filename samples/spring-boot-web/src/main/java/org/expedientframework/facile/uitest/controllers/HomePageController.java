/********************************************************************
 * File Name:    HomePageController.java
 *
 * Date Created: 19-Oct-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.facile.uitest.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

  @GetMapping("/")
  public String homePage(final Model model) {
    
    model.addAttribute("appName", appName);
    
    return "home";
  }
  
  // Private members
  @Value("${spring.application.name}")
  private String appName;  
}

