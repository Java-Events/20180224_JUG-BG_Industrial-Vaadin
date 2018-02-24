package org.rapidpm.vaadin.helloworld.server.p03;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import org.rapidpm.vaadin.helloworld.server.CoreUI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.function.Supplier;

/**
 *
 */
public class MyUI extends CoreUI {


  public Button btn(String caption) {
    Button button = new Button(caption);
    button.setSizeFull();
    return button;
  }


  public Supplier<Component> componentSupplier() {
    return () -> {
      final GridLayout grid4X4 = new GridLayout(4, 4);

      grid4X4.addComponent(btn("AA"), 0, 0);
      grid4X4.addComponent(btn("BA"), 1, 0);
      grid4X4.addComponent(btn("CA"), 2, 0);
      grid4X4.addComponent(btn("DA"), 3, 0);

      grid4X4.addComponent(btn("AB"), 0, 1);
      grid4X4.addComponent(btn("BB"), 1, 1);
      grid4X4.addComponent(btn("CB"), 2, 1);
      grid4X4.addComponent(btn("DB"), 3, 1);
      return grid4X4;
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