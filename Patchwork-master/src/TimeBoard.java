import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class that aims to represent a timeboard with all its properties.
 */
public class TimeBoard {
  /* Size, like an array size, last position aka index is 63, keep this in mind */
  private static final int SIZE = 64;
  private static final int SIDELENGTH = 8;
  private Player playerOnTop;
  private final List<Integer> buttonsPos = List.of(7, 13, 20, 26, 31, 38, 44, 51, 56);
  private final List<Integer> specialPatchesPos = new ArrayList<>(List.of(27, 33, 39, 47, 50));
  private final List<Integer> voidSpaces = List.of(0, 1, 2, 63, 62, 61, 60);
  private final List<Point> spiralPathCoordinates = new ArrayList<>();

  //TODO make it so that a timeboard it is a spiralPathCoordinates ArrayList of timeBoardSpace objects:
  // timeBoardSpace = Point + Enum(patch/button/voidSpace), players have their own coordinates so no need to put them
  // inside, this will simplify this whole program.

  /**
   * A constructor to create a Timeboard instance, with buttons and special 1x1 patches, used for the full version of
   * the game.
   *
   * @param playerOnTop       The player whose token is on top of the other player.
   * @param hasSpecialPatches A boolean to indicate if the timeboard should have specialPatches, this parameter should
   *                          be used depending on which version of the game is needed.
   */
  public TimeBoard(Player playerOnTop, boolean hasSpecialPatches) {
    Objects.requireNonNull(playerOnTop);
    this.playerOnTop = playerOnTop;
    if (!hasSpecialPatches) {
      specialPatchesPos.clear();
    }
  }

  /**
   * Getter function for the SIDELENGTH field.
   *
   * @return The SIDELENGTH field.
   */
  public int getSideLength() {
    return SIDELENGTH;
  }

  /**
   * Getter function for the buttonsPos field.
   *
   * @return The buttonsPos field.
   */
  public List<Integer> getButtonsPos() {
    return buttonsPos;
  }

  /**
   * Getter function for the voidSpaces field.
   *
   * @return The voidSpaces field.
   */
  public List<Integer> getVoidSpaces() {
    return voidSpaces;
  }

  /**
   * Getter function for the specialPatchesPos field.
   *
   * @return The specialPatchesPos field.
   */
  public List<Integer> getSpecialPatchesPos() {
    return specialPatchesPos;
  }

  /**
   * Getter function for the spiralPathCoordinates field
   *
   * @return The spiralPathCoordinates field.
   */
  public List<Point> getSpiralPathCoordinates() {
    return spiralPathCoordinates;
  }

  /**
   * Getter function for the playerOnTop field.
   *
   * @return The playerOnTop field.
   */
  public Player getPlayerOnTop() {
    return playerOnTop;
  }

  /**
   * Setter function for the playerOnTop field.
   *
   * @param playerOnTop The new player on top.
   */
  public void setPlayerOnTop(Player playerOnTop) {
    Objects.requireNonNull(playerOnTop);
    this.playerOnTop = playerOnTop;
  }


  /**
   * Returns the coordinates of the point at the specified position in the spiral path.
   *
   * @param position The position of the point in the spiral path.
   * @return The coordinates of the point at the specified position.
   * @throws IllegalArgumentException if the position is out of bounds.
   */
  public Point getCoordinates(int position) {
    if (!(0 <= position && position <= SIZE - 1)) {
      throw new IllegalArgumentException("Position out of bounds");
    }
    return spiralPathCoordinates.get(position);
  }

  /**
   * Returns the position of the point at the specified coordinates in the spiral path.
   *
   * @param coordinate The coordinates of the point.
   * @return The position of the point at the specified coordinates.
   * @throws IllegalArgumentException if the coordinates are out of bounds.
   */
  public int getPosition(Point coordinate) {
    Objects.requireNonNull(coordinate);
    if (!(0 <= coordinate.i() && coordinate.i() <= 7) || !(0 <= coordinate.j() && coordinate.j() <= 7)) {
      throw new IllegalArgumentException("Coordinates out of bounds");
    }
    return spiralPathCoordinates.indexOf(coordinate);
  }


  /**
   * Determines if the specified coordinates on the time board contains a patch.
   *
   * @param coordinate The coordinates of the point.
   * @return true if the position contains a patch, false otherwise.
   * @throws IllegalArgumentException if the coordinates are out of bounds.
   */
  public boolean spaceContainsPatch(Point coordinate) {
    Objects.requireNonNull(coordinate);
    if (!(0 <= coordinate.i() && coordinate.i() <= 7) || !(0 <= coordinate.j() && coordinate.j() <= 7)) {
      throw new IllegalArgumentException("Coordinates out of bound");
    }
    return specialPatchesPos.contains(spiralPathCoordinates.indexOf(coordinate));
  }

