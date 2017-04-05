package javagames.completegame.admin;

import javagames.sound.AudioStream;
import javagames.sound.SoundEvent;

/**
 * This class handles short audio clips.
 * 
 * @author Timothy Wright
 *
 */
public class QuickRestart extends SoundEvent {

  public static final String STATE_CLOSED  = "closed";
  public static final String STATE_WAITING = "waiting";
  public static final String STATE_RUNNING = "running";
  public static final String EVENT_FIRE    = "fire";
  public static final String EVENT_DONE    = "done";
  public static final String EVENT_OPEN    = "open";
  public static final String EVENT_CLOSE   = "close";
  private String             currentState;

  public QuickRestart(final AudioStream stream) {
    super(stream);
    currentState = QuickRestart.STATE_CLOSED;
  }

  public void open() {
    put(QuickRestart.EVENT_OPEN);
  }

  public void close() {
    put(QuickRestart.EVENT_CLOSE);
  }

  public void fire() {
    put(QuickRestart.EVENT_FIRE);
  }

  @Override
  protected void processEvent(final String event) throws InterruptedException {
    System.out.println("Quick Restart Got: " + event);
    System.out.println("Current State: " + currentState);
    if (currentState == QuickRestart.STATE_CLOSED) {
      if (event == QuickRestart.EVENT_OPEN) {
        audio.open();
        currentState = QuickRestart.STATE_WAITING;
      }
    }
    else if (currentState == QuickRestart.STATE_WAITING) {
      if (event == QuickRestart.EVENT_CLOSE) {
        audio.close();
        currentState = QuickRestart.STATE_CLOSED;
      }
      if (event == QuickRestart.EVENT_FIRE) {
        audio.start();
        currentState = QuickRestart.STATE_RUNNING;
      }
    }
    else if (currentState == QuickRestart.STATE_RUNNING) {
      if (event == QuickRestart.EVENT_FIRE) {
        audio.restart();
      }
      if (event == QuickRestart.EVENT_CLOSE) {
        audio.stop();
        audio.close();
        currentState = QuickRestart.STATE_CLOSED;
      }
      if (event == QuickRestart.EVENT_DONE) {
        currentState = QuickRestart.STATE_WAITING;
      }
    }
    System.out.println("New State: " + currentState);
  }

  @Override
  protected void onAudioFinished() {
    put(QuickRestart.EVENT_DONE);
  }
}