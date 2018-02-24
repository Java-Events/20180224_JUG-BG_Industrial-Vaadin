package org.rapidpm.vaadin.helloworld.server.p01;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import org.rapidpm.vaadin.helloworld.server.CoreUI;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.function.Supplier;

import static java.lang.System.out;

/**
 *
 */
public class MyUI extends CoreUI {


  @Override
  public Supplier<Component> componentSupplier() {
    return () -> {

      final ReconnectDialogConfiguration conf = UI
          .getCurrent()
          .getReconnectDialogConfiguration();

      conf.setDialogText("Shit..  Internet is broken..in different languages");
      conf.setDialogTextGaveUp("ok i gave up.. in different languages");
//      conf.setReconnectAttempts(2);
//      conf.setReconnectInterval(2_000); //[ms]
//      conf.setDialogGracePeriod(4_000); //[ms]


      final Button btnA = new Button();
      btnA.setComponentError(new SystemError("ohhh nooh I am a SystemError"));

      final Button btnB = new Button();
      btnB.setComponentError(new UserError("stupid User"));

      final Button btnC = new Button();
      btnC.setComponentError(new CompositeErrorMessage(new UserError("stupid User"),
                                                       new SystemError("everything is bad...")
      ));

      final Button button = new Button("Click Me!", event ->
          ((String) null).length()); // Null-pointer exception

      final VerticalLayout result = new VerticalLayout(btnA, btnB, btnC, button);


      UI
          .getCurrent()
          .setErrorHandler(new DefaultErrorHandler() {

            @Override
            public void error(com.vaadin.server.ErrorEvent event) {


              final Button btnSendMsg = new Button("send info home...");
              final TextArea textArea = new TextArea("ErrorMessage from the System");
              textArea.setValue(event.toString());
              final VerticalLayout layout = new VerticalLayout(
                  new Label("some usefull infos"),
                  textArea,
                  btnSendMsg
              );
              result.addComponent(layout);

              btnSendMsg.addClickListener(clickEvent -> {
                out.println("will send the msg home = " + event);
                result.removeComponent(layout);
              });

              // Do the default error handling (optional)
              doDefault(event);
            }
          });


      return result;
    };
  }


  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class CoreServlet extends VaadinServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
      super.init(servletConfig);

    }

    @Override
    protected void servletInitialized() throws ServletException {
      super.servletInitialized();
      getService()
          .setSystemMessagesProvider(
              new SystemMessagesProvider() {
                @Override
                public SystemMessages getSystemMessages(
                    SystemMessagesInfo systemMessagesInfo) {
                  CustomizedSystemMessages messages = new CustomizedSystemMessages();
                  messages.setCommunicationErrorCaption("Comm Err in different languages");
                  messages.setCommunicationErrorMessage("This is bad. in different languages");
                  messages.setCommunicationErrorNotificationEnabled(true);
                  messages.setCommunicationErrorURL("http://my.error.org");
                  return messages;
                }
              });
    }
  }

  @Override
  public Class<? extends VaadinServlet> servletClass() {
    return CoreServlet.class;
  }

  public static void main(String[] args) throws ServletException {
    new MyUI().startup();
  }
}
