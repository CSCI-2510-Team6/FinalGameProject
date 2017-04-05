package javagames.util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This class sets up the game loop cycle and the necessary components to run
 * the game loop.
 * 
 * @author Timothy Wright
 *
 */
public abstract class GameFramework extends JFrame implements Runnable {

  private BufferStrategy       bs;
  private volatile boolean     running;
  private Thread               gameThread;
  protected int                vx;
  protected int                vy;
  protected int                vw;
  protected int                vh;
  protected FrameRate          frameRate;
  protected RelativeMouseInput mouse;
  protected KeyboardInput      keyboard;
  protected Color              appBackground    = Color.BLACK;
  protected Color              appBorder        = Color.LIGHT_GRAY;
  protected Color              appFPSColor      = Color.GREEN;
  protected Font               appFont          =
      new Font("Courier New", Font.PLAIN, 14);
  protected String             appTitle         = "TBD-Title";
  protected float              appBorderScale   = 0.8f;
  protected int                appWidth         = 640;
  protected int                appHeight        = 640;
  protected float              appWorldWidth    = 2.0f;
  protected float              appWorldHeight   = 2.0f;
  protected long               appSleep         = 10L;
  protected boolean            appMaintainRatio = false;
  protected boolean            appDisableCursor = false;
  protected int                textPos          = 0;

  public GameFramework() {
  }

  protected abstract void createFramework();

  protected abstract void renderFrame(Graphics g);

  public abstract int getScreenWidth();

  public abstract int getScreenHeight();

  protected void createAndShowGUI() {
    createFramework();
    if (appDisableCursor) {
      disableCursor();
    }
    gameThread = new Thread(this);
    gameThread.start();
  }

  protected void setupInput(final Component component) {
    keyboard = new KeyboardInput();
    component.addKeyListener(keyboard);
    mouse = new RelativeMouseInput(component);
    component.addMouseListener(mouse);
    component.addMouseMotionListener(mouse);
    component.addMouseWheelListener(mouse);
  }

  protected void createBufferStrategy(final Canvas component) {
    component.createBufferStrategy(2);
    bs = component.getBufferStrategy();
  }

  protected void createBufferStrategy(final Window window) {
    window.createBufferStrategy(2);
    bs = window.getBufferStrategy();
  }

  protected void setupViewport(final int sw, final int sh) {
    final int w = (int) (sw * appBorderScale);
    final int h = (int) (sh * appBorderScale);
    final int x = (sw - w) / 2;
    final int y = (sh - h) / 2;
    vw = w;
    vh = (int) ((w * appWorldHeight) / appWorldWidth);
    if (vh > h) {
      vw = (int) ((h * appWorldWidth) / appWorldHeight);
      vh = h;
    }
    vx = x + ((w - vw) / 2);
    vy = y + ((h - vh) / 2);
  }

  protected Matrix3x3f getViewportTransform() {
    return Utility.createViewport(appWorldWidth, appWorldHeight,
        getScreenWidth(), getScreenHeight());
  }

  protected Matrix3x3f getReverseViewportTransform() {
    return Utility.createReverseViewport(appWorldWidth, appWorldHeight,
        getScreenWidth(), getScreenHeight());
  }

  protected Vector2f getWorldMousePosition() {
    final Matrix3x3f screenToWorld = getReverseViewportTransform();
    final Point mousePos = mouse.getPosition();
    final Vector2f screenPos = new Vector2f(mousePos.x, mousePos.y);
    return screenToWorld.mul(screenPos);
  }

  protected Vector2f getRelativeWorldMousePosition() {
    final float sx = appWorldWidth / (getScreenWidth() - 1);
    final float sy = appWorldHeight / (getScreenHeight() - 1);
    final Matrix3x3f viewport = Matrix3x3f.scale(sx, -sy);
    final Point p = mouse.getPosition();
    return viewport.mul(new Vector2f(p.x, p.y));
  }

  @Override
  public void run() {
    running = true;
    initialize();
    long curTime = System.nanoTime();
    long lastTime = curTime;
    double nsPerFrame;
    while (running) {
      curTime = System.nanoTime();
      nsPerFrame = curTime - lastTime;
      gameLoop((float) (nsPerFrame / 1.0E9));
      lastTime = curTime;
    }
    terminate();
  }

  protected void initialize() {
    frameRate = new FrameRate();
    frameRate.initialize();
  }

  protected void terminate() {
  }

  private void gameLoop(final float delta) {
    processInput(delta);
    updateObjects(delta);
    renderFrame();
    sleep(appSleep);
  }

  private void renderFrame() {
    do {
      do {
        Graphics g = null;
        try {
          g = bs.getDrawGraphics();
          renderFrame(g);
        }
        finally {
          if (g != null) {
            g.dispose();
          }
        }
      } while (bs.contentsRestored());
      bs.show();
    } while (bs.contentsLost());
  }

  private void sleep(final long sleep) {
    try {
      Thread.sleep(sleep);
    }
    catch (final InterruptedException ex) {
    }
  }

  protected void processInput(final float delta) {
    keyboard.poll();
    mouse.poll();
  }

  protected void updateObjects(final float delta) {
  }

  protected void render(final Graphics g) {
    g.setFont(appFont);
    g.setColor(appFPSColor);
    frameRate.calculate();
    textPos = Utility.drawString(g, 20, 0, frameRate.getFrameRate());
  }

  private void disableCursor() {
    final Toolkit tk = Toolkit.getDefaultToolkit();
    final Image image = tk.createImage("");
    final Point point = new Point(0, 0);
    final String name = "CanBeAnything";
    final Cursor cursor = tk.createCustomCursor(image, point, name);
    setCursor(cursor);
  }

  protected void shutDown() {
    if (Thread.currentThread() != gameThread) {
      try {
        running = false;
        gameThread.join();
        onShutDown();
      }
      catch (final InterruptedException e) {
        e.printStackTrace();
      }
      System.exit(0);
    }
    else {
      SwingUtilities.invokeLater(() -> shutDown());
    }
  }

  protected void onShutDown() {
  }

  protected static void launchApp(final GameFramework app) {
    app.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        app.shutDown();
      }
    });
    SwingUtilities.invokeLater(() -> app.createAndShowGUI());
  }
}