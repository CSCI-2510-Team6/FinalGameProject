package collision;

import collision.shapes.BoundingShape;
import javagames.util.Vector2f;

/**
 * This class is in charge of determining if different colliders are colliding
 * with one another.
 *
 * @author Andres Ward
 *
 */
public class ColliderManager {

  // Constructor
  public ColliderManager() {
  }

  /**
   * This method checks if any of the inner bounds from two colliders intersect
   *
   * @param colliderA
   * @param colliderB
   * @return boolean
   */
  public boolean checkCollidersForInnerIntersection(final Collider colliderA,
      final Collider colliderB) {

    // Check for outer intersection
    if (checkCollidersForOuterIntersection(colliderA, colliderB)) {
      // Check if inner shape lists are null
      if ((colliderA.getInnerBounds() != null)
          && (colliderB.getInnerBounds() != null)) {
        // Compare every shape in colliderA against each shape in colliderB
        for (final BoundingShape shapeA : colliderA.getInnerBounds()) {
          for (final BoundingShape shapeB : colliderB.getInnerBounds()) {
            if (shapeA.intersects(shapeB)) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  /**
   * This method checks if the outer bounds from two colliders intersect
   *
   * @param colliderA
   * @param colliderB
   * @return boolean
   */
  public boolean checkCollidersForOuterIntersection(final Collider colliderA,
      final Collider colliderB) {
    boolean result = false;

    // Check if both colliders are not null
    if ((colliderA != null) && (colliderB != null)) {
      // Check if both outer bounds are not null
      if ((colliderA.getOuterBound() != null)
          && (colliderB.getOuterBound() != null)) {
        // Check for intersection of the outer bounds
        if (colliderA.getOuterBound().intersects(colliderB.getOuterBound())) {
          result = true;
        }
      }
    }

    return result;
  }

  /**
   * This method determines on what direction the first collider intersects the
   * second collider.
   *
   * @param colliderA
   * @param colliderB
   * @return int direction
   */
  public int determineCollisionDirection(final Collider colliderA,
      final Collider colliderB) {
    int direction = 0;
    BoundingShape stageShape = null;

    // Find which inner shape was collided with
    if ((stageShape = findInnerIntersection(colliderA, colliderB)) != null) {

      final Vector2f moverCenter = colliderA.getOuterBound().getCenter();
      final Vector2f stageCenter = stageShape.getCenter();
      final Vector2f moverMin = colliderA.getOuterBound().getMin();
      final Vector2f moverMax = colliderA.getOuterBound().getMax();
      final Vector2f stageMin = stageShape.getMin();
      final Vector2f stageMax = stageShape.getMax();

      /*
       * 0 = no collision 1 = top 2 = right 3 = bottom 4 = left
       *
       */

      // mover is in top left quadrant
      if ((moverCenter.x <= stageCenter.x)
          && (moverCenter.y >= stageCenter.y)) {
        // fully right
        if (moverMin.x >= stageMin.x) {
          direction = 1;
        }
        // fully below
        else if (moverMax.y <= stageMax.y) {
          direction = 4;
        }
        // is center height above the stage max height
        else {
          direction = (moverCenter.y > stageMax.y) ? 1 : 4;
        }
      }
      // mover is in top right quadrant
      else if ((moverCenter.x > stageCenter.x)
          && (moverCenter.y >= stageCenter.y)) {
        // fully left
        if (moverMax.x <= stageMax.x) {
          direction = 1;
        }
        // fully below
        else if (moverMax.y <= stageMax.y) {
          direction = 2;
        }
        // is center height above the stage max height
        else {
          direction = (moverCenter.y > stageMax.y) ? 1 : 2;
        }
      }
      // mover is in bottom left quadrant
      else if ((moverCenter.x <= stageCenter.x)
          && (moverCenter.y < stageCenter.y)) {
        // fully right
        if (moverMin.x >= stageMin.x) {
          direction = 3;
        }
        // fully above
        else if (moverMin.y >= stageMin.y) {
          direction = 4;
        }
        // is center height below the stage min height
        else {
          direction = (moverCenter.y < stageMin.y) ? 3 : 4;
        }
      }
      // mover is in bottom right quadrant
      else if ((moverCenter.x > stageCenter.x)
          && (moverCenter.y < stageCenter.y)) {
        // fully left
        if (moverMax.x <= stageMax.x) {
          direction = 3;
        }
        // fully above
        else if (moverMin.y >= stageMin.y) {
          direction = 2;
        }
        // is center height below the stage min height
        else {
          direction = (moverCenter.y < stageMin.y) ? 3 : 2;
        }
      }
    }

    return direction;
  }

  /**
   * This method returns which inner shape was intersected
   *
   * @param colliderA
   * @param colliderB
   * @return BoundingShape
   */
  private BoundingShape findInnerIntersection(final Collider colliderA,
      final Collider colliderB) {

    // Check for outer intersection
    if (checkCollidersForOuterIntersection(colliderA, colliderB)) {
      // Check if inner shape lists are null
      if ((colliderA.getInnerBounds() != null)
          && (colliderB.getInnerBounds() != null)) {
        // Compare every shape in colliderA against each shape in colliderB
        for (final BoundingShape shapeA : colliderA.getInnerBounds()) {
          for (final BoundingShape shapeB : colliderB.getInnerBounds()) {
            if (shapeA.intersects(shapeB)) {
              return shapeB;
            }
          }
        }
      }
    }

    return null;
  }

}
