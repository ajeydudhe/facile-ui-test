/********************************************************************
 * File Name:    MockRequestPredicate.java
 *
 * Date Created: 11-Nov-2019
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2019 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.uitest.core;

import java.util.Map;

public interface MockRequestPredicate {

  boolean shouldMock(final String httpMethodName, final String url, final Map<String, String> headers);
}

