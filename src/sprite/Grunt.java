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

public class Grunt extends CollidableSprite {

  private enum Action {
    STAND, MOVE, ATTACK, DEAD
  }

  public static final float     GRAVITY     = 10.0f;
  private final BufferedImage[] standLeft   = new BufferedImage[1];
  private int                   standLeftIndex;
  private final BufferedImage[] standRight  = new BufferedImage[1];
  private int                   standRightIndex;
  private final BufferedImage[] moveLeft    = new BufferedImage[9];
  private int                   moveLeftIndex;
  private final BufferedImage[] moveRight   = new BufferedImage[9];
  private int                   moveRightIndex;
  private final BufferedImage[] attackLeft  = new BufferedImage[9];
  private int                   attackLeftIndex;
  private final BufferedImage[] attackRight = new BufferedImage[9];
  private int                   attackRightIndex;
  private final BufferedImage[] dieLeft     = new BufferedImage[9];
  private int                   dieLeftIndex;
  private final BufferedImage[] dieRight    = new BufferedImage[9];
  private int                   dieRightIndex;
  private float                 animationTime;
  private Action                action;
  private boolean               facingRight;

  public Grunt(final float xPos, final float yPos, final int width,
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

  public void handleInjury() {
    if (action != Action.DEAD) {
      action = Action.DEAD;
      setVelocity(new Vector2f(0, getVelocity().y));
    }
  }

  /**
   * This method resets the timers and animation array indexes on each state
   * change.
   */
  private void resetState() {
    animationTime = 0;
    moveLeftIndex = moveRightIndex =
        attackLeftIndex = attackRightIndex = dieLeftIndex = dieRightIndex = 0;
  }

  @Override
  public void uncollide(final int direction) {
    super.uncollide(direction);
  }

  public void updateWorld(final float delta, final Vector2f heroPos) {
    setVelocity(getVelocity().sub(new Vector2f(0, EvilKnight.GRAVITY * delta)));
    setCenterPosition(getCenterPosition().add(getVelocity().mul(delta)));
    // Set the origin in the matrix
    setWorld(Matrix3x3f.translate(getCenterPosition()));

    if (withinVision(heroPos) && (action != Action.DEAD)) {
      if (isLeft(heroPos)) {
        moveLeft();
      }
      else {
        moveRight();
      }
    }

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
      case MOVE:
        // Facing left
        if (!facingRight) {
          if (animationTime > 0.25f) {
            moveLeftIndex = ++moveLeftIndex % moveLeft.length;
            animationTime -= 0.25f;
          }
        }
        // Facing right same as above
        else {
          if (animationTime > 0.25f) {
            moveRightIndex = ++moveRightIndex % moveRight.length;
            animationTime -= 0.25f;
          }
        }
        break;
      case ATTACK:
        if (!facingRight) {
          if ((animationTime > 0.25f)) {
            attackLeftIndex = ++attackLeftIndex % attackLeft.length;
            animationTime -= 0.25f;
          }
        }
        else {
          if ((animationTime > 0.25f)) {
            attackRightIndex = ++attackRightIndex % attackRight.length;
            animationTime -= 0.25f;
          }
        }
        break;
      case DEAD:
        if (!facingRight && (dieLeftIndex != (dieLeft.length - 1))) {
          if ((animationTime > 0.25f)) {
            dieLeftIndex = ++dieLeftIndex % dieLeft.length;
            animationTime -= 0.25f;
          }
        }
        else {
          if (((animationTime > 0.25f)
              && (dieRightIndex != (dieRight.length - 1)))) {
            dieRightIndex = ++dieRightIndex % dieRight.length;
            animationTime -= 0.25f;
          }
        }
      default:
        break;
    }
    // Handle collider update
    final List<BoundingShape> inner = new ArrayList<>();
    final Vector2f maxA = getCenterPosition().add(new Vector2f(0.2f, 0.6f));
    final Vector2f minA = getCenterPosition().sub(new Vector2f(0.2f, 0.6f));
    inner.add(new BoundingBox(maxA, minA));
    setCollider(new Collider(getCenterPosition(), 0.4f, 1.2f, inner));
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
    final int topLeftX = (int) (vector.x - 85);
    final int topLeftY = (int) (vector.y - 60);
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
      case MOVE:
        if (!facingRight) {
          g.drawImage(moveLeft[moveLeftIndex], topLeftX, topLeftY, null);
        }
        else {
          g.drawImage(moveRight[moveRightIndex], topLeftX, topLeftY, null);
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
    if (drawCollider && (getCollider() != null)) {
      getCollider().render(g, screen);
    }
  }

  /**
   * This method extracts all the subimages from the sprite sheet and scales
   * them before inserting them into their arrays. Images are also mirrored to
   * get the left sides.
   */
  private void initAnimations() {
    final int width = 680;
    final int height = 370;
    final int desiredWidth = 170;
    final int desiredHeight = 110;
    standLeft[0] =
        Utility.scaleImage(getImage().getSubimage(0, 0, width, height),
            desiredWidth, desiredHeight);
    standRight[0] =
        Utility.mirrorScaleImage(getImage().getSubimage(0, 0, width, height),
            desiredWidth, desiredHeight);

    moveLeft[0] =
        Utility.scaleImage(getImage().getSubimage(width, 0, width, height),
            desiredWidth, desiredHeight);
    moveLeft[1] =
        Utility.scaleImage(getImage().getSubimage(width * 2, 0, width, height),
            desiredWidth, desiredHeight);
    moveLeft[2] =
        Utility.scaleImage(getImage().getSubimage(width * 3, 0, width, height),
            desiredWidth, desiredHeight);
    moveLeft[3] =
        Utility.scaleImage(getImage().getSubimage(width * 4, 0, width, height),
            desiredWidth, desiredHeight);
    moveLeft[4] =
        Utility.scaleImage(getImage().getSubimage(width * 5, 0, width, height),
            desiredWidth, desiredHeight);
    moveLeft[5] =
        Utility.scaleImage(getImage().getSubimage(width * 6, 0, width, height),
            desiredWidth, desiredHeight);
    moveLeft[6] =
        Utility.scaleImage(getImage().getSubimage(width * 7, 0, width, height),
            desiredWidth, desiredHeight);
    moveLeft[7] =
        Utility.scaleImage(getImage().getSubimage(width * 8, 0, width, height),
            desiredWidth, desiredHeight);
    moveLeft[8] =
        Utility.scaleImage(getImage().getSubimage(width * 9, 0, width, height),
            desiredWidth, desiredHeight);

    moveRight[0] = Utility.mirrorScaleImage(
        getImage().getSubimage(width, 0, width, height), desiredWidth,
        desiredHeight);
    moveRight[1] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 2, 0, width, height), desiredWidth,
        desiredHeight);
    moveRight[2] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 3, 0, width, height), desiredWidth,
        desiredHeight);
    moveRight[3] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 4, 0, width, height), desiredWidth,
        desiredHeight);
    moveRight[4] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 5, 0, width, height), desiredWidth,
        desiredHeight);
    moveRight[5] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 6, 0, width, height), desiredWidth,
        desiredHeight);
    moveRight[6] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 7, 0, width, height), desiredWidth,
        desiredHeight);
    moveRight[7] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 8, 0, width, height), desiredWidth,
        desiredHeight);
    moveRight[8] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 9, 0, width, height), desiredWidth,
        desiredHeight);

    attackLeft[0] =
        Utility.scaleImage(getImage().getSubimage(width, height, width, height),
            desiredWidth, desiredHeight);
    attackLeft[1] = Utility.scaleImage(
        getImage().getSubimage(width * 2, height, width, height), desiredWidth,
        desiredHeight);
    attackLeft[2] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height, width, height), desiredWidth,
        desiredHeight);
    attackLeft[3] = Utility.scaleImage(
        getImage().getSubimage(width * 4, height, width, height), desiredWidth,
        desiredHeight);
    attackLeft[4] = Utility.scaleImage(
        getImage().getSubimage(width * 5, height, width, height), desiredWidth,
        desiredHeight);
    attackLeft[5] = Utility.scaleImage(
        getImage().getSubimage(width * 6, height, width, height), desiredWidth,
        desiredHeight);
    attackLeft[6] = Utility.scaleImage(
        getImage().getSubimage(width * 7, height, width, height), desiredWidth,
        desiredHeight);
    attackLeft[7] = Utility.scaleImage(
        getImage().getSubimage(width * 8, height, width, height), desiredWidth,
        desiredHeight);
    attackLeft[8] = Utility.scaleImage(
        getImage().getSubimage(width * 9, height, width, height), desiredWidth,
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
    attackRight[4] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 4, height, width, height), desiredWidth,
        desiredHeight);
    attackRight[5] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 5, height, width, height), desiredWidth,
        desiredHeight);
    attackRight[6] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 6, height, width, height), desiredWidth,
        desiredHeight);
    attackRight[7] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 7, height, width, height), desiredWidth,
        desiredHeight);
    attackRight[8] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 8, height, width, height), desiredWidth,
        desiredHeight);

    dieLeft[0] =
        Utility.scaleImage(getImage().getSubimage(0, height * 2, width, height),
            desiredWidth, desiredHeight);
    dieLeft[1] = Utility.scaleImage(
        getImage().getSubimage(width, height * 2, width, height), desiredWidth,
        desiredHeight);
    dieLeft[2] = Utility.scaleImage(
        getImage().getSubimage(width * 2, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieLeft[3] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieLeft[4] = Utility.scaleImage(
        getImage().getSubimage(width * 4, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieLeft[5] = Utility.scaleImage(
        getImage().getSubimage(width * 5, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieLeft[6] = Utility.scaleImage(
        getImage().getSubimage(width * 6, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieLeft[7] = Utility.scaleImage(
        getImage().getSubimage(width * 7, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieLeft[8] = Utility.scaleImage(
        getImage().getSubimage(width * 8, height * 2, width, height),
        desiredWidth, desiredHeight);

    dieRight[0] = Utility.mirrorScaleImage(
        getImage().getSubimage(0, height * 2, width, height), desiredWidth,
        desiredHeight);
    dieRight[1] = Utility.mirrorScaleImage(
        getImage().getSubimage(width, height * 2, width, height), desiredWidth,
        desiredHeight);
    dieRight[2] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 2, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieRight[3] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 3, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieRight[4] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 4, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieRight[5] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 5, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieRight[6] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 6, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieRight[7] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 7, height * 2, width, height),
        desiredWidth, desiredHeight);
    dieRight[8] = Utility.mirrorScaleImage(
        getImage().getSubimage(width * 8, height * 2, width, height),
        desiredWidth, desiredHeight);
  }

  private boolean withinVision(final Vector2f heroPos) {
    boolean result = false;

    final Vector2f c = getCenterPosition().sub(heroPos);
    result = c.lenSqr() < 25;

    return result;
  }

  private boolean isLeft(final Vector2f heroPos) {
    boolean result = false;

    if (heroPos.x < getCenterPosition().x) {
      result = true;
    }

    return result;
  }

  /**
   * This method provides a left velocity of 2 units/second.
   */
  public void moveLeft() {
    setVelocity(new Vector2f(-1.5f, getVelocity().y));
    facingRight = false;
    action = Action.MOVE;
  }

  /**
   * This method provides a right velocity of 2 units/second.
   */
  public void moveRight() {
    setVelocity(new Vector2f(1.5f, getVelocity().y));
    facingRight = true;
    action = Action.MOVE;
  }
}
