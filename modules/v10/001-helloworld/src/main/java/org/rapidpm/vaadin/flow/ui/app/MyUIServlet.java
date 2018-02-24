package org.rapidpm.vaadin.flow.ui.app;

import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;
import org.rapidpm.dependencies.core.logger.HasLogger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false)
public class MyUIServlet extends VaadinServlet implements HasLogger {
  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);

  }

  @Override
  protected boolean serveStaticOrWebJarRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    logger().info("serveStaticOrWebJarRequest " + request);
    return super.serveStaticOrWebJarRequest(request, response);
  }

}
