package com.example.tetris_clone;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import java.util.*;

/**
 * This class contains all animation timers, Tetrominos, and game/menu data.
 * On each frame of the game loop, the game state is updated and TetrisRenderer is used to render
   updated graphics.
 */

public class Game
{
    private Tetromino currentTetromino;                 // The Tetromino that is controlled with the TetrisController.
    private Tetromino nextTetromino;                    // The Tetromino that is next in line to be the current Tetromino.
    private Tile[] prevNextTetrominoTiles;              // Stores the Tiles of the previous next Tetromino.
    private Tile[] preview;                             // The game board Tiles that show where the current Tetromino will be placed.
    private GameData gameData;                          // Stores all game Tile data.
    private MenuData menuData;                          // Stores all menu Tile data.
    private PlayerData[] topPlayersData;                // Stores PlayerData used to update the Database.
    private Database database;                          // Stores names and scores of top 3 players after game ends.
    private SoundPlayer soundPlayer;                    // Used to play sound effects and music.
    private final TetrisRenderer renderer;              // Draws graphics to the screen.
    private final AnimationTimer tetrominoDropper;             // Timer that drops current Tetromino at rate of gravity.
    private final AnimationTimer tetrisCharRotater;     // Timer that, once started, rotates golden TETRIS chars.
    private float gravity;                              // Speed that the current Tetromino drops at.
    private float timeBetweenDrops;                     // Time elapsed between Tetromino drops.
    private int score;                                  // The current score.
    private int topScore;                               // The current top score in the Database.
    private int lines;                                  // The current number of cleared lines.
    private int level;                                  // The current level.
    private int linesNeededToLevel;                     // The number of cleared lines needed to level up.
    private int linesClearedAtOnce;                     // Counts the number of lines to be cleared when the current Tetromino is placed.
    private int row, col;                               // The row and column number of each current Tetromino tile
    private int numOfTetrises;                          // Keeps track of how many Tetris' have been earned in the current game.
    private int place;                                  // Stores what place the current player scores in the top 3, if any.
    private int frameCounter;                           // Used to count number of frames that have passed in a given timer.
    private int[] tetrominoTypeCount;
    private int[] animatedTileIndexes;                  // The indexes of the Tiles that the row clear animation starts with.
    private long gameClockTimePassed;                   // The time that has passed since the game clock timer started.
    private long gameLoopTimePassed;                    // The time that has passed since the game loop timer started.
    private long charRotaterTimePassed;                 // Keeps track of time passed since the charRotator is started.
    private long pauseTime;                             // Used to store the time that the game is paused.
    private double timeSinceLastGameLoopCheck;          // The time since the last time the game loop timer executed.
    private double timeSinceLastGameClockCheck;         // The time since the last time the game clock timer executed.
    private double timeSinceLastRotaterCheck;           // The time since the last time the charRotator timer executed.
    private ArrayList<Integer> indexesOfRowsToClear;    // Stores the numbers of game board rows that need Tiles cleared.
    private int tetrominoDistanceToPlacement;           // Stores the distance that the current Tetromino is from its current
                                                        // placement location.

    // Flag variables
    private boolean isGameActive;           // Is game active or not
    private boolean isGamePaused;           // Is game paused or not
    private boolean isBeginning;            // Is game at beginning or not
    private boolean isMenuActive;           // Is menu active or not
    private boolean isUpPressed;            // Is controller up key pressed or not
    private boolean isDownPressed;          // Is controller down key pressed or not
    private boolean highScoreBeaten;        // Has high score been beaten or not
    private boolean isAnimationActive;      // Is animation active or not
    private boolean isDelayActive;          // Is delay active or not
    private boolean inBetweenGames;         // Used to determine when in between games so that, when toggled true,
                                            // the next time the controller's space or enter key is pressed,
                                            // the controller knows to reset the game.
    private boolean tetrominoIsPlaced;      // Is tetromino placed or not
    private boolean menuDataHasBeenChanged; // Has menu date been changed or not
    private boolean hasAnimatedRows;    // Has lines been incremented or not
    private boolean gameHasBeenRestarted;   // Has game been restarted or not
    private boolean tetrisCharsAreRotating; // Is tetrisCharRotator active or not
    private boolean hasAwardedTetrisMaster; // Has tetris master been awarded or not

