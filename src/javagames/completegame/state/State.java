package javagames.completegame.state;

import java.awt.Graphics2D;

import javagames.completegame.CompleteGame;
import javagames.util.Matrix3x3f;

/**
 * This class represents a game state.
 *
 * @author Timothy Wright
 *
 */
public class State {

  // Reference to the state controller
  protected StateController controller;

  // Reference to the entry point
  protected CompleteGame app;

  /**
   * This method sets the references for the state controller and app
   *
   * @param controller
   */
  public void setController(final StateController controller) {
    this.controller = controller;
    app = (CompleteGame) controller.getAttribute("app");
  }

  protected StateController getController() {
    return controller;
  }

  /**
   * Perform the initializations of the state
   */
  public void enter() {

  }

  /**
   * Handle input at the state level
   *
   * @param delta
   */
  public void processInput(final float delta) {
  }

  /**
   * Update game objects at the state level
   *
   * @param delta
   */
  public void updateObjects(final float delta) {
  }

  /**
   * Render game objects at the state level
   *
   * @param g
   * @param view
   */
  public void render(final Graphics2D g, final Matrix3x3f view) {
  }

  /**
   * Perform any necessary actions on state exit
   */
  public void exit() {

  }
}