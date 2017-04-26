package sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import collision.Collider;
import collision.shapes.BoundingBox;
import collision.shapes.BoundingShape;
import javagames.util.Matrix3x3f;
import javagames.util.Utility;
import javagames.util.Vector2f;

public class EvilKnight extends CollidableSprite {
  private enum Action {
    STAND, BLOCK, ATTACK, DEAD
  }

  public static final float GRAVITY = 10.0f;

  private final BufferedImage[] standLeft  = new BufferedImage[1];
  private int                   standLeftIndex;
  private final BufferedImage[] standRight = new BufferedImage[1];
  private int                   standRightIndex;

  private final BufferedImage[] blockLeft  = new BufferedImage[1];
  private int                   blockLeftIndex;
  private final BufferedImage[] blockRight = new BufferedImage[1];
  private int                   blockRightIndex;

  private final BufferedImage[] attackLeft  = new BufferedImage[4];
  private int                   attackLeftIndex;
  private final BufferedImage[] attackRight = new BufferedImage[4];
  private int                   attackRightIndex;

  private final BufferedImage[] dieLeft  = new BufferedImage[8];
  private int                   dieLeftIndex;
  private final BufferedImage[] dieRight = new BufferedImage[8];
  private int                   dieRightIndex;

  private float         animationTime;
  private Action        action;
  private final boolean facingRight;

  public EvilKnight(final float xPos, final float yPos, final int width,
      final int height, final BufferedImage image, final Collider col) {
    super(xPos, yPos, width, height, image, col);

    initAnimations();
    // Default starting state
    action = Action.STAND;
    facingRight = false;
  }

  /**
   * This method changes state to attack
   */
  public void attack() {
    switch (action) {
      case STAND:
        action = Action.ATTACK;
        resetState();
        break;
      default:
        break;
    }
  }

  /**
   * This method resets the timers and animation array indexes on each state
   * change.
   */
  private void resetState() {
    animationTime = 0;
    attackLeftIndex = attackRightIndex = 0;
  }

  @Override
  public void uncollide(final int direction) {
    super.uncollide(direction);
  }

  @Override
  public void updateWorld(final float delta) {

    setVelocity(getVelocity().sub(new Vector2f(0, EvilKnight.GRAVITY * delta)));
    setCenterPosition(getCenterPosition().add(getVelocity().mul(delta)));

    // Set the origin in the matrix
    setWorld(Matrix3x3f.translate(getCenterPosition()));

    animationTime += delta;

    // Here the animation state is determined and the proper frame of animation
    // from the animation arrays is selected. The power of modulus is really
    // useful here.
    switch (action) {
      case STAND:
        // Facing left
        if (!facingRight) {
        }
        // Facing right same as above
        else {
        }
        break;
      case BLOCK:
        // Facing left
        if (!facingRight) {
        }
        // Facing right same as above
        else {
        }
        break;
      case ATTACK:
        if (!facingRight) {
          if ((animationTime > 0.5f)) {
            attackLeftIndex = ++attackLeftIndex % attackLeft.length;
            animationTime -= 0.5f;
          }
        }
        else {
          if ((animationTime > 0.5f)) {
            attackRightIndex = ++attackRightIndex % attackRight.length;
            animationTime -= 0.5f;
          }
        }
      case DEAD:
        if (!facingRight) {
          if ((animationTime > 0.25f)) {
            dieLeftIndex = ++dieLeftIndex % dieLeft.length;
            animationTime -= 0.25f;
          }
        }
        else {
          if ((animationTime > 0.25f)) {
            dieRightIndex = ++dieRightIndex % dieRight.length;
            animationTime -= 0.25f;
          }
        }
      default:
        break;
    }

    // Handle collider update
    final List<BoundingShape> inner = new ArrayList<>();

    final Vector2f maxA = getCenterPosition().add(new Vector2f(0.6f, 0.8f));
    final Vector2f minA = getCenterPosition().sub(new Vector2f(0.6f, 0.8f));
    inner.add(new BoundingBox(maxA, minA));

    setCollider(new Collider(getCenterPosition(), 1.2f, 1.6f, inner));
  }

