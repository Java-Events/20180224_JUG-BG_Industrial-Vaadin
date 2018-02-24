package org.rapidpm.vaadin.imagecache.mapdb.ui.image;

import org.mapdb.HTreeMap;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.model.Result;
import org.rapidpm.vaadin.imagecache.PersistenceFunctions;

import java.io.File;

import static java.nio.file.Files.readAllBytes;
import static org.rapidpm.frp.memoizer.Memoizer.memoize;
import static org.rapidpm.vaadin.imagecache.PersistenceFunctions.cachingDB;
import static org.rapidpm.vaadin.imagecache.PersistenceFunctions.mapInMemoryPersistentOnDisc;

/**
 *
 */
public class BlobImageServiceMapDB implements HasLogger, BlobService {


  public static final String STORAGE_PREFIX = "_data/_nasa_pics/_0512px/";
  public static final String IMAGE_CACHE    = "imageCache";
  public static final String IMAGES         = "images";

  private static final PersistenceFunctions.DatabasePair CACHE               = memoize(cachingDB()).apply(IMAGE_CACHE);
  private static final HTreeMap<String, byte[]>          IMAGE_MAP_IN_MEMORY = mapInMemoryPersistentOnDisc().apply(CACHE, IMAGES);


  private CheckedFunction<String, byte[]> load
      = (blobID) -> readAllBytes(new File(STORAGE_PREFIX + blobID).toPath());


  @Override
  public Result<byte[]> loadBlob(String blobID) {
    //hard coded right now
    final byte[]  imageByteArray = IMAGE_MAP_IN_MEMORY.get(blobID);
    final boolean containsKey    = imageByteArray != null;
    logger().info("containsKey = " + containsKey);

    if (!containsKey) {
//      load data into system -> some slow remote system
      final Result<byte[]> imageFromRemoteSystem = load.apply(blobID);

      imageFromRemoteSystem
          .ifPresentOrElse(
              imageRAW -> IMAGE_MAP_IN_MEMORY.put(blobID, imageRAW),
              failed -> logger().warning("Image with ID " + blobID + " could not be loaded from external system")
          );
    }
    return Result.ofNullable(IMAGE_MAP_IN_MEMORY.get(blobID));
  }

}
