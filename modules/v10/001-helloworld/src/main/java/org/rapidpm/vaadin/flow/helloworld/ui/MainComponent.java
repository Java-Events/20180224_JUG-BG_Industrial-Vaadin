package org.rapidpm.vaadin.flow.helloworld.ui;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 *
 */
@Route("helloWorld")
@Theme(Lumo.class)
public class MainComponent extends VerticalLayout {
  public MainComponent() {
    add(new Label("Label from me"));
  }
}