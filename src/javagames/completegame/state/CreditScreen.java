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
 * This class represents the credit screen for the game.
 *
 * @author Timothy Wright
 * @author Andres Ward
 *
 */
public class CreditScreen extends State {

  private KeyboardInput keys;

  // Text for the credits
  private static final String[] creditInfo =
      { "Wonder Keith and No Brothers - version 1.0",
          "Programmed by: Kristopher Davlin, Paul Growney, Tyler Veseth, Andres Ward",
          "Special thanks to:", "Timothy Wright", };

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
      getController().setState(new TitleScreen());
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {
    super.render(g, view);

    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g.setFont(new Font("Arial", Font.PLAIN, 20));
    g.setColor(Color.GREEN);

    Utility.drawCenteredString(g, app.getScreenWidth(),
        app.getScreenHeight() / 3, CreditScreen.creditInfo);
  }
}
