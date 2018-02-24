package org.rapidpm.vaadin.imagecache.mapdb.ui;

import com.vaadin.server.StreamResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Image;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.imagecache.mapdb.vaadin.ImageSource;

import javax.annotation.PostConstruct;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.rapidpm.vaadin.imagecache.mapdb.ui.image.ImageFunctions.randomImageID;
import static org.rapidpm.vaadin.imagecache.mapdb.vaadin.BlobImagePushService.register;

/**
 *
 */
public class DashboardComponent extends Composite implements HasLogger {

  public static Supplier<String> nextImageName() {
    return () -> "nasa_pic_" + randomImageID().apply(100) + ".jpg";
  }


  private Function<String, Image> createImage() {
    return (imageID) -> {
      final Image image = new Image(null, createImageResource().apply(imageID));
      image.markAsDirty(); // for refresh
      return image;
    };
  }

  private Function<String, StreamResource> createImageResource() {
    return (imageID) -> {
      final StreamResource streamResource = new StreamResource(new ImageSource(imageID), imageID);
      streamResource.setCacheTime(0);
      streamResource.setFilename(imageID + "." + System.nanoTime());
      return streamResource;
    };
  }

  private Image        image;
  private Registration registration;

  @PostConstruct
  public void postConstruct() {
    image = createImage().apply(nextImageName().get());

    registration = register(imgID -> image.getUI()
                                          .access(() -> {
                                            logger().info("DashboardComponent - imgID = " + imgID);
                                            final StreamResource apply = createImageResource().apply(imgID);
                                            image.setSource(apply);
                                          }));

    setCompositionRoot(image);
  }


  //TODO to avoid Memory Leaks
  @Override
  public void detach() {
    registration.remove();
    super.detach();
  }
}