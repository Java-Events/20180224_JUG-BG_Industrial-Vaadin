package org.rapidpm.vaadin.imagecache.mapdb.ui.image;

import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.model.Result;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.Files.readAllBytes;

/**
 *
 */
public class BlobImageService implements BlobService {

  public static final String STORAGE_PREFIX = "_data/_nasa_pics/_0512px/";

  private static final Map<String, Result<byte[]>> CACHE = new ConcurrentHashMap<>(); //TODO fill up the memory

  private CheckedFunction<String, byte[]> load
      = (blobID) -> readAllBytes(new File(STORAGE_PREFIX + blobID).toPath());


  //@Override
  public Result<byte[]> loadBlob(String blobID) {
    //hard coded right now
    final Result<byte[]> result = CACHE.containsKey(blobID)
                                  ? CACHE.get(blobID)
                                  : CACHE.computeIfAbsent(blobID, load);
    return result;
  }
}
