/********************************************************************
 * File Name:    StudentController.java
 *
 * Date Created: 10-Nov-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentController {

  @GetMapping(value = "/{studentId}", produces = "application/json")
  @ResponseBody
  public Student student(final @PathVariable String studentId) {
      
    return null;
  }
}

