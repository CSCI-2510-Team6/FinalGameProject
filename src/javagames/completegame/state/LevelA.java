package javagames.completegame.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import collision.ColliderManager;
import javagames.util.Camera;
import javagames.util.KeyboardInput;
import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;
import sprite.CollidableSprite;
import sprite.Sorceress;

public class LevelA extends State {
  private Camera           camera;
  private ColliderManager  colliderManager;
  private CollidableSprite background;
  private Sorceress        hero;
  private KeyboardInput    keys;
  private boolean          drawBounds;

  public LevelA(final GameState state) {
  }

  @Override
  public void enter() {
    colliderManager = new ColliderManager();
    background = (CollidableSprite) controller.getAttribute("levelA");
    hero = (Sorceress) controller.getAttribute("hero");
    camera = new Camera(hero.getCenterPosition());

    // The default is to not draw the collider bounds
    drawBounds = false;

    keys = (KeyboardInput) controller.getAttribute("keys");
  }

  @Override
  public void processInput(final float delta) {
    // movement, don't allow both A and D keys to cause action together.
    if (keys.keyDown(KeyEvent.VK_A) != keys.keyDown(KeyEvent.VK_D)) {
      // Move Left
      if (keys.keyDown(KeyEvent.VK_A)) {
        hero.moveLeft();
      }
      // Move Right
      if (keys.keyDown(KeyEvent.VK_D)) {
        hero.moveRight();
      }
    }
    else {
      hero.stopMoving();
    }

    // jump
    if (keys.keyDown(KeyEvent.VK_J) && !hero.isJumpButtonHeld()
        && !hero.isInAir() && !hero.isJumpDisabled()) {
      hero.pressJumpButton();
    }
    // Handles variable jump based on length of time jump button is pressed
    if (hero.isJumpButtonHeld()) {
      hero.jump(delta);
    }

    if (!keys.keyDown(KeyEvent.VK_J)) {
      hero.releaseJumpButton();
    }

    if (keys.keyDownOnce(KeyEvent.VK_K)) {
      hero.melee();
    }

    // Toggle collider rendering
    if (keys.keyDownOnce(KeyEvent.VK_B)) {
      drawBounds = !drawBounds;
    }
  }

  @Override
  public void updateObjects(final float delta) {
    background.updateWorld(delta);
    hero.updateWorld(delta);

    int collisionDirection;

    // Handle hero interaction with the stage
    while ((collisionDirection = colliderManager.determineCollisionDirection(
        hero.getCollider(), background.getCollider())) != 0) {
      hero.uncollide(collisionDirection);
    }

    camera.update(new Vector2f(hero.getCenterPosition().x, 0));
    checkForLevelWon();
  }

  private void checkForLevelWon() {
    /*
     * if () { state.setLevel(state.getLevel() + 1); thruster.done();
     * getController().setState(new LevelB(state)); }
     */
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {

    // begin of camera
    final Vector2f cameraPosInScreenCoords = view.mul(camera.getPosition());

    g.translate(cameraPosInScreenCoords.x, cameraPosInScreenCoords.y);

    background.render(g, view, drawBounds);

    hero.render(g, view, drawBounds);

    // end of camera
    g.translate(-cameraPosInScreenCoords.x, -cameraPosInScreenCoords.y);

    /*
     * update hud here acme.drawLives(g, view, state.getLives());
     * acme.drawScore(g, state.getScore());
     */
  }
}
