package sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.interfaces.Drawable;
import javagames.util.Matrix3x3f;
import javagames.util.Utility;
import javagames.util.Vector2f;

/**
 * This class represents an image with the potential to be transformed or not.
 *
 * @author Andres Ward
 *
 */
public class Sprite implements Drawable {
  private BufferedImage image;
  private Matrix3x3f    world;
  private Vector2f      centerPosition;
  private Vector2f      velocity;

  // Constructor
  public Sprite(final float xPos, final float yPos, final int width,
      final int height, final BufferedImage img) {

    // Scale the image
    image = Utility.scaleImage(img, width, height);
    // Assign its spot in Cartesian coordinates
    centerPosition = new Vector2f(xPos, yPos);
    world = Matrix3x3f.identity();
    velocity = new Vector2f();
  }

  @Override
  public void updateWorld(final float delta) {
    centerPosition = centerPosition.add(velocity.mul(delta));
    world = Matrix3x3f.translate(centerPosition);
  }

  @Override
  public void render(final Graphics g, final Matrix3x3f view) {
    Vector2f vector = world.mul(new Vector2f());
    vector = view.mul(vector);

    if (image != null) {
      // Images are drawn as if they are rectangles from the top left corner.
      // The top left can be found by decrementing the center position by half
      // the image width and height.
      final int topLeftX = (int) (vector.x - (image.getWidth() / 2));
      final int topLeftY = (int) (vector.y - (image.getHeight() / 2));
      g.drawImage(image, topLeftX, topLeftY, null);
    }
  }

  public BufferedImage getImage() {
    return image;
  }

  protected void setImage(final BufferedImage image) {
    this.image = image;
  }

  public Matrix3x3f getWorld() {
    return world;
  }

  protected void setWorld(final Matrix3x3f world) {
    this.world = world;
  }

  public Vector2f getCenterPosition() {
    return centerPosition;
  }

  public void setCenterPosition(final Vector2f centerPosition) {
    this.centerPosition = centerPosition;
  }

  public Vector2f getVelocity() {
    return velocity;
  }

  protected void setVelocity(final Vector2f velocity) {
    this.velocity = velocity;
  }

}
