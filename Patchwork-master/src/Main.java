import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class that contains all the necessary variable instanciations and initialisations.
 */
public class Main {

  private static final String GRAPHICAL = "graphical";
  private static final String CONSOLE = "console";
  private static final String FULL = "full";
  private static final String BASE = "base";

  /**
   * Default constructor, creates a main object, silences a warning.
   */
  public Main() {
  }


  /**
   * A function that clears the entire console.
   */
  public static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }


  /**
   * A function that forces the user to input valid options.
   *
   * @param optionA The first option in the form of a full word.
   * @param optionB The second option in the form of a full word.
   * @param scr     A scanner object.
   * @return returns the character representing the option chosen by the user, the character is the first character of
   * optionA or optionB
   */
  public static char getValidOptions(String optionA, String optionB, Scanner scr) {
    Objects.requireNonNull(optionA);
    Objects.requireNonNull(optionB);
    Objects.requireNonNull(scr);
    System.out.println(optionA + " or " + optionB + " option ? " + optionA.charAt(0) + "/" + optionB.charAt(0));
    char mode = scr.next().charAt(0);
    while (mode != optionA.charAt(0) && mode != optionB.charAt(0)) {
      System.out.println("Please choose the " + optionA + " or " + optionB + " option ? " + optionA.charAt(0) + "/" +
                         optionB.charAt(0));
      scr.nextLine(); /* A sort of buffer clearing, in case user decided to put multiple words */
      mode = scr.next().charAt(0);
    }
    scr.nextLine();
    return mode;
  }

  /* Could make a UserChoices class to improve maintainability but there will only ever be two options anyway */

  /**
   * A function that gets the games choices of the user, the UI and the game version.
   *
   * @param myScr A scanner object.
   * @return Returns a map object that maps each choice to its boolean value.
   */
  private static Map<String, Boolean> getUserChoices(Scanner myScr) {
    var map = new HashMap<String, Boolean>();
    map.put(FULL, false);
    map.put(GRAPHICAL, false);
    char version = getValidOptions(BASE, FULL, myScr);
    char display = getValidOptions(CONSOLE, GRAPHICAL, myScr);
    if (version == 'f') {
      map.put(FULL, true);
    }
    if (display == 'g') {
      map.put(GRAPHICAL, true);
    }
    return map;
  }

  /**
   * A function that returns a Patches object based on the user-made choices.
   *
   * @param choices A map representing user choices.
   * @return Patches object.
   * @throws IOException In case the file containing the complex patches is not found.
   */
  private static Patches getPatchesByChoice(Map<String, Boolean> choices) throws IOException {
    var patches = new Patches();
    if (choices.get(FULL)) {
      patches.generatePatches(Path.of("complexPatches.txt"));
    } else {
      patches.generatePatches(); /* The simple patches */
    }
    return patches;
  }


  /**
   * A function that simulates a coin flip.
   *
   * @param player1 The first player.
   * @param player2 The second player.
   * @return returns player1 or player2 at random.
   */
  private static Player flipACoin(Player player1, Player player2) {
    int destiny = ThreadLocalRandom.current().nextInt(1, 2 + 1);
    return (destiny == 1) ? player1 : player2;
  }

  /**
   * A function that returns a Patches object based on the user-made choices.
   *
   * @param choices A map representing user choices.
   * @param player1 The first player.
   * @param player2 The second player.
   * @return TimeBoard object
   */
  private static TimeBoard getTimeBoardByChoice(Map<String, Boolean> choices, Player player1, Player player2) {
    TimeBoard timeBoard;
    if (choices.get(FULL)) {
      timeBoard = new TimeBoard(flipACoin(player1, player2), true);
    } else {
      /* No coins nor 1x1 patches on the timeboard */
      timeBoard = new TimeBoard(flipACoin(player1, player2), false);
    }
    timeBoard.initialiseSpiralPathCoordinates();
    return timeBoard;
  }

  /**
   * A function that returns a View object based on the user-made choices.
   *
   * @param choices A map representing user choices.
   * @return A view object.
   */
  private static View getViewByChoice(Map<String, Boolean> choices, Scanner scr, ApplicationContext context) {
    if (choices.get(GRAPHICAL)) {
      return new GraphicalView(context);
    }
    return new ConsoleView(scr);
  }

  /* This is a shared way of getting users names, can later implement this differently in the two views to have
  different ways of getting the names based on graphical vs console choice. */

  /**
   * A function that promptes the player his name.
   *
   * @param src A scanner object.
   * @return The player's name
   */
  private static String getPlayerName(Scanner src) {
    System.out.println("Enter player name");
    return src.next();
  }

  /**
   * Methode than starts the graphical mode of the game.
   *
   * @param myPatches   The game patches.
   * @param myTimeBoard The game's timeBoard.
   * @param player1     The first player.
   * @param player2     The second player.
   * @param choicesMap  A map object containing the user made choices.
   */
  private static void runGraphicalMode(Patches myPatches, TimeBoard myTimeBoard, Player player1, Player player2,
                                       Map<String, Boolean> choicesMap) {
    Application.run(new Color(214, 183, 154), applicationContext -> {
      var myView = new GraphicalView(applicationContext);
      var myController = new GameController(myView, myPatches, myTimeBoard, player1, player2, choicesMap);
      myView.setController(myController);
      //myController.startGame();
      graphicalModeDemo(myPatches, myTimeBoard, player1, player2, applicationContext, myView);
    });
  }

  /**
   * A method to demonstrate the almost finished graphical mode.
   *
   * @param myPatches          The game patches.
   * @param myTimeBoard        The game's timeBoard.
   * @param player1            The first player.
   * @param player2            The second player.
   * @param applicationContext The context object.
   * @param myView             The view object that will interact with the user (graphical view).
   */
  private static void graphicalModeDemo(Patches myPatches, TimeBoard myTimeBoard, Player player1, Player player2,
                                        ApplicationContext applicationContext, GraphicalView myView) {
    myView.displayTimeBoard(myTimeBoard, player1, player2);
    myView.displayQuiltBoard(player1);
    myView.displayQuiltBoard(player2);
    myView.displayPatches(myPatches);
    for (; ; ) {
      Event event = applicationContext.pollOrWaitEvent(10);
      if (event == null) {  // no event
        continue;
      }
      Event.Action action = event.getAction();
      if (action == Event.Action.KEY_PRESSED || action == Event.Action.KEY_RELEASED) {
        System.out.println("Demo ended");
        applicationContext.exit(0);
        return;
      }
    }
  }

  /**
   * Methode than starts the console mode of the game.
   *
   * @param myScr       The scanner object that will interact with the user.
   * @param myPatches   The game patches.
   * @param myTimeBoard The game's timeBoard.
   * @param player1     The first player.
   * @param player2     The second player.
   * @param choicesMap  A map object containing the user made choices.
   */
  private static void runConsoleMode(Scanner myScr, Patches myPatches, TimeBoard myTimeBoard, Player player1,
                                     Player player2, Map<String, Boolean> choicesMap) {
    var myView = new ConsoleView(myScr);
    var myController = new GameController(myView, myPatches, myTimeBoard, player1, player2, choicesMap);
    myView.setController(myController);
    myController.startGame();
  }

  /**
   * The main entry point to the program.
   *
   * @param args program arguments
   * @throws IOException In case the file containing the complex patches is not found.
   */
  public static void main(String[] args) throws IOException {
    var myScr = new Scanner(System.in);
    var choicesMap = getUserChoices(myScr);
    String player1Name = getPlayerName(myScr);
    String player2Name = getPlayerName(myScr);
    clearScreen();
    var player1 = new Player(5, 0, player1Name, new Color(5, 107, 7), new Color(161, 168, 148));
    var player2 = new Player(5, 0, player2Name, new Color(252, 186, 3), new Color(211, 183, 104));
    var myPatches = getPatchesByChoice(choicesMap);
    var myTimeBoard = getTimeBoardByChoice(choicesMap, player1, player2);
    if (choicesMap.get(GRAPHICAL)) {
      runGraphicalMode(myPatches, myTimeBoard, player1, player2, choicesMap);
      return;
    }
    runConsoleMode(myScr, myPatches, myTimeBoard, player1, player2, choicesMap);
  }
}
