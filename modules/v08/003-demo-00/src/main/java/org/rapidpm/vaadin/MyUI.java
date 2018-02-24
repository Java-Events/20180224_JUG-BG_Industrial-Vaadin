package org.rapidpm.vaadin;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.function.Supplier;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
public class MyUI extends CoreUI {

  private final Grid<Customer>  grid       = new Grid<>();
  private final TextField       filterText = new TextField();
  private final CustomerForm    form       = new CustomerForm(this);
  private       CustomerService service    = CustomerService.getInstance();


  @Override
  public Supplier<Component> componentSupplier() {
    return () -> init();
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

  protected Component init() {

    form.setVisible(false);

    filterText.setPlaceholder("filter by name...");
    filterText.addValueChangeListener(e -> updateList());
    filterText.setValueChangeMode(ValueChangeMode.LAZY);

    Button clearFilterTextBtn = new Button();
    clearFilterTextBtn.setDescription("Clear the current filter");
    clearFilterTextBtn.addClickListener(e -> filterText.clear());
    clearFilterTextBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);

    CssLayout filtering = new CssLayout(filterText, clearFilterTextBtn);
    filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

    Button addCustomerBtn = new Button("Add new customer");
    addCustomerBtn.addClickListener(e -> {
      grid.asSingleSelect().clear();
      form.setCustomer(new Customer());
    });

    grid.addColumn(Customer::getFirstName).setCaption("First Name");
    grid.addColumn(Customer::getLastName).setCaption("Last Name");
    grid.addColumn(Customer::getEmail).setCaption("Email");
    grid.asSingleSelect()
        .addValueChangeListener(event -> {
          if (event.getValue() == null) {
            form.setVisible(false);
            form.setCustomer(null);
          } else {
            form.setCustomer(event.getValue());
          }
        });
    grid.setSizeFull();
    updateList();

    HorizontalLayout main = new HorizontalLayout(grid, form);
    main.setSizeFull();
    main.setExpandRatio(grid, 1);

    HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn);
    return new VerticalLayout(toolbar, main);
  }

  public void updateList() {
    grid.setItems(service.findAll(filterText.getValue()));
  }


}
