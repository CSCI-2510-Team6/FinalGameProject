package javagames.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class handles the attributes and behaviors for keyboard input.
 *
 * @author Timothy Wright
 *
 */
public class KeyboardInput implements KeyListener {

  private final boolean[] keys;
  private final int[]     polled;

  public KeyboardInput() {
    keys = new boolean[256];
    polled = new int[256];
  }

  public boolean keyDown(final int keyCode) {
    return polled[keyCode] > 0;
  }

  public boolean keyDownOnce(final int keyCode) {
    return polled[keyCode] == 1;
  }

  public synchronized void poll() {
    for (int i = 0; i < keys.length; ++i) {
      if (keys[i]) {
        polled[i]++;
      }
      else {
        polled[i] = 0;
      }
    }
  }

  @Override
  public synchronized void keyPressed(final KeyEvent e) {
    final int keyCode = e.getKeyCode();
    if ((keyCode >= 0) && (keyCode < keys.length)) {
      keys[keyCode] = true;
    }
  }

  @Override
  public synchronized void keyReleased(final KeyEvent e) {
    final int keyCode = e.getKeyCode();
    if ((keyCode >= 0) && (keyCode < keys.length)) {
      keys[keyCode] = false;
    }
  }

  @Override
  public void keyTyped(final KeyEvent e) {
    // Not needed
  }
}