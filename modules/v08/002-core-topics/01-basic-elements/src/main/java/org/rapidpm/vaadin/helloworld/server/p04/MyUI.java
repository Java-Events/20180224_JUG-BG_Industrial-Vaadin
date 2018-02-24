package org.rapidpm.vaadin.helloworld.server.p04;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.rapidpm.vaadin.helloworld.server.CoreUI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.function.Supplier;

/**
 *
 */
public class MyUI extends CoreUI {
  @Override
  public Supplier<Component> componentSupplier() {
    return ()-> {
      final TextField txt = new TextField("TextFieldName");
      txt.setValue("my Value");
      txt.addValueChangeListener(e -> {
        System.out.println("e = " + e);
      });
      return txt;
    };
  }

  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class CoreServlet extends VaadinServlet { }

  @Override
  public Class<? extends VaadinServlet> servletClass() {
    return MyUI.CoreServlet.class;
  }

  public static void main(String[] args) throws ServletException {
    new MyUI().startup();
  }
}
