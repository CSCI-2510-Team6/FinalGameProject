package javagames.completegame;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javagames.completegame.admin.GameConstants;
import javagames.completegame.admin.QuickRestart;
import javagames.completegame.state.LoadGame;
import javagames.completegame.state.StateController;
import javagames.sound.LoopEvent;
import javagames.util.GameFramework;
import javagames.util.WindowFramework;

/**
 * This class is the entry point to the game. It extends WindowFramework to
 * handle the non full screen display.
 *
 * @author Timothy Wright
 * @author Andres Ward
 *
 */
public class CompleteGame extends WindowFramework {

  // Provides access to loaded resources and the current game state
  private StateController controller;

  // Constructor
  public CompleteGame() {
    appBorder = GameConstants.APP_BORDER;
    appWidth = GameConstants.APP_WIDTH;
    appHeight = GameConstants.APP_HEIGHT;
    appSleep = GameConstants.APP_SLEEP;
    appTitle = GameConstants.APP_TITLE;
    appWorldWidth = GameConstants.WORLD_WIDTH;
    appWorldHeight = GameConstants.WORLD_HEIGHT;
    appBorderScale = GameConstants.BORDER_SCALE;
    appDisableCursor = GameConstants.DISABLE_CURSOR;
    appMaintainRatio = GameConstants.MAINTAIN_RATIO;
  }

  @Override
  protected void initialize() {
    super.initialize();
    controller = new StateController();

    // Load initial resources
    controller.setAttribute("app", this);
    controller.setAttribute("keys", keyboard);
    controller.setAttribute("viewport", getViewportTransform());

    // Next state will be the load screen
    controller.setState(new LoadGame());
  }

  // Gracefully close all threads
  public void shutDownGame() {
    shutDown();
  }

  @Override
  protected void processInput(final float delta) {
    super.processInput(delta);
    controller.processInput(delta);
  }

  @Override
  protected void updateObjects(final float delta) {
    controller.updateObjects(delta);
  }

  @Override
  protected void render(final Graphics g) {
    // Render current game state
    controller.render((Graphics2D) g, getViewportTransform());

    // Render the frames per second
    super.render(g);
  }

  @Override
  protected void terminate() {
    super.terminate();

    // Kill all the music here
    final LoopEvent loop = (LoopEvent) controller.getAttribute("battleTheme");
    if (loop != null) {
      System.out.println("Sound: battleTheme");
      loop.done();
      loop.shutDown();
      System.out.println("Done: battleTheme");
    }

    final QuickRestart event =
        (QuickRestart) controller.getAttribute("airSlashSound");
    if (event != null) {
      System.out.println("Sound: airSlashSound");
      event.close();
      event.shutDown();
      System.out.println("Done: airSlashSound");
    }
  }

  public static void main(final String[] args) {
    GameFramework.launchApp(new CompleteGame());
  }
}