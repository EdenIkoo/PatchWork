import java.util.Optional;

/**
 * An interface whose aim is to be used to interact with the player and display the UI/GUI elements.
 */
public interface View {
  /**
   * Displays the time board, including the positions of the players and the buttons.
   *
   * @param timeBoard the time board to display
   * @param player1   the first player
   * @param player2   the second player
   */
  void displayTimeBoard(TimeBoard timeBoard, Player player1, Player player2);

  /**
   * Displays the specified quiltboard.
   *
   * @param player the player who owns the quiltboard
   */
  void displayQuiltBoard(Player player);

  /**
   * Displays the current list of patches available for purchase.
   *
   * @param patches the patches to display
   */
  void displayPatches(Patches patches);

  /**
   * Sets the game controller for this view.
   *
   * @param gameController the game controller to set
   */
  void setController(GameController gameController);

  /**
   * Displays a message indicating that it is the specified player's turn.
   *
   * @param playing the player whose turn it is
   */
  void playerTurnStartMessage(Player playing);

  /**
   * Prompts the user to make a decision on their turn.
   *
   * @param player the player whose turn it is
   * @return the character corresponding to the user's decision
   */
  char promptPlayerTurnDecision(Player player);

  /**
   * Displays the updated status of the specified player after advancing on the time board.
   *
   * @param player the player whose status to display
   */
  void displayPlayerNewStatusAfterAdvancing(Player player);

  /**
   * Displays a message indicating that the specified player has received a button as income from their quiltboard.
   *
   * @param playing the player who received the income
   */
  void displayQuiltBoardIncomeMessage(Player playing);

  /**
   * Displays a message indicating that the specified player has acquired a special patch.
   *
   * @param playing the player who acquired the special patch
   */
  void displaySpecialPatchAcquisition(Player playing);

  /**
   * Prompts the user to choose a position on their quiltboard where they would like to place a patch.
   *
   * @param player the player who is placing the patch
   * @return a Point object representing the chosen position
   */
  Point promptPlayerForPatchPlacementPosition(Player player);

  /**
   * Prompts the user to choose a patch to purchase from a list of available patches.
   *
   * @param playing the player who is purchasing the patch
   * @param patches the patches available for purchase
   * @return an Optional object containing the chosen patch, or an empty Optional if the user decides to go back
   */
  Optional<Patch> promptPlayerPatchDecision(Player playing, Patches patches);

  /**
   * Displays a message indicating that the player's quiltboard is fully patched and they cannot place any more patches on it.
   *
   * @param player the player whose quiltboard is fully patched
   */
  void quiltBoardFullyPatchedMessage(Player player);

  /**
   * Prompts the user to make a decision on how to manipulate a patch.
   *
   * @param playing the player who is manipulating the patch
   * @return the character corresponding to the user's decision
   */
  char promptPlayerPatchManipulationDecision(Player playing);

  /**
   * Displays the specified patch.
   *
   * @param patch the patch to display
   */
  void displayPatch(Patch patch);

  /**
   * Displays a message indicating that the specified player has completed a 7x7 quiltboard and will receive a reward of 7 buttons.
   *
   * @param player the player who completed the 7x7 quiltboard
   */
  void displaySevenBySevenAcquisition(Player player);

  /**
   * Displays a message indicating the winner of the game and their final score.
   *
   * @param res the game result containing the winner and their score
   */
  void displayWinnerMessage(GameResult res);

  /**
   * Displays a message indicating that the patch placement is invalid.
   */
  void displayInvalidPlacementMessage();

  /**
   * Closes the view
   */
  void closeView();
}
