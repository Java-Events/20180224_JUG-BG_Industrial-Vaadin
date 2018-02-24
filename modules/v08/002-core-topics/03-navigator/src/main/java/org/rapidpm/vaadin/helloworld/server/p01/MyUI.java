package org.rapidpm.vaadin.helloworld.server.p01;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
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

  private Navigator navigator;


  @Override
  protected void init(VaadinRequest request) {
    super.init(request);
    navigator = new Navigator(this, this);
    navigator.addView("", this.new MyViewA());
    navigator.addView("B", this.new MyViewB());
  }

  public class MyViewA extends Composite implements View {

    public MyViewA() {
      final Button buttonBack = new Button("Back");
      buttonBack.addClickListener(e -> {
        navigator.navigateTo("");

      });
      final Button buttonB = new Button("B");
      buttonB.addClickListener(e -> {
        navigator.navigateTo("B");

      });
      setCompositionRoot(new HorizontalLayout(buttonBack,buttonB));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
      Notification.show("My Viw A ", Notification.Type.WARNING_MESSAGE);
    }
  }

  public class MyViewB extends Composite implements View {

    public MyViewB() {
      final Button buttonBack = new Button("Back");
      buttonBack.addClickListener(e -> {
        navigator.navigateTo("");

      });
      final Button buttonA = new Button("A");
      buttonA.addClickListener(e -> {
        navigator.navigateTo("");

      });
      setCompositionRoot(new VerticalLayout(buttonBack,buttonA));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
      Notification.show("My Viw B ", Notification.Type.WARNING_MESSAGE);
    }
  }


  @Override
  public Supplier<Component> componentSupplier() {
    return () -> {
      return new MyViewA();
    };
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
