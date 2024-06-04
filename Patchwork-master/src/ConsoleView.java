import java.util.*;

/**
 * A class that represents the ConsoleView, it is in charge of interacting with the user and displaying the UI
 * elements.
 */
public class ConsoleView implements View {
  /**
   * A GameController instance to interact with the game model.
   */
  private GameController gameController;
  /**
   * A Scanner object to read user input.
   */
  private final Scanner scr;

  /**
   * Constructor for the ConsoleView class.
   *
   * @param myScr a Scanner object to read user input.
   * @throws NullPointerException if the Scanner object passed is null
   */
  public ConsoleView(Scanner myScr) {
    Objects.requireNonNull(myScr);
    scr = myScr;
  }

  @Override
  public void displayWinnerMessage(GameResult res) {
    Objects.requireNonNull(res);
    System.out.println("Congratulations " + res.winner().getName() + ", you won! Your final score is " + res.score());
  }

  @Override
  public void displayInvalidPlacementMessage() {
    System.out.println("Invalide patch placement");
  }

  @Override
  public void closeView() {
    scr.close();
  }


  @Override
  public void displaySevenBySevenAcquisition(Player player) {
    Objects.requireNonNull(player);
    System.out.println("Congratulations " + player.getName() + ", you completed a 7x7 quiltboard! You'll now receive " +
                       "the special bonus tile");
  }

  @Override
  public Point promptPlayerForPatchPlacementPosition(Player player) {
    Objects.requireNonNull(player);
    System.out.println(
            player.getName() + " please choose where you want to place the patch, if the patch is bigger than one " +
            "piece, the most top left piece of the patch will be the handling point");
    System.out.println("Enter the line and column of the desired placement, between 1 and 9");
    int line = scr.nextInt();
    int column = scr.nextInt();
    return new Point(line, column);
  }

  @Override
  public char promptPlayerPatchManipulationDecision(Player playing) {
    Objects.requireNonNull(playing);
    while (true) {
      System.out.println(
              playing.getName() + ", choose to either rotate, flip, place your patch on your quiltBoard or go " +
              "back to your previous decision r/f/p/q");
      char decision = scr.next().charAt(0);
      switch (decision) {
        case 'r' -> {
          return 'r';
        }
        case 'f' -> {
          return 'f';
        }
        case 'p' -> {
          return 'p';
        }
        case 'q' -> {
          return 'q';
        }
        default -> System.out.println("unrecognized choice");
      }
    }
  }

  @Override
  public Optional<Patch> promptPlayerPatchDecision(Player playing, Patches patches) {
    Objects.requireNonNull(playing);
    Objects.requireNonNull(patches);
    while (true) {
      System.out.println(
              playing.getName() + ", choose which patch you want to buy or go back on your previous decision " +
              "1/2/3/q");
      char decision = scr.next().charAt(0);
      switch (decision) {
        case '1', '2', '3' -> {
          // Modulo if the neutral pawn index gets out of the list
          Patch chosen = patches.getPatchesList().get(
                  (patches.getNeutralPawnIndex() + Integer.parseInt(String.valueOf(decision)) - 1) %
                  patches.getPatchesList().size());
          if (playing.purchasePossible(chosen)) {
            return Optional.of(chosen);
          }
          System.out.println("You can't afford this patch " + playing.getName() + ", please choose another or go back" +
                             " to the previous decision");
        }
        case 'q' -> {
          return Optional.empty();
        }
        default -> System.out.println("Unrecognised choice");
      }
    }
  }

  @Override
  public void quiltBoardFullyPatchedMessage(Player player) {
    Objects.requireNonNull(player);
    System.out.println(player.getName() + ", your quiltboard is fully patched, you cannot place this patch anywhere");
  }

  @Override
  public void displaySpecialPatchAcquisition(Player playing) {
    Objects.requireNonNull(playing);
    System.out.println(playing.getName() + ", you stumbled upon a \033[1mspecial\033[0m 1x1 patch and now you get to" +
                       " place it to patch a hole in your quiltboard");
  }

