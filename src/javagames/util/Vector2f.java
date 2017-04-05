package javagames.util;

/**
 * This class handles the attributes and behaviors of a float vector.
 *
 * @author Timothy Wright
 *
 */
public class Vector2f {

  public float x;
  public float y;
  public float w;

  public Vector2f() {
    x = 0.0f;
    y = 0.0f;
    w = 1.0f;
  }

  public Vector2f(final Vector2f v) {
    x = v.x;
    y = v.y;
    w = v.w;
  }

  public Vector2f(final float x, final float y) {
    this.x = x;
    this.y = y;
    w = 1.0f;
  }

  public Vector2f(final float x, final float y, final float w) {
    this.x = x;
    this.y = y;
    this.w = w;
  }

  public void translate(final float tx, final float ty) {
    x += tx;
    y += ty;
  }

  public void scale(final float sx, final float sy) {
    x *= sx;
    y *= sy;
  }

  public void rotate(final float rad) {
    final float tmp = (float) ((x * Math.cos(rad)) - (y * Math.sin(rad)));
    y = (float) ((x * Math.sin(rad)) + (y * Math.cos(rad)));
    x = tmp;
  }

  public void shear(final float sx, final float sy) {
    final float tmp = x + (sx * y);
    y = y + (sy * x);
    x = tmp;
  }

  public Vector2f add(final Vector2f v) {
    return new Vector2f(x + v.x, y + v.y);
  }

  public Vector2f sub(final Vector2f v) {
    return new Vector2f(x - v.x, y - v.y);
  }

  public Vector2f mul(final float scalar) {
    return new Vector2f(scalar * x, scalar * y);
  }

  public Vector2f div(final float scalar) {
    return new Vector2f(x / scalar, y / scalar);
  }

  public Vector2f inv() {
    return new Vector2f(-x, -y);
  }

  public Vector2f norm() {
    return div(len());
  }

  public float dot(final Vector2f v) {
    return (x * v.x) + (y * v.y);
  }

  public float len() {
    return (float) Math.sqrt((x * x) + (y * y));
  }

  public float lenSqr() {
    return (x * x) + (y * y);
  }

  public Vector2f perp() {
    return new Vector2f(-y, x);
  }

  public float angle() {
    return (float) Math.atan2(y, x);
  }

  public static Vector2f polar(final float angle, final float radius) {
    return new Vector2f(radius * (float) Math.cos(angle),
        radius * (float) Math.sin(angle));
  }

  @Override
  public String toString() {
    return String.format("(%s,%s)", x, y);
  }
}