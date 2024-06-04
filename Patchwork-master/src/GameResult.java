import java.util.Objects;

/**
 * A record to encapsulate the final game result.
 *
 * @param winner The winner player.
 * @param score  The winner player's final score.
 */
public record GameResult(Player winner, int score) {

  /**
   * A constructor for the GameResult record, it ensures the winner player object isn't null.
   *
   * @param winner The victor.
   * @param score  The score of the victor.
   */
  public GameResult {
    Objects.requireNonNull(winner);
  }
}