    /**
     * The constructor initializes the TetrisRenderer, initial game data, and all AnimationTimers and starts the game loop.
     * @param newRenderer The TetrisRenderer instance used by the game.
     */
    public Game(TetrisRenderer newRenderer)
    {
        renderer = newRenderer;

        setInitialGameState();

        // This timer is used to rotate TETRIS characters clockwise every 0.25 seconds.
        tetrisCharRotater = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                timeSinceLastRotaterCheck = (now - charRotaterTimePassed) / 1000000000.0;

                if (timeSinceLastRotaterCheck >= Constants.TETRIS_CHAR_ROTATE_SPEED)
                {
                    charRotaterTimePassed = now;

                    for (Tile tile : gameData.getTetrisDisplay())
                    {
                        switch (tile.getType())
                        {
                            // T Rotations
                            case T -> tile.setTileType(TileType.T90);
                            case T90 -> tile.setTileType(TileType.T180);
                            case T180 -> tile.setTileType(TileType.T240);
                            case T240 -> tile.setTileType(TileType.T);

                            // E Rotations
                            case E -> tile.setTileType(TileType.E90);
                            case E90 -> tile.setTileType(TileType.E180);
                            case E180 -> tile.setTileType(TileType.E240);
                            case E240 -> tile.setTileType(TileType.E);

                            // R Rotations
                            case R -> tile.setTileType(TileType.R90);
                            case R90 -> tile.setTileType(TileType.R180);
                            case R180 -> tile.setTileType(TileType.R240);
                            case R240 -> tile.setTileType(TileType.R);

                            // I Rotations
                            case I -> tile.setTileType(TileType.I180);
                            case I180 -> tile.setTileType(TileType.I);

                            // S Rotations
                            case S -> tile.setTileType(TileType.S180);
                            case S180 -> tile.setTileType(TileType.S);
                        }
                    }

                    renderer.drawGameDataSprites(gameData, gameData.getTetrisDisplay());
                }
            }
        };

        // This timer is responsible for dropping the current Tetromino at the speed of gravity.
        tetrominoDropper = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if (!isGamePaused)
                {
                    if (gameClockTimePassed == 0)
                        gameClockTimePassed = now;
                }

                timeSinceLastGameClockCheck = (now - gameClockTimePassed) / 1000000000.0;

                if (isDownPressed)
                    timeBetweenDrops = Constants.TIME_BTWN_SOFT_DROPS;
                else
                    timeBetweenDrops = gravity;

                if (timeSinceLastGameClockCheck >= timeBetweenDrops)
                {
                    gameClockTimePassed = now;
                    currentTetromino.fall(gameData.getGameboard());

                    if (isDownPressed)
                        currentTetromino.incrementRowsSoftDropped();
                }
            }
        };

        // The game loop is responsible for updating the game data and menu data to reflect changes in game state as well as rendering graphics.
        AnimationTimer gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                timeSinceLastGameLoopCheck = (now - gameLoopTimePassed) / 1000000000.0;

                if (timeSinceLastGameLoopCheck >= Constants.FRAME_RATE)
                {
                    gameLoopTimePassed = now;

                    // If menu data has changed, render menu graphics.
                    if (isMenuActive && menuDataHasBeenChanged)
                    {
                        renderer.drawMenuSprites(menuData.getMenus()[getMenuData().getActiveMenu()]);
                        menuDataHasBeenChanged = false;
                    }

                    // If menu is not active and game is active, update game state and render graphics as needed.
                    if (isGameActive && !isMenuActive)
                    {
                        updateGameState();

                        // At beginning of game, render all game graphics.
                        if (isBeginning)
                            renderer.renderAllGameGraphics(gameData);
                        else
                        {
                            if (isGameActive)
                            {
                                // Render Tetromino and its preview on every frame as long as game is active.
                                if (!isAnimationActive)
                                    renderer.drawTetromino(currentTetromino);

                                // Render game board while game is active.
                                renderer.drawGameSprites(gameData.getGameboard());
                            }
                            else
                                renderer.clearTetromino();
                        }
                    }

                    if (isUpPressed)
                        isUpPressed = false;
                }
            }
        };

        gameLoop.start();
    }

    /**
     * Sets initial game data to their default initial values, loads top player data from the Database,
       loads game Images from their respective directory, assembles game data Tile arrays, and prepares
       the GUI's initial appearance.
     */
    public void setInitialGameState()
    {
        isMenuActive = true;
        menuDataHasBeenChanged = true;
        gameHasBeenRestarted = false;

        database = new Database();
        gameData = new GameData();
        menuData = new MenuData();

        indexesOfRowsToClear = new ArrayList<>();
        tetrominoTypeCount = new int[] {0, 0, 0, 0, 0, 0, 0};
        animatedTileIndexes = new int[] {Constants.GAMEBOARD_COLS / 2 - 1, Constants.GAMEBOARD_COLS / 2};

        loadTopPlayersData();

        topScore = topPlayersData[0].getScore();
        soundPlayer = SoundPlayer.getInstance();
        soundPlayer.getSounds();

        gameData.mapInitialGameData(topScore);
        menuData.mapInitialMenuData();
        renderer.openMenu(4, 0, menuData);
    }

    /**
     * Sets all game and menu data/objects to their default initial values.
     */
    public void resetGame()
    {
        gravity = 1f;
        numOfTetrises = 0;
        score = 0;
        lines = 0;
        level = 0;
        linesNeededToLevel = 10;
        linesClearedAtOnce = 0;

        isGameActive = false;
        isGamePaused = false;
        isBeginning = true;
        isMenuActive = true;
        isUpPressed = false;
        isDownPressed = false;
        highScoreBeaten = false;
        isAnimationActive = false;
        isDelayActive = false;
        inBetweenGames = false;
        tetrominoIsPlaced = false;
        menuDataHasBeenChanged = true;
        hasAnimatedRows = false;
        gameHasBeenRestarted = false;
        tetrisCharsAreRotating = false;
        hasAwardedTetrisMaster = false;

        // The value of each Tile's length and width.
        frameCounter = 0;
        gameClockTimePassed = 0;
        gameLoopTimePassed = 0;
        charRotaterTimePassed = 0;
        pauseTime = 0;
        timeSinceLastGameLoopCheck = 0;
        timeSinceLastGameClockCheck = 0;
        timeSinceLastRotaterCheck = 0;

        // Reset game data back to initial state.
        for (int statsRow = 0; statsRow < gameData.getStats().length; statsRow++)
        {
            tetrominoTypeCount[statsRow] = 0;
            gameData.updateTileData(gameData.getStats()[statsRow], tetrominoTypeCount[statsRow]);
        }
        gameData.updateTileData(gameData.getLines(), lines);
        gameData.updateTileData(gameData.getScore(), score);
        gameData.updateTileData(gameData.getLevel(), level);
        gameData.resetBoardTiles();
        menuData.resetPlayerName();
        animatedTileIndexes[0] = Constants.GAMEBOARD_COLS / 2 - 1;
        animatedTileIndexes[1] = Constants.GAMEBOARD_COLS / 2;

        currentTetromino = new Tetromino();
        nextTetromino    = new Tetromino();
        currentTetromino.generateNewTileList(Constants.CURRENT_TETROMINO_STARTING_X, Constants.CURRENT_TETROMINO_STARTING_Y);
        nextTetromino.generateNewTileList(Constants.NEXT_TETROMINO_STARTING_X, Constants.NEXT_TETROMINO_STARTING_Y);
        initializeTileLists();

        renderer.changeBackgroundImage(0);
        renderer.openMenu(4, 0, menuData);

        gameData.clearTetrisChars();
        renderer.drawGameDataSprites(gameData, gameData.getTetrisDisplay());

        // Render all game graphics at once to start.
        renderer.renderAllGameGraphics(gameData);
    }

    /**
     * This method conducts all checks that need to be done on the game data each frame of the game loop.
     */
    public void updateGameState()
    {
        // Set all tiles on the game board that are not a placed or preview Tetromino tile to 'EMPTY'
        if (!isAnimationActive)
        {
            // Draw Tetromino preview Tiles.
            updatePreview();
        }

        if (!isDelayActive && !isAnimationActive)
        {
            // Update the initial next tetromino display at the beginning of the game.
            if (isBeginning)
            {
                incrementTetrominoTypeCount(currentTetromino.getTetrominoType());
                gameData.updateNextDisplay(nextTetromino);
                renderer.drawGameSprites(gameData.getNextDisplay());
                isBeginning = false;
            }

            // Check if Tetromino is ready to be placed.
            for (Tile tile : currentTetromino.getTiles())
            {
                if (tile.getIsPlacedTetrominoTile())
                {
                    System.out.println("1 TETROMINO TILES ARE PLACED");

                    if (isDownPressed)
                        isDownPressed = false;

                    tetrominoIsPlaced = true;
                    break;
                }
            }

            // If Tetromino is ready for placement, check to see if it needs to be delayed.
            if (tetrominoIsPlaced)
            {
                if (!isUpPressed)
                {
                    System.out.println("2A TETROMINO WAS PLACED WITHOUT HARD DROP");
                    soundPlayer.playSound(3);
                    tetrominoDropper.stop();
                    isDelayActive = true;
                }
                else
                {
                    System.out.println("2B TETROMINO WAS HARD DROPPED");
                    for (Tile tile : currentTetromino.getTiles())
                    {
                        getRowAndCol(tile);
                        gameData.getGameboard()[row][col].setTileType(tile.getType());
                        gameData.getGameboard()[row][col].setIsPlacedTetrominoTile(true);
                    }
                }
            }
        }

        if (tetrominoIsPlaced && isDelayActive)
        {
            if (!isGamePaused)
                frameCounter++;

            System.out.println("C IN DELAY: "+frameCounter);

            if (frameCounter >= Constants.DELAY_FRAME_COUNT)
            {
                frameCounter = 0;
                isDelayActive = false;
                tetrominoIsPlaced = true;
                tetrominoDropper.start();
            }
        }

        if (!isDelayActive && !isAnimationActive)
        {
            if (tetrominoIsPlaced)
            {
                // After delay is over, drop the Tetromino to its updated placement location before proceeding.
                forceDropTetromino();

                for (Tile tile : currentTetromino.getTiles())
                {
                    getRowAndCol(tile);
                    gameData.getGameboard()[row][col].setTileType(tile.getType());
                    gameData.getGameboard()[row][col].setIsPlacedTetrominoTile(true);
                }

                System.out.println("D CHECKING FOR ROWS TO CLEAR");
                for (Tile tile : currentTetromino.getTiles())
                {
                    int numOfFilledTiles = 0;
                    row = Helper.getRow(tile);

                    if (!indexesOfRowsToClear.contains(row))
                    {
                        for (Tile boardTile : gameData.getGameboard()[row])
                        {
                            if (boardTile.getIsPlacedTetrominoTile())
                                numOfFilledTiles++;
                        }
                    }

                    if (numOfFilledTiles == Constants.GAMEBOARD_COLS)
                    {
                        indexesOfRowsToClear.add(row);
                        linesClearedAtOnce++;
                    }
                }

                // Animate rows as needed.
                if (indexesOfRowsToClear.size() > 0)
                {
                    tetrominoDropper.stop();
                    isAnimationActive = true;
                }
            }
        }

        if (tetrominoIsPlaced && isAnimationActive)
        {
            renderer.clearTetromino();
            Helper.sortDescending(indexesOfRowsToClear);

            // Increment lines by the number of rows that were cleared
            if (!hasAnimatedRows)
            {
                soundPlayer.playSound(1);

                if (linesClearedAtOnce == Constants.LINES_REQ_FOR_TETRIS)
                {
                    soundPlayer.playSound(9);

                    if (numOfTetrises < Constants.NUM_OF_CHARS_IN_TETRIS)
                    {
                        numOfTetrises++;
                        gameData.drawTetrisChar(numOfTetrises);
                    }

                    if (!tetrisCharsAreRotating)
                    {
                        tetrisCharRotater.start();
                        tetrisCharsAreRotating = true;
                    }
                }

                // So that sound effects and/or tetrisCharRotator are only executed once during animation instead of on
                // each animation frame.
                hasAnimatedRows = true;
            }

            System.out.println("E IN ANIMATION "+frameCounter);
            // Animate the filled game board rows to be cleared.
            if (!isGamePaused)
            {
                if (frameCounter % Constants.ANIMATION_FRAME_COUNT == 0)
                {
                    // Once each cycle's sprite has been selected, set it as the new tiletype of every tile in
                    // the rows to be cleared.
                    for (int rowNumber : indexesOfRowsToClear)
                    {
                        gameData.getGameboard()[rowNumber][animatedTileIndexes[0]].setTileType(TileType.WHITE);
                        gameData.getGameboard()[rowNumber][animatedTileIndexes[1]].setTileType(TileType.WHITE);

                        if (animatedTileIndexes[0] < 4 && animatedTileIndexes[1] > 5)
                        {
                            gameData.getGameboard()[rowNumber][animatedTileIndexes[0] + 1].setTileType(TileType.EMPTY);
                            gameData.getGameboard()[rowNumber][animatedTileIndexes[1] - 1].setTileType(TileType.EMPTY);
                        }
                    }

                    animatedTileIndexes[0]--;
                    animatedTileIndexes[1]++;
                }

                frameCounter++;
            }

            // After 0.5 seconds have passed, stop the animation and resume the game.
            if (frameCounter == Constants.ANIMATION_FRAME_COUNT * 5)
            {
                dropClearedRows();

                frameCounter = 0;
                animatedTileIndexes[0] = 4;
                animatedTileIndexes[1] = 5;
                hasAnimatedRows = false;
                isAnimationActive = false;
                tetrominoDropper.start();
            }
        }

        if (tetrominoIsPlaced && !isDelayActive && !isAnimationActive)
        {
            System.out.println("F CHECKING FOR GAME OVER");
            // Check for game over state.
            checkIfGameIsOver();

            // If game is still active, prepare for next tetromino.
            if (isGameActive)
            {
                System.out.println("G GAME STILL ACTIVE");

                // Un-place current Tetromino.
                tetrominoIsPlaced = false;
                for (Tile tile : currentTetromino.getTiles())
                    tile.setIsPlacedTetrominoTile(false);

                // Increment lines as needed
                if (linesClearedAtOnce > 0)
                {
                    incrementLines(linesClearedAtOnce);
                    linesNeededToLevel -= linesClearedAtOnce;
                }

                // Increment score
                incrementScore();
                currentTetromino.resetRowsHardAndSoftDropped();

                // Increment level as needed
                if (linesNeededToLevel <= 0)
                    incrementLevel();

                // Increment the type of the new current Tetromino
                incrementTetrominoTypeCount(currentTetromino.getTetrominoType());

                // Update the next Tetromino display to reflect its new TileType and render updated gameboard / next display Tiles.
                gameData.updateNextDisplay(nextTetromino);
                renderer.drawGameSprites(gameData.getNextDisplay());
            }
        }
    }

    /**
     * Check current score to see if it makes it into the top 3.
     * @return The place in the top 3, if any, that the current player got.
     *         If player did not get top score, then 0 is returned.
     */
    private int checkForNewHighScore()
    {
        int place = 0;
        topPlayersData = database.retrieveEntries();
        String[] names = new String[topPlayersData.length + 1];
        int[] scores = new int[topPlayersData.length + 1];

        // Store top names and scores.
        names[0] = topPlayersData[0].getName();
        names[1] = topPlayersData[1].getName();
        names[2] = topPlayersData[2].getName();
        names[3] = "???";
        scores[0] = topPlayersData[0].getScore();
        scores[1] = topPlayersData[1].getScore();
        scores[2] = topPlayersData[2].getScore();
        scores[3] = score;

        // sort 'scores' in descending order.
        for (int i = 0; i < scores.length; i++)
        {
            for (int j = i + 1; j < scores.length; j++)
            {
                if (scores[i] < scores[j])
                {
                    String tempName = names[i];
                    int tempScore = scores[i];
                    names[i] = names[j];
                    names[j] = tempName;
                    scores[i] = scores[j];
                    scores[j] = tempScore;
                }
            }
        }

        for (int i = 0; i < topPlayersData.length; i++)
        {
            topPlayersData[i].setName(names[i]);
            topPlayersData[i].setScore(scores[i]);
            System.out.println(topPlayersData[i].getName()+", "+topPlayersData[i].getScore());
        }

        // return number signifying what place, if any, in the top 3 'score' is in:
        // 0 if NO, 1 if first place, 2 if 2nd place, and 3 if 3rd place
        if (scores[0] == score)
            place = 1;
        else if (scores[1] == score)
            place = 2;
        else if (scores[2] == score)
            place = 3;

        return place;
    }

    /**
     * Increment the number of cleared lines in the current game, update the game data Tiles, and render the line display
       graphics to reflect the updates.
     * @param rowsCleared The number of cleared lines in the current game.
     */
    public void incrementLines(int rowsCleared)
    {
        lines += rowsCleared;
        gameData.updateTileData(gameData.getLines(), lines);
        renderer.drawGameDataSprites(gameData, gameData.getLines());
    }

    /**
     * Increment the current score based on level, number of lines cleared, and number of rows soft and hard dropped.
     * If player has activated all 6 TETRIS chars, then activate 'TETRIS MASTER' score boost.
     */
    public void incrementScore()
    {
        if (level == 0)
        {
            switch (linesClearedAtOnce)
            {
                case 1 -> score += 40;
                case 2 -> score += 100;
                case 3 -> score += 300;
                case 4 -> score += 1200;
            }
        }
        else if (level > 0 && level < 10)
        {
            switch (linesClearedAtOnce)
            {
                case 1 -> score += 40 * level;
                case 2 -> score += 100 * level;
                case 3 -> score += 300 * level;
                case 4 -> score += 1200 * level;
            }
        } else
        {
            switch (linesClearedAtOnce)
            {
                case 1 -> score += 40 * 10;
                case 2 -> score += 100 * 10;
                case 3 -> score += 300 * 10;
                case 4 -> score += 1200 * 10;
            }
        }

        soundPlayer.playSound(6);
        soundPlayer.playSound(5);

        if (numOfTetrises == Constants.NUM_OF_CHARS_IN_TETRIS && !hasAwardedTetrisMaster)
        {
            score += 50000;
            hasAwardedTetrisMaster = true;
            renderer.changeBackgroundImage(8);
            renderer.renderAllGameGraphics(gameData);
            soundPlayer.playSound(10);
        }

        score += currentTetromino.getRowsSoftDropped() + currentTetromino.getRowsHardDropped() * 2;
        linesClearedAtOnce = 0;

        if (score > topScore)
        {
            gameData.updateTileData(gameData.getTopScore(), score);
            renderer.drawGameDataSprites(gameData, gameData.getTopScore());
        }

        gameData.updateTileData(gameData.getScore(), score);
        renderer.drawGameDataSprites(gameData, gameData.getScore());
    }

    /**
     * Increment the current level based on number of lines cleared and update speed of gravity based on level.
     */
    public void incrementLevel()
    {
        level++;

        switch (level)
        {
            case 1, 2, 3, 4, 5 -> linesNeededToLevel = 10;
            case 6, 7, 8, 9 -> linesNeededToLevel = 15;
            case 10, 11, 12, 13 -> linesNeededToLevel = 20;
            case 14, 15, 16, 17 -> linesNeededToLevel = 25;
            default -> linesNeededToLevel = 30;
        }

        linesNeededToLevel -= lines % linesNeededToLevel;

        if (level >= 9)
            gravity *= 0.8;

        soundPlayer.playSound(4);
        gameData.updateTileData(gameData.getLevel(), level);
        renderer.drawGameDataSprites(gameData, gameData.getLevel());
    }

    /**
     * Increment the count for the TileType passed in.
     * @param tetrominoType The TileType of the type to accumulate.
     */
    public void incrementTetrominoTypeCount(TileType tetrominoType)
    {
        int typeIndex = 0;

        switch (tetrominoType)
        {
            case T_TET -> typeIndex += 0;
            case L_TET -> typeIndex = 1;
            case Z_TET -> typeIndex = 2;
            case O_TET -> typeIndex = 3;
            case S_TET -> typeIndex = 4;
            case J_TET -> typeIndex = 5;
            case I_TET -> typeIndex = 6;
        }

        tetrominoTypeCount[typeIndex]++;
        gameData.updateTileData(gameData.getStats()[typeIndex], tetrominoTypeCount[typeIndex]);
        renderer.drawGameDataSprites(gameData, gameData.getStats()[typeIndex]);
    }

    /**
     * Update the Tetromino preview Tiles to reflect the position of the current Teteromino.
     */
    public void updatePreview()
    {
        // Get the distance from each current Tetromino Tile to either the nearest placed Tile in its column,
        // or the bottom of the game board.
        tetrominoDistanceToPlacement = Helper.getTetrominoDropDistance(currentTetromino.getTiles(), gameData.getGameboard());
        int previewTileIndex = 0;

        // Clear current preview Tiles.
        eraseOldPreview();

        // Draw new preview Tiles.
        for (Tile tile : currentTetromino.getTiles())
        {
            // Get new preview Tile positions by adding the shortest distance in tileDistances to each Tile.
            preview[previewTileIndex].setX(tile.getX());
            preview[previewTileIndex].setY(tile.getY() + tetrominoDistanceToPlacement);

            getRowAndCol(tile, tetrominoDistanceToPlacement);

            // Draw the preview Tiles unless preview is in the same position as the current Tetromino.
            if (!gameData.getGameboard()[row][col].getIsPlacedTetrominoTile())
                gameData.getGameboard()[row][col].setTileType(TileType.PREVIEW);

            previewTileIndex++;
        }
    }

    /**
     * Clear preview Tiles.
     */
    public void eraseOldPreview()
    {
        for (Tile tile : preview)
        {
            getRowAndCol(tile);

            if (!gameData.getGameboard()[row][col].getIsPlacedTetrominoTile())
                gameData.getGameboard()[row][col].setTileType(TileType.EMPTY);
        }
    }

    /**
     * @return The current Tetromino.
     */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /**
     * Set isGameActive to the boolean value passed in.
     * @param status The boolean indicating whether game is active or not.
     */
    public void setGameActive(boolean status) {
        isGameActive = status;
    }

    /**
     * Set isGamePaused to the boolean value passed in.
     * @param status The boolean indicating whether game is paused or not.
     */
    public void setGamePaused(boolean status) {
        isGamePaused = status;
    }

    /**
     * @return Game active status.
     */
    public boolean getGameActive() {
        return isGameActive;
    }

    /**
     * @return Game paused status.
     */
    public boolean getGamePaused() {
        return !isGamePaused;
    }

    /**
     * @return Beginning of game status.
     */
    public boolean getIsBeginning() {
        return !isBeginning;
    }

    /**
     * @return Game data.
     */
    public GameData getGameData() {
        return gameData;
    }

    /**
     * @return Top players database.
     */
    public Database getDatabase()
    {
        return database;
    }

    /**
     * @return Menu data.
     */
    public MenuData getMenuData() {
        return menuData;
    }

    /**
     * @return The game's sound player instance.
     */
    public SoundPlayer getSoundPlayer()
    {
        return soundPlayer;
    }

    /**
     * @return The game's TetrisRenderer instance.
     */
    public TetrisRenderer getRenderer() {
        return renderer;
    }

    /**
     * Set isUpPressed to the boolean value passed in.
     * @param status Boolean indicating whether TetrisController's up key is pressed or not.
     */
    public void setIsUpPressed(boolean status) {
        isUpPressed = status;
    }

    /**
     * Set isDownPressed to the boolean value passed in.
     * @param status Boolean indicating whether TetrisController's down key is pressed or not.
     */
    public void setIsDownPressed(boolean status) {
        isDownPressed = status;
    }

    /**
     * Set isMenuActive to the boolean value passed in.
     * @param status Boolean indicating whether menu is active or not.
     */
    public void setIsMenuActive(boolean status) {
        isMenuActive = status;
    }

    /**
     * @return Menu active status.
     */
    public boolean getIsMenuActive() {
        return isMenuActive;
    }

    /**
     * Used by TetrisController to call sound player's' button press sound.
     */
    public void playButtonSound()
    {
        soundPlayer.playSound(0);
    }

    /**
     * Used by TetrisController to call sound player's' out of bounds sound.
     */
    public void playOutOfBoundsSound()
    {
        soundPlayer.playSound(3);
    }

    /**
     * Used by TetrisController to call sound player's' Tetromino Rotation sound.
     */
    public void playRotationSound()
    {
        soundPlayer.playSound(7);
    }

    /**
     * @return Delay active status.
     */
    public boolean isDelayActive()
    {
        return isDelayActive;
    }

    /**
     * @return Animation active status.
     */
    public boolean isAnimationActive()
    {
        return !isAnimationActive;
    }

    /**
     * Shut down the program.
     */
    public void shutdownGame()
    {
        Platform.exit();
    }

    /**
     * After row clear animation, clear remaining Tiles and drop the cleared rows down.
     */
    private void dropClearedRows()
    {
        int topRowWithPlacedTile = 0;
        boolean rowHasPlacedTiles;
        boolean skipRow;

        // Get the tallest row on the game board that has placed tiles in it.
        for (int r = indexesOfRowsToClear.get(indexesOfRowsToClear.size() - 1); r > 0; r--)
        {
            rowHasPlacedTiles = false;

            for (Tile tile : gameData.getGameboard()[r - 1])
            {
                if (tile.getIsPlacedTetrominoTile())
                {
                    rowHasPlacedTiles = true;
                    break;
                }
            }

            if (!rowHasPlacedTiles)
            {
                topRowWithPlacedTile = r;
                break;
            }
        }

        // Clear animated rows.
        for(int rowIndex : indexesOfRowsToClear)
        {
            for (Tile tile : gameData.getGameboard()[rowIndex])
            {
                tile.setTileType(TileType.EMPTY);
                tile.setIsPlacedTetrominoTile(false);
            }
        }

        // Starting with the number of the row furthest down row being cleared, iterate rows backwards.
        // On each row that has not been cleared, calculate how many rows it needs to drop to compensate
        // for the cleared row(s) underneath it.
        for (int rowToDrop = indexesOfRowsToClear.get(0); rowToDrop >= topRowWithPlacedTile; rowToDrop--)
        {
            // If rowToDrop is a row that has been cleared, move on to the next row up.
            skipRow = indexesOfRowsToClear.contains(rowToDrop);

            // If rowToDrop is not a cleared row, calculate how many rows it needs to drop, exchange types and
            // placement status with the target row, and then clear rowToDrop. Repeat until the tallest row
            // with placed tiles has been dropped.
            if (!skipRow)
            {
                int dropDistance = Helper.getTetrominoDropDistance(gameData.getGameboard()[rowToDrop], gameData.getGameboard());
                int targetRow = Helper.getRow(gameData.getGameboard()[rowToDrop][col], dropDistance);

                for (int col = 0; col < Constants.GAMEBOARD_COLS; col++)
                {
                    gameData.getGameboard()[targetRow][col].setTileType(gameData.getGameboard()[rowToDrop][col].getType());
                    gameData.getGameboard()[targetRow][col].setIsPlacedTetrominoTile(gameData.getGameboard()[rowToDrop][col].getIsPlacedTetrominoTile());
                    gameData.getGameboard()[rowToDrop][col].setTileType(TileType.EMPTY);
                    gameData.getGameboard()[rowToDrop][col].setIsPlacedTetrominoTile(false);
                }
            }
        }

        // Once all rows have been cleared, reset the list of rows that need to be cleared.
        indexesOfRowsToClear.clear();
    }

    /**
     * Drop the Tetromino to its placement location - used after delay.
     */
    private void forceDropTetromino()
    {
        // Stores the distance that the current Tetromino is from its current
        int tetrominoDistanceToPlacement = Helper.getTetrominoDropDistance(currentTetromino.getTiles(), gameData.getGameboard());

        if (tetrominoDistanceToPlacement > 0)
        {
            for (Tile tile : currentTetromino.getTiles())
                tile.setY(tile.getY() + tetrominoDistanceToPlacement);
        }
    }

    /**
     * Get the current game board row and column of the Tile passed in.
     * @param tile The Tile to check.
     */
    private void getRowAndCol(Tile tile)
    {
        row = Helper.getRow(tile);
        col = Helper.getCol(tile);
    }

    /**
     * Get the current game board row and column of the placement position of the Tile passed in.
     * @param tile The Tile to check.
     * @param distance The distance to placement of the Tile.
     */
    private void getRowAndCol(Tile tile, int distance)
    {
        row = Helper.getRow(tile, distance);
        col = Helper.getCol(tile);
    }

    private void initializeTileLists()
    {
        prevNextTetrominoTiles = new Tile[nextTetromino.getTiles().length];
        preview = new Tile[currentTetromino.getTiles().length];
        tetrominoDistanceToPlacement = Helper.getTetrominoDropDistance(currentTetromino.getTiles(), gameData.getGameboard());
        int prevIndex = 0;

        for (Tile tile : currentTetromino.getTiles())
        {
            preview[prevIndex] = new Tile(tile.getX(), tile.getY() + tetrominoDistanceToPlacement, TileType.PREVIEW);
            prevNextTetrominoTiles[prevIndex] = new Tile(0, 0, TileType.EMPTY);
            prevIndex++;
        }
    }

    /**
     * Run tests to check if game over state has been reached.
     */
    private void checkIfGameIsOver()
    {
        boolean needToDrawLastTetrominos = false;       // Flag that indicates whether the placed Tetromino that ends the game
                                                        // has room to be drawn to the game board or not.
        boolean gameOverTestResult = false;             // Flag indicating game over test result.
        int previousNextIndex = 0;

        // Update previous Next Tetromino Tiles and clear Next Tetromino display.
        for (Tile tile : nextTetromino.getTiles())
        {
            prevNextTetrominoTiles[previousNextIndex].setX(tile.getX());
            prevNextTetrominoTiles[previousNextIndex].setY(tile.getY());
            prevNextTetrominoTiles[previousNextIndex].setTileType(tile.getType());
            getRowAndCol(tile);
            gameData.getNextDisplay()[row][col].setTileType(TileType.EMPTY);
            previousNextIndex++;
        }

        // Get the next type for current tetromino and generate new type for next.
        currentTetromino.setTetrominoType(nextTetromino.getTetrominoType());
        nextTetromino.setTetrominoType(nextTetromino.generateNewType());

        // Reset Tetromino positions and increment the count of the newly current type.
        currentTetromino.resetTetromino(Constants.CURRENT_TETROMINO_STARTING_X, Constants.CURRENT_TETROMINO_STARTING_Y);
        nextTetromino.resetTetromino(Constants.NEXT_TETROMINO_STARTING_X, Constants.NEXT_TETROMINO_STARTING_Y);
        gameClockTimePassed = 0;

        // Test 1
        // If this test is failed, then the most recently placed Tetromino ended the game because the next one will not
        // fit on the game board.
        for (Tile tile : getCurrentTetromino().getTiles())
        {
            getRowAndCol(tile);

            if (row <= 1 && gameData.getGameboard()[row][col].getIsPlacedTetrominoTile())
            {
                gameOverTestResult = true;
                break;
            }
        }

        // Test 2
        // If test 1 was passed, but this test fails, then the new current Tetromino fits on the game board, but it
        // triggers game over.
        if (!gameOverTestResult)
        {
            for (Tile tile : currentTetromino.getTiles())
            {
                getRowAndCol(tile);

                if (row <= 1 && gameData.getGameboard()[row + 1][col].getIsPlacedTetrominoTile())
                {
                    needToDrawLastTetrominos = true;
                    gameOverTestResult = true;
                    break;
                }
            }
        }

        // If game over state has been reached, render graphics one last time and then end game.
        // If game ending Tetromino could not fit, then set the Next Tetromino display to show the
        // previous Next Tetromino.
        if (gameOverTestResult)
        {
            // If the new Tetromino is what triggers the end game, then place it on the game board, increment its type,
            // and update the Next Display.
            if (needToDrawLastTetrominos)
            {
                for (Tile tile : currentTetromino.getTiles())
                {
                    getRowAndCol(tile);
                    gameData.getGameboard()[row][col].setTileType(tile.getType());
                }

                incrementTetrominoTypeCount(currentTetromino.getTetrominoType());

                // Update the next Tetromino display to reflect its new TileType.
                for (Tile tile : nextTetromino.getTiles())
                {
                    getRowAndCol(tile);
                    gameData.getNextDisplay()[row][col].setTileType(tile.getType());
                }
            }
            // If game ending is caused by the last placed Tetromino, then update the Next Display
            // to reflect the previous Next Tetromino's TileType.
            else
            {
                for (Tile tile : prevNextTetrominoTiles)
                {
                    getRowAndCol(tile);
                    gameData.getNextDisplay()[row][col].setTileType(currentTetromino.getTetrominoType());
                }
            }

            // Render game board and next display graphics one last time and then end the game.
            renderer.drawGameSprites(gameData.getGameboard());
            renderer.drawGameSprites(gameData.getNextDisplay());
            gameOver(false);
        }
    }

    /**
     * End the game. If game was not quit, then check for high score. If high score was reached, then open new high score menu.
     * If game was quit, then open new game menu.
     * @param wasGameQuit Boolean indicating whether game was quit or not.
     */
    public void gameOver(boolean wasGameQuit)
    {
        isGameActive = false;
        inBetweenGames = true;
        tetrominoDropper.stop();
        tetrisCharRotater.stop();
        soundPlayer.stopMusic();
        gameClockTimePassed = 0;

        for (Tile tile : currentTetromino.getTiles())
            tile.setIsPlacedTetrominoTile(false);

        if (!wasGameQuit)
        {
            place = checkForNewHighScore();

            if (place != 0)
            {
                highScoreBeaten = true;
                isMenuActive = true;
                renderer.openMenu(7, 4, menuData);
            }
            else
                menuData.setActiveMenu(0);

            if (highScoreBeaten)
                soundPlayer.playSound(11);
            else
            {
                if (!isGamePaused)
                    soundPlayer.playSound(2);
            }
        }
        else
        {
            topScore = topPlayersData[0].getScore();
            gameData.updateTileData(gameData.getTopScore(), topScore);
            menuData.setActiveMenu(0);
        }
    }

    /**
     * @return The game clock AnimationTimer.
     */
    public AnimationTimer getTetrominoDropper()
    {
        return tetrominoDropper;
    }

    public void setGameHasBeenRestarted(boolean status)
    {
        gameHasBeenRestarted = status;
    }

    /**
     * @return Game has been restarted status.
     */
    public boolean getGameHasBeenRestarted()
    {
        return gameHasBeenRestarted;
    }

    /**
     * Pause the game and store the time of the pause so that when resumed, it can be subtracted from the game clock timer
       so that effectively no time has passed since pausing, making it so that the Tetromino does not immediately fall upon
       resuming game.
     */
    public void pauseGame()
    {
        isGamePaused = true;
        pauseTime = System.nanoTime();
    }

    /**
     * Resume a paused game.
     */
    public void resumeGame()
    {
        isGamePaused = false;
        long now = System.nanoTime();
        long elapsedTimePaused = now - pauseTime;
        gameClockTimePassed += elapsedTimePaused;
        pauseTime = 0;
    }

    /**
     * @return Score.
     */
    public int getScore()
    {
        return score;
    }

    /**
     * @return What place the player came in.
     */
    public int getPlace()
    {
        return place;
    }

    /**
     * Set inBetweenGamesStatus to the boolean value passed in.
     * @param status Boolean indicating whether inBetweenGames is true or false.
     */
    public void setInBetweenGamesStatus(boolean status)
    {
        inBetweenGames = status;
    }

    /**
     * @return In between games status.
     */
    public boolean getInBetweenGamesStatus()
    {
        return inBetweenGames;
    }

    public void setGravity(float newGravity)
    {
        gravity = newGravity;
    }

    public float getGravity()
    {
        return gravity;
    }

    public PlayerData[] getTopPlayersData()
    {
        return topPlayersData;
    }

    /**
     * Load top player data from the database.
     */
    public void loadTopPlayersData()
    {
        topPlayersData = database.retrieveEntries();

        for (PlayerData entry : topPlayersData)
            System.out.println("N: "+entry.getName()+", "+"S: "+entry.getScore());

        menuData.setTopScoresData(topPlayersData);
    }

    /**
     * Set menuDataHasBeenChanged to the boolean value passed in.
     * @param status Boolean indicating whether menu data has been changed or not.
     */
    public void setMenuDataHasBeenChanged(boolean status)
    {
        menuDataHasBeenChanged = status;
    }
}
