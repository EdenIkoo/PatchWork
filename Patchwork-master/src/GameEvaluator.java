import java.util.Objects;

/**
 * A Record that contains the necessary functions to evaluates the end results of the game.
 */
public record GameEvaluator() {

  /**
   * Counts number of empty spaces in a given quiltboard.
   *
   * @param quiltBoard quiltboard in question
   * @return returns the number of empty spaces in a given quiltboard
   */
  private static int emptySpacesNumber(QuiltBoard quiltBoard) {
    int count = 0;
    var booleanBoard = quiltBoard.getStructure();
    for (var line : booleanBoard) {
      for (var elem : line) {
        if (elem == null) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Function that computes the total score of a given player.
   *
   * @param player The given player.
   * @return The player's total score.
   */
  private static int totalScore(Player player) {
    return player.getButtons() + (player.hasBonusTile() ? 0 : 1) * 7 - (emptySpacesNumber(player.getQuiltBoard()) * 2);
  }

  /**
   * A function that computes which of the two players is the winner.
   *
   * @param player1 The first player.
   * @param player2 The second player.
   * @return returns an object that contains the winning player and their respective score.
   */
  public static GameResult evaluateWinner(Player player1, Player player2) {
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    int playerOneTotalScore = totalScore(player1);
    int playerTwoTotalScore = totalScore(player2);
    if (playerOneTotalScore > playerTwoTotalScore) {
      return new GameResult(player1, playerOneTotalScore);
    }
    if (playerTwoTotalScore > playerOneTotalScore) {
      return new GameResult(player2, playerTwoTotalScore);
    }
    return player1.isFirst() ? new GameResult(player1, playerOneTotalScore) :
           new GameResult(player2, playerTwoTotalScore);
  }
}
