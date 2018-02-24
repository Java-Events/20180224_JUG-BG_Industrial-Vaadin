package org.rapidpm.vaadin.helloworld.server;

import io.undertow.Undertow;
import io.undertow.util.Headers;

import java.util.Optional;

/**
 *
 */
public class HelloWorldMain {


  public static void start() {
    main(new String[0]);
  }

  public static void shutdown() {
    undertowOptional.ifPresent(Undertow::stop);
  }

  private static Optional<Undertow> undertowOptional;


  public static void main(String[] args) {
    Undertow server = Undertow
        .builder()
        .addHttpListener(8080, "localhost")
        .setHandler(exchange -> {
          exchange.getResponseHeaders()
                  .put(Headers.CONTENT_TYPE, "text/plain");
          exchange.getResponseSender()
                  .send("Hello World");
        })
        .build();
    server.start();

    undertowOptional = Optional.of(server);

    server.getListenerInfo().forEach(System.out::println);
  }
}
