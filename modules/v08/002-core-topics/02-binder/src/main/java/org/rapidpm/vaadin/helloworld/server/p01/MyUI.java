package org.rapidpm.vaadin.helloworld.server.p01;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
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

  private Binder<DataHolder> binder     = new Binder<>();
  private TextField          nameField  = new TextField("--name--");
  private TextField          ageField   = new TextField("--age--");
  private DataHolder         dataHolder = new DataHolder("Initial Name", -1);

  private Label message = new Label();

  public MyUI() {
    binder.forField(nameField).bind(DataHolder::getName, DataHolder::setName);
    binder.forField(ageField)
          .bind((ValueProvider<DataHolder, String>) dataHolder -> dataHolder.getAge().toString(),
                (Setter<DataHolder, String>) (dataHolder, age) -> dataHolder.setAge(Integer.parseInt(age))
          );
    binder.readBean(dataHolder);
  }

  @Override
  public Supplier<Component> componentSupplier() {
    return () -> {
      final Button button = new Button("work");
      button.addClickListener(e -> {
                                try {
                                  binder.writeBean(dataHolder);
                                  System.out.println("dataHolder= " + dataHolder);
                                  message.setCaption(dataHolder.toString());
                                } catch (ValidationException e1) {
                                  e1.printStackTrace();
                                }
                              }
      );
      return new FormLayout(nameField, ageField, button,message);
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
