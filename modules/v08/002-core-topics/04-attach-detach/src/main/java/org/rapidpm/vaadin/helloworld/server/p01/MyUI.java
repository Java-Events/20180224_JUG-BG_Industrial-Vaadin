package org.rapidpm.vaadin.helloworld.server.p01;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.helloworld.server.CoreUI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.function.Supplier;

/**
 *
 */

@PreserveOnRefresh
public class MyUI extends CoreUI {

  public static class DataHolder {
    private String  name;
    private Integer age;

    public DataHolder(String name, Integer age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }

    @Override
    public String toString() {
      return "DataHolder{" +
             "name='" + name + '\'' +
             ", age=" + age +
             '}';
    }
  }


  public static class MyTextfield extends TextField implements HasLogger {
    public MyTextfield(String caption) {
      super(caption);
    }

    @Override
    public void attach() {
      super.attach();
      logger().warning("uiuiui I am touched....");
    }

    @Override
    public void detach() {
      super.detach();
      logger().warning("dammmmnnnn     feeling soooo lonely now..");
    }
  }


  private Supplier<Layout> fields() {
    return () -> new FormLayout(new MyTextfield("--name--"),
                                new MyTextfield("--age--")
    );
  }

  private boolean visible = true;

  @Override
  public Supplier<Component> componentSupplier() {
    return () -> {
      final VerticalLayout layout = new VerticalLayout();

      final Supplier<Layout> fields = fields();
      final Layout           f      = fields.get();
      f.setId("fieldsID");

      final Button button = new Button("work");
      button.addClickListener(e -> {
                                visible = !visible;
//                                fields.setVisible(visible); //attach/deattach ?
                                if (!visible) {
                                  for (final Component next : layout) {
                                    final String id = next.getId();
                                    if (id != null &&
                                        id.equals("fieldsID"))
                                      layout.removeComponent(next);
                                  }
                                } else {
                                  layout.addComponentAsFirst(fields.get());
                                }
                              }
      );

      layout.addComponents(f, button);
      return layout;
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
