package javagames.util;

/**
 * This class represents a virtual camera to follow the player
 *
 * @author Andres
 *
 */
public class Camera {
  private Vector2f position;

  // Constructor
  public Camera(final Vector2f pos) {
    position = pos;
  }

  public Vector2f getPosition() {
    return position;
  }

  public void update(final Vector2f pos) {
    position = pos.add(new Vector2f(8, 4.5f));

    // Move opposite of the input
    position = new Vector2f(-position.x, position.y);
  }
}
