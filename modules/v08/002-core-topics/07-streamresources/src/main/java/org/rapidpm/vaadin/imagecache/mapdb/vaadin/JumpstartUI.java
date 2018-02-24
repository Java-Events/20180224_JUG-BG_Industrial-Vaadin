/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.rapidpm.vaadin.imagecache.mapdb.vaadin;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.helloworld.server.CoreUI;
import org.rapidpm.vaadin.imagecache.mapdb.ui.DashboardComponent;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.function.Supplier;

@PreserveOnRefresh
@Title("JumpstartServlet")
@Push
public class JumpstartUI extends CoreUI implements HasLogger {

  @Override
  public Supplier<Component> componentSupplier() {
    return () -> {
      final DashboardComponent components = new DashboardComponent();
      components.postConstruct();
      return components;

    };
  }


  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = JumpstartUI.class)
  public static class CoreServlet extends VaadinServlet {
  }

  @Override
  public Class<? extends VaadinServlet> servletClass() {
    return JumpstartUI.CoreServlet.class;
  }

  public static void main(String[] args) throws ServletException {
    new JumpstartUI().startup();
  }

}
