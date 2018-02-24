package org.rapidpm.vaadin.helloworld.server;

/**
 *
 */
public interface PropertyService {

  String resolve(String key);

  boolean hasKey(String key);
}
