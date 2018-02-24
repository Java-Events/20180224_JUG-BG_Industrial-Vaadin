package org.rapidpm.vaadin.helloworld.server.p01;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.rapidpm.vaadin.helloworld.server.CoreUI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.function.Supplier;

/**
 *
 */
public class MyUI extends CoreUI {


  public static class MyComponentA extends VerticalLayout {
    private TextField name        = new TextField("Name");
    private TextField description = new TextField("Description");

    public MyComponentA() {
      addComponents(name, description);
    }

    public MyComponentA(Component... children) {
      throw new RuntimeException("hhmm lack of inheritance ..");
    }
  }

  public static class MyComponentB extends Composite {



    private TextField name        = new TextField("Name");
    private TextField description = new TextField("Description");

    public MyComponentB() {
      setCompositionRoot(new VerticalLayout(name, description));
    }

  }


  @Override
  public Supplier<Component> componentSupplier() {
    return () -> new HorizontalLayout(new MyComponentA(), new MyComponentB());
  }


  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class CoreServlet extends VaadinServlet {
  }

  @Override
  public Class<? extends VaadinServlet> servletClass() {
    return CoreServlet.class;
  }

  public static void main(String[] args) throws ServletException {
    new MyUI().startup();
  }
}
