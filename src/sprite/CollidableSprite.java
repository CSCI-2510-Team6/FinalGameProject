package sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import collision.Collider;
import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;

/**
 * This class extends the Sprite class by adding a collider to it.
 *
 * @author Andres Ward
 *
 */
public class CollidableSprite extends Sprite {

  private Collider collider;

  public CollidableSprite(final float xPos, final float yPos, final int width,
      final int height, final BufferedImage image, final Collider col) {
    super(xPos, yPos, width, height, image);

    collider = col;
  }

  @Override
  public void updateWorld(final float delta) {
    super.updateWorld(delta);

    // The collider needs to be updated so that it accurately matches the image
    // it is tied to.
    collider.updateWorld(getCenterPosition());
  }

  /**
   * This method calls the Sprite class' render and also branches for drawing
   * its collider.
   *
   * @param g
   * @param view
   * @param drawCollider
   */
  public void render(final Graphics g, final Matrix3x3f view,
      final boolean drawCollider) {
    super.render(g, view);

    if (drawCollider) {
      collider.render(g, view);
    }
  }

  /**
   * This method uses the direction of collision to update the position and
   * velocity of the collidable sprite.
   *
   * @param direction
   */
  public void uncollide(final int direction) {
    /*
     * 0 = no collision 1 = top 2 = right 3 = bottom 4 = left
     *
     */
    switch (direction) {

      case 1:
        // Increment position up
        setCenterPosition(getCenterPosition().add(new Vector2f(0, 0.01f)));
        // Zero out y velocity
        setVelocity(new Vector2f(getVelocity().x, 0));
        break;

      case 2:
        // Increment position right
        setCenterPosition(getCenterPosition().add(new Vector2f(0.01f, 0)));
        // Zero out x velocity
        setVelocity(new Vector2f(0, getVelocity().y));
        break;

      case 3:
        // Increment position down
        setCenterPosition(getCenterPosition().add(new Vector2f(0, -0.01f)));
        // Zero out y velocity
        setVelocity(new Vector2f(getVelocity().x, 0));
        break;

      case 4:
        // Increment position left
        setCenterPosition(getCenterPosition().add(new Vector2f(-0.01f, 0)));
        // Zero out x velocity
        setVelocity(new Vector2f(0, getVelocity().y));
        break;

      default:
        System.out.println("Unrecognized collision direction");
        break;
    }

    // Update collider with new position
    updateWorld(0);
  }

  public Collider getCollider() {
    return collider;
  }

  public void setCollider(final Collider collider) {
    this.collider = collider;
  }

  /**
   * This was a helpful method to monitor the position and velocity of the
   * sprite during testing.
   *
   * @return String
   */
  public String getStats() {
    return String.format(
        "velocity: x = %.2f y = %.2f\nposition: x = %.2f y = %.2f\n\n",
        getVelocity().x, getVelocity().y, getCenterPosition().x,
        getCenterPosition().y);
  }
}
