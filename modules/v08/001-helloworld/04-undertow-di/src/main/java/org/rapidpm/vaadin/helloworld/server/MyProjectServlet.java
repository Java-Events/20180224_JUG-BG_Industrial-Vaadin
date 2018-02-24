package org.rapidpm.vaadin.helloworld.server;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import javax.servlet.annotation.WebServlet;

/**
 *
 */
@WebServlet(urlPatterns = "/*")
@VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
public class MyProjectServlet extends VaadinServlet { }
