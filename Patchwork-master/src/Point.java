/**
 * A record that represents a coordinate.
 *
 * @param i The line (y) of a specified point.
 * @param j The column (x) of a specified point.
 */
public record Point(int i, int j) {
  /**
   * A function that returns the line that the Point object holds, equivalent to the i field.
   *
   * @return the line.
   */
  public int line() {
    return i;
  }

  /**
   * A function that returns the column that the Point object holds, equivalent to the j field.
   *
   * @return the column.
   */
  public int column() {
    return j;
  }
}
