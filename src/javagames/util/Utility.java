package javagames.util;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * This class provides utility methods for handling the viewport, drawing
 * polygons or text and scaling images.
 *
 * @author Timothy Wright
 *
 */
public class Utility {

  public static Matrix3x3f createViewport(final float worldWidth,
      final float worldHeight, final float screenWidth,
      final float screenHeight) {
    final float sx = (screenWidth - 1) / worldWidth;
    final float sy = (screenHeight - 1) / worldHeight;
    final float tx = (screenWidth - 1) / 2.0f;
    final float ty = (screenHeight - 1) / 2.0f;
    Matrix3x3f viewport = Matrix3x3f.scale(sx, -sy);
    viewport = viewport.mul(Matrix3x3f.translate(tx, ty));
    return viewport;
  }

  public static Matrix3x3f createReverseViewport(final float worldWidth,
      final float worldHeight, final float screenWidth,
      final float screenHeight) {
    final float sx = worldWidth / (screenWidth - 1);
    final float sy = worldHeight / (screenHeight - 1);
    final float tx = (screenWidth - 1) / 2.0f;
    final float ty = (screenHeight - 1) / 2.0f;
    Matrix3x3f viewport = Matrix3x3f.translate(-tx, -ty);
    viewport = viewport.mul(Matrix3x3f.scale(sx, -sy));
    return viewport;
  }

  public static void drawPolygon(final Graphics g, final Vector2f[] polygon) {
    Vector2f P;
    Vector2f S = polygon[polygon.length - 1];
    for (final Vector2f element : polygon) {
      P = element;
      g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
      S = P;
    }
  }

  public static void drawPolygon(final Graphics g,
      final List<Vector2f> polygon) {
    Vector2f S = polygon.get(polygon.size() - 1);
    for (final Vector2f P : polygon) {
      g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
      S = P;
    }
  }

  public static void fillPolygon(final Graphics2D g, final Vector2f[] polygon) {
    final Polygon p = new Polygon();
    for (final Vector2f v : polygon) {
      p.addPoint((int) v.x, (int) v.y);
    }
    g.fill(p);
  }

  public static void fillPolygon(final Graphics2D g,
      final List<Vector2f> polygon) {
    final Polygon p = new Polygon();
    for (final Vector2f v : polygon) {
      p.addPoint((int) v.x, (int) v.y);
    }
    g.fill(p);
  }

  public static int drawString(final Graphics g, final int x, final int y,
      final String str) {
    return Utility.drawString(g, x, y, new String[] { str });
  }

  public static int drawString(final Graphics g, final int x, final int y,
      final List<String> str) {
    return Utility.drawString(g, x, y, str.toArray(new String[0]));
  }

  public static int drawString(final Graphics g, final int x, int y,
      final String... str) {
    final FontMetrics fm = g.getFontMetrics();
    final int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
    for (final String s : str) {
      g.drawString(s, x, y + fm.getAscent());
      y += height;
    }
    return y;
  }

  public static int drawCenteredString(final Graphics g, final int w,
      final int y, final String str) {
    return Utility.drawCenteredString(g, w, y, new String[] { str });
  }

  public static int drawCenteredString(final Graphics g, final int w,
      final int y, final List<String> str) {
    return Utility.drawCenteredString(g, w, y, str.toArray(new String[0]));
  }

  public static int drawCenteredString(final Graphics g, final int w, int y,
      final String... str) {
    final FontMetrics fm = g.getFontMetrics();
    final int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
    for (final String s : str) {
      final Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
      final int x = (w - (int) bounds.getWidth()) / 2;
      g.drawString(s, x, y + fm.getAscent());
      y += height;
    }
    return y;
  }

  public static BufferedImage scaleImage(final BufferedImage toScale,
      final int targetWidth, final int targetHeight) {
    final int width = toScale.getWidth();
    final int height = toScale.getHeight();
    if ((targetWidth < width) || (targetHeight < height)) {
      return Utility.scaleDownImage(toScale, targetWidth, targetHeight);
    }
    else {
      return Utility.scaleUpImage(toScale, targetWidth, targetHeight);
    }
  }

  private static BufferedImage scaleUpImage(final BufferedImage toScale,
      final int targetWidth, final int targetHeight) {
    final BufferedImage image = new BufferedImage(targetWidth, targetHeight,
        BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2d = image.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.drawImage(toScale, 0, 0, image.getWidth(), image.getHeight(), null);
    g2d.dispose();
    return image;
  }

  private static BufferedImage scaleDownImage(BufferedImage toScale,
      final int targetWidth, final int targetHeight) {

    int w = toScale.getWidth();
    int h = toScale.getHeight();
    do {
      w = w / 2;
      if (w < targetWidth) {
        w = targetWidth;
      }
      h = h / 2;
      if (h < targetHeight) {
        h = targetHeight;
      }
      final BufferedImage tmp =
          new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      final Graphics2D g2d = tmp.createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2d.drawImage(toScale, 0, 0, w, h, null);
      g2d.dispose();
      toScale = tmp;
    } while ((w != targetWidth) || (h != targetHeight));

    return toScale;
  }
}