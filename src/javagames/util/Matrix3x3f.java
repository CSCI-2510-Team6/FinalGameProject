package javagames.util;

/**
 * This class handles the attributes and behaviors for a three by three float
 * matrix.
 *
 * @author Timothy Wright
 *
 */
public class Matrix3x3f {

  private float[][] m = new float[3][3];

  public Matrix3x3f() {
  }

  public Matrix3x3f(final float[][] m) {
    setMatrix(m);
  }

  public Matrix3x3f add(final Matrix3x3f m1) {
    return new Matrix3x3f(new float[][] {
        { m[0][0] + m1.m[0][0], m[0][1] + m1.m[0][1], m[0][2] + m1.m[0][2] },
        { m[1][0] + m1.m[1][0], m[1][1] + m1.m[1][1], m[1][2] + m1.m[1][2] },
        { m[2][0] + m1.m[2][0], m[2][1] + m1.m[2][1], m[2][2] + m1.m[2][2] } });
  }

  public Matrix3x3f sub(final Matrix3x3f m1) {
    return new Matrix3x3f(new float[][] {
        { m[0][0] - m1.m[0][0], m[0][1] - m1.m[0][1], m[0][2] - m1.m[0][2] },
        { m[1][0] - m1.m[1][0], m[1][1] - m1.m[1][1], m[1][2] - m1.m[1][2] },
        { m[2][0] - m1.m[2][0], m[2][1] - m1.m[2][1], m[2][2] - m1.m[2][2] } });
  }

  public Matrix3x3f mul(final Matrix3x3f m1) {
    return new Matrix3x3f(new float[][] { { (m[0][0] * m1.m[0][0] // ******
        ) + (m[0][1] * m1.m[1][0] // M[0,0]
        ) + (m[0][2] * m1.m[2][0]), // ******
        (m[0][0] * m1.m[0][1] // ******
        ) + (m[0][1] * m1.m[1][1] // M[0,1]
        ) + (m[0][2] * m1.m[2][1]), // ******
        (m[0][0] * m1.m[0][2] // ******
        ) + (m[0][1] * m1.m[1][2] // M[0,2]
        ) + (m[0][2] * m1.m[2][2]) }, // ******
        { (m[1][0] * m1.m[0][0] // ******
        ) + (m[1][1] * m1.m[1][0] // M[1,0]
        ) + (m[1][2] * m1.m[2][0]), // ******
            (m[1][0] * m1.m[0][1] // ******
            ) + (m[1][1] * m1.m[1][1] // M[1,1]
            ) + (m[1][2] * m1.m[2][1]), // ******
            (m[1][0] * m1.m[0][2] // ******
            ) + (m[1][1] * m1.m[1][2] // M[1,2]
            ) + (m[1][2] * m1.m[2][2]) }, // ******
        { (m[2][0] * m1.m[0][0] // ******
        ) + (m[2][1] * m1.m[1][0] // M[2,0]
        ) + (m[2][2] * m1.m[2][0]), // ******
            (m[2][0] * m1.m[0][1] // ******
            ) + (m[2][1] * m1.m[1][1] // M[2,1]
            ) + (m[2][2] * m1.m[2][1]), // ******
            (m[2][0] * m1.m[0][2] // ******
            ) + (m[2][1] * m1.m[1][2] // M[2,2]
            ) + (m[2][2] * m1.m[2][2]) } // ******
    });
  }

  public void setMatrix(final float[][] m) {
    this.m = m;
  }

  public static Matrix3x3f zero() {
    return new Matrix3x3f(new float[][] { { 0.0f, 0.0f, 0.0f },
        { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f } });
  }

  public static Matrix3x3f identity() {
    return new Matrix3x3f(new float[][] { { 1.0f, 0.0f, 0.0f },
        { 0.0f, 1.0f, 0.0f }, { 0.0f, 0.0f, 1.0f } });
  }

  public static Matrix3x3f translate(final Vector2f v) {
    return Matrix3x3f.translate(v.x, v.y);
  }

  public static Matrix3x3f translate(final float x, final float y) {
    return new Matrix3x3f(new float[][] { { 1.0f, 0.0f, 0.0f },
        { 0.0f, 1.0f, 0.0f }, { x, y, 1.0f } });
  }

  public static Matrix3x3f scale(final Vector2f v) {
    return Matrix3x3f.scale(v.x, v.y);
  }

  public static Matrix3x3f scale(final float x, final float y) {
    return new Matrix3x3f(new float[][] { { x, 0.0f, 0.0f }, { 0.0f, y, 0.0f },
        { 0.0f, 0.0f, 1.0f } });
  }

  public static Matrix3x3f shear(final Vector2f v) {
    return Matrix3x3f.shear(v.x, v.y);
  }

  public static Matrix3x3f shear(final float x, final float y) {
    return new Matrix3x3f(new float[][] { { 1.0f, y, 0.0f }, { x, 1.0f, 0.0f },
        { 0.0f, 0.0f, 1.0f } });
  }

  public static Matrix3x3f rotate(final float rad) {
    return new Matrix3x3f(
        new float[][] { { (float) Math.cos(rad), (float) Math.sin(rad), 0.0f },
            { (float) -Math.sin(rad), (float) Math.cos(rad), 0.0f },
            { 0.0f, 0.0f, 1.0f } });
  }

  public Vector2f mul(final Vector2f vec) {
    return new Vector2f((vec.x * m[0][0] //
    ) + (vec.y * m[1][0] // V.x
    ) + (vec.w * m[2][0]), //
        (vec.x * m[0][1] //
        ) + (vec.y * m[1][1] // V.y
        ) + (vec.w * m[2][1]), //
        (vec.x * m[0][2] //
        ) + (vec.y * m[1][2] // V.w
        ) + (vec.w * m[2][2] //
        ));
  }

  @Override
  public String toString() {
    final StringBuilder buf = new StringBuilder();
    for (int i = 0; i < 3; ++i) {
      buf.append("[");
      buf.append(m[i][0]);
      buf.append(",\t");
      buf.append(m[i][1]);
      buf.append(",\t");
      buf.append(m[i][2]);
      buf.append("]\n");
    }
    return buf.toString();
  }
}