package javagames.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Handle loading from the file path or resource path
 *
 * @author Timothy Wright
 *
 */
public class ResourceLoader {

  /**
   * This method tries to load from the resource path and if unable then the
   * file path
   * 
   * @param clazz
   * @param filePath
   * @param resPath
   * @return
   */
  public static InputStream load(final Class<?> clazz, final String filePath,
      final String resPath) {
    // try the resource first
    InputStream in = null;
    if (!((resPath == null) || resPath.isEmpty())) {
      in = clazz.getResourceAsStream(resPath);
    }
    if (in == null) {
      // try the file path
      try {
        in = new FileInputStream(filePath);
      }
      catch (final FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return in;
  }

}