package org.rapidpm.vaadin.flow.helloworld.server;

import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 *
 */
@WebServlet(urlPatterns = "/helloWorld/*", name = "MyProjectServlet", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false)
public class MyProjectServlet extends VaadinServlet {

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("servletConfig = " + servletConfig);
  }
}
