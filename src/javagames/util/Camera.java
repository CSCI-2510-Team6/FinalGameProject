package javagames.util;

public class Camera {
  private Vector2f position;

  public Camera(final Vector2f pos) {
    position = pos;
  }

  public Vector2f getPosition() {
    return position;
  }

  public void update(final Vector2f pos) {
    position = pos.add(new Vector2f(8, 4.5f));
    position = new Vector2f(-1 * position.x, position.y);
  }
}
