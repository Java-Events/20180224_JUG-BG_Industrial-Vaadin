package org.rapidpm.vaadin.helloworld.server.p02;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import org.rapidpm.vaadin.helloworld.server.CoreUI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.Set;
import java.util.function.Supplier;

import static java.lang.Integer.parseInt;
import static java.util.concurrent.ConcurrentHashMap.newKeySet;

/**
 *
 */
public class MyUI extends CoreUI {



  @Override
  public Supplier<Component> componentSupplier() {
    return () -> {
      return new Label("not yet implemented");
    };
  }


  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class CoreServlet extends VaadinServlet { }

  @Override
  public Class<? extends VaadinServlet> servletClass() {
    return CoreServlet.class;
  }

  public static void main(String[] args) throws ServletException {
    new MyUI().startup();
  }

}
