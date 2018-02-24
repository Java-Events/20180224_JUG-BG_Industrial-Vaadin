package org.rapidpm.vaadin.imagecache.mapdb.ui.image;

import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.model.Result;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.Files.readAllBytes;

/**
 *
 */
public class BlobImageServiceCached implements HasLogger, BlobService {


  public static final String STORAGE_PREFIX = "_data/_nasa_pics/_0512px/";

  private static final Map<String, WeakReference<Result<byte[]>>> CACHE = new ConcurrentHashMap<>(); //TODO fill up the memory

  private CheckedFunction<String, byte[]> load
      = (blobID) -> readAllBytes(new File(STORAGE_PREFIX + blobID).toPath());


  //@Override
  public Result<byte[]> loadBlob(String blobID) {
    //hard coded right now
    final boolean containsKey = CACHE.containsKey(blobID);
    logger().info("containsKey = " + containsKey);
    if (containsKey) {
      final WeakReference<Result<byte[]>> weakReference = CACHE.get(blobID);
      final Result<byte[]>                result        = weakReference.get();

      logger().info((result == null)
                         ? "blobId " + blobID + " was eaten by GC "
                         : "blobId " + blobID + " was cached so far"
      );
      if (result == null) {
        CACHE.remove(blobID);
        return CACHE.computeIfAbsent(blobID, s -> new WeakReference<>(load.apply(blobID)))
                    .get();
      } else {
        return result;
      }
    } else {
      return CACHE
          .computeIfAbsent(blobID, s -> new WeakReference<>(load.apply(blobID)))
          .get();
    }
  }


}
