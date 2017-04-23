package collision.shapes;

import javagames.util.Vector2f;

/**
 * This class represents a circle boundary for intersection testing
 *
 * @author Andres Ward
 *
 */
public class BoundingCircle implements BoundingShape {
  private final float radius;
  private Vector2f    center;

  public BoundingCircle(final float r, final Vector2f c) {
    radius = r;
    center = c;
  }

  @Override
  public boolean intersects(final BoundingShape shape) {
    boolean result = false;

    if (shape instanceof BoundingCircle) {
      result =
          intersectCircle(center, radius, ((BoundingCircle) shape).getCenter(),
              ((BoundingCircle) shape).getRadius());
    }
    else if (shape instanceof BoundingBox) {
      result = intersectCircleAABB(center, radius,
          ((BoundingBox) shape).getMin(), ((BoundingBox) shape).getMax());
    }

    return result;
  }

  /**
   * Test two circles to see if they overlap
   *
   * @param c0
   * @param r0
   * @param c1
   * @param r1
   * @return
   */
  private boolean intersectCircle(final Vector2f c0, final float r0,
      final Vector2f c1, final float r1) {
    final Vector2f c = c0.sub(c1);
    final float r = r0 + r1;
    return c.lenSqr() < (r * r);
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
    // If radius is left of rect
    if (c.x < min.x) {
      d += (c.x - min.x) * (c.x - min.x);
    }
    // If radius is right of rect
    if (c.x > max.x) {
      d += (c.x - max.x) * (c.x - max.x);
    }
    // If radius is above rect
    if (c.y < min.y) {
      d += (c.y - min.y) * (c.y - min.y);
    }
    // If radius is below rect
    if (c.y > max.y) {
      d += (c.y - max.y) * (c.y - max.y);
    }
    return d < (r * r);
  }

  public float getRadius() {
    return radius;
  }

  public Vector2f getCenter() {
    return center;
  }

  public Vector2f getMax() {
    return center.add(new Vector2f(radius, radius));
  }

  public Vector2f getMin() {
    return center.sub(new Vector2f(radius, radius));
  }

  public void setCenter(final Vector2f center) {
    this.center = center;
  }

}
