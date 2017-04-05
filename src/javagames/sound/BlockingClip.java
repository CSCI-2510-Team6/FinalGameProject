package javagames.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class supports audio clips.
 *
 * @author Timothy Wright
 *
 */
public class BlockingClip extends AudioStream {

  private Clip    clip;
  private boolean restart;

  public BlockingClip(final byte[] soundData) {
    super(soundData);
  }

  /*
   * This guy could throw a bunch of exceptions. We're going to wrap them all in
   * a custom exception handler that is a RuntimeException so we don't have to
   * catch and throw all these exceptions.
   */
  @Override
  public void open() {
    lock.lock();
    try {
      final ByteArrayInputStream in = new ByteArrayInputStream(soundData);
      final AudioInputStream ais = AudioSystem.getAudioInputStream(in);
      clip = AudioSystem.getClip();
      clip.addLineListener(this);
      clip.open(ais);
      while (!open) {
        cond.await();
      }
      // UPDATE
      createControls(clip);
      // UPDATE
      System.out.println("open");
    }
    catch (final UnsupportedAudioFileException ex) {
      throw new SoundException(ex.getMessage(), ex);
    }
    catch (final LineUnavailableException ex) {
      throw new SoundException(ex.getMessage(), ex);
    }
    catch (final IOException ex) {
      throw new SoundException(ex.getMessage(), ex);
    }
    catch (final InterruptedException ex) {
      ex.printStackTrace();
    }
    finally {
      lock.unlock();
    }
  }

  @Override
  public void start() {
    lock.lock();
    try {
      clip.flush();
      clip.setFramePosition(0);
      clip.start();
      while (!started) {
        cond.await();
      }
      System.out.println("It's Started");
    }
    catch (final InterruptedException e) {
      e.printStackTrace();
    }
    finally {
      lock.unlock();
    }
  }

  @Override
  public void loop(final int count) {
    lock.lock();
    try {
      clip.flush();
      clip.setFramePosition(0);
      clip.loop(count);
      while (!started) {
        cond.await();
      }
      System.out.println("It's Started");
    }
    catch (final InterruptedException e) {
      e.printStackTrace();
    }
    finally {
      lock.unlock();
    }
  }

  @Override
  public void restart() {
    restart = true;
    stop();
    restart = false;
    start();
  }

  @Override
  protected void fireTaskFinished() {
    if (!restart) {
      super.fireTaskFinished();
    }
  }

  @Override
  public void stop() {
    lock.lock();
    try {
      clip.stop();
      while (started) {
        cond.await();
      }
    }
    catch (final InterruptedException ex) {
      ex.printStackTrace();
    }
    finally {
      lock.unlock();
    }
  }

  @Override
  public void close() {
    lock.lock();
    try {
      clip.close();
      while (open) {
        cond.await();
      }
      clip = null;
      // UPDATE
      clearControls();
      // UPDATE
      System.out.println("Turned off");
    }
    catch (final InterruptedException ex) {
      ex.printStackTrace();
    }
    finally {
      lock.unlock();
    }
  }
}