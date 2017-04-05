package javagames.sound;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class creates a thread for sound events.
 *
 * @author Timothy Wright
 *
 */
public class SoundEvent implements Runnable {

  public static final String      SHUT_DOWN = "shutdown";
  protected AudioStream           audio;
  protected BlockingQueue<String> queue;
  private Thread                  consumer;

  public SoundEvent(final AudioStream audio) {
    this.audio = audio;
  }

  public void initialize() {
    audio.addListener(getListener());
    queue = new LinkedBlockingQueue<>();
    consumer = new Thread(this);
    consumer.start();
  }

  public void put(final String event) {
    try {
      queue.put(event);
    }
    catch (final InterruptedException e) {
    }
  }

  public void shutDown() {
    final Thread temp = consumer;
    consumer = null;
    try {
      // send event to wake up consumer
      // and/or stop.
      queue.put(SoundEvent.SHUT_DOWN);
      temp.join(10000L);
      System.out.println("Event shutdown!!!");
    }
    catch (final InterruptedException ex) {
    }
  }

  @Override
  public void run() {
    while (Thread.currentThread() == consumer) {
      try {
        processEvent(queue.take());
      }
      catch (final InterruptedException e) {
      }
    }
  }

  protected void processEvent(final String event) throws InterruptedException {
  }

  protected void onAudioFinished() {
  }

  private BlockingAudioListener getListener() {
    return () -> onAudioFinished();
  }
}