import java.awt.*;
import java.util.Objects;

/**
 * A record that represent a single space/tile of a patch object, so, a patch object is a collection of PatchSpace
 * objects.
 *
 * @param button Boolean that specifies whether the space has a button or not.
 * @param color  The color of the space.
 */
public record PatchSpace(boolean button, Color color) {
  /**
   * A compact constructor that ensures the color object isn't null.
   *
   * @param button Boolean stating whether the space contains a button.
   * @param color  The color of the patch space.
   */
  public PatchSpace {
    Objects.requireNonNull(color);
  }
}
