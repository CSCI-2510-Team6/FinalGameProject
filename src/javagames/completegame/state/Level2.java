package javagames.completegame.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import collision.ColliderManager;
import javagames.completegame.admin.Hud;
import javagames.completegame.admin.QuickRestart;
import javagames.util.Camera;
import javagames.util.Dialogue;
import javagames.util.KeyboardInput;
import javagames.util.Matrix3x3f;
import javagames.util.Utility;
import javagames.util.Vector2f;
import sprite.CollidableSprite;
import sprite.EvilKnight;
import sprite.Grunt;
import sprite.Sorceress;
import sprite.Sprite;

public class Level2 extends State {

  private Camera                 camera;
  private ColliderManager        colliderManager;
  private CollidableSprite       background;
  private Sorceress              hero;
  private Grunt                  grunt;
  private EvilKnight             evilKnight;
  private KeyboardInput          keys;
  private boolean                drawBounds;
  private Hud                    hud;
  private Sprite                 hudDisplay;
  private Sprite                 heart;
  private final GameState        state;
  private List<CollidableSprite> shots;
  private QuickRestart           daggerSound;
  private QuickRestart           jumpSound;
  private QuickRestart           deathSound;
  private QuickRestart           shieldHit;
  private QuickRestart           continueSound;
  private float                  invincibilityTime;
  private boolean                dialogFlag;
  private int                    index;
  private String[]               text   = new String[4];
  protected Dialogue             dialog = new Dialogue();

  public Level2(final GameState state) {
    this.state = state;
    for (int x = 0; x < text.length; x++) {
      text[x] = "";
    }
  }

  @Override
  public void enter() {
    index = 0;
    dialogFlag = true;
    colliderManager = new ColliderManager();
    background = (CollidableSprite) controller.getAttribute("level2");
    hero = (Sorceress) controller.getAttribute("hero");
    hero.setAirSlashImage(
        (BufferedImage) controller.getAttribute("airSlashImage"));
    hero.setCenterPosition(new Vector2f(-45, 0));

    grunt = (Grunt) controller.getAttribute("grunt");
    grunt.setCenterPosition(new Vector2f(-40, 0));
    grunt.resetState();
    grunt.defaultAction();

    evilKnight = (EvilKnight) controller.getAttribute("evilKnight");
    evilKnight.setCenterPosition(new Vector2f(40, 0));
    evilKnight.resetState();
    evilKnight.defaultAction();

    daggerSound = (QuickRestart) controller.getAttribute("airSlashSound");
    jumpSound = (QuickRestart) controller.getAttribute("jumpSound");
    deathSound = (QuickRestart) controller.getAttribute("deathSound");
    shieldHit = (QuickRestart) controller.getAttribute("shieldHit");
    continueSound = (QuickRestart) controller.getAttribute("continue");
    camera = new Camera(hero.getCenterPosition());

    hudDisplay = (Sprite) controller.getAttribute("hudDisplay");
    heart = (Sprite) controller.getAttribute("heart");
    hud = new Hud(hudDisplay, heart);

    // The default is to not draw the collider bounds
    drawBounds = false;
    shots = new ArrayList<>();
    keys = (KeyboardInput) controller.getAttribute("keys");
    invincibilityTime = 0.0f;
  }

