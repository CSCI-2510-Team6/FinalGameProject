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
 * This class represents the credit screen for the game.
 *
 * @author Timothy Wright
 * @author Andres Ward
 * @author Kristopher
 *
 */
public class CreditScreen extends State {

  private KeyboardInput keys;
  private String[]      textB  = new String[8];
  protected Dialogue    dialog = new Dialogue();
  private int           index;
  private QuickRestart  continueSound;

  // Constructor
  public CreditScreen() {
    for (int x = 0; x < textB.length; x++) {
      textB[x] = "";
    }
  }

  // Text for the credits
  private static final String[] creditInfo =
      { "Wonder Keith and No Brothers - version 1.0", "",
          "Programmed by: Kristopher Davlin, Paul Growney, Tyler Veseth, Andres Ward",
          "", "Special thanks to:", "", "Timothy Wright", "cynicmusic",
          "artisticdude", "Damaged Panda", "Buch", "skoam", "trashmney034",
          "Matheus Carvalho" };

  @Override
  public void enter() {
    super.enter();
    keys = (KeyboardInput) controller.getAttribute("keys");
    continueSound = (QuickRestart) controller.getAttribute("continue");
  }

  @Override
  public void processInput(final float delta) {
    if (textB == null) {
      super.processInput(delta);

      // Return to title screen
      if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
        getController().setState(new TitleScreen());
      }
    }
    else {
      if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
        if (index < textB.length) {
          textB[index] = dialog.LevelThreeBDialogue();
          index++;
          continueSound.fire();
        }
        else {
          textB = null;
          index = 0;
        }
      }
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {

    if (textB != null) {
      g.setFont(new Font("Arial", Font.PLAIN, 20));
      g.setColor(Color.ORANGE);

      Utility.drawString(g, 10, 100, textB);

    }
    else {
      super.render(g, view);

      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      g.setFont(new Font("Arial", Font.PLAIN, 20));
      g.setColor(Color.GREEN);

      Utility.drawCenteredString(g, app.getScreenWidth(),
          app.getScreenHeight() / 3, CreditScreen.creditInfo);
    }
  }
}
