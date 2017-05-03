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
import sprite.Sorceress;
import sprite.Sprite;

public class Level3 extends State {

  private Camera                 camera;
  private ColliderManager        colliderManager;
  private CollidableSprite       background;
  private Sorceress              hero;
  private KeyboardInput          keys;
  private boolean                drawBounds, levelOver;
  private Hud                    hud;
  private Sprite                 hudDisplay;
  private Sprite                 heart;
  private final GameState        state;
  private List<CollidableSprite> shots;
  private QuickRestart           daggerSound;
  private boolean				 dialogFlag;
  private int					 index;
  private String[]        		 textA   = new String[4];
  protected Dialogue      dialog = new Dialogue();

  public Level3(final GameState state) {
	  
    this.state = state;
    for (int x = 0; x < textA.length; x++) {
        textA[x] = "";
      }

	dialogFlag = levelOver = false;
  }

  @Override
  public void enter() {
	index = 0;
    colliderManager = new ColliderManager();
    background = (CollidableSprite) controller.getAttribute("level3");
    hero = (Sorceress) controller.getAttribute("hero");
    hero.setAirSlashImage(
        (BufferedImage) controller.getAttribute("airSlashImage"));
    hero.setCenterPosition(new Vector2f(-25, 0));

    daggerSound = (QuickRestart) controller.getAttribute("airSlashSound");
    camera = new Camera(hero.getCenterPosition());

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
	  if(textA == null)
		{
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
	 
		else
		{
			hero.stopMoving();
		 if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
		      if (index < textA.length) {
		        textA[index] = dialog.LevelThreeADialogue();
		        index++;
		      }
		      else {
		        textA = null;
		        index = 0;
		        dialog = new Dialogue();
		      }
		    }
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

    updateShots(delta);
    camera.update(new Vector2f(hero.getCenterPosition().x, 0));
    hud.update(delta);
    checkForLevelWon();
    checkForOutOfBounds();
  }

  private void checkForLevelWon() {
    if (hero.getCenterPosition().x > 25) {  
    	state.setLevel(state.getLevel() + 1);
        getController().setState(new CreditScreen());
    }
  }

  private void checkForOutOfBounds() {
    if (hero.getCenterPosition().y < -5.5f) {
      hero.setCenterPosition(new Vector2f(-25f, 0f));
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {
    // begin of camera
	  
	if (textA != null) 
    {
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.setColor(Color.ORANGE);

      Utility.drawString(g, 10, 100, textA);

    }
	else
	{
	    final Vector2f cameraPosInScreenCoords = view.mul(camera.getPosition());
	    g.translate(cameraPosInScreenCoords.x, cameraPosInScreenCoords.y);
	    background.render(g, view, drawBounds);
	    for (final CollidableSprite shot : shots) {
	      shot.render(g, view, drawBounds);
	    }
	
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

    // See if shot left stage
    if (!colliderManager.checkCollidersForOuterIntersection(shot.getCollider(),
        background.getCollider())) {
      shots.remove(shot);
    }
  }
}
