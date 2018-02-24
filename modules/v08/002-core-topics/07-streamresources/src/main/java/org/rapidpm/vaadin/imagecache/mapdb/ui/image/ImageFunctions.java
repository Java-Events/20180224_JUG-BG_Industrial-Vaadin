package org.rapidpm.vaadin.imagecache.mapdb.ui.image;

import java.util.function.Function;

import static java.lang.String.format;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 *
 */
public interface ImageFunctions {


  public static Function<Integer, String> randomImageID() {
    return (boundary) -> format("%05d" , current().nextInt(boundary) + 1);
  }

}
