package javagames.sound;

/**
 * This class supports looping audio.
 *
 * @author Timothy Wright
 *
 */
public class LoopEvent extends SoundEvent {

  public static final String STATE_WAITING = "waiting";
  public static final String STATE_RUNNING = "running";
  public static final String EVENT_FIRE    = "fire";
  public static final String EVENT_DONE    = "done";
  private String             currentState;

  public LoopEvent(final AudioStream audio) {
    super(audio);
    currentState = LoopEvent.STATE_WAITING;
  }

  public void fire() {
    put(LoopEvent.EVENT_FIRE);
  }

  public void done() {
    put(LoopEvent.EVENT_DONE);
  }

  @Override
  protected void processEvent(final String event) throws InterruptedException {
    System.out.println("Got " + event + " event");
    if (currentState == LoopEvent.STATE_WAITING) {
      if (event == LoopEvent.EVENT_FIRE) {
        audio.open();
        audio.loop(AudioStream.LOOP_CONTINUOUSLY);
        currentState = LoopEvent.STATE_RUNNING;
      }
    }
    else if (currentState == LoopEvent.STATE_RUNNING) {
      if (event == LoopEvent.EVENT_DONE) {
        audio.stop();
        audio.close();
        currentState = LoopEvent.STATE_WAITING;
      }
    }
  }
}