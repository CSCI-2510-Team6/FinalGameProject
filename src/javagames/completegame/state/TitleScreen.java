package javagames.completegame.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import javagames.completegame.admin.QuickRestart;
import javagames.util.Dialogue;
import javagames.util.KeyboardInput;
import javagames.util.Matrix3x3f;
import javagames.util.Utility;

/**
 * This class represents the title screen of the game
 *
 * @author Timothy Wright
 * @author Andres Ward
 * @author Kristopher
 *
 */
public class TitleScreen extends State {

  private QuickRestart    continueSound;
  protected KeyboardInput keys;
  protected Dialogue      dialog = new Dialogue();
  int                     index  = 0;
  private String[]        text   = new String[24];
  private String[]        msg    = { "WONDER KEITH AND NO BROTHERS", "", "", "",
      "PRESS SPACE TO PLAY", "", "PRESS ESC TO EXIT" };

  // Constructor
  public TitleScreen() {
    for (int x = 0; x < text.length; x++) {
      text[x] = "";
    }
  }

  @Override
  public void enter() {
    super.enter();
    keys = (KeyboardInput) controller.getAttribute("keys");

    continueSound = (QuickRestart) controller.getAttribute("continue");
  }

  @Override
  public void processInput(final float delta) {
    super.processInput(delta);

    if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
      if (index < text.length) {
        msg = null;
        text[index] = dialog.LevelOneDialogue();
        index++;
        continueSound.fire();
      }
      else {
        text = null;
      }
    }
    if (keys.keyDownOnce(KeyEvent.VK_SPACE) && (text == null)) {
      final GameState state = new GameState();
      state.setLevel(1);
      state.setHearts(3);
      getController().setState(new LevelStarting1(state));
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

    if (msg != null) {
      Utility.drawCenteredString(g, width, height / 3, msg);
    }
    if (text[0] != null) {
      Utility.drawString(g, 10, 10, text);
    }
  }
}
