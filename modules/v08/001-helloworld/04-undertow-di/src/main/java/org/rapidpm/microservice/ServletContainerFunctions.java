package org.rapidpm.microservice;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.stagemonitor.web.servlet.initializer.MainStagemonitorServletContainerInitializer;

import javax.servlet.ServletContainerInitializer;
import java.util.Collections;
import java.util.function.Function;

/**
 *
 */
public interface ServletContainerFunctions {

  static Function<DeploymentInfo, DeploymentInfo> addStagemonitor() {
    return (deploymentInfo) -> deploymentInfo.addServletContainerInitalizer(
        new ServletContainerInitializerInfo(
            MainStagemonitorServletContainerInitializer.class,
            new ImmediateInstanceFactory<ServletContainerInitializer>(new MainStagemonitorServletContainerInitializer()),
            Collections.emptySet()
        ));
  }

}
