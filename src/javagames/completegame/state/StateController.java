package javagames.completegame.state;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javagames.util.Matrix3x3f;

/**
 * This class creates a centralized location for handling game resources and
 * state.
 *
 * @author Timothy Wright
 *
 */
public class StateController {

  private final Map<String, Object> attributes;
  private State                     currentState;

  // Constructor
  public StateController() {
    attributes = Collections.synchronizedMap(new HashMap<String, Object>());
  }

  /**
   * This method records the current state of the game
   *
   * @param newState
   */
  public void setState(final State newState) {
    if (currentState != null) {
      currentState.exit();
    }
    if (newState != null) {
      newState.setController(this);
      newState.enter();
    }
    currentState = newState;
  }

  /**
   * Directs current state to handle input
   *
   * @param delta
   */
  public void processInput(final float delta) {
    currentState.processInput(delta);
  }

  /**
   * Directs current state to update objects
   *
   * @param delta
   */
  public void updateObjects(final float delta) {
    currentState.updateObjects(delta);
  }

  /**
   * Directs current state to render objects
   * 
   * @param g
   * @param view
   */
  public void render(final Graphics2D g, final Matrix3x3f view) {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    currentState.render(g, view);
  }

  public Object getAttribute(final String name) {
    return attributes.get(name);
  }

  public Object removeAttribute(final String name) {
    return attributes.remove(name);
  }

  public void setAttribute(final String name, final Object attribute) {
    attributes.put(name, attribute);
  }

  public Set<String> getAttributeNames() {
    return attributes.keySet();
  }
}