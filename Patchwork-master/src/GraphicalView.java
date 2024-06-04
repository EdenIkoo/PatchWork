import fr.umlv.zen5.ApplicationContext;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An implementation of the View interface that aims to represent a GUI and interact graphically with the user.
 */
public class GraphicalView implements View {

  private GameController gameController;
  private final ApplicationContext context;
  private final float screenWidth;
  private final float screenHeight;

  private static final int TIMEBOARDTILESIZE = 60;
  private static final int TILESMARGIN = 1;
  private static final int TIMEBOARDSPACESIZE = TIMEBOARDTILESIZE - TILESMARGIN * 2;
  private static final int QUILTBOARDTILESIZE = 50;

  private static final int PATCHTILESIZE = QUILTBOARDTILESIZE;
  private static final int QUILTBOARDSPACESIZE = QUILTBOARDTILESIZE - TILESMARGIN * 2;

  private static final int TIMEBOARDOUTERMARGIN = TIMEBOARDTILESIZE / 8;
  private static final int QUILTOARDOUTERMARGIN = QUILTBOARDSPACESIZE / 8;
  private static final float TIMEBOARDSIZE = 8 * TIMEBOARDTILESIZE;
  private static final float QUILTBOARDSIZE = 9 * QUILTBOARDTILESIZE;
  private final float quiltBoardStartingY;
  private final float timeBoardStartingY;

  private final float timeBoardStartingtX;

  private static final Color[] BUTTON_COLORS = {new Color(20, 20, 204), new Color(185, 185, 230)};

  //TODO fix the margin superposition (in the patches that are displayed especially, this will be incredibly difficult),
  // modify drawCentered button so it accepts the length and width of the button.
  // make it so the top right corner of a board INCLUDES THE MARGIN as opposed to the bare board. Make it so that the
  // middle patch of the three selectable patches by the place is displayed exactly in the middle of the screen with
  // the rest of the patches on its left and right side, this will give a rotation effect when a patch is selected.
  // Make it so that a price tag is displayed inside a patch not next to it.
  // Make it so that the graphical display actually fucking works.

  /**
   * Constructor for the GraphicalView
   *
   * @param context the ApplicationContext object
   */
  public GraphicalView(ApplicationContext context) {
    Objects.requireNonNull(context);
    this.context = context;
    screenWidth = context.getScreenInfo().getWidth();
    screenHeight = context.getScreenInfo().getHeight();
    quiltBoardStartingY = screenHeight / 15;
    timeBoardStartingY = screenHeight / 15;
    timeBoardStartingtX = screenWidth / 2 - TIMEBOARDSIZE / 2;
  }

  @Override
  public void setController(GameController gameController) {
    Objects.requireNonNull(gameController);
    this.gameController = gameController;
  }

  @Override
  public void playerTurnStartMessage(Player playing) {

  }

  @Override
  public char promptPlayerTurnDecision(Player player) {
    return 0;
  }

  @Override
  public void displayPlayerNewStatusAfterAdvancing(Player player) {

  }

  @Override
  public void displayQuiltBoardIncomeMessage(Player playing) {

  }

  @Override
  public void displaySpecialPatchAcquisition(Player playing) {

  }

  @Override
  public Point promptPlayerForPatchPlacementPosition(Player player) {
    return null;
  }

  @Override
  public Optional<Patch> promptPlayerPatchDecision(Player playing, Patches patches) {
    return Optional.empty();
  }

  @Override
  public void quiltBoardFullyPatchedMessage(Player player) {

  }

  @Override
  public char promptPlayerPatchManipulationDecision(Player playing) {
    return 0;
  }

  @Override
  public void displayPatch(Patch patch) {

  }

  @Override
  public void displaySevenBySevenAcquisition(Player player) {

  }

  @Override
  public void displayWinnerMessage(GameResult res) {

  }

  @Override
  public void displayInvalidPlacementMessage() {

  }

  @Override
  public void closeView() {

  }

  /**
   * set the position of patches on the screen
   *
   * @param patches the patches which needs to be drawn
   */
  private void setPatchesPositions(Patches patches) {
    float lowestBoardY = Math.max(timeBoardStartingY + TIMEBOARDSIZE + TIMEBOARDOUTERMARGIN,
                                  quiltBoardStartingY + QUILTBOARDSIZE + QUILTOARDOUTERMARGIN);
    float maxPatchLength = 5 * PATCHTILESIZE;
    float patchesY = (lowestBoardY + screenHeight) / 2 - maxPatchLength / 2;

    float patchesStartingX = QUILTBOARDTILESIZE * 1.2F;
    List<Patch> patchesList = patches.getPatchesList();
    for (int i = 0; i < patches.getPatchesList().size(); i++) {
      Patch currentPatch = patchesList.get((patches.getNeutralPawnIndex() + i) % patchesList.size());
      currentPatch.setY(patchesY);
      currentPatch.setX(patchesStartingX);
      patchesStartingX += currentPatch.getStructure()[0].length * PATCHTILESIZE + PATCHTILESIZE * 2;
    }
  }


