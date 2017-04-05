package javagames.completegame.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javagames.util.KeyboardInput;
import javagames.util.Matrix3x3f;
import javagames.util.Sprite;

/**
 * This abstract class represents the attributes and behaviors that all non play
 * screens should have.
 * 
 * @author Andres Ward
 *
 */
public abstract class NonStageScreen extends State {

  private Sprite          background;
  protected KeyboardInput keys;

  @Override
  public void enter() {
    keys = (KeyboardInput) controller.getAttribute("keys");
    background = (Sprite) controller.getAttribute("background");
  }

  @Override
  public void updateObjects(final float delta) {
  }

  @Override
  public void processInput(final float delta) {
    if (keys.keyDownOnce(KeyEvent.VK_ESCAPE)) {
      app.shutDownGame();
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {
    // background.render(g, view);
  }
}
