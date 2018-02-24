package org.rapidpm.vaadin.imagecache.mapdb.ui.image;

import org.rapidpm.frp.model.Result;

/**
 *
 */
public interface BlobService {
  public Result<byte[]> loadBlob(String blobID);
}
