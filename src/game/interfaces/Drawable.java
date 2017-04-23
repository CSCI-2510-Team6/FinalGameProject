package game.interfaces;

import java.awt.Graphics;

import javagames.util.Matrix3x3f;

/**
 * This interface provides shell methods for drawing an object
 *
 * @author Patrick Cavanaugh
 *
 */
public interface Drawable {

  // Increment object positions
  void updateWorld(float delta);

  // Draw the object with passed Graphics and viewport matrix
  void render(Graphics g, Matrix3x3f view);
}
