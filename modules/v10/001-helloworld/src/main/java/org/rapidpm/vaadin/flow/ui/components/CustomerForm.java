package org.rapidpm.vaadin.flow.ui.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcons;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.shared.Customer;
import org.rapidpm.vaadin.shared.CustomerStatus;
import org.rapidpm.vaadin.srv.api.PropertyService;
import org.rapidpm.vaadin.srv.impl.PropertyServiceInMemory;

import javax.annotation.PostConstruct;
import java.util.Set;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;
import static org.rapidpm.vaadin.flow.ComponentIDGenerator.*;

public class CustomerForm extends Composite<Div> implements HasLogger {

  public static final String TF_FIRST_NAME_ID      = textfieldID().apply(CustomerForm.class, "tf_first_name");
  public static final String TF_FIRST_NAME_CAPTION = caption().apply(TF_FIRST_NAME_ID);

  public static final String TF_LAST_NAME_ID      = textfieldID().apply(CustomerForm.class, "tf_last_name");
  public static final String TF_LAST_NAME_CAPTION = caption().apply(TF_LAST_NAME_ID);

  public static final String TF_EMAIL_ID      = textfieldID().apply(CustomerForm.class, "tf_email");
  public static final String TF_EMAIL_CAPTION = caption().apply(TF_EMAIL_ID);

  public static final String CB_STATUS_ID      = comboBoxID().apply(CustomerForm.class, "cb_status");
  public static final String CB_STATUS_CAPTION = caption().apply(CB_STATUS_ID);

  public static final String DF_BIRTHDAY_ID          = datePickerID().apply(CustomerForm.class, "df_birthday");
  public static final String DF_BIRTHDAY_CAPTION     = caption().apply(DF_BIRTHDAY_ID);
  public static final String DF_BIRTHDAY_PLACEHOLDER = placeholder().apply(DF_BIRTHDAY_ID);

  public static final String BTN_SAVE_ID      = buttonID().apply(CustomerForm.class, "btn_save");
  public static final String BTN_SAVE_CAPTION = caption().apply(BTN_SAVE_ID);

  public static final String BTN_DELETE_ID      = buttonID().apply(CustomerForm.class, "btn_delete");
  public static final String BTN_DELETE_CAPTION = caption().apply(BTN_DELETE_ID);

  public static final String BTN_CANCEL_ID      = buttonID().apply(CustomerForm.class, "btn_cancel");
  public static final String BTN_CANCEL_CAPTION = caption().apply(BTN_CANCEL_ID);


  private final TextField                firstName = new TextField();
  private final TextField                lastName  = new TextField();
  private final TextField                email     = new TextField();
  private final ComboBox<CustomerStatus> status    = new ComboBox<>();
  private final DatePicker               birthday  = new DatePicker();
  private final Button                   save      = new Button();
  private final Button                   delete    = new Button();
  private final Button                   cancel    = new Button();


  private final Binder<Customer> beanBinder      = new Binder<>(Customer.class);
  private final Set<UpdateEvent> saveListeners   = newKeySet();
  private final Set<UpdateEvent> deleteListeners = newKeySet();
  private       Customer         customer;

  private PropertyService propertyService = new PropertyServiceInMemory();

  public String resolve(String key) {
    return propertyService.resolve(key);
  }

  private final FormLayout layout = new FormLayout(firstName,
                                                   lastName,
                                                   email,
                                                   status,
                                                   birthday,
                                                   new HorizontalLayout(save, delete, cancel)
  );

  public CustomerForm() {
//    setCompositionRoot(layout);
//    setSizeUndefined();
//    ((PropertyServiceInMemory) propertyService).init();
    getContent().add(layout);
  }

  @PostConstruct
  public void postConstruct() {
//  private void initContent() {
    firstName.setId(TF_FIRST_NAME_ID);
    firstName.setLabel(resolve(TF_FIRST_NAME_CAPTION));

    lastName.setId(TF_LAST_NAME_ID);
    lastName.setLabel(resolve(TF_LAST_NAME_CAPTION));

    email.setId(TF_EMAIL_ID);
    email.setLabel(resolve(TF_EMAIL_CAPTION));

    status.setId(CB_STATUS_ID);
    status.setLabel(resolve(CB_STATUS_CAPTION));
    status.setItems(CustomerStatus.values());

    birthday.setId(DF_BIRTHDAY_ID);
    birthday.setLabel(resolve(DF_BIRTHDAY_CAPTION));
    birthday.setPlaceholder(resolve(DF_BIRTHDAY_PLACEHOLDER));
    // have a look at -> new DatePicker.DatePickerI18n()

    save.setId(BTN_SAVE_ID);
//    save.setCaptionAsHtml(true);
    save.setIcon(VaadinIcons.THUMBS_UP_O.create());
//    save.setDescription(resolve(BTN_SAVE_CAPTION));
//    save.setClickShortcut(KeyboardEvent.KeyCode.ENTER);
    save.addClickListener(e -> this.save());

    delete.setId(BTN_DELETE_ID);
//    delete.setCaptionAsHtml(true);
    delete.setIcon(VaadinIcons.THUMBS_DOWN_O.create());
//    delete.setDescription(resolve(BTN_DELETE_CAPTION));
    delete.addClickListener(e -> this.delete());

    cancel.setId(BTN_CANCEL_ID);
//    cancel.setCaptionAsHtml(true);
    cancel.setIcon(VaadinIcons.CLOSE.create());
//    cancel.setDescription(resolve(BTN_CANCEL_CAPTION));
    cancel.addClickListener(e -> {
      this.customer = null;
      setVisible(false);
    });
    beanBinder.bindInstanceFields(this);
  }


  public void setCustomer(Customer customer) {
    this.customer = customer;
    beanBinder.setBean(customer);

    // Show delete button for only customers already in the database
    delete.setVisible(customer.isPersisted());
    setVisible(true);
//    firstName.selectAll();
  }

  private void delete() {
    setVisible(false);
    deleteListeners.forEach(listener -> listener.update(customer));
  }

  private void save() {
    setVisible(false);
    saveListeners.forEach(listener -> listener.update(customer));
  }

  public Registration registerSaveListener(UpdateEvent updateEvent) {
    saveListeners.add(updateEvent);
    return () -> saveListeners.remove(updateEvent);
  }

  public Registration registerDeleteListener(UpdateEvent updateEvent) {
    deleteListeners.add(updateEvent);
    return () -> deleteListeners.remove(updateEvent);
  }


  public interface UpdateEvent {
    void update(Customer customer);
  }
}