  @Override
  public void displayQuiltBoardIncomeMessage(Player playing) {
    Objects.requireNonNull(playing);
    System.out.println(playing.getName() + ", you stumbled upon a \033[1mbutton\033[0m and now, your income you " +
                       "shall receive.");
    System.out.println("You receive " + playing.getQuiltBoard().getTotalIncome() + " buttons.");
  }

  @Override
  public void displayPlayerNewStatusAfterAdvancing(Player player) {
    Objects.requireNonNull(player);
    // Bold
    System.out.println(
            player.getName() + ", you \033[1madvanced.\033[0m You now have " + player.getButtons() + " Buttons.");
  }

  @Override
  public char promptPlayerTurnDecision(Player player) {
    Objects.requireNonNull(player);
    System.out.println(player.getName() + ", would you like to choose a patch from the patches laid in front of you " +
                       "or advance until you overtake your opponent and receive that many buttons ?");
    System.out.printf("\033[1mYou have %d button/s.\033[0m\n", player.getButtons());
    return Main.getValidOptions("choose", "advance", scr);
  }

  @Override
  public void playerTurnStartMessage(Player playing) {
    Objects.requireNonNull(playing);
    System.out.println(playing.getName() + " it's your turn, your time to shine baby");
  }

  @Override
  public void setController(GameController gameController) {
    Objects.requireNonNull(gameController);
    this.gameController = gameController;
  }

  @Override
  public void displayTimeBoard(TimeBoard timeBoard, Player player1, Player player2) {
    Objects.requireNonNull(timeBoard);
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    // System.out.print(" ● ■ —‾_");
    Point playerOnePos = timeBoard.getSpiralPathCoordinates().get(player1.getPosition());
    Point playerTwoPos = timeBoard.getSpiralPathCoordinates().get(player2.getPosition());
    for (int i = 0; i < timeBoard.getSideLength(); i++) {
      for (int j = 0; j < timeBoard.getSideLength(); j++) {
        if (j == 0) {
          displayLeftSide(new Point(i, j), timeBoard, playerOnePos, playerTwoPos, player1, player2);
        } else if (j == timeBoard.getSideLength() - 1) {
          displayRightSide(new Point(i, j), timeBoard, playerOnePos, playerTwoPos, player1, player2);
        } else {
          displayMiddleSide(new Point(i, j), timeBoard, playerOnePos, playerTwoPos, player1, player2);
        }
      }
      System.out.print("\n");
    }
    System.out.print("\n");
  }

  @Override
  public void displayQuiltBoard(Player player) {
    Objects.requireNonNull(player);
    for (int i = 0; i < player.getQuiltBoard().getQBOARDSIZE(); i++) {
      for (int j = 0; j < player.getQuiltBoard().getQBOARDSIZE(); j++) {
        if (j == player.getQuiltBoard().getQBOARDSIZE() - 1) {
          if (player.getQuiltBoard().getStructure()[i][j] != null) {
            System.out.print("■");
          } else {
            System.out.print(".");
          }
        } else {
          if (player.getQuiltBoard().getStructure()[i][j] != null) {
            System.out.print("■  ");
          } else {
            System.out.print(".  ");
          }
        }
      }
      System.out.print("\n");
    }
    System.out.println("Your current income is " + player.getQuiltBoard().getTotalIncome() + "\n");
  }

  /**
   * Displays the description of a patch given a line of the said patch.
   *
   * @param currentLine current line of the patch
   * @param patch       patch in question
   */
  private static void displayPatchDescription(int currentLine, Patch patch) {
    switch (currentLine) {
      case 0 -> System.out.printf("Price    :%2d", patch.getPrice());
      case 1 -> System.out.printf("TimeCost :%2d", patch.getTimeCost());
      case 2 -> System.out.printf("income   :%2d", patch.getIncome());
      default -> System.out.print("            "); // For the alignement
    }
  }

