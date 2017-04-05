package javagames.sound;

/**
 * This class creates an exception for sound errors.
 *
 * @author Timothy Wright
 *
 */
public class SoundException extends RuntimeException {

  public SoundException(final String message) {
    super(message);
  }

  public SoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

}