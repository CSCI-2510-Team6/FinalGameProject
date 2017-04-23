package javagames.completegame.state;

public class GameState {
  private int level;
  private int lives;

  public void setLevel(final int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
  }

  public void setLives(final int lives) {
    this.lives = lives;
  }

  public int getLives() {
    return lives;
  }
}