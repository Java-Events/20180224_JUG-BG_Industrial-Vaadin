package org.rapidpm.vaadin.flow.ui.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcons;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.shared.Customer;
import org.rapidpm.vaadin.srv.api.CustomerService;
import org.rapidpm.vaadin.srv.api.PropertyService;
import org.rapidpm.vaadin.srv.impl.CustomerServiceImpl;
import org.rapidpm.vaadin.srv.impl.PropertyServiceInMemory;

import javax.annotation.PostConstruct;

import static org.rapidpm.vaadin.flow.ComponentIDGenerator.*;

/**
 *
 */
@Route("")
@Theme(Lumo.class)
public class CustomerBoardComponent extends Composite<Div> implements HasLogger {

  public static final String FILTER_TF_ID          = textfieldID().apply(CustomerBoardComponent.class, "filterTF");
  public static final String FILTER_TF_PLACEHOLDER = FILTER_TF_ID + "." + "placeholder";

  public static final String CLEAR_FILTER_BTN_ID          = buttonID().apply(CustomerBoardComponent.class, "clearFilterBTN");
  public static final String CLEAR_FILTER_BTN_DESCRIPTION = CLEAR_FILTER_BTN_ID + "." + "description";

  public static final String NEW_CUSTOMER_BTN_ID      = buttonID().apply(CustomerBoardComponent.class, "newCustomerBTN");
  public static final String NEW_CUSTOMER_BTN_CAPTION = NEW_CUSTOMER_BTN_ID + "." + "caption";

  public static final String DATA_GRID_ID                     = gridID().apply(CustomerBoardComponent.class, "dataGrid");
  public static final String DATA_GRID_COL                    = DATA_GRID_ID + "." + "col";
  public static final String DATA_GRID_COL_CAPTION_FIRST_NAME = DATA_GRID_COL + "." + "firstName";
  public static final String DATA_GRID_COL_CAPTION_LAST_NAME  = DATA_GRID_COL + "." + "lastName";
  public static final String DATA_GRID_COL_CAPTION_EMAIL      = DATA_GRID_COL + "." + "email";

  public static final String CUSTOMERFORM_ID = genericID().apply(CustomerForm.class,
                                                                 CustomerBoardComponent.class,
                                                                 "customerForm"
  );


  //  @Inject private CustomerService service;
//  @Inject private PropertyService propertyService;
//  @Inject private CustomerForm    customerForm;
  private CustomerService service         = new CustomerServiceImpl();
  private PropertyService propertyService = new PropertyServiceInMemory();
  private CustomerForm    customerForm    = new CustomerForm();

  public CustomerBoardComponent() {
//    DI.activatePackages("org.rapidpm");
//    DI.activateDI(this);
//    ((CustomerServiceImpl)service).ensureTestData();
//    ((PropertyServiceInMemory)propertyService).init();
//    customerForm.postConstruct();
//    postConstruct();
  }

  private final Grid<Customer> grid               = new Grid<>();
  private final TextField      filterText         = new TextField();
  private final Button         clearFilterTextBtn = new Button();
  private final Button         addCustomerBtn     = new Button();

  private Registration deleteRegistration;
  private Registration saveRegistration;

  private String resolve(String key) {
    return propertyService.resolve(key);
  }

  @PostConstruct
  private void postConstruct() {

    filterText.setId(FILTER_TF_ID);
    filterText.setPlaceholder(resolve(FILTER_TF_PLACEHOLDER));
    filterText.addValueChangeListener(e -> updateGridItems());
    filterText.setValueChangeMode(ValueChangeMode.EAGER);

    clearFilterTextBtn.setId(CLEAR_FILTER_BTN_ID);
    clearFilterTextBtn.setIcon(VaadinIcons.CLOSE_BIG.create());
//    clearFilterTextBtn.setText(resolve(CLEAR_FILTER_BTN_DESCRIPTION));
    clearFilterTextBtn.addClickListener(e -> filterText.clear());

    addCustomerBtn.setId(NEW_CUSTOMER_BTN_ID);
//    addCustomerBtn.setCaptionAsHtml(true);
    addCustomerBtn.setIcon(VaadinIcons.USER.create());
//    addCustomerBtn.setDescription(resolve(NEW_CUSTOMER_BTN_CAPTION));
//    addCustomerBtn.setIconAlternateText(resolve(NEW_CUSTOMER_BTN_CAPTION));
    addCustomerBtn.addClickListener(e -> {
      grid.asSingleSelect().clear();
      customerForm.setCustomer(new Customer());
    });

    final Div filtering = new Div(filterText, clearFilterTextBtn);
//    filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
//    ((CssLayout) filtering).setResponsive(true);
    final HorizontalLayout buttons = new HorizontalLayout(filtering, addCustomerBtn);
    buttons.setHeight("40px");
//    ((HorizontalLayout) buttons).setResponsive(true);

    grid.setId(DATA_GRID_ID);
    grid.addColumn(Customer::getFirstName)
        .setHeader(resolve(DATA_GRID_COL_CAPTION_FIRST_NAME))
        .setId(DATA_GRID_COL_CAPTION_FIRST_NAME);
    grid.addColumn(Customer::getLastName)
        .setHeader(resolve(DATA_GRID_COL_CAPTION_LAST_NAME))
        .setId(DATA_GRID_COL_CAPTION_LAST_NAME);
    grid.addColumn(Customer::getEmail)
        .setHeader(resolve(DATA_GRID_COL_CAPTION_EMAIL))
        .setId(DATA_GRID_COL_CAPTION_EMAIL);

//    grid.setColumnOrder(
//        DATA_GRID_COL_CAPTION_FIRST_NAME,
//        DATA_GRID_COL_CAPTION_LAST_NAME,
//        DATA_GRID_COL_CAPTION_EMAIL
//    );

    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() == null) {
        customerForm.setVisible(false);
      } else {
        customerForm.setCustomer(event.getValue());
      }
    });
    grid.setSizeFull();
    updateGridItems();

    customerForm.setId(CUSTOMERFORM_ID);
    customerForm.setVisible(false);

    final HorizontalLayout main = new HorizontalLayout(grid, customerForm);
    main.setSizeFull();
//    main.setExpandRatio(grid, 1);

    final VerticalLayout compositionRoot = new VerticalLayout(buttons, main);
//    compositionRoot.setExpandRatio(main, 1);
    compositionRoot.setSizeFull();
//    setCompositionRoot(compositionRoot);
    getContent().add(compositionRoot);

    deleteRegistration = customerForm.registerDeleteListener(customer -> {
      service.delete(customer);
      updateGridItems();
    });
    saveRegistration = customerForm.registerSaveListener(customer -> {
      service.save(customer);
      updateGridItems();
    });
  }

//  @Override
//  public void detach() {
//    super.detach();
//    ((CheckedExecutor) () -> deleteRegistration.remove()).execute();
//    ((CheckedExecutor) () -> saveRegistration.remove()).execute();
//  }

  private void updateGridItems() {
    grid.setItems(service.findAll(filterText.getValue()));
  }
}
