/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.rapidpm.microservice.optionals;


import org.rapidpm.ddi.DI;
import org.rapidpm.microservice.MainUndertow;

import javax.servlet.annotation.WebServlet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static java.lang.System.getProperty;
import static org.rapidpm.microservice.Main.DEFAULT_HOST;
import static org.rapidpm.microservice.MainUndertow.*;

public class ActiveUrlsDetector {


  public ActiveUrlsHolder detectUrls() {
    final ActiveUrlsHolder activeUrlsHolder = new ActiveUrlsHolder();

    //print all URLs
    final Set<Class<?>> typesAnnotatedWith = DI.getTypesAnnotatedWith(WebServlet.class);

    long servletCount = typesAnnotatedWith.stream()
        .map(aClass1 -> aClass1.getAnnotation(WebServlet.class))
        .filter(ws -> ws.urlPatterns().length > 0)
        .count();

    activeUrlsHolder.setServletCount(servletCount);

//

    final String realServletPort = getProperty(SERVLET_PORT_PROPERTY, DEFAULT_SERVLET_PORT + "");
    final String realServletHost = getProperty(SERVLET_HOST_PROPERTY, DEFAULT_HOST);

    typesAnnotatedWith
        .stream()
        .map(c -> c.getAnnotation(WebServlet.class))
        .map(WebServlet::urlPatterns)
        .map(Arrays::asList)
        .flatMap(Collection::stream)
        .forEach(url -> activeUrlsHolder.addServletUrl("http://" + realServletHost + ":" + realServletPort + MainUndertow.MYAPP + url));

    return activeUrlsHolder;
  }

}
