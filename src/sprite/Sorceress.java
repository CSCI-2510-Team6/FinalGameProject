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

public class Sorceress extends CollidableSprite {

  private enum Action {
    STAND, MOVE, JUMP, MELEE
  }

  public static final float GRAVITY = 10.0f;

  private final BufferedImage[] standLeft  = new BufferedImage[1];
  private int                   standLeftIndex;
  private final BufferedImage[] standRight = new BufferedImage[1];
  private int                   standRightIndex;

  private final BufferedImage[] moveLeft  = new BufferedImage[4];
  private int                   moveLeftIndex;
  private final BufferedImage[] moveRight = new BufferedImage[4];
  private int                   moveRightIndex;

  private final BufferedImage[] jumpLeft  = new BufferedImage[2];
  private int                   jumpLeftIndex;
  private final BufferedImage[] jumpRight = new BufferedImage[2];
  private int                   jumpRightIndex;

  private final BufferedImage[] meleeLeft  = new BufferedImage[5];
  private int                   meleeLeftIndex;
  private final BufferedImage[] meleeRight = new BufferedImage[5];
  private int                   meleeRightIndex;

  private float         animationTime;
  private float         jumpTime;
  private Action        action;
  private boolean       inAir;
  private boolean       facingRight;
  private boolean       jumpButtonHeld;
  private final boolean jumpDisabled;

  public Sorceress(final float xPos, final float yPos, final int width,
      final int height, final BufferedImage image, final Collider col) {
    super(xPos, yPos, width, height, image, col);

    initAnimations();
    // Default starting state
    action = Action.STAND;
    inAir = false;
    facingRight = true;
    jumpDisabled = false;
  }

  /**
   * This method provides a left velocity of 2 units/second.
   */
  public void moveLeft() {
    setVelocity(new Vector2f(-2, getVelocity().y));
    facingRight = false;

    // Handle possible transitions of state
    switch (action) {
      case STAND:
        action = Action.MOVE;
        resetState();
        break;
      case MOVE:
        action = Action.MOVE;
        break;
      case MELEE:
        action = Action.MOVE;
        resetState();
        break;
      default:
        break;
    }
  }

  /**
   * This method provides a right velocity of 2 units/second.
   */
  public void moveRight() {
    setVelocity(new Vector2f(2, getVelocity().y));
    facingRight = true;

    // Handle possible transitions of state
    switch (action) {
      case STAND:
        action = Action.MOVE;
        resetState();
        break;
      case MOVE:
        action = Action.MOVE;
        break;
      case MELEE:
        action = Action.MOVE;
        resetState();
        break;
      default:
        break;
    }
  }

  /**
   * This only occurs if the player tried to hold down the A and D keys at the
   * same time
   */
  public void stopMoving() {
    setVelocity(new Vector2f(0, getVelocity().y));

    switch (action) {
      case MOVE:
        action = Action.STAND;
        resetState();
        break;
      default:
        break;
    }
  }

  /**
   * This method handles the variable jump
   *
   * @param delta
   */
  public void jump(final float delta) {
    jumpTime += delta;

    // If the user continues to hold the jump button for more than 1/4 second
    // and less than or equal to 1/2 second they get some height
    if (inAir && (jumpTime <= 0.5)) {
      setVelocity(new Vector2f(getVelocity().x, 3.25f));
    }

    // Holding the button over 1/2 second gets a small amount left if the Jump
    // time wasn't quite at 1/2 second yet.
    if (inAir && (jumpTime > 0.5)) {
      if ((jumpTime - delta) <= 0.5) {
        setVelocity(new Vector2f(getVelocity().x, 3.5f));
      }
    }

    // Jump can only start if the sprite isn't in the air and has a low jumpTime
    if (!inAir && (jumpTime < 0.3)) {
      inAir = true;
      // The initial 1/4 second has a high starting velocity
      if (jumpTime <= 0.25) {
        setVelocity(new Vector2f(getVelocity().x, 3f));
      }
      action = Action.JUMP;
      resetState();
    }
  }

  /**
   * This method is required since a little more awareness of the key being held
   * down is necessary to avoid some edge cases.
   */
  public void pressJumpButton() {
    jumpButtonHeld = true;
  }

  /**
   * The jump button is released and the jumpTime is reset.
   */
  public void releaseJumpButton() {
    jumpButtonHeld = false;
    jumpTime = 0;
  }

