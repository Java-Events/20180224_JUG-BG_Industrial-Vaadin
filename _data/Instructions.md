<center>
<a href="https://vaadin.com">
 <img src="https://vaadin.com/images/hero-reindeer.svg" width="200" height="200" /></a>
</center>

# Special Infos
## OSX and OpenJDK8
you need to install on your machine **libpng** and  **freetype** to get rid of
the linkage error. This will happen if you are using the 2D Libs from the JDK.

````java
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
````

```brew install libpng freetype ```