  /**
   * Determines if the specified coordinates on the time board contains a button.
   *
   * @param coordinate The coordinates of the point.
   * @return true if the position contains a button, false otherwise.
   * @throws IllegalArgumentException if the coordinates are out of bounds.
   */
  public boolean spaceContainsButton(Point coordinate) {
    Objects.requireNonNull(coordinate);
    if (!(0 <= coordinate.i() && coordinate.i() <= 7) || !(0 <= coordinate.j() && coordinate.j() <= 7)) {
      throw new IllegalArgumentException("Coordinates out of bound");
    }
    return buttonsPos.contains(spiralPathCoordinates.indexOf(coordinate));
  }

  /**
   * Determines if the specified coordinate on the time board is void.
   *
   * @param coordinate The coordinates of the point.
   * @return true if the position is void, false otherwise.
   * @throws IllegalArgumentException if the coordinates are out of bounds.
   */
  public boolean isVoidSpace(Point coordinate) {
    Objects.requireNonNull(coordinate);
    if (!(0 <= coordinate.i() && coordinate.i() <= 7) || !(0 <= coordinate.j() && coordinate.j() <= 7)) {
      throw new IllegalArgumentException("Coordinates out of bound");
    }
    return voidSpaces.contains(spiralPathCoordinates.indexOf(coordinate));
  }

  /**
   * Determines if the specified position on the time board contains a patch.
   *
   * @param position The position to check.
   * @return true if the position contains a patch, false otherwise.
   * @throws IllegalArgumentException if the position is out of bounds.
   */
  public boolean spaceContainsPatch(int position) {
    if (!(0 <= position && position <= SIZE - 1)) {
      throw new IllegalArgumentException("Position out of bounds");
    }
    return specialPatchesPos.contains(position);
  }

  /**
   * Determines if the specified position on the time board contains a button.
   *
   * @param position The position to check.
   * @return true if the position contains a button, false otherwise.
   * @throws IllegalArgumentException if the position is out of bounds.
   */
  public boolean spaceContainsButton(int position) {
    if (!(0 <= position && position <= SIZE - 1)) {
      throw new IllegalArgumentException("Position out of bounds");
    }
    return buttonsPos.contains(position);
  }

  /**
   * Determines if the specified position on the time board is void.
   *
   * @param position The position to check.
   * @return true if the position is void, false otherwise.
   * @throws IllegalArgumentException if the position is out of bounds.
   */
  public boolean isVoidSpace(int position) {
    if (!(0 <= position && position <= SIZE - 1)) {
      throw new IllegalArgumentException("Position out of bounds");
    }
    return voidSpaces.contains(position);
  }


  /**
   * A function that fills the spiralPathCoordinates List. it fills it with the positions of the spiral route starting
   * from the starting point of the timeboard (7,7)
   */
  public void initialiseSpiralPathCoordinates() {
    // arrayList holding the directions and their positions. (holding the vectors (di, dj))
    List<Point> directions = new ArrayList<>(
            List.of(new Point(0, -1), new Point(-1, 0), new Point(0, 1), new Point(1, 0)));
    // The index in the directions arraylist that corresponds to the current position
    int currentDirectionIndex = 0;

    // length of current segment
    int segmentLength = SIDELENGTH - 1;
    int SpacesPassed = 0;
    // current position (i = sideLength, j = sideLength)
    Point currentPoint = new Point(SIDELENGTH - 1, SIDELENGTH - 1);
    fillSpiralPathCoordinates(directions, currentDirectionIndex, segmentLength, SpacesPassed, currentPoint);
  }

  /**
   * A function that does the main for loop to fill the spiralPathCoordinates list.
   *
   * @param directions            A list containing the four possible directions in order of their need and relevance in
   *                              this spiral path course.
   * @param currentDirectionIndex An integer, the index of the directions list corresponding to the current direction.
   * @param segmentLength         The current segment length, starting by 7 (player is already on the first position).
   * @param spacesPassed          How many spaces have been passed.
   * @param currentPoint          The current course coordinate, starting by (7,7).
   */
  private void fillSpiralPathCoordinates(List<Point> directions, int currentDirectionIndex, int segmentLength,
                                         int spacesPassed, Point currentPoint) {
    for (int k = 0; k < SIDELENGTH * SIDELENGTH; ++k) {
      spiralPathCoordinates.add(currentPoint);
      // make a step, 'add' 'direction' vector (di, dj) to current Point (i, j)
      currentPoint = new Point(currentPoint.i() + directions.get(currentDirectionIndex).i(),
                               currentPoint.j() + directions.get(currentDirectionIndex).j());
      ++spacesPassed;
      if (spacesPassed == segmentLength) {
        // done with current segment
        spacesPassed = 0;

        // 'rotate' directions
        currentDirectionIndex = (currentDirectionIndex + 1) % directions.size();

        // change segment length if necessary
        if ((directions.get(currentDirectionIndex).i() == 1) ||
            ((directions.get(currentDirectionIndex).i() == -1) && segmentLength != SIDELENGTH - 1)) {
          --segmentLength;
        }
      }
    }
  }

}