  /**
   * Displays the current line of the patch
   *
   * @param currentLine the current line of the patch
   * @param patch       the patch being displayed
   * @param line        the current line of the patch
   */
  private static void displayPatchLine(int currentLine, Patch patch, PatchSpace[] line) {
    for (int elementPosition = 0; elementPosition < line.length; elementPosition++) {
      if (line[elementPosition] != null) {
        System.out.print("■ ");
      } else {
        System.out.print("  ");
      }
      if (elementPosition == line.length - 1) {
        displayPatchDescription(currentLine, patch);
      }
    }
  }

  /**
   * Displays all the patches in the list
   *
   * @param selectablePatchesList list of patches that can be selected
   * @param currentLine           the current line of the patches
   */
  private static void displayPatchesLine(List<Patch> selectablePatchesList, int currentLine) {
    for (Patch patch : selectablePatchesList) {
      PatchSpace[] line;
      if (currentLine < patch.getStructure().length) {
        line = patch.getStructure()[currentLine];
      } else {
        line = new PatchSpace[patch.getStructure()[0].length];
      }
      displayPatchLine(currentLine, patch, line);
      System.out.print("  ");
    }
  }

  @Override
  public void displayPatches(Patches patches) {
    Objects.requireNonNull(patches);
    var patchesList = patches.getPatchesList();
    var patchesStartingIndex = patches.getNeutralPawnIndex() % patchesList.size();
    var selectablePatchesList = patchesList.subList(patchesStartingIndex,
                                                    (patchesStartingIndex + 7) % patchesList.size());
    // % patchesList.size() to ensure we never get an index out of range
    int longestPatch = selectablePatchesList.stream().map(elem -> elem.getStructure().length).max(
            Integer::compareTo).orElse(0);

    for (int currentLine = 0; currentLine < Math.max(longestPatch, 3); currentLine++) {
      displayPatchesLine(selectablePatchesList, currentLine);
      System.out.print("\n");
    }
  }

  @Override
  public void displayPatch(Patch patch) {
    Objects.requireNonNull(patch);
    for (int currentLine = 0; currentLine < (Math.max(patch.getStructure().length, 3)); currentLine++) {
      displayPatchesLine(new ArrayList<>(List.of(patch)), currentLine);
      System.out.print("\n");
    }
  }

  /**
   * Check if a point contains a player
   *
   * @param coordinates the point being checked
   * @param player      the player being checked
   * @return true if the point contains the player, false otherwise
   */
  private static boolean containsPlayer(Point coordinates, Point player) {
    return coordinates.equals(player);
  }

  /**
   * Check if a point contains either player
   *
   * @param coordinates  the point being checked
   * @param playerOnePos the first player
   * @param playerTwoPos the second player
   * @return true if the point contains either player, false otherwise
   */
  private static boolean containsPlayers(Point coordinates, Point playerOnePos, Point playerTwoPos) {
    return containsPlayer(coordinates, playerOnePos) || containsPlayer(coordinates, playerTwoPos);
  }

  /**
   * Displays a player on the board
   *
   * @param timeBoard    the game board
   * @param coordinates  the point on the board where the player is located
   * @param playerOnePos the position of the first player
   * @param playerTwoPos the position of the second player
   * @param player1      the first player
   * @param player2      the second player
   * @param template     the template for displaying the player
   */
  private static void displayPlayer(TimeBoard timeBoard, Point coordinates, Point playerOnePos, Point playerTwoPos,
                                    Player player1, Player player2, String template) {
    if (containsPlayer(coordinates, playerOnePos) && containsPlayer(coordinates, playerTwoPos)) {
      if (timeBoard.getPlayerOnTop().equals(player1)) {
        System.out.printf(template, player1.getName().charAt(0));
      } else {
        System.out.printf(template, player2.getName().charAt(0));
      }
    } else if (containsPlayer(coordinates, playerOnePos)) {
      System.out.printf(template, player1.getName().charAt(0));
    } else {
      System.out.printf(template, player2.getName().charAt(0));
    }
  }

