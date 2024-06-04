import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * A class that contains and controls the intrinsic game logic, separated from any user interaction.
 */
public class GameController {
  private final View view;
  private final Patches patches;
  private final TimeBoard timeBoard;
  private final Player player1;
  private final Player player2;
  private final Map<String, Boolean> choicesMap;

  /**
   * This constructor creates a GameController instance.
   *
   * @param view       A game view, the instance that interacts with the user.
   * @param patches    A Patches instance.
   * @param timeBoard  A Timeboard instance.
   * @param player1    A player instance, representing the first player.
   * @param player2    A player instance, representing the second player.
   * @param choicesMap A Map object representing the user's choices.
   */
  public GameController(View view, Patches patches, TimeBoard timeBoard, Player player1, Player player2,
                        Map<String, Boolean> choicesMap) {
    if (!Stream.of(view, patches, timeBoard, player1, player2, choicesMap).allMatch(Objects::nonNull)) {
      throw new NullPointerException("One or more parameters is null");
    }
    this.view = view;
    this.patches = patches;
    this.timeBoard = timeBoard;
    this.player1 = player1;
    this.player2 = player2;
    this.choicesMap = choicesMap;
    view.setController(this); // linking them both, this might be a power move
  }

  /**
   * A function that determines if the game has ended.
   *
   * @return boolean.
   */
  private boolean gameEnd() {
    // The game ends when both players have run out of time, aka reached the middle space of the timeboard
    return player1.getPosition() >= 60 && player2.getPosition() >= 60;
  }

  /**
   * A function that starts the actions of when a player stumbles upon a button on the timeboard.
   *
   * @param playing The player that's currently playing.
   */
  private void triggerQuiltBoardIncome(Player playing) {
    view.displayQuiltBoardIncomeMessage(playing);
    playing.setButtons(playing.getButtons() + playing.getQuiltBoard().getTotalIncome());
  }

  /**
   * A function that determines if a <strong>user given</strong> point is inside the dimensions of the quiltboard.
   *
   * @param point A Point object.
   * @return A boolean.
   */
  private boolean insideTheQuiltBoard(Point point) {
    return 1 <= point.i() && point.i() <= 9 && 1 <= point.j() && point.j() <= 9;
  }

  /**
   * A function that starts the actions of when a player stumbles upon a special 1x1 patch.
   *
   * @param playing The player that's currently playing.
   */
  private void triggerSpecialPatchAcquisition(Player playing) {
    view.displaySpecialPatchAcquisition(playing);
    view.displayQuiltBoard(playing);
    var position = view.promptPlayerForPatchPlacementPosition(playing);

    while (!insideTheQuiltBoard(position) ||
           playing.getQuiltBoard().getStructure()[position.i() - 1][position.j() - 1] != null) {
      position = view.promptPlayerForPatchPlacementPosition(playing);
    }
    playing.getQuiltBoard().getStructure()[position.i() - 1][position.j() - 1] = new PatchSpace(false,
                                                                                                new Color(110, 46, 36));
  }

  /**
   * Checks if the player has reached the first place on the time board and sets their first place status accordingly.
   *
   * @param advancing the player to check
   * @param other     the other player
   * @return true if the player has reached first place, false otherwise
   */
  private boolean checkIfReachedFirstPlace(Player advancing, Player other) {
    if (advancing.getPosition() == 60 && !other.isFirst()) {
      advancing.setFirst(true);
      return true;
    }
    return false;
  }

  /**
   * Checks if the player has passed a button on the time board and triggers the corresponding quiltboard income if
   * applicable.
   *
   * @param advancing the player to check
   */
  private void passButtonAction(Player advancing) {
    if (timeBoard.getButtonsPos().contains(advancing.getPosition())) {
      triggerQuiltBoardIncome(advancing);
    }
  }

