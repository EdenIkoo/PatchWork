import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A class that aims to represent the patches set and their properties.
 */
public class Patches {
  /* neutralPawnIndex can be bigger than the list's size, because patchesList represents a circular array here, this
  is why neutralPawnIndex should always be used with the mod operator */
  private int neutralPawnIndex;
  private final List<Patch> patchesList;

  /**
   * A constructor for a Patches instance
   */
  public Patches() {
    this.neutralPawnIndex = 0;
    this.patchesList = new ArrayList<>();
  }

  // Putting the needed getters and setters before the actual functions

  /**
   * Getter function for the patchesList field.
   *
   * @return A List containing the Patch objects.
   */
  public List<Patch> getPatchesList() {
    return patchesList;
  }

  /**
   * Getter function for the neutralPawnIndex field.
   *
   * @return an Integer, the neutralPawnIndex.
   */
  public int getNeutralPawnIndex() {
    return neutralPawnIndex;
  }

  /**
   * A setter function for the neutralPawnIndex.
   *
   * @param index an Integer, the new neutralPawnIndex index in the patchesList.
   */
  public void setNeutralPawnIndex(int index) {
    if (neutralPawnIndex < 0) {
      throw new IllegalArgumentException("The neutral pawn can't have a negative index");
    }
    neutralPawnIndex = index;
  }

  /**
   * A function that adds a Patch object to patchesList;
   *
   * @param patch The patch to be added.
   */
  private void add(Patch patch) {
    Objects.requireNonNull(patch);
    patchesList.add(patch);
  }

  /**
   * A toString method that returns a string representing the calling Patches object.
   *
   * @return A string.
   */
  @Override
  public String toString() {
    var myBuilder = new StringBuilder();
    for (var patch : patchesList) {
      myBuilder.append(patch).append("\n");
    }
    return myBuilder.toString();
  }

  /**
   * A function that takes a string of booleans separated by sep, and returns the corresponding PatchSpace array.
   *
   * @param string The string representing the sequence of booleans.
   * @param sep    The separator separating the booleans.
   * @param color  The color of the patch.
   * @return A boolean array.
   */
  private PatchSpace[] stringToPatchSpaceArray(String string, String sep, Color color) {
    String[] parts = string.split(sep);
    PatchSpace[] PatchSpaceTable = new PatchSpace[parts.length];
    for (int i = 0; i < parts.length; i++) {
      PatchSpaceTable[i] = Boolean.parseBoolean(parts[i]) ? new PatchSpace(false, color) : null;
    }
    return PatchSpaceTable;
  }

  /**
   * A function that parses the patch structure (2D boolean array) from a text that follows a predetermined style.
   *
   * @param reader A BufferedReader object.
   * @param length The length of the 2D boolean array to be generated.
   * @param width  The width of the 2D boolean array to be generated.
   * @param color  The color of the patch.
   * @return A 2D boolean array representing a patch structure.
   * @throws IOException If an I/O error occurs
   */
  private PatchSpace[][] parsePatchStructFromFile(BufferedReader reader, int length, int width,
                                                  Color color) throws IOException {
    int index = 0;
    PatchSpace[][] patchStruct = new PatchSpace[length][width];

    String boolLine = reader.readLine();
    while (boolLine != null && !boolLine.isEmpty()) {
      patchStruct[index] = stringToPatchSpaceArray(boolLine, ", ", color);
      index++;
      boolLine = reader.readLine();
    }
    return patchStruct;
  }

  /**
   * A function that determines if neither of the two players can buy the first three patches presented to them.
   *
   * @param patches       A Patches object, representing the patches of the game.
   * @param playerButtons How many buttons both players have.
   * @return A boolean.
   */
  private static boolean cantAffordPatches(Patches patches, int playerButtons) {
    var patchesList = patches.getPatchesList();
    var patchesStartingIndex = patches.getNeutralPawnIndex() % patchesList.size();
    var selectablePatchesList = patchesList.subList(patchesStartingIndex,
                                                    (patchesStartingIndex + 3) % patchesList.size());
    return selectablePatchesList.stream().mapToInt(Patch::getPrice).allMatch(price -> price > playerButtons);
  }

  /**
   * A function that ensures either player can buy at least one of the three patches presented to them at the beginning
   * of the game.
   */
  private void ensureGoodShuffle() {
    Collections.shuffle(patchesList);
    // We don't need to test for both players because they both have 5 buttons at the start of the game.
    // I really hate having a bare 5 here instead of a variable to replace it.
    while (cantAffordPatches(this, 5)) {
      Collections.shuffle(patchesList);
    }
  }

  /**
   * A function that takes a patch structure and adds the necessary buttons to it.
   *
   * @param structure       The structure of a patch.
   * @param numberOfButtons number of buttons the patch has.
   */
  private void addButtons(PatchSpace[][] structure, int numberOfButtons) {
    for (int line = 0; line < structure.length; line++) {
      for (int column = 0; column < structure[0].length; column++) {
        if (numberOfButtons == 0) {
          return;
        }
        if (structure[line][column] != null) {
          structure[line][column] = new PatchSpace(true, structure[line][column].color());
          numberOfButtons--;
        }
      }
    }
  }

  /* Can't make this static without dragging all other methods that are used by it into the static shadow realm */

  /**
   * A function that fills the patchesList list with the patches parsed from a text file.
   *
   * @param path The patch to the text file containing the patches' information. Used for the full version of the game.
   * @throws IOException If an I/O error occurs
   */
  public void generatePatches(Path path) throws IOException {
    Objects.requireNonNull(path);
    try (var reader = Files.newBufferedReader(path)) {
      String line;
      while ((line = reader.readLine()) != null) {
        var paramsTable = line.split(", ");
        line = reader.readLine();
        var dimensions = line.split(", ");
        line = reader.readLine();
        var colors = line.split(", ");
        Color color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
        PatchSpace[][] patchStruct = parsePatchStructFromFile(reader, Integer.parseInt(dimensions[0]),
                                                              Integer.parseInt(dimensions[1]), color);
        addButtons(patchStruct, Integer.parseInt(paramsTable[2]));
        add(new Patch(Integer.parseInt(paramsTable[0]), Integer.parseInt(paramsTable[1]),
                      Integer.parseInt(paramsTable[2]), patchStruct, color, 0, 0));
      }
      Patch twoByOne = patchesList.remove(patchesList.size() - 1);
      // Keeping it competitive.
      ensureGoodShuffle();
      add(twoByOne);
    }
  }

  /* I think it's better to have a method that defaults to adding the simple patches instead of a file. */

  /**
   * A function that fills the patchesList list with simple 2x2 patches. Used for the basic version of the game.
   */
  public void generatePatches() {
    Color model1Color = new Color(255, 179, 179);
    Color model2Color = new Color(119, 214, 119);
    for (int i = 0; i < 20; i++) {
      add(new Patch(3, 4, 1, new PatchSpace[][]{{new PatchSpace(false, model1Color), new PatchSpace(true, model1Color)},
                                                {new PatchSpace(false, model1Color),
                                                 new PatchSpace(false, model1Color)}}, model1Color, 0, 0));
      add(new Patch(2, 2, 0,
                    new PatchSpace[][]{{new PatchSpace(false, model2Color), new PatchSpace(false, model2Color)},
                                       {new PatchSpace(false, model2Color), new PatchSpace(false, model2Color)}},
                    model2Color, 0, 0));
    }
  }
}
