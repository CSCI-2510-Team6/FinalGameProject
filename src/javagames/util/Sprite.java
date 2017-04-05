package javagames.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * This class handles storing and rendering a buffered image.
 *
 * @author Timothy Wright
 *
 */
public class Sprite {

  private final BufferedImage image;
  private BufferedImage       scaled;
  private final Vector2f      topLeft;
  private final Vector2f      bottomRight;

  public Sprite(final BufferedImage image, final Vector2f topLeft,
      final Vector2f bottomRight) {
    this.image = image;
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  public void render(final Graphics2D g, final Matrix3x3f view) {
    render(g, view, new Vector2f(), 0.0f);
  }

  public void render(final Graphics2D g, final Matrix3x3f view,
      final Vector2f position, final float angle) {
    if (image != null) {
      final Vector2f tl = view.mul(topLeft);
      final Vector2f br = view.mul(bottomRight);
      final int width = (int) Math.abs(br.x - tl.x);
      final int height = (int) Math.abs(br.y - tl.y);
      if ((scaled == null) || (width != scaled.getWidth())
          || (height != scaled.getHeight())) {
        scaled = Utility.scaleImage(image, width, height);
      }
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      final Vector2f screen = view.mul(position);
      final AffineTransform transform =
          AffineTransform.getTranslateInstance(screen.x, screen.y);
      transform.rotate(-angle);
      transform.translate(-scaled.getWidth() / 2, -scaled.getHeight() / 2);
      g.drawImage(scaled, transform, null);
    }
  }

  public void scaleImage(final Matrix3x3f view) {
    final Vector2f screenTopLeft = view.mul(topLeft);
    final Vector2f screenBottomRight = view.mul(bottomRight);
    final int scaledWidth =
        (int) Math.abs(screenBottomRight.x - screenTopLeft.x);
    final int scaledHeight =
        (int) Math.abs(screenBottomRight.y - screenTopLeft.y);
    scaled = Utility.scaleImage(image, scaledWidth, scaledHeight);
  }
}