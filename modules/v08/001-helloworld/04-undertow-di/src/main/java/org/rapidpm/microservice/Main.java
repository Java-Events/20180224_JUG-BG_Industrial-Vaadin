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

package org.rapidpm.microservice;

import org.rapidpm.ddi.DI;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.dependencies.core.logger.Logger;
import org.rapidpm.dependencies.core.logger.LoggingService;
import org.rapidpm.frp.functions.CheckedSupplier;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static java.time.Duration.between;


public class Main implements HasLogger {

  private static final LoggingService LOGGER = Logger.getLogger(Main.class);

  private static final Timer  TIMER           = new Timer(true);
  public static final  String DEFAULT_HOST    = "0.0.0.0";
  private static final String DI_PACKAGE_FILE = "microservice.packages";

  private static Optional<String[]> cliArguments;

  private Main() {
  }

  public static void main(String[] args) {
    cliArguments = Optional.ofNullable(args);
    deploy(cliArguments);
  }

  public static void deploy() {
    deploy(Optional.empty());
  }

  public static void deploy(Optional<String[]> args) {
    cliArguments = args;
    final String packages = Main.class.getPackage().getName().replace("." , "/") + "/" + DI_PACKAGE_FILE;
    String property = System.getProperty(DI.ORG_RAPIDPM_DDI_PACKAGESFILE , packages);
    System.setProperty(DI.ORG_RAPIDPM_DDI_PACKAGESFILE , property);

    final LocalDateTime dependencyBootstrapStart = LocalDateTime.now();
    DI.bootstrap();
    final LocalDateTime dependencyBootstrapStop = LocalDateTime.now();



    final LocalDateTime deployStart = LocalDateTime.now();
    MainUndertow.deploy();
    final LocalDateTime deployStop = LocalDateTime.now();


    final long startupDDI = between(dependencyBootstrapStart , dependencyBootstrapStop).toMillis();
    final long startupUndertow = between(deployStart , deployStop).toMillis();
    final long startupComplete = between(dependencyBootstrapStart , deployStop).toMillis();

    final String ddi = String.format("%1$4s" , startupDDI);
    final String undertow = String.format("%1$4s" , startupUndertow);
    final String complete = String.format("%1$4s" , startupComplete);

    System.out.println("");
    System.out.println(" ############  Startup finished  = " + deployStop + " ############  ");
    System.out.println(" ############  DDI            = " + ddi + " [ms]                  ############");
    System.out.println(" ############  Undertow       = " + undertow + " [ms]                  ############");
    System.out.println(" ############  Complete       = " + complete + " [ms]                  ############");
    System.out.println(" ###############################  Enjoy ###############################");

    System.out.println("");
  }


  public static void stop(long delayMS) {
    LOGGER.warning("shutdown delay [ms] = " + delayMS);

    TIMER.schedule(new TimerTask() {
      @Override
      public void run() {
        LOGGER.warning("delayed shutdown  now = " + LocalDateTime.now());
        stop();
      }
    } , delayMS);
  }

  public static void stop() {
    MainUndertow.stop(); // TODO make it non-static
  }



}