  @Override
  public void processInput(final float delta) {

    if (!dialogFlag) {
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
        jumpSound.fire();
      }
      // Handles variable jump based on length of time jump button is pressed
      if (hero.isJumpButtonHeld()) {
        hero.jump(delta);
      }
      if (!keys.keyDown(KeyEvent.VK_J)) {
        hero.releaseJumpButton();
      }
      if (keys.keyDownOnce(KeyEvent.VK_K) && hero.canAttack()) {
        shots.add(hero.melee());
        // Play air slash sound
        daggerSound.fire();
      }
      // Toggle collider rendering
      if (keys.keyDownOnce(KeyEvent.VK_B)) {
        drawBounds = !drawBounds;
      }
    }
    else {
      hero.stopMoving();
      if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
        if (index < text.length) {
          text[index] = dialog.LevelTwoDialogue();
          index++;
          continueSound.fire();
        }
        else {
          text = null;
          dialogFlag = false;
        }
      }
    }
  }

  @Override
  public void updateObjects(final float delta) {
    background.updateWorld(delta);

    hero.updateWorld(delta);

    int collisionDirection;

    grunt.updateWorld(delta, hero.getCenterPosition());
    evilKnight.updateWorld(delta, hero.getCenterPosition());

    // Handle hero interaction with the stage
    while ((collisionDirection = colliderManager.determineCollisionDirection(
        hero.getCollider(), background.getCollider())) != 0) {
      hero.uncollide(collisionDirection);
    }

    while ((collisionDirection = colliderManager.determineCollisionDirection(
        grunt.getCollider(), background.getCollider())) != 0) {
      grunt.uncollide(collisionDirection);
    }

    while ((collisionDirection = colliderManager.determineCollisionDirection(
        evilKnight.getCollider(), background.getCollider())) != 0) {
      evilKnight.uncollide(collisionDirection);
    }

    invincibilityTime += delta;
    // Handle interaction between the hero and enemy
    if (colliderManager.checkCollidersForInnerIntersection(hero.getCollider(),
        evilKnight.getCollider())) {
      hero.handleInjury();
      if (invincibilityTime > 1.0f) {
        state.setHearts(state.getHearts() - 1);
        invincibilityTime = 0.0f;
      }
    }

    if (colliderManager.checkCollidersForInnerIntersection(hero.getCollider(),
        grunt.getCollider())) {
      hero.handleInjury();
      if (invincibilityTime > 1.0f) {
        state.setHearts(state.getHearts() - 1);
        invincibilityTime = 0.0f;
      }
    }

    updateShots(delta);
    camera.update(new Vector2f(hero.getCenterPosition().x, 0));
    hud.update(delta);
    checkForLevelWon();
    checkForOutOfBounds();
    checkForEmptyHearts();
  }

  private void checkForLevelWon() {
    if (hero.getCenterPosition().x > 45) {
      state.setLevel(state.getLevel() + 1);
      getController().setState(new LevelStarting3(state));
    }
  }

  private void checkForOutOfBounds() {
    if (hero.getCenterPosition().y < -5.5f) {
      hero.setCenterPosition(new Vector2f(-45f, 0f));
      deathSound.fire();
    }
  }

  private void checkForEmptyHearts() {
    if (state.getHearts() < 1) {
      hero.setCenterPosition(new Vector2f(-45f, 0f));
      state.setHearts(3);
      deathSound.fire();
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {
    // begin of camera

    if (text != null) {
      g.setFont(new Font("Arial", Font.PLAIN, 20));
      g.setColor(Color.ORANGE);

      Utility.drawString(g, 10, 100, text);

    }
    else {
      final Vector2f cameraPosInScreenCoords = view.mul(camera.getPosition());
      g.translate(cameraPosInScreenCoords.x, cameraPosInScreenCoords.y);
      background.render(g, view, drawBounds);
      for (final CollidableSprite shot : shots) {
        shot.render(g, view, drawBounds);
      }

      grunt.render(g, view, drawBounds);
      evilKnight.render(g, view, drawBounds);

      hero.render(g, view, drawBounds);
      // end of camera
      g.translate(-cameraPosInScreenCoords.x, -cameraPosInScreenCoords.y);
      g.translate(0, 0);
      // update Hud display
      hud.drawHud(g, view, state.getHearts());
    }

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

    if (colliderManager.checkCollidersForInnerIntersection(shot.getCollider(),
        evilKnight.getCollider())) {
      if (evilKnight.handleInjury()) {
        shieldHit.fire();
      }
      shots.remove(shot);
    }

    if (colliderManager.checkCollidersForInnerIntersection(shot.getCollider(),
        grunt.getCollider())) {
      grunt.handleInjury();
      shots.remove(shot);
    }

    // See if shot left stage
    if (!colliderManager.checkCollidersForOuterIntersection(shot.getCollider(),
        background.getCollider())) {
      shots.remove(shot);
    }
  }
}
