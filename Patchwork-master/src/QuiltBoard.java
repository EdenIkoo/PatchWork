import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * A class that aims to represent a quiltboard and all its properties.
 */
public class QuiltBoard {
  private final int QBOARDSIZE = 9;
  private final PatchSpace[][] structure;
  private final Color color;
  private int totalIncome;
  private final int owner;

  /**
   * A constructor that generates an empty quiltboard with a specified color and owner.
   *
   * @param color The color of the quiltboard.
   * @param owner The owner of the quiltboard.
   */
  public QuiltBoard(Color color, int owner) {
    Objects.requireNonNull(color);
    this.structure = new PatchSpace[QBOARDSIZE][QBOARDSIZE];
    this.totalIncome = 0;
    this.color = color;
    this.owner = owner;
  }

  /**
   * Getter function for the id field.
   *
   * @return An integer, id.
   */
  public int getOwner() {
    return owner;
  }

  /**
   * Getter function for the totalIncome field.
   *
   * @return The totalIncome field.
   */
  public int getTotalIncome() {
    return totalIncome;
  }

  /**
   * Getter function for the QBOARDSIZE field.
   *
   * @return The QBOARDSIZE field.
   */
  public int getQBOARDSIZE() {
    return QBOARDSIZE;
  }

  /**
   * Getter function for the color field.
   *
   * @return The color field.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Getter function for the structure field.
   *
   * @return The structure field.
   */
  public PatchSpace[][] getStructure() {
    return structure;
  }

  /**
   * Setter function for the totalIncome field.
   *
   * @param totalIncome The totalIncome field.
   */
  public void setTotalIncome(int totalIncome) {
    if (totalIncome < 0) {
      throw new IllegalArgumentException("income must be positive");
    }
    this.totalIncome = totalIncome;
  }

  /**
   * A function that returns the point on the quiltboard where the top left corner (first element) of the patch
   * structure has to be placed.
   *
   * @param point A point that represents the point on the quiltboard where the player want to place the first
   *              true element of the patch structure.
   * @param patch The patch the player wants to place on the quiltboard.
   * @return The point on the quiltboard that the first element of the patch will occupy.
   */
  private static Point realPlacementPosition(Point point, Patch patch) {
    int realI = point.i() - 1, realJ = point.j() - 1;
    int shift = 0;
    for (int j = 0; j < patch.getStructure()[0].length; j++) {
      if (patch.getStructure()[0][j] == null) {
        shift++;
      } else {
        break;
      }
    }
    realJ = realJ - shift;
    return new Point(realI, realJ);
  }

  /**
   * A function that places a patch on an already valide user given point (line/column between 1 and 9) on the
   * quiltboard, this
   * function doesn't do coordinates verifications.
   *
   * @param point The point on the quiltboard where the first true element of the patch will be placed.
   * @param patch The patch to be placed.
   */
  public void placePatch(Point point, Patch patch) {
    Objects.requireNonNull(point);
    Objects.requireNonNull(patch);
    Point realPlacement = realPlacementPosition(point, patch);
    for (int i = 0; i < patch.getStructure().length; i++) {
      for (int j = 0; j < patch.getStructure()[0].length; j++) {
        if (patch.getStructure()[i][j] != null) {
          structure[i + realPlacement.i()][j + realPlacement.j()] = patch.getStructure()[i][j];
        }
      }
    }
  }

  /**
   * Determines if a point is within the bounds of the quiltboard.
   *
   * @param point The point to check.
   * @return `true` if the point is within the bounds of the quiltboard, `false` otherwise.
   */
  private static boolean validUserPoint(Point point) {
    // check if a point is in the quiltboard
    return 1 <= point.i() && point.i() <= 9 && 1 <= point.j() && point.j() <= 9;
  }

  /**
   * Determines if a patch fits within the bounds of the quiltboard.
   *
   * @param patch         The patch to check.
   * @param realPlacement The real placement position of the patch on the quiltboard.
   * @return `true` if the patch fits within the bounds of the quiltboard, `false` otherwise.
   */
  private boolean patchFitsInQuiltboard(Patch patch, Point realPlacement) {
    // Check if the patch fits within the bounds of the quiltboard
    return 0 <= realPlacement.i() && realPlacement.i() + patch.getStructure().length <= QBOARDSIZE &&
           0 <= realPlacement.j() && realPlacement.j() + patch.getStructure()[0].length <= QBOARDSIZE;
  }

  /**
   * Determines if a patch overlaps with any existing patches on the quiltboard.
   *
   * @param patch         The patch to check.
   * @param realPlacement The real placement position of the patch on the quiltboard.
   * @return `true` if the patch overlaps with any existing patches on the quiltboard, `false` otherwise.
   */
  private boolean patchOverlapsOther(Patch patch, Point realPlacement) {
    // Check if the patch overlaps with any existing patches on the quiltboard
    for (int i = 0; i < patch.getStructure().length; i++) {
      for (int j = 0; j < patch.getStructure()[0].length; j++) {
        if (structure[i + realPlacement.i()][j + realPlacement.j()] != null && patch.getStructure()[i][j] != null) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Determines if a patch can be placed at a user given point on the quiltboard.
   *
   * @param point The point on the quiltboard where the top left corner of the patch will be placed.
   * @param patch The patch to be placed on the quiltboard.
   * @return `true` if the patch can be placed at the given point, `false` otherwise.
   */
  public boolean validUserPlacement(Point point, Patch patch) {
    Objects.requireNonNull(point);
    Objects.requireNonNull(patch);
    if (!validUserPoint(point)) return false;
    // Calculate the real placement position of the patch on the quiltboard
    Point realPlacement = realPlacementPosition(point, patch);
    return patchFitsInQuiltboard(patch, realPlacement) && !patchOverlapsOther(patch, realPlacement);
  }

  /**
   * An equals function to test if a quiltBoard is equal to the object passed as argument.
   *
   * @param obj an Object object.
   * @return True in case the object is equal to the quiltBoard.
   */
  @Override
  public boolean equals(Object obj) {
    // this should be enough unless both quilBoards have the same color and the exact same patches configurations and
    // the same income, cutting it real close here.
    return obj instanceof QuiltBoard quiltBoard && Arrays.deepEquals(structure, quiltBoard.structure) &&
           color.equals(quiltBoard.color) && totalIncome == quiltBoard.totalIncome;
  }

  /**
   * A function that creates and returns a string representation of a quiltboard.
   *
   * @return String.
   */
  @Override
  public String toString() {
    var myBuilder = new StringBuilder();
    for (var subList : structure) {
      for (var elem : subList) {
        myBuilder.append(elem).append(", ");
      }
      myBuilder.append("\n");
    }
    return myBuilder.toString();
  }


  /**
   * A function that takes in a 2D PatchSpace array and tests if all its elements are true.
   *
   * @param array The 2D boolean array in question.
   * @return A boolean.
   */
  public static boolean fullyPatched(PatchSpace[][] array) {
    Objects.requireNonNull(array);
    for (var line : array) {
      for (var elem : line) {
        if (elem == null) {
          return false;
        }
      }
    }
    return true;
  }


  /**
   * A function that determines if the quiltboard have a 7x7 section composed entirely en true elements (filled
   * entirely with patches).
   *
   * @return A boolean.
   */
  public boolean containsSevenBySeven() {
    PatchSpace[][] subArr = new PatchSpace[7][7];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        for (int k = 0; k < 7; k++) {
          subArr[k] = Arrays.copyOfRange(structure[k + i], j, structure[0].length - (3 - j) + 1);
        }
        if (fullyPatched(subArr)) {
          return true;
        }
      }
    }
    return false;
  }
}
