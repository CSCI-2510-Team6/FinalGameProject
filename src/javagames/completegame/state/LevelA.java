package javagames.completegame.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import collision.ColliderManager;
import javagames.completegame.admin.Hud;
import javagames.completegame.admin.QuickRestart;
import javagames.util.Camera;
import javagames.util.KeyboardInput;
import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;
import sprite.CollidableSprite;
import sprite.EvilKnight;
import sprite.Sorceress;
import sprite.Sprite;

public class LevelA extends State {
  private Camera           camera;
  private ColliderManager  colliderManager;
  private CollidableSprite background;
  private Sorceress        hero;
  private EvilKnight       evilKnight;
  private KeyboardInput    keys;
  private boolean          drawBounds;
  private Hud              hud;
  private Sprite           hudDisplay;
  private Sprite           heart;
  private final GameState  state;

  private List<CollidableSprite> shots;
  private QuickRestart           daggerSound;

  public LevelA(final GameState state) {
    this.state = state;
  }

  @Override
  public void enter() {
    colliderManager = new ColliderManager();
    background = (CollidableSprite) controller.getAttribute("levelA");

    hero = (Sorceress) controller.getAttribute("hero");
    hero.setAirSlashImage(
        (BufferedImage) controller.getAttribute("airSlashImage"));

    daggerSound = (QuickRestart) controller.getAttribute("airSlashSound");

    camera = new Camera(hero.getCenterPosition());

    evilKnight = (EvilKnight) controller.getAttribute("evilKnight");

    hudDisplay = (Sprite) controller.getAttribute("hudDisplay");
    heart = (Sprite) controller.getAttribute("heart");
    hud = new Hud(hudDisplay, heart);

    // The default is to not draw the collider bounds
    drawBounds = false;
    shots = new ArrayList<>();

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

    if (keys.keyDownOnce(KeyEvent.VK_K) && !hero.isInAir()) {
      shots.add(hero.melee());
      // Play air slash sound
      daggerSound.fire();
    }

    // Toggle collider rendering
    if (keys.keyDownOnce(KeyEvent.VK_B)) {
      drawBounds = !drawBounds;
    }
  }

  @Override
  public void updateObjects(final float delta) {
    background.updateWorld(delta);

    evilKnight.updateWorld(delta);

    hero.updateWorld(delta);

    int collisionDirection;

    // Handle hero interaction with the stage
    while ((collisionDirection = colliderManager.determineCollisionDirection(
        hero.getCollider(), background.getCollider())) != 0) {
      hero.uncollide(collisionDirection);
    }

    // Handle enemy interaction with the stage
    while ((collisionDirection = colliderManager.determineCollisionDirection(
        evilKnight.getCollider(), background.getCollider())) != 0) {
      evilKnight.uncollide(collisionDirection);
    }

    updateShots(delta);

    camera.update(new Vector2f(hero.getCenterPosition().x, 0));
    hud.update(delta);
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

    for (final CollidableSprite shot : shots) {
      shot.render(g, view, drawBounds);
    }

    evilKnight.render(g, view, drawBounds);

    hero.render(g, view, drawBounds);

    // end of camera
    g.translate(-cameraPosInScreenCoords.x, -cameraPosInScreenCoords.y);

    g.translate(0, 0);
    // update Hud display
    hud.drawHud(g, view, state.getHearts());
  }

  /**
   * Iterate over each shot and update it. I got the idea for this from the text
   * book chapter 17
   *
   * @param delta
   */
  public void updateShots(final float delta) {
    final ArrayList<CollidableSprite> copy = new ArrayList<>(shots);
    for (final CollidableSprite shot : copy) {
      updateShot(delta, shot);
    }
  }

  /**
   * Update the shot and check for intersections
   *
   * @param delta
   * @param shot
   */
  public void updateShot(final float delta, final CollidableSprite shot) {
    shot.updateWorld(delta);

    // See if shot left stage
    if (!colliderManager.checkCollidersForOuterIntersection(shot.getCollider(),
        background.getCollider())) {
      shots.remove(shot);
    }
  }
}