  /**
   * Checks if the player has passed a special patch on the time board and triggers the corresponding special patch
   * acquisition if applicable.
   *
   * @param advancing the player to check
   */
  private void passPatchAction(Player advancing) {
    if (timeBoard.getSpecialPatchesPos().contains(advancing.getPosition())) {
      if (QuiltBoard.fullyPatched(advancing.getQuiltBoard().getStructure())) {
        view.quiltBoardFullyPatchedMessage(advancing);
      } else {
        // First come, first served.
        timeBoard.getSpecialPatchesPos().remove(Integer.valueOf(advancing.getPosition()));
        triggerSpecialPatchAcquisition(advancing);
      }
    }
  }

  /**
   * Checks if the player is occupying the same position on the time board as the other player and sets the top player
   * accordingly.
   *
   * @param advancing the player to check
   * @param other     the other player
   */
  private void occupyingSamePositionAsOtherPlayerAction(Player advancing, Player other) {
    // this function will be used by the patches time cost too so this should always be verified
    if (advancing.getPosition() == other.getPosition()) {
      timeBoard.setPlayerOnTop(advancing);
    }
  }

  /**
   * Advances the player to the specified position on the time board.
   *
   * @param finalDestination the position on the time board to advance to
   * @param advancing        the player to advance
   * @param other            the other player
   */
  public void advanceToPosition(int finalDestination, Player advancing, Player other) {
    Objects.requireNonNull(advancing);
    Objects.requireNonNull(other);

    int initialPosition = advancing.getPosition();
    for (int i = initialPosition; i < finalDestination; i++) {
      advancing.setPosition(advancing.getPosition() + 1);
      if (checkIfReachedFirstPlace(advancing, other)) {
        break; // Stop advancing.
      }
      passButtonAction(advancing);
      passPatchAction(advancing);
    }
    occupyingSamePositionAsOtherPlayerAction(advancing, other);
  }

  /**
   * The function that is in charge of a player's decision if he chooses to advance.
   *
   * @param playing The player that's currently playing.
   * @param other   The other player.
   */
  private void processAdvancingDecision(Player playing, Player other) {
    playing.setButtons(playing.getButtons() + (other.getPosition() - playing.getPosition()) + 1);
    advanceToPosition(other.getPosition() + 1, playing, other);
    view.displayPlayerNewStatusAfterAdvancing(playing);
  }


  /**
   * A function to update the state of the patches list after a patch has been placed.
   *
   * @param placedPatch The patch that has been placed.
   */
  private void updatePatchesListAfterPlacement(Patch placedPatch) {
    // We move the neutral pawn to the chosen patch position in the list
    patches.setNeutralPawnIndex(patches.getPatchesList().indexOf(placedPatch));
    // We remove the patch from the patches list whose index is now at NeutralPawnIndex
    patches.getPatchesList().remove(patches.getNeutralPawnIndex());
    // We reset the neutral pawn index with modulo again, crucial in case the removed patch is the last one in the list
    patches.setNeutralPawnIndex(patches.getNeutralPawnIndex() % patches.getPatchesList().size());
  }

  /**
   * A function to place a patch on a player's quiltboard and update the player's status.
   *
   * @param playing        The player whose quiltboard the patch is being placed on.
   * @param other          The other player in the game.
   * @param placedPatch    The patch being placed.
   * @param placementPoint The user-specified placement point for the patch.
   */
  private void placePatchAndUpdatePlayerStatus(Player playing, Player other, Patch placedPatch, Point placementPoint) {
    playing.getQuiltBoard().placePatch(placementPoint, placedPatch);
    playing.getQuiltBoard().setTotalIncome(playing.getQuiltBoard().getTotalIncome() + placedPatch.getIncome());
    advanceToPosition(playing.getPosition() + placedPatch.getTimeCost(), playing, other);
    playing.setButtons(playing.getButtons() - placedPatch.getPrice());
    view.displayPlayerNewStatusAfterAdvancing(playing);
    view.displayQuiltBoard(playing);
  }