  /**
   * This method is necessary to see if the button is still held for calculating
   * additional height.
   *
   * @return boolean
   */
  public boolean isJumpButtonHeld() {
    return jumpButtonHeld;
  }

  /**
   * Tell if the sprite is airborne
   *
   * @return boolean inAir
   */
  public boolean isInAir() {
    return inAir;
  }

  /**
   * This method is necessary to block jumping after the sprite is injured for a
   * short amount of time.
   *
   * @return boolean
   */
  public boolean isJumpDisabled() {
    return jumpDisabled;
  }

  /**
   * This method changes state to attack
   */
  public void melee() {
    switch (action) {
      case STAND:
        action = Action.MELEE;
        resetState();
        break;
      case MOVE:
        action = Action.MELEE;
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
    moveLeftIndex = moveRightIndex = meleeLeftIndex = meleeRightIndex = 0;
  }

  @Override
  public void uncollide(final int direction) {
    if (direction == 1) {
      inAir = false;
    }

    super.uncollide(direction);
  }

  @Override
  public void updateWorld(final float delta) {
    // Either the jump button has been release or the 1/2 second window to hold
    // the button is over. In this case gravity gets "turned on".
    if (!jumpButtonHeld || (jumpTime > 0.5)) {
      setVelocity(
          getVelocity().sub(new Vector2f(0, Sorceress.GRAVITY * delta)));
      setCenterPosition(getCenterPosition().add(getVelocity().mul(delta)));
    }
    // Jump button is held and 1/2 second jump window is still open
    // Don't use gravity for the ascent
    if (jumpButtonHeld && (jumpTime <= 0.5)) {
      setCenterPosition(getCenterPosition().add(getVelocity().mul(delta)));
    }

    // Set the origin in the matrix
    setWorld(Matrix3x3f.translate(getCenterPosition()));

    // Handle state transitions
    if (!inAir) {
      switch (action) {
        case JUMP:
          action = Action.STAND;
          resetState();
          break;
        default:
          break;
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
      case JUMP:
        int frame;

        if (getVelocity().y > 0) {
          frame = 0;
        }
        else {
          frame = 1;
        }

        if (!facingRight) {
          jumpLeftIndex = frame;
        }
        else {
          jumpRightIndex = frame;
        }
        break;
      case MOVE:
        if (!facingRight) {
          // Update walk animation frame every 1/4 second.
          if (animationTime > 0.25f) {
            moveLeftIndex = ++moveLeftIndex % moveLeft.length;
            animationTime -= 0.25f;
          }
        }
        else {
          if (animationTime > 0.25f) {
            moveRightIndex = ++moveRightIndex % moveRight.length;
            animationTime -= 0.25f;
          }
        }
        break;
      case MELEE:
        if (!facingRight) {
          if ((animationTime > 0.08f) && (meleeLeftIndex != 4)) {
            meleeLeftIndex = ++meleeLeftIndex % meleeLeft.length;
            animationTime -= 0.08f;
          }
          if (meleeLeftIndex == 4) {
            action = Action.STAND;
            resetState();
          }
        }
        else {
          if ((animationTime > 0.08f) && (meleeRightIndex != 4)) {
            meleeRightIndex = ++meleeRightIndex % meleeRight.length;
            animationTime -= 0.08f;
          }
          if (meleeRightIndex == 4) {
            action = Action.STAND;
            resetState();
          }
        }
      default:
        break;
    }

    // Handle collider update
    final List<BoundingShape> inner = new ArrayList<>();

    // The airborne collider bounds have slightly taller bounds
    if ((action == Action.JUMP)) {
      final Vector2f shiftedCenter =
          getCenterPosition().add(new Vector2f(0, 0f));
      final Vector2f maxA = shiftedCenter.add(new Vector2f(0.6f, 0.8f));
      final Vector2f minA = shiftedCenter.sub(new Vector2f(0.6f, 0.8f));
      inner.add(new BoundingBox(maxA, minA));

      setCollider(new Collider(shiftedCenter, 1.2f, 1.6f, inner));
    }
    // The grounded collider bounds are smaller
    else {
      final Vector2f maxA = getCenterPosition().add(new Vector2f(0.6f, 0.8f));
      final Vector2f minA = getCenterPosition().sub(new Vector2f(0.6f, 0.8f));
      inner.add(new BoundingBox(maxA, minA));

      setCollider(new Collider(getCenterPosition(), 1.2f, 1.6f, inner));
    }
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
    final int topLeftX = (int) (vector.x - 48);
    final int topLeftY = (int) (vector.y - 64);

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
      case JUMP:
        if (!facingRight) {
          g.drawImage(jumpLeft[jumpLeftIndex], topLeftX, topLeftY, null);
        }
        else {
          g.drawImage(jumpRight[jumpRightIndex], topLeftX, topLeftY, null);
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
      case MELEE:
        if (!facingRight) {
          g.drawImage(meleeLeft[meleeLeftIndex], topLeftX, topLeftY, null);
        }
        else {
          g.drawImage(meleeRight[meleeRightIndex], topLeftX, topLeftY, null);
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
    final int width = 24;
    final int height = 32;
    final int desiredWidth = width * 4;
    final int desiredHeight = height * 4;

    standLeft[0] = Utility.scaleImage(
        getImage().getSubimage(width * 2, height * 3, width, height),
        desiredWidth, desiredHeight);

    standRight[0] =
        Utility.scaleImage(getImage().getSubimage(0, height, width, height),
            desiredWidth, desiredHeight);

    moveLeft[0] = Utility.scaleImage(
        getImage().getSubimage(width * 5, height * 3, width, height),
        desiredWidth, desiredHeight);
    moveLeft[1] = Utility.scaleImage(
        getImage().getSubimage(width * 4, height * 3, width, height),
        desiredWidth, desiredHeight);
    moveLeft[2] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height * 3, width, height),
        desiredWidth, desiredHeight);
    moveLeft[3] = Utility.scaleImage(
        getImage().getSubimage(width * 4, height * 3, width, height),
        desiredWidth, desiredHeight);

    moveRight[0] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height, width, height), desiredWidth,
        desiredHeight);
    moveRight[1] = Utility.scaleImage(
        getImage().getSubimage(width * 4, height, width, height), desiredWidth,
        desiredHeight);
    moveRight[2] = Utility.scaleImage(
        getImage().getSubimage(width * 5, height, width, height), desiredWidth,
        desiredHeight);
    moveRight[3] = Utility.scaleImage(
        getImage().getSubimage(width * 4, height, width, height), desiredWidth,
        desiredHeight);

    jumpLeft[0] = Utility.scaleImage(
        getImage().getSubimage(width * 10, height * 3, width, height),
        desiredWidth, desiredHeight);
    jumpLeft[1] = Utility.scaleImage(
        getImage().getSubimage(width * 11, height * 3, width, height),
        desiredWidth, desiredHeight);

    jumpRight[0] = Utility.scaleImage(
        getImage().getSubimage(width * 10, height, width, height), desiredWidth,
        desiredHeight);
    jumpRight[1] = Utility.scaleImage(
        getImage().getSubimage(width * 11, height, width, height), desiredWidth,
        desiredHeight);

    meleeLeft[0] =
        Utility.scaleImage(getImage().getSubimage(0, height * 7, width, height),
            desiredWidth, desiredHeight);
    meleeLeft[1] = Utility.scaleImage(
        getImage().getSubimage(width, height * 7, width, height), desiredWidth,
        desiredHeight);
    meleeLeft[2] = Utility.scaleImage(
        getImage().getSubimage(width * 2, height * 7, width, height),
        desiredWidth, desiredHeight);
    meleeLeft[3] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height * 7, width, height),
        desiredWidth, desiredHeight);
    meleeLeft[4] = Utility.scaleImage(
        getImage().getSubimage(width * 4, height * 7, width, height),
        desiredWidth, desiredHeight);
    meleeRight[0] =
        Utility.scaleImage(getImage().getSubimage(0, height * 5, width, height),
            desiredWidth, desiredHeight);
    meleeRight[1] = Utility.scaleImage(
        getImage().getSubimage(width, height * 5, width, height), desiredWidth,
        desiredHeight);
    meleeRight[2] = Utility.scaleImage(
        getImage().getSubimage(width * 2, height * 5, width, height),
        desiredWidth, desiredHeight);
    meleeRight[3] = Utility.scaleImage(
        getImage().getSubimage(width * 3, height * 5, width, height),
        desiredWidth, desiredHeight);
    meleeRight[4] = Utility.scaleImage(
        getImage().getSubimage(width * 4, height * 5, width, height),
        desiredWidth, desiredHeight);
  }

}