  /**
   * Draw all the patches on the screen
   *
   * @param graphics2D Graphics2D object used for drawing
   * @param patches    the patches which needs to be drawn
   */
  private void drawPatches(Graphics2D graphics2D, Patches patches) {
    setPatchesPositions(patches);
    List<Patch> patchesList = patches.getPatchesList();
    for (Patch patch : patchesList) {
      drawPatch(graphics2D, patch);
    }
  }

  /**
   * Draws the hourglass shape on the screen using arguments of type float
   *
   * @param g2d        Graphics2D object used for drawing
   * @param width      width of the hourglass
   * @param length     length of the hourglass
   * @param startX     x-coordinate of the starting point
   * @param startY     y-coordinate of the starting point
   * @param waistSize  size of the waist of the hourglass
   * @param colorUpper color of the upper part of the hourglass
   * @param colorLower color of the lower part of the hourglass
   * @param drawUpper  if upper part of the hourglass needs to be drawn
   * @param drawLower  if lower part of the hourglass needs to be drawn
   */
  private static void drawHourglass(Graphics2D g2d, float width, float length, float startX, float startY,
                                    float waistSize, Color colorUpper, Color colorLower, boolean drawUpper,
                                    boolean drawLower) {
    drawHourglass(g2d, Math.round(width), Math.round(length), Math.round(startX), Math.round(startY),
                  Math.round(waistSize), colorUpper, colorLower, drawUpper, drawLower);
  }

  /**
   * Draws the hourglass shape on the screen
   *
   * @param g2d        Graphics2D object used for drawing
   * @param width      width of the hourglass
   * @param length     length of the hourglass
   * @param startX     x-coordinate of the starting point
   * @param startY     y-coordinate of the starting point
   * @param waistSize  size of the waist of the hourglass
   * @param colorUpper color of the upper part of the hourglass
   * @param colorLower color of the lower part of the hourglass
   * @param drawUpper  if upper part of the hourglass needs to be drawn
   * @param drawLower  if lower part of the hourglass needs to be drawn
   */
  private static void drawHourglass(Graphics2D g2d, int width, int length, int startX, int startY, int waistSize,
                                    Color colorUpper, Color colorLower, boolean drawUpper, boolean drawLower) {
    int[] xPointsUpper = {startX, startX + width / 2 - waistSize / 2, startX + width / 2 + waistSize / 2,
                          startX + width};
    int[] yPointsUpper = {startY, startY + length / 2, startY + length / 2, startY};
    int[] xPointsLower = {startX + width / 2 - waistSize / 2, startX, startX + width,
                          startX + width / 2 + waistSize / 2};
    int[] yPointsLower = {startY + length / 2, startY + length, startY + length, startY + length / 2};
    int nPoints = 4;
    if (drawUpper) {
      g2d.setColor(colorUpper);
      g2d.fillPolygon(xPointsUpper, yPointsUpper, nPoints);
    }
    if (drawLower) {
      g2d.setColor(colorLower);
      g2d.fillPolygon(xPointsLower, yPointsLower, nPoints);
    }

  }

  /**
   * Draws a price tag on the screen
   *
   * @param graphics2D     Graphics2D object used for drawing
   * @param patch          the patch for which the price tag needs to be drawn
   * @param rectangleX     x-coordinate of the rectangle
   * @param rectangleY     y
   * @param rectangleWidth width of the rectangle
   * @param priceTagColor  color of the price tag
   */
  private void drawPriceTag(Graphics2D graphics2D, Patch patch, float rectangleX, float rectangleY,
                            float rectangleWidth, Color priceTagColor) {
    float tagWidth = rectangleWidth / 1.4F;
    float tagHeight = rectangleWidth * 2.3F;
    Color buttonColor = new Color(105, 105, 254);
    float tagStartingX = rectangleX + rectangleWidth / 2 - tagWidth / 2;
    float tagStartingY = rectangleY + rectangleWidth / 4;

    // Draw the price tag
    drawPriceTagSilhouette(graphics2D, rectangleY, priceTagColor, tagWidth, tagHeight, tagStartingX, tagStartingY);

    // Draw the price
    float buttonY = drawPatchPriceOnPriceTag(graphics2D, patch, rectangleWidth, priceTagColor, tagWidth, buttonColor,
                                             tagStartingX, tagStartingY);

    // Draw the time cost
    float timeCostY = drawPatchTimeCostOnPriceTag(graphics2D, patch, rectangleWidth, tagWidth, tagStartingX, buttonY);

    // Draw the hourglass
    drawHourGlassOnPriceTag(graphics2D, rectangleWidth, priceTagColor, tagWidth, tagStartingX, timeCostY);
  }

