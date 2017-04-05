package javagames.completegame.admin;

import javagames.sound.AudioStream;
import javagames.sound.SoundEvent;

/**
 * This class handles long running audio.
 * 
 * @author Timothy Wright
 *
 */
public class QuickLooper extends SoundEvent {

  public static final String STATE_CLOSED  = "closed";
  public static final String STATE_WAITING = "waiting";
  public static final String STATE_RUNNING = "running";
  public static final String EVENT_FIRE    = "fire";
  public static final String EVENT_DONE    = "done";
  public static final String EVENT_OPEN    = "open";
  public static final String EVENT_CLOSE   = "close";
  private String             currentState;

  public QuickLooper(final AudioStream audio) {
    super(audio);
    currentState = QuickLooper.STATE_CLOSED;
  }

  public void open() {
    put(QuickLooper.EVENT_OPEN);
  }

  public void close() {
    put(QuickLooper.EVENT_CLOSE);
  }

  public void fire() {
    put(QuickLooper.EVENT_FIRE);
  }

  public void done() {
    put(QuickLooper.EVENT_DONE);
  }

  @Override
  protected void processEvent(String event) throws InterruptedException {
    while ((queue.peek() == QuickLooper.EVENT_DONE)
        || (queue.peek() == QuickLooper.EVENT_FIRE)) {
      event = queue.take();
    }
    if (currentState == QuickLooper.STATE_CLOSED) {
      if (event == QuickLooper.EVENT_OPEN) {
        audio.open();
        currentState = QuickLooper.STATE_WAITING;
      }
    }
    else if (currentState == QuickLooper.STATE_WAITING) {
      if (event == QuickLooper.EVENT_CLOSE) {
        audio.close();
        currentState = QuickLooper.STATE_CLOSED;
      }
      if (event == QuickLooper.EVENT_FIRE) {
        audio.loop(AudioStream.LOOP_CONTINUOUSLY);
        currentState = QuickLooper.STATE_RUNNING;
      }
    }
    else if (currentState == QuickLooper.STATE_RUNNING) {
      if (event == QuickLooper.EVENT_CLOSE) {
        audio.stop();
        audio.close();
        currentState = QuickLooper.STATE_CLOSED;
      }
      if (event == QuickLooper.EVENT_DONE) {
        audio.stop();
        currentState = QuickLooper.STATE_WAITING;
      }
    }
  }
}