package javagames.completegame.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import collision.Collider;
import collision.shapes.BoundingBox;
import collision.shapes.BoundingShape;
import javagames.util.Matrix3x3f;
import javagames.util.ResourceLoader;
import javagames.util.Utility;
import javagames.util.Vector2f;
import sprite.CollidableSprite;
import sprite.Sorceress;
import sprite.Sprite;

/**
 * This class handles the initial loading of game assets into memory.
 *
 * @author Timothy Wright
 * @author Andres Ward
 *
 */
public class LoadGame extends State {

  private ExecutorService         threadPool;
  private List<Callable<Boolean>> loadTasks;
  private List<Future<Boolean>>   loadResults;
  private int                     numberOfTasks;
  private float                   percent;
  private float                   wait;

  @Override
  public void enter() {
    threadPool = Executors.newCachedThreadPool();
    loadTasks = new ArrayList<>();

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/backgrounds/iceCastle.jpg",
          "/images/backgrounds/iceCastle.jpg");
      final BufferedImage image = ImageIO.read(stream);

      // Initialize levelA background
      final List<BoundingShape> backInner = new ArrayList<>();

      // background inner bounding shapes
      final BoundingBox bottom =
          new BoundingBox(new Vector2f(16, -4f), new Vector2f(-16, -4.5f));
      backInner.add(bottom);

      // background collider object
      final Collider backgroundCollider =
          new Collider(new Vector2f(0, 0), 32.0f, 9.0f, backInner);
      // Create the background collidable sprite
      final CollidableSprite background =
          new CollidableSprite(0, 0, 1280 * 2, 720, image, backgroundCollider);

      controller.setAttribute("levelA", background);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/characters/fumiko.png",
          "/images/characters/fumiko.png");
      final BufferedImage image = ImageIO.read(stream);

      // Initialize hero
      final Vector2f heroCenter = new Vector2f(-6, 0);

      // hero inner bounding shape
      final List<BoundingShape> heroInner = new ArrayList<>();
      final Vector2f heroMaxA = heroCenter.add(new Vector2f(0.4f, 0.8f));
      final Vector2f heroMinA = heroCenter.sub(new Vector2f(0.4f, 0.8f));
      heroInner.add(new BoundingBox(heroMaxA, heroMinA));

      // hero collider object
      final Collider heroCollider =
          new Collider(heroCenter, 0.8f, 1.6f, heroInner);
      // Create the hero sprite
      final Sorceress hero = new Sorceress(heroCenter.x, heroCenter.y, 864, 256,
          image, heroCollider);

      controller.setAttribute("hero", hero);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/other/scrollHud.png",
          "/images/other/scrollHud.png");
      final BufferedImage image = ImageIO.read(stream);

      // Create the hud sprite
      final Sprite hudDisplay = new Sprite(0, 3.8f, 1280, 120, image);

      controller.setAttribute("hudDisplay", hudDisplay);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/other/heart.png", "/images/other/heart.png");
      final BufferedImage image = ImageIO.read(stream);

      // Create the heart sprite
      final Sprite heart = new Sprite(0, 0, 80, 80, image);

      controller.setAttribute("heart", heart);
      return Boolean.TRUE;
    });

    loadResults = new ArrayList<>();
    for (final Callable<Boolean> task : loadTasks) {
      loadResults.add(threadPool.submit(task));
    }

    numberOfTasks = loadResults.size();
    if (numberOfTasks == 0) {
      numberOfTasks = 1;
    }
  }

  @Override
  public void updateObjects(final float delta) {
    // remove finished tasks
    final Iterator<Future<Boolean>> it = loadResults.iterator();

    while (it.hasNext()) {
      final Future<Boolean> next = it.next();
      if (next.isDone()) {
        try {
          if (next.get()) {
            it.remove();
          }
        }
        catch (final Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    // update progress bar
    percent = (numberOfTasks - loadResults.size()) / (float) numberOfTasks;

    if (percent >= 1.0f) {
      threadPool.shutdown();
      wait += delta;
    }

    if ((wait > 1.0f) && threadPool.isShutdown()) {
      // Transition state to the title screen
      getController().setState(new TitleScreen());
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {
    super.render(g, view);

    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g.setFont(new Font("Arial", Font.PLAIN, 20));
    g.setColor(Color.YELLOW);

    final String message = "LOADING";

    Utility.drawCenteredString(g, app.getScreenWidth(),
        app.getScreenHeight() / 3, message);

    final int vw = (int) (app.getScreenWidth() * .9f);
    final int vh = (int) (app.getScreenWidth() * .05f);
    final int vx = (app.getScreenWidth() - vw) / 2;
    final int vy = (app.getScreenWidth() - vh) / 2;

    // fill in progress
    g.setColor(Color.YELLOW);
    final int width = (int) (vw * percent);
    g.fillRect(vx, vy, width, vh);

    // draw border
    g.setColor(Color.BLUE);
    g.drawRect(vx, vy, vw, vh);
  }

}
