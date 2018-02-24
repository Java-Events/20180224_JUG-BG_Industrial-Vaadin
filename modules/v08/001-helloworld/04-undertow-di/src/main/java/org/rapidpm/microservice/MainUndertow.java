package org.rapidpm.microservice;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.*;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.rapidpm.ddi.DI;
import org.rapidpm.ddi.reflections.ReflectionUtils;
import org.rapidpm.dependencies.core.logger.Logger;
import org.rapidpm.dependencies.core.logger.LoggingService;
import org.rapidpm.microservice.optionals.ActiveUrlsDetector;
import org.rapidpm.microservice.optionals.header.ActiveUrlPrinter;
import org.rapidpm.microservice.optionals.header.HeaderScreenPrinter;
import org.rapidpm.microservice.servlet.ServletInstanceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.EventListener;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.undertow.servlet.Servlets.*;
import static org.rapidpm.microservice.ServletContainerFunctions.addStagemonitor;

/**
 * Copyright (C) 2010 RapidPM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by RapidPM - Team on 08.08.16.
 */
public class MainUndertow {

  private MainUndertow() {
  }

  private static final LoggingService LOGGER = Logger.getLogger(MainUndertow.class);

  public static final String DEFAULT_FILTER_MAPPING       = "/*";
  public static final String MYAPP                        = "/microservice";
  public static final String CONTEXT_PATH_REST            = "/rest";
  public static final int    DEFAULT_REST_PORT            = 7081;
  public static final int    DEFAULT_SERVLET_PORT         = 7080;
  public static final String SERVLET_PORT_PROPERTY        = "org.rapidpm.microservice.servlet.port";
  public static final String SERVLET_HOST_PROPERTY        = "org.rapidpm.microservice.servlet.host";
  public static final String STAGEMONITOR_ACTIVE_PROPERTY = "org.rapidpm.microservice.security.stagemonitor.active";


  private static Undertow undertowServer;

  private static Optional<String[]> cliArguments; //TODO switch to Result

  public static void deploy() {
    deploy(Optional.empty());
  }

  public static void deploy(Optional<String[]> args) {
    cliArguments = args;
    final Builder builder = Undertow.builder() //TODO
                                    .setDirectBuffers(true)
                                    .setServerOption(UndertowOptions.ENABLE_HTTP2, true);

    // deploy servlets
    DeploymentInfo deploymentInfo = createServletDeploymentInfos();
    final boolean  anyServlets    = !deploymentInfo.getServlets().isEmpty();
    if (anyServlets) {
      try {
        deployServlets(builder, deploymentInfo);
      } catch (ServletException e) {
        e.printStackTrace();
        LOGGER.warning("deploy Servlets ", e);
      }
    }

    undertowServer = builder.build();
    undertowServer.start();
    new HeaderScreenPrinter().printOnScreen();
    new ActiveUrlPrinter().printActiveURLs(new ActiveUrlsDetector().detectUrls());
  }


  //TODO extend for a Vaadin App Version
  private static DeploymentInfo createServletDeploymentInfos() {

    final Set<Class<?>> typesAnnotatedWith = DI.getTypesAnnotatedWith(WebServlet.class, true);

    final List<ServletInfo> servletInfos = typesAnnotatedWith
        .stream()
        .filter(s -> new ReflectionUtils().checkInterface(s, HttpServlet.class))
        .map(c -> {
          Class<HttpServlet> servletClass = (Class<HttpServlet>) c;
          final ServletInfo  servletInfo  = servlet(c.getSimpleName(), servletClass, new ServletInstanceFactory<>(servletClass));
          if (c.isAnnotationPresent(WebInitParam.class)) {
            final WebInitParam[] annotationsByType = c.getAnnotationsByType(WebInitParam.class);
            for (WebInitParam webInitParam : annotationsByType) {
              final String value = webInitParam.value();
              final String name  = webInitParam.name();
              servletInfo.addInitParam(name, value);
            }
          }
          final WebServlet annotation  = c.getAnnotation(WebServlet.class);
          final String[]   urlPatterns = annotation.urlPatterns();
          for (String urlPattern : urlPatterns) {
            servletInfo.addMapping(urlPattern);
          }
          servletInfo.setAsyncSupported(annotation.asyncSupported());
          return servletInfo;
        })
        .filter(servletInfo -> !servletInfo.getMappings().isEmpty())
        .collect(Collectors.toList());

    final Set<Class<?>> weblisteners = DI.getTypesAnnotatedWith(WebListener.class);
    final List<ListenerInfo> listenerInfos = weblisteners.stream()
                                                         .map(c -> new ListenerInfo((Class<? extends EventListener>) c))
                                                         .collect(Collectors.toList());


    final DeploymentInfo deploymentInfo = deployment()
        .setClassLoader(Main.class.getClassLoader())
        .setContextPath(MYAPP)
        .setDeploymentName("ROOT" + ".war")
        .setDefaultEncoding("UTF-8");


    final Boolean stagemonitorActive = Boolean.valueOf(System.getProperty(STAGEMONITOR_ACTIVE_PROPERTY, "false"));
    if (stagemonitorActive) addStagemonitor().apply(deploymentInfo);

    return deploymentInfo
        .addListeners(listenerInfos)
        .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, new WebSocketDeploymentInfo())
        .addServlets(servletInfos);
  }

  static void deployServlets(final Builder builder, final DeploymentInfo deploymentInfo) throws ServletException {
    final ServletContainer  servletContainer = defaultContainer();
    final DeploymentManager manager          = servletContainer.addDeployment(deploymentInfo);
    manager.deploy();
    final HttpHandler servletHandler = manager.start();
    final PathHandler pathServlet = Handlers
        .path(Handlers.redirect(MYAPP))
        .addPrefixPath(MYAPP, servletHandler);
    final String realServletPort = System.getProperty(SERVLET_PORT_PROPERTY, DEFAULT_SERVLET_PORT + "");
    final String realServletHost = System.getProperty(SERVLET_HOST_PROPERTY, Main.DEFAULT_HOST);

    builder.addHttpListener(Integer.parseInt(realServletPort), realServletHost, pathServlet);
  }


  public static void stop() {
    try {
      undertowServer.stop();
    } catch (Exception e) {
      LOGGER.warning("undertowServer.stop()", e);
    }
  }
}
