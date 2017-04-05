package javagames.completegame.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javagames.util.Matrix3x3f;
import javagames.util.Utility;

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
