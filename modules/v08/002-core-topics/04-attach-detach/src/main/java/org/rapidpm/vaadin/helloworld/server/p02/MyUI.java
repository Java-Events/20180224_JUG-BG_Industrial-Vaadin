package org.rapidpm.vaadin.helloworld.server.p02;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
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

  public static final Set<Updater> registrations = newKeySet();

  public interface Updater {
    void update(String name, Integer age);
  }


  public static Registration register(Updater updater) {
    registrations.add(updater);
    return (Registration) () -> registrations.remove(updater);
  }

  public static class MyFormLayout extends FormLayout {

    private TextField name = new TextField("name");
    private TextField age  = new TextField("age");

    public MyFormLayout() {
      addComponents(name, age);
    }

    private Registration registration;

    public void register() {
      registration = MyUI.register((name, age) -> {
        MyFormLayout.this.name.setValue(name);
        MyFormLayout.this.age.setValue(String.valueOf(age));
      });
    }

    @Override
    public void detach() {
      super.detach();
      registration.remove();
    }
  }

  @Override
  public Supplier<Component> componentSupplier() {
    return () -> {

      final TextField name = new TextField("name");
      final TextField age  = new TextField("age");

      final Button btn = new Button("sync now");
      btn.addClickListener(e -> registrations.forEach(r -> r.update(name.getValue(),
                                                                    parseInt(age.getValue())
      )));

      final MyFormLayout myForm = new MyFormLayout();

      myForm.register();

      return new FormLayout(name, age, btn, myForm);
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
