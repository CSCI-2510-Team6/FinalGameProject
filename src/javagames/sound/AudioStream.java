package javagames.sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

/**
 * This abstract class supports controlling audio.
 *
 * @author Timothy Wright
 *
 */
public abstract class AudioStream implements LineListener {

  public static final int    LOOP_CONTINUOUSLY = -1;
  protected final Lock       lock              = new ReentrantLock();
  protected final Condition  cond              = lock.newCondition();
  protected volatile boolean open              = false;
  protected volatile boolean started           = false;
  // UPDATES
  protected FloatControl gainControl;
  protected FloatControl panControl;
  // UPDATES
  protected byte[]                          soundData;
  private final List<BlockingAudioListener> listeners =
      Collections.synchronizedList(new ArrayList<BlockingAudioListener>());

  public AudioStream(final byte[] soundData) {
    this.soundData = soundData;
  }

  public abstract void open();

  public abstract void close();

  public abstract void start();

  public abstract void loop(int count);

  public abstract void restart();

  public abstract void stop();

  public boolean addListener(final BlockingAudioListener listener) {
    return listeners.add(listener);
  }

  protected void fireTaskFinished() {
    synchronized (listeners) {
      for (final BlockingAudioListener listener : listeners) {
        listener.audioFinished();
      }
    }
  }

  @Override
  public void update(final LineEvent event) {
    final boolean wasStarted = started;
    lock.lock();
    try {
      if (event.getType() == Type.OPEN) {
        open = true;
      }
      else if (event.getType() == Type.CLOSE) {
        open = false;
      }
      else if (event.getType() == Type.START) {
        started = true;
      }
      else if (event.getType() == Type.STOP) {
        started = false;
      }
      cond.signalAll();
    }
    finally {
      lock.unlock();
    }
    if (wasStarted && !started) {
      fireTaskFinished();
    }
  }

  // UPDATES
  public void clearControls() {
    gainControl = null;
    panControl = null;
  }

  public void createControls(final Line line) {
    if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
      gainControl =
          (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
    }
    if (line.isControlSupported(FloatControl.Type.PAN)) {
      panControl = (FloatControl) line.getControl(FloatControl.Type.PAN);
    }
  }

  public boolean hasGainControl() {
    return gainControl != null;
  }

  public void setGain(final float fGain) {
    if (hasGainControl()) {
      gainControl.setValue(fGain);
    }
  }

  public float getGain() {
    return hasGainControl() ? gainControl.getValue() : 0.0f;
  }

  public float getMaximum() {
    return hasGainControl() ? gainControl.getMaximum() : 0.0f;
  }

  public float getMinimum() {
    return hasGainControl() ? gainControl.getMinimum() : 0.0f;
  }

  public boolean hasPanControl() {
    return panControl != null;
  }

  public float getPrecision() {
    return hasPanControl() ? panControl.getPrecision() : 0.0f;
  }

  public float getPan() {
    return hasPanControl() ? panControl.getValue() : 0.0f;
  }

  public void setPan(final float pan) {
    if (hasPanControl()) {
      panControl.setValue(pan);
    }
  }
  // UPDATES
}