import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * A class that aims to represent a patch and all its properties.
 */
public class Patch {
  private final int price, timeCost, income;
  private float x, y;
  private final Color color;
  private PatchSpace[][] structure;

  /**
   * A constructor for the Patch object.
   *
   * @param price     The price in buttons of the patch.
   * @param timeCost  The time cost (space cost) of the patch.
   * @param income    The amount of buttons this patch will give if it were placed on the player's quiltboard and if the
   *                  player passes by a button symbol on the timeboard.
   * @param color     The color of the patch.
   * @param structure The boolean two-dimensional array representing the structure of the patch
   * @param x         The x coordinate of the top left piece of the patch.
   * @param y         The y coordinate of the top left piece of the patch.
   */
  public Patch(int price, int timeCost, int income, PatchSpace[][] structure, Color color, float x, float y) {
    if (price < 0) {
      throw new IllegalArgumentException("Negative price");
    }
    if (timeCost < 0) {
      throw new IllegalArgumentException("Negative timeCost");
    }
    if (income < 0) {
      throw new IllegalArgumentException("Negative income");
    }
    this.price = price;
    this.timeCost = timeCost;
    this.income = income;
    this.structure = structure;
    this.color = color;
    this.y = y;
    this.x = x;
  }

  // Putting the needed getters and setters before the actual functions

  /**
   * Getter function for structure field.
   *
   * @return structure field
   */
  public PatchSpace[][] getStructure() {
    return structure;
  }

  /**
   * Getter function for color field.
   *
   * @return color field
   */
  public Color getColor() {
    return color;
  }

  /**
   * Getter function for price field.
   *
   * @return price field
   */
  public int getPrice() {
    return price;
  }

  /**
   * Getter function for timeCost field.
   *
   * @return timeCost field
   */
  public int getTimeCost() {
    return timeCost;
  }

  /**
   * Getter function for income field.
   *
   * @return income field
   */
  public int getIncome() {
    return income;
  }

  /**
   * Getter function for x field.
   *
   * @return x field
   */
  public float getX() {
    return x;
  }

  /**
   * Getter function for y field.
   *
   * @return y field
   */
  public float getY() {
    return y;
  }

  /**
   * Setter function for the x field.
   *
   * @param x new x coordinate
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * Setter function for the y field
   *
   * @param y new y coordinate
   */
  public void setY(float y) {
    this.y = y;
  }

  /**
   * A function that takes a two-dimensional boolean array and rotates it clock wise.
   *
   * @param structure The two-dimensional array in quesiton.
   * @return retuns the rotated version of the two-dimensional array.
   */
  private static PatchSpace[][] rotateStructureClockWise(PatchSpace[][] structure) {
    final int length = structure.length;
    final int width = structure[0].length;
    PatchSpace[][] rotated = new PatchSpace[width][length];
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        rotated[j][length - 1 - i] = structure[i][j];
      }
    }
    return rotated;
  }

  /**
   * A function that flips a patch's structure on the vertical axis.
   */
  public void flipPatch() {
    for (int j = 0; j < structure.length; j++) {
      for (int i = 0; i < structure[j].length / 2; i++) {
        PatchSpace temp = structure[j][i];
        structure[j][i] = structure[j][structure[j].length - i - 1];
        structure[j][structure[j].length - i - 1] = temp;
      }
    }
  }

  /**
   * A function that rotates the patch clock wise.
   */
  public void rotatePatch() {
    structure = rotateStructureClockWise(structure);
  }

  /**
   * A function that takes in two two-dimensional boolean arrays and computes if their lengths and widths are equal.
   *
   * @param structureOne The first structure.
   * @param structureTwo The second structure.
   * @return boolean.
   */
  private static boolean structureDimensionsEquality(PatchSpace[][] structureOne, PatchSpace[][] structureTwo) {
    return structureOne.length == structureTwo.length && structureOne[0].length == structureTwo[0].length;
  }

  /**
   * A function that takes in two two-dimensional boolean arrays and computes if their contents are identical.
   *
   * @param structureOne The first structure.
   * @param structureTwo The second structure.
   * @return boolean.
   */
  private static boolean structureElementsEquality(PatchSpace[][] structureOne, PatchSpace[][] structureTwo) {
    for (int i = 0; i < structureTwo.length; i++) {
      for (int j = 0; j < structureTwo[0].length; j++) {
        if (structureTwo[i][j] != structureOne[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * A function that computes if the two two-dimensional boolean arrays passed as parameters are completely equal,
   * dimensionaly and content wise.
   *
   * @param structureOne The first structure.
   * @param structureTwo The second structure.
   * @return boolean.
   */
  private static boolean structureEquality(PatchSpace[][] structureOne, PatchSpace[][] structureTwo) {
    return structureDimensionsEquality(structureOne, structureTwo) &&
           structureElementsEquality(structureOne, structureTwo);

  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Patch patch && price == patch.price && income == patch.income && timeCost == patch.timeCost &&
           structureEquality(structure, patch.structure);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPrice(), getTimeCost(), getIncome(), Arrays.deepHashCode(getStructure()));
  }

  /* To string methods should only be used for testing, need a separate function/class for display */
  @Override
  public String toString() {
    var myBuilder = new StringBuilder();
    myBuilder.append("price = ").append(price).append(" ").append("timeCost = ").append(timeCost).append(" ").append(
            "income = ").append(income).append("\n");
    for (var line : structure) {
      for (var col : line) {
        myBuilder.append(col).append(", ");
      }
      myBuilder.append("\n");
    }
    return myBuilder.toString();
  }
}