  /**
   * Displays an item (patch or button) on the timeboard on the given coordinates if it exists.
   *
   * @param coordinates A point on the timeboard.
   * @param timeBoard   The game board
   * @param template    The template for displaying the item.
   */
  private static void displayItem(Point coordinates, TimeBoard timeBoard, String template) {
    if (timeBoard.spaceContainsPatch(coordinates)) {
      System.out.printf(template, "■");
    } else if (timeBoard.spaceContainsButton(coordinates)) {
      System.out.printf(template, "●");
    } else {
      System.out.printf(template, "_");
    }
  }

  /**
   * Display the player or the item on the timeboard if they existed.
   *
   * @param coordinates    The coordinates of the space of the player/item.
   * @param timeBoard      the game board.
   * @param playerOnePos   the first player's positon.
   * @param playerTwoPos   the second player's position.
   * @param player1        the first player.
   * @param player2        the second player.
   * @param playerTemplate the template for displaying the player.
   * @param itemTemplate   the template for displaying the item.
   */
  private static void displayPlayerAndItem(Point coordinates, TimeBoard timeBoard, Point playerOnePos,
                                           Point playerTwoPos, Player player1, Player player2, String playerTemplate,
                                           String itemTemplate) {
    if (containsPlayers(coordinates, playerOnePos, playerTwoPos)) {
      displayPlayer(timeBoard, coordinates, playerOnePos, playerTwoPos, player1, player2, playerTemplate);
    } else {
      displayItem(coordinates, timeBoard, itemTemplate);
    }
  }

  /**
   * Displays a space on the left side of the timeboard.
   *
   * @param coordinates  The coordinates of the space on the left side of the timeboard.
   * @param timeBoard    The timeboard.
   * @param playerOnePos the first player's positon.
   * @param playerTwoPos the second player's position.
   * @param player1      the first player.
   * @param player2      the second player.
   */
  private static void displayLeftSide(Point coordinates, TimeBoard timeBoard, Point playerOnePos, Point playerTwoPos,
                                      Player player1, Player player2) {
    displayPlayerAndItem(coordinates, timeBoard, playerOnePos, playerTwoPos, player1, player2, "|%s", "|%s");
  }

  /**
   * Displays a space on the right side of the timeboard.
   *
   * @param coordinates  The coordinates of the space on the right side of the timeboard.
   * @param timeBoard    The timeboard.
   * @param playerOnePos the first player's positon.
   * @param playerTwoPos the second player's position.
   * @param player1      the first player.
   * @param player2      the second player.
   */
  private static void displayRightSide(Point coordinates, TimeBoard timeBoard, Point playerOnePos, Point playerTwoPos,
                                       Player player1, Player player2) {
//    if (isEmptySpace(i, j, timeBoard)) {
//      displayPlayerAndItem(i, j, timeBoard, playerOnePos, playerTwoPos, player1, player2, "___%s|", "___%s|");
//      return;
//    }
    displayPlayerAndItem(coordinates, timeBoard, playerOnePos, playerTwoPos, player1, player2, "_|_%s|", "_|_%s|");
  }

  /**
   * Displays a middle space of the timeboard.
   *
   * @param coordinates  The coordinates of the space in the middle of the timeboard.
   * @param timeBoard    The timeboard.
   * @param playerOnePos the first player's positon.
   * @param playerTwoPos the second player's position.
   * @param player1      the first player.
   * @param player2      the second player.
   */
  private static void displayMiddleSide(Point coordinates, TimeBoard timeBoard, Point playerOnePos, Point playerTwoPos,
                                        Player player1, Player player2) {
//    if (isEmptySpace(i, j, timeBoard)) {
//      displayPlayerAndItem(i, j, timeBoard, playerOnePos, playerTwoPos, player1, player2, "__%s", "__%s");
//      return;
//    }
    displayPlayerAndItem(coordinates, timeBoard, playerOnePos, playerTwoPos, player1, player2, "_|%s", "_|%s");
  }
}
