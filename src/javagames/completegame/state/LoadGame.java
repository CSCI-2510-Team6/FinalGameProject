package javagames.completegame.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import javagames.completegame.admin.QuickRestart;
import javagames.sound.BlockingClip;
import javagames.sound.BlockingDataLine;
import javagames.sound.LoopEvent;
import javagames.util.Matrix3x3f;
import javagames.util.ResourceLoader;
import javagames.util.Utility;
import javagames.util.Vector2f;
import sprite.CollidableSprite;
import sprite.EvilKnight;
import sprite.Grunt;
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
          "res/assets/images/backgrounds/Forest1.png",
          "/images/backgrounds/Forest1.png");
      final BufferedImage image = ImageIO.read(stream);

      // Initialize levelA background
      final List<BoundingShape> backInner = new ArrayList<>();

      // background inner bounding shapes
      final BoundingBox leftBound =
          new BoundingBox(new Vector2f(-49.5f, 0.5f), new Vector2f(-50, -3f));
      backInner.add(leftBound);

      final BoundingBox firstStep =
          new BoundingBox(new Vector2f(-18f, -1.4f), new Vector2f(-27.6f, -3f));
      backInner.add(firstStep);

      final BoundingBox secondStep = new BoundingBox(new Vector2f(-18f, 0.2f),
          new Vector2f(-22.7f, -1.4f));
      backInner.add(secondStep);

      final BoundingBox thirdStep =
          new BoundingBox(new Vector2f(-2f, -1.4f), new Vector2f(-10f, -3f));
      backInner.add(thirdStep);

      final BoundingBox leftBottom =
          new BoundingBox(new Vector2f(-2, -3f), new Vector2f(-50, -4.5f));
      backInner.add(leftBottom);

      final BoundingBox platform1 =
          new BoundingBox(new Vector2f(4.3f, 0.4f), new Vector2f(-0.5f, 0.2f));
      backInner.add(platform1);

      final BoundingBox platform2 =
          new BoundingBox(new Vector2f(7.3f, -1.8f), new Vector2f(4.0f, -2f));
      backInner.add(platform2);

      final BoundingBox platform3 = new BoundingBox(new Vector2f(12.5f, -0.6f),
          new Vector2f(9.3f, -0.8f));
      backInner.add(platform3);

      final BoundingBox platform4 =
          new BoundingBox(new Vector2f(17.6f, 1.5f), new Vector2f(14.4f, 1.3f));
      backInner.add(platform4);

      final BoundingBox fourthStep =
          new BoundingBox(new Vector2f(27.2f, 0.2f), new Vector2f(20.9f, 0.0f));
      backInner.add(fourthStep);

      final BoundingBox rightBottom =
          new BoundingBox(new Vector2f(50f, -3f), new Vector2f(32.1f, -4.5f));
      backInner.add(rightBottom);

      final BoundingBox rightBound =
          new BoundingBox(new Vector2f(50f, 3.5f), new Vector2f(49.7f, -3f));
      backInner.add(rightBound);

      // background collider object
      final Collider backgroundCollider =
          new Collider(new Vector2f(0, 0), 100.0f, 9.0f, backInner);
      // Create the background collidable sprite
      final CollidableSprite background =
          new CollidableSprite(0, 0, 8000, 720, image, backgroundCollider);

      controller.setAttribute("level1", background);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/backgrounds/Forest2.png",
          "/images/backgrounds/Forest2.png");
      final BufferedImage image = ImageIO.read(stream);

      // Initialize levelA background
      final List<BoundingShape> backInner = new ArrayList<>();

      // background inner bounding shapes
      final BoundingBox leftBound = new BoundingBox(new Vector2f(-49.15f, 1.9f),
          new Vector2f(-50.75f, -3f));
      backInner.add(leftBound);

      final BoundingBox leftBottom = new BoundingBox(new Vector2f(5.25f, -3f),
          new Vector2f(-50.75f, -4.5f));
      backInner.add(leftBottom);

      final BoundingBox firstStep = new BoundingBox(
          new Vector2f(-17.25f, -1.4f), new Vector2f(-31.6f, -3f));
      backInner.add(firstStep);

      final BoundingBox secondStep = new BoundingBox(
          new Vector2f(-17.25f, 0.2f), new Vector2f(-28.35f, -1.4f));
      backInner.add(secondStep);

      final BoundingBox platform1 =
          new BoundingBox(new Vector2f(-2.75f, 0.2f), new Vector2f(-14f, 0.0f));
      backInner.add(platform1);

      final BoundingBox middleBottom =
          new BoundingBox(new Vector2f(18f, -3f), new Vector2f(8.5f, -4.5f));
      backInner.add(middleBottom);

      final BoundingBox platform2 = new BoundingBox(new Vector2f(22.9f, -1.4f),
          new Vector2f(19.7f, -1.6f));
      backInner.add(platform2);

      final BoundingBox platform3 = new BoundingBox(new Vector2f(27.7f, -0.1f),
          new Vector2f(24.5f, -0.3f));
      backInner.add(platform3);

      final BoundingBox platform4 =
          new BoundingBox(new Vector2f(30.7f, -1.8f), new Vector2f(27.6f, -2f));
      backInner.add(platform4);

      final BoundingBox platform5 = new BoundingBox(new Vector2f(35.7f, -1.1f),
          new Vector2f(32.5f, -1.3f));
      backInner.add(platform5);

      final BoundingBox rightBottom = new BoundingBox(new Vector2f(50.75f, -3f),
          new Vector2f(35.7f, -4.5f));
      backInner.add(rightBottom);

      final BoundingBox rightBound = new BoundingBox(new Vector2f(50.75f, 3.5f),
          new Vector2f(50.05f, -3f));
      backInner.add(rightBound);

      // background collider object
      final Collider backgroundCollider =
          new Collider(new Vector2f(0, 0), 101.5f, 9.0f, backInner);
      // Create the background collidable sprite
      final CollidableSprite background =
          new CollidableSprite(0, 0, 8120, 720, image, backgroundCollider);

      controller.setAttribute("level2", background);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/backgrounds/CastleLevel.png",
          "/images/backgrounds/CastleLevel.png");
      final BufferedImage image = ImageIO.read(stream);

      // Initialize levelA background
      final List<BoundingShape> backInner = new ArrayList<>();

      // background inner bounding shapes
      final BoundingBox leftBound =
          new BoundingBox(new Vector2f(-31.3f, 2.5f), new Vector2f(-32, -3f));
      backInner.add(leftBound);

      final BoundingBox leftBottom =
          new BoundingBox(new Vector2f(-13, -3f), new Vector2f(-32, -4.5f));
      backInner.add(leftBottom);

      final BoundingBox stair1a = new BoundingBox(new Vector2f(-14.4f, -2.3f),
          new Vector2f(-14.6f, -2.4f));
      backInner.add(stair1a);
      final BoundingBox stair1b = new BoundingBox(new Vector2f(-14.5f, -2.4f),
          new Vector2f(-14.8f, -2.5f));
      backInner.add(stair1b);
      final BoundingBox stair1c = new BoundingBox(new Vector2f(-14.6f, -2.5f),
          new Vector2f(-15.0f, -2.6f));
      backInner.add(stair1c);
      final BoundingBox stair1d = new BoundingBox(new Vector2f(-14.7f, -2.6f),
          new Vector2f(-15.2f, -2.7f));
      backInner.add(stair1d);
      final BoundingBox stair1e = new BoundingBox(new Vector2f(-14.8f, -2.7f),
          new Vector2f(-15.4f, -2.8f));
      backInner.add(stair1e);
      final BoundingBox stair1f = new BoundingBox(new Vector2f(-14.9f, -2.8f),
          new Vector2f(-15.6f, -2.9f));
      backInner.add(stair1f);
      final BoundingBox stair1g =
          new BoundingBox(new Vector2f(-15f, -2.9f), new Vector2f(-15.8f, -3f));
      backInner.add(stair1g);

      final BoundingBox stair4a =
          new BoundingBox(new Vector2f(-13, -3f), new Vector2f(-32, -4.5f));
      backInner.add(stair4a);
      final BoundingBox stair4b =
          new BoundingBox(new Vector2f(-13, -3f), new Vector2f(-32, -4.5f));
      backInner.add(stair4b);
      final BoundingBox stair4c =
          new BoundingBox(new Vector2f(-13, -3f), new Vector2f(-32, -4.5f));
      backInner.add(stair4c);
      final BoundingBox stair4d =
          new BoundingBox(new Vector2f(-13, -3f), new Vector2f(-32, -4.5f));
      backInner.add(stair4d);
      final BoundingBox stair4e =
          new BoundingBox(new Vector2f(-13, -3f), new Vector2f(-32, -4.5f));
      backInner.add(stair4e);
      final BoundingBox stair4f =
          new BoundingBox(new Vector2f(-13, -3f), new Vector2f(-32, -4.5f));
      backInner.add(stair4f);
      final BoundingBox stair4g =
          new BoundingBox(new Vector2f(-13, -3f), new Vector2f(-32, -4.5f));
      backInner.add(stair4g);

      final BoundingBox step1 =
          new BoundingBox(new Vector2f(-13, -2.2f), new Vector2f(-14.4f, -3f));
      backInner.add(step1);

      final BoundingBox midBottom1 = new BoundingBox(new Vector2f(-6.1f, -3f),
          new Vector2f(-10.6f, -4.5f));
      backInner.add(midBottom1);

      final BoundingBox bridge1 =
          new BoundingBox(new Vector2f(-3.8f, -3f), new Vector2f(-6.1f, -3.2f));
      backInner.add(bridge1);

      final BoundingBox bridge2 = new BoundingBox(new Vector2f(-9.0f, 0.7f),
          new Vector2f(-10.65f, 0.5f));
      backInner.add(bridge2);

      final BoundingBox stair3a =
          new BoundingBox(new Vector2f(-8.8f, 0.6f), new Vector2f(-9.0f, 0.5f));
      backInner.add(stair3a);
      final BoundingBox stair3b =
          new BoundingBox(new Vector2f(-8.6f, 0.5f), new Vector2f(-8.8f, 0.4f));
      backInner.add(stair3b);
      final BoundingBox stair3c =
          new BoundingBox(new Vector2f(-8.4f, 0.4f), new Vector2f(-8.6f, 0.3f));
      backInner.add(stair3c);
      final BoundingBox stair3d =
          new BoundingBox(new Vector2f(-8.2f, 0.3f), new Vector2f(-8.4f, 0.2f));
      backInner.add(stair3d);
      final BoundingBox stair3e =
          new BoundingBox(new Vector2f(-8.0f, 0.2f), new Vector2f(-8.2f, 0.1f));
      backInner.add(stair3e);
      final BoundingBox stair3f =
          new BoundingBox(new Vector2f(-7.8f, 0.1f), new Vector2f(-8.0f, 0.0f));
      backInner.add(stair3f);

      final BoundingBox bridge3 =
          new BoundingBox(new Vector2f(-2.7f, 2.2f), new Vector2f(-6.0f, 2.0f));
      backInner.add(bridge3);

      final BoundingBox bridge4 =
          new BoundingBox(new Vector2f(2.6f, 2.2f), new Vector2f(-0.55f, 2.0f));
      backInner.add(bridge4);

      final BoundingBox bridge5 =
          new BoundingBox(new Vector2f(-5.1f, 0f), new Vector2f(-7.8f, -0.2f));
      backInner.add(bridge5);

      final BoundingBox stair2a = new BoundingBox(new Vector2f(-4.9f, -0.1f),
          new Vector2f(-5.1f, -0.2f));
      backInner.add(stair2a);
      final BoundingBox stair2b = new BoundingBox(new Vector2f(-4.7f, -0.2f),
          new Vector2f(-4.9f, -0.3f));
      backInner.add(stair2b);
      final BoundingBox stair2c = new BoundingBox(new Vector2f(-4.5f, -0.3f),
          new Vector2f(-4.7f, -0.4f));
      backInner.add(stair2c);
      final BoundingBox stair2d = new BoundingBox(new Vector2f(-4.3f, -0.4f),
          new Vector2f(-4.5f, -0.5f));
      backInner.add(stair2d);
      final BoundingBox stair2e = new BoundingBox(new Vector2f(-4.1f, -0.5f),
          new Vector2f(-4.3f, -0.6f));
      backInner.add(stair2e);
      final BoundingBox stair2f = new BoundingBox(new Vector2f(-3.9f, -0.6f),
          new Vector2f(-4.1f, -0.7f));
      backInner.add(stair2f);
      final BoundingBox stair2g = new BoundingBox(new Vector2f(-3.7f, -0.7f),
          new Vector2f(-3.9f, -0.8f));
      backInner.add(stair2g);

      final BoundingBox midBottom2 = new BoundingBox(
          new Vector2f(-0.55f, -2.2f), new Vector2f(-2.7f, -4.5f));
      backInner.add(midBottom2);

      final BoundingBox highBlock = new BoundingBox(new Vector2f(-0.55f, 2.2f),
          new Vector2f(-2.7f, 0.5f));
      backInner.add(highBlock);

      final BoundingBox rightBottom =
          new BoundingBox(new Vector2f(32, -3f), new Vector2f(3.85f, -4.5f));
      backInner.add(rightBottom);

      final BoundingBox step2 = new BoundingBox(new Vector2f(15.3f, -2.3f),
          new Vector2f(7.7f, -3.0f));
      backInner.add(step2);

      final BoundingBox step3 = new BoundingBox(new Vector2f(15.3f, -1.5f),
          new Vector2f(10.7f, -2.3f));
      backInner.add(step3);

      final BoundingBox rightBound =
          new BoundingBox(new Vector2f(32f, 1.5f), new Vector2f(31.3f, -3f));
      backInner.add(rightBound);

      // background collider object
      final Collider backgroundCollider =
          new Collider(new Vector2f(0, 0), 64.0f, 9.0f, backInner);
      // Create the background collidable sprite
      final CollidableSprite background =
          new CollidableSprite(0, 0, 5120, 720, image, backgroundCollider);

      controller.setAttribute("level3", background);
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
      final Vector2f heroMaxA = heroCenter.add(new Vector2f(0.3f, 0.8f));
      final Vector2f heroMinA = heroCenter.sub(new Vector2f(0.3f, 0.8f));
      heroInner.add(new BoundingBox(heroMaxA, heroMinA));

      // hero collider object
      final Collider heroCollider =
          new Collider(heroCenter, 0.6f, 1.6f, heroInner);
      // Create the hero sprite
      final Sorceress hero = new Sorceress(heroCenter.x, heroCenter.y, 864, 256,
          image, heroCollider);

      controller.setAttribute("hero", hero);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/characters/knightSheet.png",
          "/images/characters/knightSheet.png");
      final BufferedImage image = ImageIO.read(stream);

      // Initialize hero
      final Vector2f enemyCenter = new Vector2f(0, 0);

      // hero inner bounding shape
      final List<BoundingShape> enemyInner = new ArrayList<>();
      final Vector2f enemyMaxA = enemyCenter.add(new Vector2f(0.4f, 0.8f));
      final Vector2f enemyMinA = enemyCenter.sub(new Vector2f(0.4f, 0.8f));
      enemyInner.add(new BoundingBox(enemyMaxA, enemyMinA));

      // enemy collider object
      final Collider enemyCollider =
          new Collider(enemyCenter, 0.8f, 1.6f, enemyInner);
      // Create the enemy sprite
      final EvilKnight enemy = new EvilKnight(enemyCenter.x, enemyCenter.y, 680,
          440, image, enemyCollider);

      controller.setAttribute("evilKnight", enemy);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/characters/GruntSprites.png",
          "/images/characters/GruntSprites.png");
      final BufferedImage image = ImageIO.read(stream);

      // Initialize hero
      final Vector2f enemyCenter = new Vector2f(0, 0);

      // hero inner bounding shape
      final List<BoundingShape> enemyInner = new ArrayList<>();
      final Vector2f enemyMaxA = enemyCenter.add(new Vector2f(0.2f, 0.6f));
      final Vector2f enemyMinA = enemyCenter.sub(new Vector2f(0.2f, 0.6f));
      enemyInner.add(new BoundingBox(enemyMaxA, enemyMinA));

      // enemy collider object
      final Collider enemyCollider =
          new Collider(enemyCenter, 0.4f, 1.2f, enemyInner);
      // Create the enemy sprite
      final Grunt enemy = new Grunt(enemyCenter.x, enemyCenter.y, 6800, 1480,
          image, enemyCollider);

      controller.setAttribute("grunt", enemy);
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

    loadTasks.add(() -> {
      final InputStream stream = ResourceLoader.load(LoadGame.class,
          "res/assets/images/other/airSlash2.png",
          "/images/other/airSlash2.png");
      final BufferedImage image = ImageIO.read(stream);

      controller.setAttribute("airSlashImage", image);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final byte[] soundBytes = loadSound("battleThemeA.wav");
      // Java 7.0
      final LoopEvent loopEvent = new LoopEvent(new BlockingClip(soundBytes));
      // Java 6.0
      // LoopEvent loopEvent = new LoopEvent(
      // new BlockingDataLine(
      // soundBytes ) );
      loopEvent.initialize();
      controller.setAttribute("battleTheme", loopEvent);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final byte[] soundBytes = loadSound("swish.wav");
      final QuickRestart restartClip =
          new QuickRestart(new BlockingDataLine(soundBytes));
      restartClip.initialize();
      restartClip.open();
      controller.setAttribute("airSlashSound", restartClip);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final byte[] soundBytes = loadSound("Blip_Select8.wav");
      final QuickRestart restartClip =
          new QuickRestart(new BlockingDataLine(soundBytes));
      restartClip.initialize();
      restartClip.open();
      controller.setAttribute("shieldHit", restartClip);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final byte[] soundBytes = loadSound("Jump17.wav");
      final QuickRestart restartClip =
          new QuickRestart(new BlockingDataLine(soundBytes));
      restartClip.initialize();
      restartClip.open();
      controller.setAttribute("jumpSound", restartClip);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final byte[] soundBytes = loadSound("Pickup_Coin18.wav");
      final QuickRestart restartClip =
          new QuickRestart(new BlockingDataLine(soundBytes));
      restartClip.initialize();
      restartClip.open();
      controller.setAttribute("continue", restartClip);
      return Boolean.TRUE;
    });

    loadTasks.add(() -> {
      final byte[] soundBytes = loadSound("Randomize13.wav");
      final QuickRestart restartClip =
          new QuickRestart(new BlockingDataLine(soundBytes));
      restartClip.initialize();
      restartClip.open();
      controller.setAttribute("deathSound", restartClip);
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

  private byte[] loadSound(final String path) {
    final InputStream in = ResourceLoader.load(LoadGame.class,
        "res/assets/sound/" + path, "/sound/" + path);
    return readBytes(in);
  }

  private byte[] readBytes(final InputStream in) {
    try {
      final BufferedInputStream buf = new BufferedInputStream(in);
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      int read;
      while ((read = buf.read()) != -1) {
        out.write(read);
      }
      in.close();
      return out.toByteArray();
    }
    catch (final IOException ex) {
      ex.printStackTrace();
      return null;
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
      final LoopEvent loop = (LoopEvent) controller.getAttribute("battleTheme");
      loop.fire();
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
