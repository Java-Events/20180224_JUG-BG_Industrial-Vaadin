package org.rapidpm.vaadin.helloworld.server.p01;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.rapidpm.vaadin.helloworld.server.CoreUI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.time.Instant;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;

/**
 *
 */
@Push
public class MyUI extends CoreUI {

  public static final Set<Updater> registrations = newKeySet();

  public interface Updater {
    void update(String message);
  }

  public static Registration register(Updater updater) {
    registrations.add(updater);
    return (Registration) () -> registrations.remove(updater);
  }


  public static class MyFormLayout extends FormLayout {

    private TextField message = new TextField("message");

    public MyFormLayout() {
      addComponents(message);
    }

    private Registration registration;

    public MyFormLayout register() {
      registration = MyUI.register((msg) -> MyFormLayout.this.message.getUI().access(() -> message.setValue(msg)));
      return this;
    }

    @Override
    public void detach() {
      super.detach();
      registration.remove();
    }
  }

  @Override
  public Supplier<Component> componentSupplier() {
    return () -> new MyFormLayout().register();
  }

  private static final Timer timer = new Timer(true);

  static {
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            registrations.forEach(updater -> updater.update(Instant.now().toString()));
          }
        },
        3_000,
        2_000
    );
  }


  @WebServlet(
      urlPatterns = "/*",
      name = "JumpstartServlet",
      displayName = "JumpstartServlet",
      asyncSupported = true)
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