  /**
   * Draws the hourglass on the price tag.
   *
   * @param graphics2D     the Graphics2D object to draw on
   * @param rectangleWidth width of the rectangle
   * @param priceTagColor  color of the price tag
   * @param tagWidth       width of the tag
   * @param tagStartingX   starting x-coordinate of the tag
   * @param timeCostY      y-coordinate of the time cost text
   */
  private static void drawHourGlassOnPriceTag(Graphics2D graphics2D, float rectangleWidth, Color priceTagColor,
                                              float tagWidth, float tagStartingX, float timeCostY) {
    float hourGlassWidth = tagWidth / 2;
    float hourGlassHeight = tagWidth / 2 * 1.26F;
    float hourGlassMargin = hourGlassWidth / 3;
    drawHourglass(graphics2D, hourGlassWidth, hourGlassHeight, tagStartingX + tagWidth / 2 - hourGlassWidth / 2,
                  timeCostY + rectangleWidth / 6, hourGlassWidth / 2, Color.BLACK, Color.BLACK, true, true);
    drawHourglass(graphics2D, hourGlassWidth - hourGlassMargin, hourGlassHeight - hourGlassMargin,
                  tagStartingX + tagWidth / 2 - hourGlassWidth / 2 + hourGlassMargin / 2,
                  timeCostY + rectangleWidth / 6 + hourGlassMargin / 2, hourGlassWidth / 2 - hourGlassMargin,
                  priceTagColor, priceTagColor, true, true);
  }

  /**
   * Draws the time cost of the patch on the price tag.
   *
   * @param graphics2D     the Graphics2D object to draw on
   * @param patch          the patch whose time cost is to be drawn
   * @param rectangleWidth width of the rectangle
   * @param tagWidth       width of the tag
   * @param tagStartingX   starting x-coordinate of the tag
   * @param buttonY        y-coordinate of the button
   * @return the y-coordinate of the time cost text
   */
  private static float drawPatchTimeCostOnPriceTag(Graphics2D graphics2D, Patch patch, float rectangleWidth,
                                                   float tagWidth, float tagStartingX, float buttonY) {
    float timeCostX = tagStartingX + tagWidth / 2 -
                      graphics2D.getFontMetrics().stringWidth(Integer.toString(patch.getTimeCost())) / 2.0F;
    float timeCostY = buttonY + rectangleWidth + rectangleWidth / 15;
    graphics2D.setColor(Color.BLACK);
    graphics2D.drawString(Integer.toString(patch.getTimeCost()), timeCostX, timeCostY);
    return timeCostY;
  }