  @Override
  public void render(final Graphics g, final Matrix3x3f screen,
      final boolean drawCollider) {

    // Get position from matrix
    Vector2f vector = getWorld().mul(new Vector2f());
    // Convert Cartesian coordinates to screen coordinates
    vector = screen.mul(vector);

    // Determine the top left coordinates based of the center position and the
    // size of the sprite images
    final int topLeftX = (int) (vector.x - 160);
    final int topLeftY = (int) (vector.y - 150);

    // Render the correct image based off the animation state and frame position
    switch (action) {
      case STAND:
        if (!facingRight) {
          g.drawImage(standLeft[standLeftIndex], topLeftX, topLeftY, null);
        }
        else {
          g.drawImage(standRight[standRightIndex], topLeftX, topLeftY, null);
        }
        break;
      case BLOCK:
        if (!facingRight) {
          g.drawImage(blockLeft[blockLeftIndex], topLeftX, topLeftY, null);
        }
        else {
          g.drawImage(blockRight[blockRightIndex], topLeftX, topLeftY, null);
        }
        break;
      case ATTACK:
        if (!facingRight) {
          g.drawImage(attackLeft[attackLeftIndex], topLeftX, topLeftY, null);
        }
        else {
          g.drawImage(attackRight[attackRightIndex], topLeftX, topLeftY, null);
        }
        break;
      case DEAD:
        if (!facingRight) {
          g.drawImage(dieLeft[dieLeftIndex], topLeftX, topLeftY, null);
        }
        else {
          g.drawImage(dieRight[dieRightIndex], topLeftX, topLeftY, null);
        }
      default:
        break;
    }

    // Draw the collider shapes if necessary
    if (drawCollider) {
      getCollider().render(g, screen);
    }
  }

  /**
   * This method extracts all the subimages from the sprite sheet and scales
   * them before inserting them into their arrays. Images are also mirrored to
   * get the left sides.
   */
  private void initAnimations() {
    final int width = 170;
    final int height = 110;
    final int desiredWidth = width * 2;
    final int desiredHeight = height * 2;

    standLeft[0] =
        Utility.scaleImage(getImage().getSubimage(width, 0, width, height),
            desiredWidth, desiredHeight);

    standRight[0] = Utility.mirrorScaleImage(
        getImage().getSubimage(width, 0, width, height), desiredWidth,
        desiredHeight);

    blockLeft[0] =
        Utility.scaleImage(getImage().getSubimage(0, 0, width, height),
            desiredWidth, desiredHeight);

    blockRight[0] =
        Utility.mirrorScaleImage(getImage().getSubimage(0, 0, width, height),
            desiredWidth, desiredHeight);

    attackLeft[0] =
        Utility.scaleImage(getImage().getSubimage(0, height, width, height),
            desiredWidth, desiredHeight);
    attackLeft[1] =
        Utility.scaleImage(getImage().getSubimage(width, height, width, height),
            desiredWidth, desiredHeight);
    attackLeft[2] = Utility.scaleImage(
        getImage().getSubimage(width * 2, height, width, height), desiredWidth,
        desiredHeight);
    attackLeft[3] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height, width, height), desiredWidth,
        desiredHeight);

    attackRight[0] = Utility.mirrorScaleImage(
        getImage().getSubimage(0, height, width, height), desiredWidth,
        desiredHeight);
    attackRight[1] = Utility.mirrorScaleImage(
        getImage().getSubimage(width, height, width, height), desiredWidth,
        desiredHeight);
    attackRight[2] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 2, height, width, height), desiredWidth,
        desiredHeight);
    attackRight[3] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 3, height, width, height), desiredWidth,
        desiredHeight);

    dieLeft[0] = Utility.mirrorScaleImage(
        getImage().getSubimage(0, height * 2, width, height), desiredWidth,
        desiredHeight);
    dieLeft[1] = Utility.mirrorScaleImage(
        getImage().getSubimage(width, height * 2, width, height), desiredWidth,
        desiredHeight);
    dieLeft[2] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 2, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieLeft[3] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 3, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieLeft[4] = Utility.mirrorScaleImage(
        getImage().getSubimage(0, height * 3, width, height), desiredWidth,
        desiredHeight);
    dieLeft[5] = Utility.mirrorScaleImage(
        getImage().getSubimage(width, height * 3, width, height), desiredWidth,
        desiredHeight);
    dieLeft[6] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 2, height * 3, width, height),
        desiredWidth, desiredHeight);
    dieLeft[7] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 3, height * 3, width, height),
        desiredWidth, desiredHeight);

    dieRight[0] =
        Utility.scaleImage(getImage().getSubimage(0, height * 2, width, height),
            desiredWidth, desiredHeight);
    dieRight[1] = Utility.scaleImage(
        getImage().getSubimage(width, height * 2, width, height), desiredWidth,
        desiredHeight);
    dieRight[2] = Utility.scaleImage(
        getImage().getSubimage(width * 2, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieRight[3] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieRight[4] =
        Utility.scaleImage(getImage().getSubimage(0, height * 3, width, height),
            desiredWidth, desiredHeight);
    dieRight[5] = Utility.scaleImage(
        getImage().getSubimage(width, height * 3, width, height), desiredWidth,
        desiredHeight);
    dieRight[6] = Utility.scaleImage(
        getImage().getSubimage(width * 2, height * 3, width, height),
        desiredWidth, desiredHeight);
    dieRight[7] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height * 3, width, height),
        desiredWidth, desiredHeight);
  }

}
