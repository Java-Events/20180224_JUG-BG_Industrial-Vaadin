package org.rapidpm.vaadin.helloworld.server.p02;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ClassResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.helloworld.server.CoreUI;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * will crash on OSX and OpenJDK8 (9 and 10 is ok) -> Linkage error
 */
@PreserveOnRefresh
public class MyUI extends CoreUI {

  public static class MyImageSource implements StreamResource.StreamSource, HasLogger {

    public MyImageSource() {
      logger().warning("MyImageSource - constructor");
    }

    ByteArrayOutputStream imagebuffer = null;
    int                   reloads     = 0;

    public InputStream getStream() {

      logger().info("reloads " + reloads);
      // Create an image
      BufferedImage image = new BufferedImage(400, 400,
                                              BufferedImage.TYPE_INT_RGB
      );
      Graphics2D drawable = image.createGraphics();
      // Draw something static
      drawable.setStroke(new BasicStroke(5));
      drawable.setColor(Color.WHITE);
      drawable.fillRect(0, 0, 400, 400);
      drawable.setColor(Color.BLACK);
      drawable.drawOval(50, 50, 300, 300);
      // Draw something dynamic
      drawable.setFont(new Font(null, Font.PLAIN, 48));
      drawable.drawString("Reloads=" + reloads, 75, 216);
      reloads++;
      drawable.setColor(new Color(0, 165, 235));
      int x = (int) (200 - 10 + 150 * Math.sin(reloads * 0.3));
      int y = (int) (200 - 10 + 150 * Math.cos(reloads * 0.3));
      drawable.fillOval(x, y, 20, 20);
      try {
        // Write the image to a buffer
        imagebuffer = new ByteArrayOutputStream();
        ImageIO.write(image, "png", imagebuffer);
        // Return a stream from the buffer
        return new ByteArrayInputStream(imagebuffer.toByteArray());
      } catch (IOException e) {
        return null;
      }
    }
  }

  @Override
  public Supplier<Component> componentSupplier() {
    return () -> {
      final StreamResource streamResource = new StreamResource(new MyImageSource(), "generated" + System.nanoTime() + ".png");
      streamResource.setCacheTime(0);
//      streamResource.setMIMEType();
      return new Image("Dynamic Pic " + System.nanoTime(), streamResource);
    };
  }


  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class CoreServlet extends VaadinServlet {
  }

  @Override
  public Class<? extends VaadinServlet> servletClass() {
    return CoreServlet.class;
  }

  public static void main(String[] args) throws ServletException {
    new MyUI().startup();
  }

}
