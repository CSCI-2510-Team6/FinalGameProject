package collision;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import collision.shapes.BoundingBox;
import collision.shapes.BoundingCircle;
import collision.shapes.BoundingShape;
import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;

/**
 * This class represents an object that another object may contain to use for
 * collision detection.
 *
 * @author Andres Ward
 *
 */
public class Collider {

  private final BoundingShape       outerBound;
  private final List<BoundingShape> innerBounds;
  private final Matrix3x3f          world;

  // Constructor for Collider with a Bounding Box as outer bounding shape
  public Collider(final Vector2f center, final float width, final float height,
      final List<BoundingShape> inner) {
    final Vector2f topRight =
        new Vector2f(center.x + (width / 2), center.y + (height / 2));
    final Vector2f bottomLeft =
        new Vector2f(center.x - (width / 2), center.y - (height / 2));
    outerBound = new BoundingBox(topRight, bottomLeft);
    innerBounds = inner;
    world = Matrix3x3f.identity();
  }

  // Constructor for Collider with a Bounding Circle as outer bounding shape
  public Collider(final Vector2f center, final float radius,
      final List<BoundingShape> inner) {
    outerBound = new BoundingCircle(radius, center);
    innerBounds = inner;
    world = Matrix3x3f.identity();
  }

  /**
   * Returns the outer bounding shape of a collider
   *
   * @return BoundingShape outer bound
   */
  public BoundingShape getOuterBound() {
    return outerBound;
  }

  /**
   * Returns a list of the inner bounding shapes of a collider
   *
   * @return List<BoundingShape> inner bounds
   */
  public List<BoundingShape> getInnerBounds() {
    return innerBounds;
  }

  /**
   * A collider doesn't care about time. It just needs to know where to recenter
   * its bounding shapes.
   *
   * @param center
   */
  public void updateWorld(final Vector2f center) {
    Vector2f shift = new Vector2f();

    if (outerBound instanceof BoundingBox) {
      // Find the center of a bounding box by taking the average of the max and
      // min corners
      final Vector2f shapeCenter = (((BoundingBox) outerBound).getMax()
          .add(((BoundingBox) outerBound).getMin())).div(2);

      // The difference between the old center of a bounding shape and a new
      // point is the amount that all the other bounding shapes need to be
      // shifted
      shift = center.sub(shapeCenter);

      shiftShape(outerBound, shift);
    }
    else if (outerBound instanceof BoundingCircle) {
      shift = center.sub(((BoundingCircle) outerBound).getCenter());

      shiftShape(outerBound, shift);
    }

    for (final BoundingShape inner : innerBounds) {
      shiftShape(inner, shift);
    }
  }

  /**
   * Colliders know how to draw their bounding shapes to the screen
   *
   * @param g
   * @param screen
   */
  public void render(final Graphics g, final Matrix3x3f screen) {
    // Yellow is for the outer bound
    g.setColor(Color.YELLOW);
    if (outerBound instanceof BoundingBox) {
      drawBoundingBox(outerBound, g, screen);
    }
    else if (outerBound instanceof BoundingCircle) {
      drawBoundingCircle(outerBound, g, screen);
    }

    // Orange is for the inner bounds
    g.setColor(Color.ORANGE);

    for (final BoundingShape shape : innerBounds) {
      if (shape instanceof BoundingBox) {
        drawBoundingBox(shape, g, screen);
      }
      else if (shape instanceof BoundingCircle) {
        drawBoundingCircle(shape, g, screen);
      }
    }
  }

  /**
   * Draw the borders of a bounding box shape to the screen
   *
   * @param shape
   * @param g
   * @param screen
   */
  public void drawBoundingBox(final BoundingShape shape, final Graphics g,
      final Matrix3x3f screen) {
    Vector2f topRight;
    Vector2f bottomLeft;
    int x;
    int y;
    int width;
    int height;

    // Get the max and min points and convert to screen coordinates
    topRight = world.mul(((BoundingBox) shape).getMax());
    topRight = screen.mul(topRight);
    bottomLeft = world.mul(((BoundingBox) shape).getMin());
    bottomLeft = screen.mul(bottomLeft);

    // Extract the x and y coordinates from the screen coordinate vectors
    x = (int) bottomLeft.x;
    y = (int) topRight.y;
    // Calculate the width and height of the box
    width = (int) (topRight.x - bottomLeft.x);
    height = (int) (bottomLeft.y - topRight.y);

    // draw the left side
    g.drawLine(x, y + height, x, y);
    // draw the top side
    g.drawLine(x, y, x + width, y);
    // draw the right side
    g.drawLine(x + width, y, x + width, y + height);
    // draw the bottom side
    g.drawLine(x + width, y + height, x, y + height);
  }

  /**
   * Draw the perimeter of a bounding circle shape to the screen
   *
   * @param shape
   * @param g
   * @param screen
   */
  public void drawBoundingCircle(final BoundingShape shape, final Graphics g,
      final Matrix3x3f screen) {
    Vector2f topRight;
    Vector2f bottomLeft;
    int x;
    int y;
    int width;
    int height;

    // Get the max and min points and convert to screen coordinates
    topRight = world.mul(((BoundingCircle) shape).getMax());
    topRight = screen.mul(topRight);
    bottomLeft = world.mul(((BoundingCircle) shape).getMin());
    bottomLeft = screen.mul(bottomLeft);

    // Extract the x and y coordinates from the screen coordinate vectors
    x = (int) bottomLeft.x;
    y = (int) topRight.y;
    // Calculate the width and height of the box
    width = (int) (topRight.x - bottomLeft.x);
    height = (int) (bottomLeft.y - topRight.y);
    // Draw the perimeter of the circle
    g.drawArc(x, y, width, height, 0, 360);
  }

  /**
   * Shift a bounding shape to its new spot
   * 
   * @param shape
   * @param shift
   */
  private void shiftShape(final BoundingShape shape, final Vector2f shift) {
    if (shape instanceof BoundingBox) {
      ((BoundingBox) shape).setMax(((BoundingBox) shape).getMax().add(shift));
      ((BoundingBox) shape).setMin(((BoundingBox) shape).getMin().add(shift));
    }
    else if (shape instanceof BoundingCircle) {
      ((BoundingCircle) shape)
          .setCenter(((BoundingCircle) shape).getCenter().add(shift));
    }
  }
}
