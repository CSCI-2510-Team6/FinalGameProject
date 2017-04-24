package javagames.completegame.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import javagames.util.KeyboardInput;
import javagames.util.Matrix3x3f;
import javagames.util.Utility;

/**
 * This class represents the title screen of the game
 *
 * @author Timothy Wright
 * @author Andres Ward
 *
 */
public class TitleScreen extends State {

  protected KeyboardInput keys;

  @Override
  public void enter() {
    super.enter();
    keys = (KeyboardInput) controller.getAttribute("keys");

    // Replace background parameter with actual background to be used
    // background = (Sprite) controller.getAttribute("background");
  }

  @Override
  public void processInput(final float delta) {
    super.processInput(delta);

    if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
      final GameState state = new GameState();
      state.setLevel(1);
      state.setHearts(3);
      getController().setState(new LevelStarting(state));
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {
    super.render(g, view);

    final int width = app.getScreenWidth();
    final int height = app.getScreenHeight();

    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g.setFont(new Font("Arial", Font.PLAIN, 20));
    g.setColor(Color.ORANGE);

    final String[] msg = { "GAME TITLE", "", "", "", "PRESS SPACE TO PLAY", "",
        "PRESS ESC TO EXIT" };

    Utility.drawCenteredString(g, width, height / 3, msg);
  }
}
