package org.rapidpm.vaadin.imagecache.mapdb.vaadin;

import com.vaadin.shared.Registration;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.rapidpm.vaadin.imagecache.mapdb.ui.DashboardComponent.nextImageName;


/**
 *
 */
public class BlobImagePushService {


  private BlobImagePushService() {
  }

  //TODO JVM static
  private static final Set<ImagePushListener> REGISTRY = ConcurrentHashMap.newKeySet();

  private static final Timer TIMER = new Timer(true);

  public interface ImagePushListener {
    void updateImage(final String imageID);
  }


  public static Registration register(ImagePushListener imagePushListener) {

    REGISTRY.add(imagePushListener);

    return () -> {
      REGISTRY.remove(imagePushListener);
      Logger.getAnonymousLogger().info("removed registration");
    };
  }

  //TODO run every 5 sec -> Timer
  public static void updateImages() {
    // not nice coupled
    REGISTRY.forEach(e -> e.updateImage(nextImageName().get()));
  }

  // TODO not nice
  static {
    TIMER.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            BlobImagePushService.updateImages();
          }
        },
        5_000,
        5_000
    );
  }
}
