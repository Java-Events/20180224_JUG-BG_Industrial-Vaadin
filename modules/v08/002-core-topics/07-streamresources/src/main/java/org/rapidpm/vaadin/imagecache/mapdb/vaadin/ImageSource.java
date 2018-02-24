package org.rapidpm.vaadin.imagecache.mapdb.vaadin;

import com.vaadin.server.StreamResource;
import org.rapidpm.frp.model.Result;
import org.rapidpm.vaadin.imagecache.mapdb.ui.image.BlobImageService;
import org.rapidpm.vaadin.imagecache.mapdb.ui.image.BlobService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class ImageSource implements StreamResource.StreamSource {

  //here an BlobImageService
//  @Inject private BlobService blobService;
  private BlobService blobService = new BlobImageService();

  private final String imageID;

  public ImageSource(String imageID) {
    this.imageID = imageID;
  }

  @Override
  public InputStream getStream() {
    //into Memory because we want to show it !!
    return blobService
        .loadBlob(imageID)
        .or(() -> Result.success(createFailedLoadImage(imageID)))
        .map(ByteArrayInputStream::new)
        .get();
  }


  private byte[] createFailedLoadImage(String imageID) {
    BufferedImage image = new BufferedImage(512, 512,
                                            BufferedImage.TYPE_INT_RGB
    );
    Graphics2D drawable = image.createGraphics();
    drawable.setStroke(new BasicStroke(5));
    drawable.setColor(Color.WHITE);
    drawable.fillRect(0, 0, 512, 512);
    drawable.setColor(Color.BLACK);
    drawable.drawOval(50, 50, 300, 300);

    drawable.setFont(new Font("Montserrat", Font.PLAIN, 20));
    drawable.drawString(imageID, 75, 216);
    drawable.setColor(new Color(0, 165, 235));
    try {
      // Write the image to a buffer
      ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", imagebuffer);

      // Return a stream from the buffer
      return imagebuffer.toByteArray();
    } catch (IOException e) {
      return new byte[0];
    }
  }
}
