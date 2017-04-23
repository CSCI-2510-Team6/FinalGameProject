package sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import collision.Collider;
import javagames.util.Matrix3x3f;
import javagames.util.ResourceLoader;
import javagames.util.Utility;
import javagames.util.Vector2f;

/**
 * This class extends the CollidableSprite class and was not designed with
 * extension in mind.
 *
 * @author Andres Ward
 *
 */
public class Enemy extends CollidableSprite {

  public static final float GRAVITY = 10.0f;

  private final BufferedImage[] unharmedImage;
  // Added image
  private BufferedImage[] injuredImage;
  private float           angleRad;
  private final float     rotationRad;
  private float           animationTimer;
  // Reference to image to be drawn
  private BufferedImage imageToDraw;

  public Enemy(final float xPos, final float yPos, final int width,
      final int height, final BufferedImage image, final Collider col) {
    super(xPos, yPos, width, height, image, col);

    angleRad = 0;
    rotationRad = (float) Math.PI / 2;
    // Begin with velocity to cause enemy to move left
    setVelocity(getVelocity().sub(new Vector2f(2, 0)));
    // Default image to drawn is the one from the constructor
    unharmedImage = new BufferedImage[2];
    injuredImage = new BufferedImage[2];

    // The default image loaded from the constructor is actually the left facing
    // image. I want the left aka "mirrored images" to be second in the image
    // array.
    unharmedImage[1] = getImage();
    unharmedImage[0] =
        Utility.mirrorScaleImage(unharmedImage[1], width, height);
    imageToDraw = unharmedImage[1];
  }

  /**
   * This method is invoked when it has become determined that the enemy has
   * been hit with something that causes damage.
   */
  public void handleInjury() {
    // reset timer
    animationTimer = 0;
    // Switch image to be drawn to the injured frame
    imageToDraw = (getVelocity().x > 0) ? injuredImage[0] : injuredImage[1];
  }

  @Override
  public void updateWorld(final float delta) {
    animationTimer += delta;

    // If the enemy is moving left then rotate counter clockwise
    if (getVelocity().x < 0) {
      angleRad -= rotationRad * delta;
    }
    // rotate clockwise
    else {
      angleRad += rotationRad * delta;
    }

    // Keep angleRad under 6.28
    if (angleRad > (2 * Math.PI)) {
      angleRad -= 2 * Math.PI;
    }

    // update position
    setVelocity(getVelocity().sub(new Vector2f(0, Sorceress.GRAVITY * delta)));
    setCenterPosition(getCenterPosition().add(getVelocity().mul(delta)));
    // update origin of matrix
    setWorld(Matrix3x3f.translate(getCenterPosition()));
    // update collider positions
    getCollider().updateWorld(getCenterPosition());

    // Only use the injured image for the first 1/3 seconds intersection
    if (animationTimer > 0.3f) {
      imageToDraw = (getVelocity().x > 0) ? unharmedImage[0] : unharmedImage[1];
    }
  }

  @Override
  public void render(final Graphics g, final Matrix3x3f screen,
      final boolean drawCollider) {
    // Get position of enemy from matrix
    Vector2f vector = getWorld().mul(new Vector2f());
    // Convert position to screen coordinates
    vector = screen.mul(vector);

    final Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    // Use AffineTransfrom to rotate images
    final AffineTransform transform =
        AffineTransform.getTranslateInstance(vector.x, vector.y);
    transform.rotate(angleRad);
    transform.translate(-getImage().getWidth() / 2,
        -getImage().getHeight() / 2);
    g2d.drawImage(imageToDraw, transform, null);

    if (drawCollider) {
      getCollider().render(g2d, screen);
    }
  }

  @Override
  public void uncollide(final int direction) {
    super.uncollide(direction);

    if (direction == 2) {
      setVelocity(new Vector2f(2, getVelocity().y));
    }
    if (direction == 4) {
      setVelocity(new Vector2f(-2, getVelocity().y));
    }
  }

  /**
   * This method adds an injured imaged to the object. It would better to have
   * all the images in one sprite sheet.
   *
   * @param fileName
   * @param width
   * @param height
   */
  public void setInjuredImage(final String fileName, final int width,
      final int height) {
    final InputStream in = ResourceLoader.load(Enemy.class,
        "res/assets/images/" + fileName, "/images/" + fileName);
    try {
      // Read in image from file
      injuredImage[0] = ImageIO.read(in);
    }
    catch (final IOException e) {
      e.printStackTrace();
      injuredImage = null;
    }
    // Scale image and mirror appropriately
    injuredImage[1] = Utility.scaleImage(injuredImage[0], width, height);
    injuredImage[0] = Utility.mirrorScaleImage(injuredImage[0], width, height);
  }
}
