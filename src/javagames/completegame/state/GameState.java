package javagames.completegame.state;

/**
 * This class stores the current level number and number of hearts
 * 
 * @author Andres
 *
 */
public class GameState {
  private int level;
  private int hearts;

  public void setLevel(final int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
  }

  public void setHearts(final int hearts) {
    this.hearts = hearts;
  }

  public int getHearts() {
    return hearts;
  }
}