package org.rapidpm.vaadin.helloworld.server;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.ServletException;
import java.util.function.Supplier;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.redirect;
import static io.undertow.servlet.Servlets.servlet;

/**
 *
 */
public abstract class CoreUI extends UI {

  public abstract Supplier<Component> componentSupplier();

  @Override
  protected void init(VaadinRequest request) {
    setContent(componentSupplier().get());
  }

  public static final String CONTEXT_PATH = "/";

  public abstract Class<? extends VaadinServlet> servletClass();

  public void startup() throws ServletException {
    DeploymentInfo servletBuilder
        = Servlets.deployment()
                  .setClassLoader(CoreUI.class.getClassLoader())
                  .setContextPath(CONTEXT_PATH)
                  .setDeploymentName("ROOT.war")
                  .setDefaultEncoding("UTF-8")
                  .addServlets(
                      servlet(
                          servletClass().getSimpleName(),
                          servletClass()
                      ).addMapping("/*")
                       .setAsyncSupported(true)
                  );

    final DeploymentManager manager = Servlets
        .defaultContainer()
        .addDeployment(servletBuilder);
    manager.deploy();
    PathHandler path = path(redirect(CONTEXT_PATH))
        .addPrefixPath(CONTEXT_PATH, manager.start());
    Undertow.builder()
            .addHttpListener(8080, "0.0.0.0")
            .setHandler(path)
            .build()
            .start();
  }
}
