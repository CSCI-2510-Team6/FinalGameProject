package collision.shapes;

import javagames.util.Vector2f;

/**
 * This interface has the minimum methods needed for comparing bounding shape
 *
 * @author Andres Ward
 *
 */
public interface BoundingShape {

  /**
   * This method compares two bounding shapes to see if they intersect
   *
   * @param shape
   * @return
   */
  boolean intersects(BoundingShape shape);

  /**
   * This method returns the center coordinates of the shape
   *
   * @return Vector2f center
   */
  Vector2f getCenter();

  /**
   * This method returns the maximum point of the shape
   * 
   * @return Vector2f max
   */
  Vector2f getMax();

  /**
   * This method returns the minimum point of the shape
   * 
   * @return Vector2f min
   */
  Vector2f getMin();
}