  /**
   * A function to process patch placement decisions for a given player, other player, and patch.
   *
   * @param playing  The player that is placing the patch.
   * @param other    The other player.
   * @param possible The patch being placed.
   * @return true if the patch placement was successful, false otherwise.
   */
  private boolean processPatchPlacementDecision(Player playing, Player other, Patch possible) {
    Point userPlacement = view.promptPlayerForPatchPlacementPosition(playing);
    if (!playing.getQuiltBoard().validUserPlacement(userPlacement, possible)) {
      view.displayInvalidPlacementMessage();
      return false;
    }
    // Patch placement actions
    updatePatchesListAfterPlacement(possible);
    placePatchAndUpdatePlayerStatus(playing, other, possible, userPlacement);
    return true;
  }

  /**
   * A function to process patch manipulation decisions for a given player, other player, and patch.
   *
   * @param playing  The player that is manipulating the patch.
   * @param other    The other player.
   * @param possible The patch being manipulated.
   * @return true if the patch was successfully placed, false otherwise.
   */
  private boolean processPatchManipulationDecision(Player playing, Player other, Patch possible) {
    while (true) {
      char decision = view.promptPlayerPatchManipulationDecision(playing);
      switch (decision) {
        case 'r':
          possible.rotatePatch();
          view.displayPatch(possible);
          break;
        case 'f':
          possible.flipPatch();
          view.displayPatch(possible);
          break;
        case 'p':
          if (!processPatchPlacementDecision(playing, other, possible)) {
            continue;
          } else {
            return true;
          }
        case 'q':
          return false;
      }
    }
  }

  /**
   * A function that takes care of a player's decision, be it to advance or choose and buy to patch then rotate it.
   *
   * @param playing The player that's currently playing.
   * @param other   The other player.
   */
  private void processPlayerDecision(Player playing, Player other) {
    while (true) {
      char choice = view.promptPlayerTurnDecision(playing);
      if (choice == 'a') {
        processAdvancingDecision(playing, other);
        return;
      }
      var possible = view.promptPlayerPatchDecision(playing, patches);
      if (possible.isEmpty()) {
        continue;
      }
      if (processPatchManipulationDecision(playing, other, possible.get())) {
        return;
      }
    }
  }


  /**
   * A function that determines if the seven by seven bonus piece has not been claimed yet.
   *
   * @param player1 A Player object, the first player.
   * @param player2 A Player object, the second player.
   * @return A boolean.
   */
  private boolean sevenBySevenNotClaimed(Player player1, Player player2) {
    return !player1.hasBonusTile() && !player2.hasBonusTile();
  }

  /**
   * The function that's responsible for all possible events of a player's turn.
   *
   * @param playing The player that's currently playing.
   * @param other   The other player.
   */
  private void managePlayerTurn(Player playing, Player other) {
    view.playerTurnStartMessage(playing);
    view.displayQuiltBoard(playing);
    view.displayPatches(patches);
    processPlayerDecision(playing, other);
    if (choicesMap.get("full")) {
      if (sevenBySevenNotClaimed(playing, other) && playing.getQuiltBoard().containsSevenBySeven()) {
        view.displaySevenBySevenAcquisition(playing);
        playing.setBonusTile(true);
      }
    }
  }

  /**
   * A function that computes whose turn is it out of the two players.
   *
   * @return The player whose turn it is to play.
   */
  public Player whoseTurnIsIt() {
    if ((player1.getPosition() < player2.getPosition()) ||
        (player1.getPosition() == player2.getPosition() && timeBoard.getPlayerOnTop().equals(player1))) {
      return player1;
    }
    return player2;
  }

  /**
   * A function that starts the game, from beginning to end.
   */
  public void startGame() {
    while (!gameEnd()) {
      if (whoseTurnIsIt().equals(player1)) {
        managePlayerTurn(player1, player2);
      } else {
        managePlayerTurn(player2, player1);
      }
      view.displayTimeBoard(timeBoard, player1, player2);
    }
    var res = GameEvaluator.evaluateWinner(player1, player2);
    view.displayWinnerMessage(res);
    view.closeView();
  }
}
