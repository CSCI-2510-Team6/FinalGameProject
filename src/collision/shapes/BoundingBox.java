package collision.shapes;

import javagames.util.Vector2f;

/**
 * This class represents a rectangular boundary for intersection testing
 *
 * @author Andres Ward
 *
 */
public class BoundingBox implements BoundingShape {
  private Vector2f max;
  private Vector2f min;

  // Constructor
  public BoundingBox(final Vector2f upperRight, final Vector2f lowerLeft) {
    max = upperRight;
    min = lowerLeft;
  }

  @Override
  public boolean intersects(final BoundingShape shape) {
    boolean result = false;

    if (shape instanceof BoundingBox) {
      result = intersectAABB(min, max, ((BoundingBox) shape).getMin(),
          ((BoundingBox) shape).getMax());
    }
    else if (shape instanceof BoundingCircle) {
      result = intersectCircleAABB(((BoundingCircle) shape).getCenter(),
          ((BoundingCircle) shape).getRadius(), min, max);
    }

    return result;
  }

  /**
   * Compare two axis aligned bounding boxes and see if they intersect
   *
   * @param minA
   * @param maxA
   * @param minB
   * @param maxB
   * @return
   */
  private boolean intersectAABB(final Vector2f minA, final Vector2f maxA,
      final Vector2f minB, final Vector2f maxB) {
    // If fully left or right
    if ((minA.x > maxB.x) || (minB.x > maxA.x)) {
      return false;
    }
    // If fully above or below
    if ((minA.y > maxB.y) || (minB.y > maxA.y)) {
      return false;
    }
    return true;
  }

  /**
   * Compare a circle and an axis alligned bounding box to see if they intersect
   *
   * @param c
   * @param r
   * @param min
   * @param max
   * @return
   */
  private boolean intersectCircleAABB(final Vector2f c, final float r,
      final Vector2f min, final Vector2f max) {
    float d = 0.0f;
    // If circle is left of rect
    if (c.x < min.x) {
      d += (c.x - min.x) * (c.x - min.x);
    }
    // If circle is right of rect
    if (c.x > max.x) {
      d += (c.x - max.x) * (c.x - max.x);
    }
    // If circle is above rect
    if (c.y < min.y) {
      d += (c.y - min.y) * (c.y - min.y);
    }
    // If circle is below rect
    if (c.y > max.y) {
      d += (c.y - max.y) * (c.y - max.y);
    }
    return d < (r * r);
  }

  public Vector2f getMax() {
    return max;
  }

  public Vector2f getMin() {
    return min;
  }

  public void setMax(final Vector2f max) {
    this.max = max;
  }

  public void setMin(final Vector2f min) {
    this.min = min;
  }

  @Override
  public Vector2f getCenter() {
    final float x = (getMax().x + getMin().x) / 2;
    final float y = (getMax().y + getMin().y) / 2;

    return new Vector2f(x, y);
  }

}
