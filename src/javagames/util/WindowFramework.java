package javagames.util;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * This class handles the attributes and behaviors of the game canvas.
 *
 * @author Timothy Wright
 *
 */
public class WindowFramework extends GameFramework {

  private Canvas canvas;

  @Override
  protected void createFramework() {
    canvas = new Canvas();
    canvas.setBackground(appBackground);
    canvas.setIgnoreRepaint(true);
    getContentPane().add(canvas);
    setLocationByPlatform(true);
    if (appMaintainRatio) {
      getContentPane().setBackground(appBorder);

      // Andres added the 40 pixels to the width and height to
      // account for the JFrame needing to be bigger than the canvas
      setSize(appWidth + 40, appHeight + 40);
      canvas.setSize(appWidth, appHeight); // bugfix Jan 2015
      setLayout(null);
      getContentPane().addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(final ComponentEvent e) {
          onComponentResized(e);
        }
      });
    }
    else {
      canvas.setSize(appWidth, appHeight);
      pack();
    }
    setTitle(appTitle);
    setupInput(canvas);
    setVisible(true);
    createBufferStrategy(canvas);
    canvas.requestFocus();
  }

  protected void onComponentResized(final ComponentEvent e) {
    final Dimension size = getContentPane().getSize();
    setupViewport(size.width, size.height);
    canvas.setLocation(vx, vy);
    canvas.setSize(vw, vh);
  }

  @Override
  public int getScreenWidth() {
    return canvas.getWidth();
  }

  @Override
  public int getScreenHeight() {
    return canvas.getHeight();
  }

  @Override
  protected void renderFrame(final Graphics g) {
    g.clearRect(0, 0, getScreenWidth(), getScreenHeight());
    render(g);
  }
}