  /**
   * Draws the price of the patch on the price tag.
   *
   * @param graphics2D     the Graphics2D object to draw on
   * @param patch          the patch whose price is to be drawn
   * @param rectangleWidth width of the rectangle
   * @param priceTagColor  color of the price tag
   * @param tagWidth       width of the tag
   * @param buttonColor    color of the button
   * @param tagStartingX   starting x-coordinate of the tag
   * @param tagStartingY   starting y-coordinate of the tag
   * @return the y-coordinate of the button
   */
  private float drawPatchPriceOnPriceTag(Graphics2D graphics2D, Patch patch, float rectangleWidth, Color priceTagColor,
                                         float tagWidth, Color buttonColor, float tagStartingX, float tagStartingY) {
    graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, Math.round(tagWidth / 2)));
    float priceX = tagStartingX + tagWidth / 2 -
                   graphics2D.getFontMetrics().stringWidth(Integer.toString(patch.getPrice())) / 2.0F;
    float priceY = tagStartingY + graphics2D.getFontMetrics().getAscent();
    float buttonY = priceY + rectangleWidth / 15;
    graphics2D.setColor(buttonColor);
    graphics2D.drawString(Integer.toString(patch.getPrice()), priceX, priceY);
    drawCenteredButton(graphics2D, tagStartingX, buttonY, tagWidth, tagWidth, buttonColor, priceTagColor, buttonColor);
    return buttonY;
  }

  /**
   * Draws the silhouette of the price tag.
   *
   * @param graphics2D    the Graphics2D object to draw on
   * @param rectangleY    starting y-coordinate of the rectangle
   * @param priceTagColor color of the price tag
   * @param tagWidth      width of the tag
   * @param tagHeight     height of the tag
   * @param tagStartingX  starting x-coordinate of the tag
   * @param tagStartingY  starting y-coordinate of the tag
   */
  private static void drawPriceTagSilhouette(Graphics2D graphics2D, float rectangleY, Color priceTagColor,
                                             float tagWidth, float tagHeight, float tagStartingX, float tagStartingY) {
    final int margin = 1;
    final var topTagRightCorner = Math.round(tagStartingX + tagWidth * 2 / 3);
    final var topLeftCorner = Math.round(tagStartingX + tagWidth * 1 / 3);
    final var bottomCornersY = Math.round(tagStartingY + tagHeight);
    final var topCornerX = Math.round(tagStartingX + tagWidth);

    int[] xPointsTag = {Math.round(tagStartingX), Math.round(tagStartingX), topCornerX, topCornerX, topTagRightCorner,
                        topLeftCorner};
    int[] yPointsTag = {Math.round(tagStartingY), bottomCornersY, bottomCornersY, Math.round(tagStartingY),
                        Math.round(rectangleY), Math.round(rectangleY)};
    graphics2D.setColor(Color.BLACK);
    graphics2D.fillPolygon(xPointsTag, yPointsTag, 6);

    int[] xPointsTagInner = {Math.round(tagStartingX) + margin, Math.round(tagStartingX) + margin, topCornerX - margin,
                             topCornerX - margin, topTagRightCorner - margin, topLeftCorner + margin};
    int[] yPointsTagInner = {Math.round(tagStartingY) + margin, bottomCornersY - margin, bottomCornersY - margin,
                             Math.round(tagStartingY) + margin, Math.round(rectangleY) + margin,
                             Math.round(rectangleY) + margin};
    graphics2D.setColor(priceTagColor);
    graphics2D.fillPolygon(xPointsTagInner, yPointsTagInner, 6);
  }


  /**
   * This method is used to draw a patch of a quilt on the screen. It loops through the structure of the patch, and for
   * each non-null space it calls the method drawPatchSpace() to draw the space and if the space is a button it calls
   * the method drawCenteredButton() to draw the button.
   *
   * @param graphics2D The graphics object used to draw the patch on the screen.
   * @param patch      The patch object that represents the patch to be drawn on the screen.
   */
  private void drawPatch(Graphics2D graphics2D, Patch patch) {
    for (int i = 0; i < patch.getStructure().length; i++) {
      for (int j = 0; j < patch.getStructure()[0].length; j++) {
        if (patch.getStructure()[i][j] != null) {
          float subPatchX = patch.getX() + PATCHTILESIZE * j;
          float subPatchY = patch.getY() + PATCHTILESIZE * i;
          drawPatchSpace(graphics2D, patch, subPatchX, subPatchY);
          if (patch.getStructure()[i][j].button()) {
            drawCenteredButton(graphics2D, subPatchX, subPatchY, PATCHTILESIZE, PATCHTILESIZE, BUTTON_COLORS[0],
                               BUTTON_COLORS[1], Color.BLACK);
          }
        }
      }
    }
    drawPriceTag(graphics2D, patch, patch.getX() + patch.getStructure()[0].length * PATCHTILESIZE, patch.getY(),
                 PATCHTILESIZE, darkerColor(new Color(254, 251, 234), 10));
  }


  /**
   * Draws a single patch space, including the black border and the color inside.
   *
   * @param graphics2D The Graphics2D object to draw the patch space on.
   * @param patch      The patch that the space is a part of.
   * @param subPatchX  The x position of the patch space.
   * @param subPatchY  The y position of the patch space.
   */
  private void drawPatchSpace(Graphics2D graphics2D, Patch patch, float subPatchX, float subPatchY) {
    graphics2D.setColor(Color.BLACK);
    graphics2D.fill(new Rectangle2D.Float(subPatchX, subPatchY, PATCHTILESIZE, PATCHTILESIZE));
    // TODO saturation issue, colors are too saturated, change colors in complexPatches.txt file.
    graphics2D.setColor(darkerColor(patch.getColor(), 25));
    graphics2D.fill(
            new Rectangle2D.Float(subPatchX + TILESMARGIN, subPatchY + TILESMARGIN, PATCHTILESIZE - TILESMARGIN * 2,
                                  PATCHTILESIZE - TILESMARGIN * 2));
  }

  @Override
  public void displayPatches(Patches patches) {
    context.renderFrame(graphics2D -> drawPatches(graphics2D, patches));

  }

  /**
   * Returns the x-coordinate of the left edge of the quilt board on the screen.
   *
   * @param quiltBoard The QuiltBoard object that holds information about the quilt board
   * @return The x-coordinate of the left edge of the quilt board on the screen
   */
  private float quiltBoardStartingX(QuiltBoard quiltBoard) {
    if (quiltBoard.getOwner() == 1) {
      return QUILTBOARDTILESIZE + QUILTOARDOUTERMARGIN;
    }
    return screenWidth - QUILTBOARDSIZE - QUILTBOARDTILESIZE - QUILTOARDOUTERMARGIN * 2;
  }

  /**
   * Draws a quilt board on the screen using the Graphics2D object and a Player object
   *
   * @param graphics2D The Graphics2D object used to draw the quilt board
   * @param player     The player object that holds information about the quilt board
   */
  private void drawQuiltBoard(Graphics2D graphics2D, Player player) {
    QuiltBoard playersQuiltBoard = player.getQuiltBoard();
    float quiltBoardLeftX = quiltBoardStartingX(playersQuiltBoard);
    graphics2D.setColor(darkerColor(playersQuiltBoard.getColor(), 30));
    graphics2D.fill(
            new Rectangle2D.Float(quiltBoardLeftX - QUILTOARDOUTERMARGIN, quiltBoardStartingY - QUILTOARDOUTERMARGIN,
                                  QUILTBOARDSIZE + QUILTOARDOUTERMARGIN * 2,
                                  QUILTBOARDSIZE + QUILTOARDOUTERMARGIN * 2));
    writeQuiltBoardInfo(graphics2D, player, quiltBoardLeftX, darkerColor(playersQuiltBoard.getColor(), 70));

    drawQuiltBoardTiles(graphics2D, playersQuiltBoard, quiltBoardLeftX);

  }

  /**
   * Draws the quilt board tiles on the screen using the Graphics2D object, the QuiltBoard object and the Color object
   *
   * @param graphics2D        The Graphics2D object used to draw the quilt board tiles
   * @param playersQuiltBoard The QuiltBoard object that holds information about the quilt board
   * @param quiltBoardLeftX   The x-coordinate of the left edge of the quilt board on the screen
   */
  private void drawQuiltBoardTiles(Graphics2D graphics2D, QuiltBoard playersQuiltBoard, float quiltBoardLeftX) {
    for (int i = 0; i < playersQuiltBoard.getQBOARDSIZE(); i++) {
      for (int j = 0; j < playersQuiltBoard.getQBOARDSIZE(); j++) {
        float patchX = quiltBoardLeftX + j * QUILTBOARDTILESIZE;
        float patchY = quiltBoardStartingY + i * QUILTBOARDTILESIZE;
        graphics2D.setColor(Color.BLACK);
        graphics2D.fill(new Rectangle2D.Float(patchX, patchY, QUILTBOARDTILESIZE, QUILTBOARDTILESIZE));
        if (detectAndDrawPatchSpace(graphics2D, playersQuiltBoard, i, j, patchX, patchY)) continue;
        graphics2D.setColor(playersQuiltBoard.getColor());
        graphics2D.fill(new Rectangle2D.Float(patchX + TILESMARGIN, patchY + TILESMARGIN, QUILTBOARDSPACESIZE,
                                              QUILTBOARDSPACESIZE));
      }
    }
  }

  private boolean detectAndDrawPatchSpace(Graphics2D graphics2D, QuiltBoard playersQuiltBoard, int i, int j,
                                          float patchX, float patchY) {
    if (playersQuiltBoard.getStructure()[i][j] != null) {
      graphics2D.setColor(playersQuiltBoard.getStructure()[i][j].color());
      graphics2D.fill(new Rectangle2D.Float(patchX + TILESMARGIN, patchY + TILESMARGIN, QUILTBOARDSPACESIZE,
                                            QUILTBOARDSPACESIZE));
      drawButtonOnPatchSpace(graphics2D, playersQuiltBoard, i, j, patchX, patchY);
      return true;
    }
    return false;
  }

  private void drawButtonOnPatchSpace(Graphics2D graphics2D, QuiltBoard playersQuiltBoard, int i, int j, float patchX,
                                      float patchY) {
    if (playersQuiltBoard.getStructure()[i][j].button()) {
      drawCenteredButton(graphics2D, patchX, patchY, QUILTBOARDTILESIZE, QUILTBOARDTILESIZE, BUTTON_COLORS[0],
                         BUTTON_COLORS[1], Color.BLACK);
    }
  }

  /**
   * Writes the quiltboard info on the screen using the Graphics2D object, the Player object and the Color object
   *
   * @param graphics2D      The Graphics2D object used to write the quilt board characteristics
   * @param player          The player object that holds information about the quilt board
   * @param quiltBoardLeftX The x-coordinate of the left edge of the quilt board on the screen
   * @param color           The color of the text for the quilt board characteristics
   */
  private void writeQuiltBoardInfo(Graphics2D graphics2D, Player player, float quiltBoardLeftX, Color color) {
    graphics2D.setColor(color);
    graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, QUILTBOARDTILESIZE / 2));
    float ownerX = quiltBoardLeftX - QUILTOARDOUTERMARGIN + (QUILTBOARDSIZE + QUILTOARDOUTERMARGIN * 2) / 2 -
                   graphics2D.getFontMetrics().stringWidth(player.getName() + "'s Quiltboard") / 2.0F;
    float ownerY = quiltBoardStartingY - QUILTOARDOUTERMARGIN - 5;
    graphics2D.drawString(player.getName() + "'s Quiltboard", ownerX, ownerY);
    float incomeY = quiltBoardStartingY - QUILTOARDOUTERMARGIN + (QUILTBOARDSIZE + QUILTOARDOUTERMARGIN * 2) +
                    graphics2D.getFontMetrics().getAscent();
    float incomeX = quiltBoardLeftX - QUILTOARDOUTERMARGIN;
    graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, Math.round(QUILTBOARDTILESIZE / 2.5F)));
    graphics2D.drawString("income : " + player.getQuiltBoard().getTotalIncome() + "   Buttons : " + player.getButtons(),
                          incomeX, incomeY);
  }

  @Override
  public void displayQuiltBoard(Player player) {
    context.renderFrame(graphics2D -> drawQuiltBoard(graphics2D, player));
  }


  /**
   * Draws a generic space using the Graphics2D object, a given x and y coordinate, a size, and a color.
   *
   * @param graphics2D The Graphics2D object used to draw the space
   * @param x          The x-coordinate of the space
   * @param y          The y-coordinate of the space
   * @param size       The size of the space
   * @param color      The color of the space
   */
  private void drawGenericSpace(Graphics2D graphics2D, float x, float y, int size, Color color) {
    graphics2D.setColor(color);
    graphics2D.fill(new Rectangle2D.Float(x, y, size, size));
  }

  /**
   * Returns a new Color object that is a lighter version of the input color by the given amount.
   *
   * @param color The color to be lightened
   * @param i     The amount by which the color will be lightened
   * @return A new Color object that is a lighter version of the input color
   */
  private Color lighterColor(Color color, int i) {
    return new Color(Math.min(255, color.getRed() + i), Math.min(255, color.getGreen() + i),
                     Math.min(255, color.getBlue() + i));
  }

  /**
   * Returns a new Color object that is a darker version of the input color by the given amount.
   *
   * @param color The color to be darkened
   * @param i     The amount by which the color will be darkened
   * @return A new Color object that is a darker version of the input color
   */
  private Color darkerColor(Color color, int i) {
    return new Color(Math.max(0, color.getRed() - i), Math.max(0, color.getGreen() - i),
                     Math.max(0, color.getBlue() - i));
  }

  private void writeTimeBoardInfo(Graphics2D graphics2D, Player playing, Color marginColor) {
    graphics2D.setColor(marginColor);
    graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, TIMEBOARDTILESIZE / 2));
    float whoseTurnX = timeBoardStartingtX - TIMEBOARDOUTERMARGIN + (TIMEBOARDSIZE + TIMEBOARDOUTERMARGIN * 2) / 2 -
                       graphics2D.getFontMetrics().stringWidth(playing.getName() + "'s Turn") / 2.0F;
    float whoseTurnY = timeBoardStartingY - TIMEBOARDOUTERMARGIN - 5;
    graphics2D.drawString(playing.getName() + "'s turn", whoseTurnX, whoseTurnY);
  }

  /**
   * Draws the time board using the Graphics2D object, a TimeBoard object, and two Player objects.
   *
   * @param graphics2D The Graphics2D object used to draw the time board
   * @param timeBoard  The TimeBoard object that holds information about the time board
   * @param player1    The first Player object
   * @param player2    The second Player object
   */
  private void drawTimeBoard(Graphics2D graphics2D, TimeBoard timeBoard, Player player1, Player player2) {
    Color marginColor = new Color(102, 106, 154);
    graphics2D.setColor(marginColor);
    graphics2D.fill(
            new Rectangle2D.Float(timeBoardStartingtX - TIMEBOARDOUTERMARGIN, timeBoardStartingY - TIMEBOARDOUTERMARGIN,
                                  TIMEBOARDSIZE + TIMEBOARDOUTERMARGIN * 2, TIMEBOARDSIZE + TIMEBOARDOUTERMARGIN * 2));
    writeTimeBoardInfo(graphics2D, gameController.whoseTurnIsIt(), darkerColor(marginColor, 50));

    Color[] tileColors = {new Color(170, 150, 39), new Color(139, 114, 51)};
    Color[] patchColors = {new Color(110, 46, 36), new Color(110, 39, 29)};
    Color voidSpaceColor = new Color(230, 95, 71);

    drawTimeBoardTiles(graphics2D, timeBoard, player1, player2, tileColors, patchColors, voidSpaceColor);
  }

  /**
   * Draws the tiles of the time board using the Graphics2D object, a TimeBoard object, two Player objects, a starting x
   * and y coordinate, arrays of colors for the tiles, patches, and buttons, and a color for void spaces.
   *
   * @param graphics2D     The Graphics2D object used to draw the time board tiles
   * @param timeBoard      The TimeBoard object that holds information about the time board
   * @param player1        The first Player object
   * @param player2        The second Player object
   * @param tileColors     An array of colors for the tiles
   * @param patchColors    An array of colors for the patches
   * @param voidSpaceColor The color for void spaces
   */
  private void drawTimeBoardTiles(Graphics2D graphics2D, TimeBoard timeBoard, Player player1, Player player2,
                                  Color[] tileColors, Color[] patchColors, Color voidSpaceColor) {
    for (int position = 0; position < timeBoard.getSpiralPathCoordinates().size(); position++) {
      float tileX =
              timeBoardStartingtX + timeBoard.getSpiralPathCoordinates().get(position).column() * TIMEBOARDTILESIZE;
      float tileY = timeBoardStartingY + timeBoard.getSpiralPathCoordinates().get(position).line() * TIMEBOARDTILESIZE;
      graphics2D.setColor(Color.BLACK);
      graphics2D.fill(new Rectangle2D.Float(tileX, tileY, TIMEBOARDTILESIZE, TIMEBOARDTILESIZE));
      drawPlayerAndItem(graphics2D, timeBoard, lighterColor(tileColors[position % 2], position),
                        patchColors[position % 2], voidSpaceColor, position, tileX + TILESMARGIN, tileY + TILESMARGIN,
                        player1, player2);
    }
  }

  /**
   * Determines whether the input position contains both of the input players.
   *
   * @param position The position to check
   * @param player1  The first player
   * @param player2  The second player
   * @return true if the input position contains both of the input players, false otherwise
   */
  private boolean containsPlayers(int position, Player player1, Player player2) {
    return player1.getPosition() == position || player2.getPosition() == position;
  }

  /**
   * Determines whether the input position contains the input player.
   *
   * @param position The position to check
   * @param player   The player to check for
   * @return true if the input position contains the input player, false otherwise
   */
  private boolean containsPlayer(int position, Player player) {
    return player.getPosition() == position;
  }

  /**
   * Given a TimeBoard object, two Player objects, and a position, returns the Player object that should be drawn on top
   * at that position.
   *
   * @param timeBoard The TimeBoard object that holds information about the players' positions
   * @param player1   The first Player object
   * @param player2   The second Player object
   * @param position  The position to check
   * @return The Player object that should be drawn on top at the input position
   */
  private Player whichPlayerToDraw(TimeBoard timeBoard, Player player1, Player player2, int position) {
    if (containsPlayer(position, player1) && containsPlayer(position, player2)) {
      if (timeBoard.getPlayerOnTop().equals(player1)) {
        return player1;
      }
      return player2;
    }
    if (containsPlayer(position, player1)) {
      return player1;
    }
    return player2;
  }

  /**
   * Draws a player using the Graphics2D object, a Player object, and x and y coordinates.
   *
   * @param graphics2D The Graphics2D object used to draw the player
   * @param player     The Player object that holds information about the player
   * @param tileX      The x-coordinate of the player
   * @param tileY      The y-coordinate of the player
   */
  private void drawPlayer(Graphics2D graphics2D, Player player, float tileX, float tileY) {
    drawCenteredEllipse(graphics2D, darkerColor(player.getColor(), 25), tileX, tileY, TIMEBOARDSPACESIZE / 1.4F,
                        TIMEBOARDSPACESIZE / 1.4F, TIMEBOARDSPACESIZE, TIMEBOARDSPACESIZE);
    drawCenteredEllipse(graphics2D, player.getColor(), tileX - (TIMEBOARDSPACESIZE / 1.4F) / 10,
                        tileY - (TIMEBOARDSPACESIZE / 1.4F) / 10, TIMEBOARDSPACESIZE / 1.4F, TIMEBOARDSPACESIZE / 1.4F,
                        TIMEBOARDSPACESIZE, TIMEBOARDSPACESIZE);
  }

  /**
   * Draws a player and an item (e.g. patch, button, void space) on a tile of the time board using the Graphics2D
   * object, a TimeBoard object, and two Player objects, along with arrays of colors for the tile, patch, button, and
   * void space.
   *
   * @param graphics2D     The Graphics2D object used to draw the player and item
   * @param timeBoard      The TimeBoard object that holds information about the time board
   * @param tileColor      The color of the tile
   * @param patchColor     The color of the patch
   * @param voidSpaceColor The color of the void space
   * @param position       The position of the tile on the time board
   * @param tileX          The x-coordinate of the tile
   * @param tileY          The y-coordinate of the tile
   * @param player1        The first Player object
   * @param player2        The second Player object
   */
  private void drawPlayerAndItem(Graphics2D graphics2D, TimeBoard timeBoard, Color tileColor, Color patchColor,
                                 Color voidSpaceColor, int position, float tileX, float tileY, Player player1,
                                 Player player2) {
    drawItem(graphics2D, timeBoard, tileColor, patchColor, voidSpaceColor, position, tileX, tileY);
    if (containsPlayers(position, player1, player2)) {
      drawPlayer(graphics2D, whichPlayerToDraw(timeBoard, player1, player2, position), tileX, tileY);
    }
  }


  /**
   * Draws an item (e.g. patch, button, void space) on a tile of the time board using the Graphics2D object, a TimeBoard
   * object, and arrays of colors for the tile, patch, button, and void space.
   *
   * @param graphics2D     The Graphics2D object used to draw the item
   * @param timeBoard      The TimeBoard object that holds information about the time board
   * @param tileColor      The color of the tile
   * @param patchColor     The color of the patch
   * @param voidSpaceColor The color of the void space
   * @param position       The position of the tile on the time board
   * @param tileX          The x-coordinate of the tile
   * @param tileY          The y-coordinate of the tile
   */
  private void drawItem(Graphics2D graphics2D, TimeBoard timeBoard, Color tileColor, Color patchColor,
                        Color voidSpaceColor, int position, float tileX, float tileY) {
    drawGenericSpace(graphics2D, tileX, tileY, TIMEBOARDSPACESIZE, lighterColor(tileColor, position));
    if (timeBoard.isVoidSpace(position)) {
      drawGenericSpace(graphics2D, tileX, tileY, TIMEBOARDSPACESIZE, voidSpaceColor);
    } else if (timeBoard.spaceContainsPatch(position)) {
      drawGenericSpace(graphics2D, tileX, tileY, TIMEBOARDSPACESIZE, patchColor);
    } else if (timeBoard.spaceContainsButton(position)) {
      drawCenteredButton(graphics2D, tileX, tileY, TIMEBOARDSPACESIZE, TIMEBOARDSPACESIZE, BUTTON_COLORS[0],
                         BUTTON_COLORS[1], Color.BLACK);
    }
  }

  /**
   * Draws a centered button on the screen using the Graphics2D object.
   *
   * @param graphics2D      The Graphics2D object used to draw the ellipse
   * @param tileX           the X coordinate of the upper-left corner of the framing rectangle
   * @param tileY           the Y coordinate of the upper-left corner of the framing rectangle
   * @param rectangleWidth  The width of the rectangle the ellipse will be drawn on.
   * @param rectangleHeight The length of the rectangle the ellipse will be drawn on.
   * @param outerRing       The color of the outer ring.
   * @param innerDisk       The color of the inner disk.
   * @param buttonholes     The color of the buttonholes.
   */
  private void drawCenteredButton(Graphics2D graphics2D, float tileX, float tileY, float rectangleWidth,
                                  float rectangleHeight, Color outerRing, Color innerDisk, Color buttonholes) {
    // Constants
    float buttonOuterRingSpaceSizeRatio = 1.5F;
    float buttonInnerDiskSpaceSizeRatio = 1.9F;
    float holeButtonRadiusSizeRatio = 3F;
    // Draw the outer ring
    drawCenteredEllipse(graphics2D, outerRing, tileX, tileY, rectangleWidth / buttonOuterRingSpaceSizeRatio,
                        rectangleHeight / buttonOuterRingSpaceSizeRatio, rectangleWidth, rectangleHeight);
    // Draw inner disk
    drawCenteredEllipse(graphics2D, innerDisk, tileX, tileY, rectangleWidth / buttonInnerDiskSpaceSizeRatio,
                        rectangleHeight / buttonInnerDiskSpaceSizeRatio, rectangleWidth, rectangleHeight);
    drawCenteredEllipse(graphics2D, innerDisk, tileX, tileY, rectangleWidth / buttonInnerDiskSpaceSizeRatio,
                        rectangleHeight / buttonInnerDiskSpaceSizeRatio, rectangleWidth, rectangleHeight);

    drawHoles(graphics2D, tileX, tileY, rectangleWidth, rectangleHeight, buttonInnerDiskSpaceSizeRatio,
              holeButtonRadiusSizeRatio, buttonholes);
  }

  /**
   * Draws holes on the button
   *
   * @param graphics2D                    The Graphics2D object used to draw the button.
   * @param tileX                         The X coordinate of the upper-left corner of the framing rectangle.
   * @param tileY                         The Y coordinate of the upper-left corner of the framing rectangle.
   * @param rectangleWidth                The width of the rectangle the holes will be drawn on.
   * @param rectangleHeight               The length of the rectangle the holes will be drawn on.
   * @param buttonInnerDiskSpaceSizeRatio The ratio used to calculate the position and size of the buttonholes.
   * @param holeButtonRadiusSizeRatio     The ratio used to calculate the radius of the buttonholes.
   * @param color                         The color of the holes.
   */
  private void drawHoles(Graphics2D graphics2D, float tileX, float tileY, float rectangleWidth, float rectangleHeight,
                         float buttonInnerDiskSpaceSizeRatio, float holeButtonRadiusSizeRatio, Color color) {
    float buttonInnerDiskSquareLeftX = tileX + (rectangleWidth - rectangleWidth / buttonInnerDiskSpaceSizeRatio) / 2;
    float buttonInnerDiskSquareUpperY = tileY + (rectangleWidth - rectangleWidth / buttonInnerDiskSpaceSizeRatio) / 2;
    float buttonSemiHorizontalAxis = tileX + (rectangleWidth / 2) - buttonInnerDiskSquareLeftX;
    float buttonSemiVerticalAxis = tileY + (rectangleHeight / 2) - buttonInnerDiskSquareUpperY;

    float horizontalCloseness = buttonSemiHorizontalAxis / 4.5F;
    float verticalCloseness = buttonSemiVerticalAxis / 4.5F;
    float[][] positions = {{horizontalCloseness, verticalCloseness},
                           {buttonSemiHorizontalAxis - horizontalCloseness, verticalCloseness},
                           {horizontalCloseness, buttonSemiVerticalAxis - verticalCloseness},
                           {buttonSemiHorizontalAxis - horizontalCloseness,
                            buttonSemiVerticalAxis - verticalCloseness}};
    // Draw buttonholes
    for (float[] pos : positions) {
      drawCenteredEllipse(graphics2D, color, buttonInnerDiskSquareLeftX + pos[0], buttonInnerDiskSquareUpperY + pos[1],
                          buttonSemiHorizontalAxis / holeButtonRadiusSizeRatio,
                          buttonSemiVerticalAxis / holeButtonRadiusSizeRatio, buttonSemiHorizontalAxis,
                          buttonSemiVerticalAxis);
    }
  }

  /**
   * Draws a centered ellipse on the screen using the Graphics2D object, a color, and x and y coordinates and
   * ellipseWidth and height of the ellipse.
   *
   * @param graphics2D      The Graphics2D object used to draw the ellipse
   * @param color           The color of the ellipse
   * @param tileX           the X coordinate of the upper-left corner of the framing rectangle
   * @param tileY           the Y coordinate of the upper-left corner of the framing rectangle
   * @param ellipseWidth    The ellipseWidth of the ellipse
   * @param ellipseLength   The ellipseLength of the ellipse
   * @param rectangleWidth  The width of the rectangle the ellipse will be drawn on.
   * @param rectangleHeight The length of the rectangle the ellipse will be drawn on.
   */
  private void drawCenteredEllipse(Graphics2D graphics2D, Color color, float tileX, float tileY, float ellipseWidth,
                                   float ellipseLength, float rectangleWidth, float rectangleHeight) {
    graphics2D.setColor(color);
    graphics2D.fillOval(Math.round(tileX + (rectangleWidth - ellipseWidth) / 2),
                        Math.round(tileY + (rectangleHeight - ellipseLength) / 2), Math.round(ellipseWidth),
                        Math.round(ellipseLength));
  }

  @Override
  public void displayTimeBoard(TimeBoard timeBoard, Player player1, Player player2) {
    context.renderFrame(graphics2D -> drawTimeBoard(graphics2D, timeBoard, player1, player2));
  }
}
