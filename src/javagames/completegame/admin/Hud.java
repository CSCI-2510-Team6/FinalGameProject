package javagames.completegame.admin;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javagames.util.Matrix3x3f;
import sprite.Sprite;

/**
 * This class is in charge of drawing the virtual interface
 *
 * @author Andres
 *
 */
public class Hud {
  private final Sprite       hud;
  private final List<Sprite> hearts;

  // Constructor
  public Hud(final Sprite hud, final Sprite heart) {
    this.hud = hud;
    hearts = new ArrayList<>();
    for (int i = 0; i < 3; ++i) {
      hearts.add(new Sprite(-7.25f + i + 0.5f, 3.8f, 80, 80, heart.getImage()));
    }
  }

  public void update(final float delta) {
    hud.updateWorld(delta);
    for (final Sprite s : hearts) {
      s.updateWorld(delta);
    }
  }

  private void drawHearts(final Graphics2D g, final Matrix3x3f view,
      final int remaining) {
    for (int i = 0; i < remaining; ++i) {
      hearts.get(i).render(g, view);
    }
  }

  public void drawHud(final Graphics2D g, final Matrix3x3f view,
      final int remaining) {
    hud.render(g, view);
    drawHearts(g, view, remaining);
  }
}
