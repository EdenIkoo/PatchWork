import java.awt.*;
import java.util.Objects;

/**
 * A class that aims to represent a player and all his properties.
 */
public class Player {
  private int buttons;
  private int position;
  private final QuiltBoard quiltBoard;
  private final String name;
  private boolean bonusTile;
  private boolean first;
  private final Color color;

  private static int nextId = 1;

  /**
   * A contractor for a Player instance.
   *
   * @param buttons         The number of buttons that the player starts out with.
   * @param position        The position that the player starts in.
   * @param name            The name of the player.
   * @param PlayerColor     The color of the time token of the player.
   * @param quiltBoardColor The color of the player's quiltboard.
   */
  public Player(int buttons, int position, String name, Color PlayerColor, Color quiltBoardColor) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(PlayerColor);
    if (buttons < 0) {
      throw new IllegalArgumentException("Negative number of buttons");
    }
    if (position < 0) {
      throw new IllegalArgumentException("Negative position");
    }
    int id = nextId++;
    this.buttons = buttons;
    this.quiltBoard = new QuiltBoard(quiltBoardColor, id); // Smart quiltboard feature (lmao). A QB knows its
    // master.
    this.position = position;
    this.name = name;
    this.bonusTile = false;
    this.first = false;
    this.color = PlayerColor;
  }

  /**
   * Getter function for the position field.
   *
   * @return An integer, position.
   */
  public int getPosition() {
    return position;
  }

  /**
   * Getter function for the quiltBoard field.
   *
   * @return The quiltBoard field.
   */
  public QuiltBoard getQuiltBoard() {
    return quiltBoard;
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
   * Getter function for the buttons field.
   *
   * @return The buttons field.
   */
  public int getButtons() {
    return buttons;
  }

  /**
   * Getter function for the name field.
   *
   * @return The name field.
   */
  public String getName() {
    return name;
  }

  /**
   * Getter function for the bonusTile field.
   *
   * @return The bonusTile field.
   */
  public boolean hasBonusTile() {
    return bonusTile;
  }

  /**
   * Getter function for the first field.
   *
   * @return The first field.
   */
  public boolean isFirst() {
    return first;
  }

  /**
   * Setter function for the buttons fields.
   *
   * @param buttons The new number of buttons.
   */
  public void setButtons(int buttons) {
    if (buttons < 0) {
      throw new IllegalArgumentException("Negative number of buttons");
    }
    this.buttons = buttons;
  }

  /**
   * A function that determines if a player is able to buy a patch.
   *
   * @param patch The patch to be bought, or not.
   * @return A boolean.
   */
  public boolean purchasePossible(Patch patch) {
    Objects.requireNonNull(patch);
    return buttons >= patch.getPrice();
  }

  /**
   * Setter function for the position field.
   *
   * @param position The new position.
   */
  public void setPosition(int position) {
    if (position < 0) {
      throw new IllegalArgumentException("Negative position");
    }
    if (position < this.position) {
      throw new IllegalArgumentException("Player can't go backwards");
    }
    this.position = position;
  }

  /**
   * Setter function for the bonusTile field.
   *
   * @param bonusTile The new bonusTile field.
   */
  public void setBonusTile(boolean bonusTile) {
    this.bonusTile = bonusTile;
  }

  /**
   * Setter function for the first field.
   *
   * @param first The new first field.
   */
  public void setFirst(boolean first) {
    this.first = first;
  }

  /**
   * An equals function to test if a player is equal to the object passed as argument.
   *
   * @param obj an Object object.
   * @return True in case the object is equal to the player.
   */
  @Override
  public boolean equals(Object obj) {
    // No need to check the equality of quiltBoard as it is a slow verification, and also checking the equality of
    // names is mostly enough, checking other equalities just in case the two players have the same name which
    // shouldn't happen.
    return obj instanceof Player player && name.equals(player.name) && buttons == player.buttons &&
           position == player.position && first == player.first && bonusTile == player.bonusTile;

  }

  /**
   * A hashcode function to complement the equals function.
   *
   * @return An integer, the hashcode.
   */
  @Override
  public int hashCode() {
    return Objects.hash(buttons, position, name, bonusTile, first);
  }

  /**
   * A method that returns a string representation of the player.
   *
   * @return String.
   */
  @Override
  public String toString() {
    return buttons + " buttons" + "\n" + quiltBoard;
  }
}
