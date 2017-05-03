package javagames.completegame.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javagames.util.Matrix3x3f;
import javagames.util.Utility;
import sprite.CollidableSprite;

/**
 * This class displays a preview of the upcoming level and its stage number
 *
 * @author Andres
 *
 */
public class LevelStarting3 extends State {

  private CollidableSprite background;
  private final GameState  state;
  private double           time;

  // Constructor
  public LevelStarting3(final GameState state) {
    this.state = state;
  }

  @Override
  public void enter() {
    background = (CollidableSprite) controller.getAttribute("level3");
    state.setHearts(3);
    time = 0.0;
  }

  @Override
  public void updateObjects(final float delta) {
    time += delta;
    if (time > 2.0) {
      getController().setState(new Level3(state));
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {
    super.render(g, view);
    background.render(g, view);

    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setFont(new Font("Arial", Font.PLAIN, 20));
    g.setColor(Color.RED);
    Utility.drawCenteredString(g, app.getScreenWidth(),
        app.getScreenHeight() / 4, "L E V E L " + state.getLevel());
  }
}
