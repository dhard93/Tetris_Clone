package com.example.tetris_clone;

import java.util.*;

/**
 * This class contains all Tiles used in the game and provides methods for initializing, updating, and retrieving the Tiles.
 */

public class GameData
{
    private final Tile[][][] tileDisplays;
    private final Tile[][] stats;
    private final Tile[] lines;
    private final Tile[] topScore;
    private final Tile[] score;
    private final Tile[] level;
    private final Tile[] tetrisDisplay;
    private char[] tetrisChars;
    private final Tile[][] gameboard;
    private final Tile[][] nextDisplay;

    /**
     * Constructor initializes Tiles arrays used in game.
     */
    public GameData()
    {
        stats = new Tile[Constants.NUM_OF_TETROMINOS][Constants.STATS_PLACE_NUMBER];
        lines = new Tile[Constants.LINES_PLACE_NUMBER];
        topScore = new Tile[Constants.TOP_SCORE_PLACE_NUMBER];
        score = new Tile[Constants.SCORE_PLACE_NUMBER];
        level = new Tile[Constants.LEVEL_PLACE_NUMBER];
        tetrisDisplay = new Tile[Constants.NUM_OF_CHARS_IN_TETRIS];
        tetrisChars = "      ".toCharArray();
        gameboard = new Tile[Constants.GAMEBOARD_ROWS][Constants.GAMEBOARD_COLS];
        nextDisplay  = new Tile[Constants.NEXT_DISPLAY_SIZE][Constants.NEXT_DISPLAY_SIZE];
        tileDisplays = new Tile[][][]{gameboard, nextDisplay};
    }

    /**
     * Initialize the Tiles in a 1D Tile array with x,y position and type.
     * @param tiles The Tile array that contains the Tiles being initialized.
     * @param startingX The x position of the first Tile in the array.
     * @param endX The x position that marks the end of a row of Tiles.
     * @param startingY The y position of the first Tile in the array.
     * @param endY The y position that, if reached, is used to terminate the loop.
     * @param xIncrement The horizontal distance between Tiles.
     * @param yIncrement The vertical distance between Tiles.
     */
    public void initializeGameTileData(Tile[] tiles, int startingX, int endX, int startingY, int endY, int xIncrement, int yIncrement)
    {
        int acc = 0;

        for (int y = startingY; y < endY; y += yIncrement)
        {
            for (int x = startingX; x < endX; x += xIncrement)
            {
                tiles[acc] = new Tile(x, y, TileType.ZERO);
                acc++;

                if (acc == tiles.length)
                    acc = 0;
            }
        }
    }

    /**
     * Overloaded version of initializeGameTileData for initializing the Tiles in a 2D Tile array.
     * @param tiles The Tile array that contains the Tiles being initialized.
     * @param startingX The x position of the first Tile in the array.
     * @param endX The x position that marks the end of a row of Tiles.
     * @param startingY The y position of the first Tile in the array.
     * @param endY The y position that, if reached, is used to terminate the loop.
     * @param xIncrement The horizontal distance between Tiles.
     * @param yIncrement The vertical distance between Tiles.
     */
    public void initializeGameTileData(Tile[][] tiles, int startingX, int endX, int startingY, int endY, int xIncrement, int yIncrement)
    {
        int rowIndex = 0;
        int colIndex = 0;

        for (int y = startingY; y < endY; y += yIncrement)
        {
            for (int x = startingX; x < endX; x += xIncrement)
            {
                tiles[rowIndex][colIndex] = new Tile(x, y, TileType.ZERO);
                colIndex++;

                if (colIndex == tiles[rowIndex].length)
                {
                    colIndex = 0;
                    rowIndex++;
                }
            }
        }
    }

    /**
     * Initializes Tiles for all Tile arrays used in gameplay.
     * @param top The top score from the database.
     */
    public void mapInitialGameData(int top)
    {
        initializeGameTileData(stats, Constants.STATS_STARTING_X, Constants.STATS_ENDING_X, Constants.STATS_STARTING_Y, Constants.STATS_ENDING_Y, Constants.STATS_X_INCREMENT, Constants.STATS_Y_INCREMENT);
        initializeGameTileData(lines, Constants.LINES_STARTING_X, Constants.LINES_ENDING_X, Constants.LINES_STARTING_Y, Constants.LINES_ENDING_Y, Constants.LINES_X_INCREMENT, Constants.LINES_Y_INCREMENT);
        initializeGameTileData(topScore, Constants.TOP_SCORE_STARTING_X, Constants.TOP_SCORE_ENDING_X, Constants.TOP_SCORE_STARTING_Y, Constants.TOP_SCORE_ENDING_Y, Constants.TOP_SCORE_X_INCREMENT, Constants.TOP_SCORE_Y_INCREMENT);
        updateTileData(topScore, top);
        initializeGameTileData(score, Constants.SCORE_STARTING_X, Constants.SCORE_ENDING_X, Constants.SCORE_STARTING_Y, Constants.SCORE_ENDING_Y, Constants.SCORE_X_INCREMENT, Constants.SCORE_Y_INCREMENT);
        initializeGameTileData(level, Constants.LEVEL_STARTING_X, Constants.LEVEL_ENDING_X, Constants.LEVEL_STARTING_Y, Constants.LEVEL_ENDING_Y, Constants.LEVEL_X_INCREMENT, Constants.LEVEL_Y_INCREMENT);
        initializeGameTileData(tetrisDisplay, Constants.TETRIS_STARTING_X, Constants.TETRIS_ENDING_X, Constants.TETRIS_STARTING_Y, Constants.TETRIS_ENDING_Y, Constants.TETRIS_X_INCREMENT, Constants.TETRIS_Y_INCREMENT);
        populateBoardTiles();
    }

    /**
     * Update the TETRIS display to display a character depending on the number of tetris' the player has gotten.
     * @param tetrisCount The number of tetris' the player has gotten in the current game.
     */
    public void drawTetrisChar(int tetrisCount)
    {
        char charToDraw = ' ';
        int charIndex = 0;

        switch (tetrisCount)
        {
            case 1, 3 -> charToDraw = 'T';
            case 2    -> charToDraw = 'E';
            case 4    -> charToDraw = 'R';
            case 5    -> charToDraw = 'I';
            case 6    -> charToDraw = 'S';
        }

        tetrisChars[tetrisCount - 1] = charToDraw;

        for (Tile tile : tetrisDisplay)
        {
            switch (tetrisChars[charIndex])
            {
                case 'T' -> tile.setTileType(TileType.T);
                case 'E' -> tile.setTileType(TileType.E);
                case 'R' -> tile.setTileType(TileType.R);
                case 'I' -> tile.setTileType(TileType.I);
                case 'S' -> tile.setTileType(TileType.S);
                default  -> tile.setTileType(TileType.EMPTY);
            }

            charIndex++;
        }
    }

    /**
     * Clears the TETRIS display of chars when the game is reset.
     */
    public void clearTetrisChars()
    {
        tetrisChars = "      ".toCharArray();

        for (Tile tile : tetrisDisplay)
            tile.setTileType(TileType.EMPTY);
    }

    /**
     * Update the next Tetromino display to reflect the Tiles of the next Tetromino.
     * @param nextTetromino The next Tetromino in the current game.
     */
    public void updateNextDisplay(Tetromino nextTetromino)
    {
        for (Tile tile : nextTetromino.getTiles())
        {
            int row = Helper.getRow(tile);
            int col = Helper.getCol(tile);
            nextDisplay[row][col].setTileType(tile.getType());
        }
    }

    /**
     * Update the TileTypes of the Tiles in 'tiles' to reflect 'numToConvert'.
     * @param tiles The Tile array to be updated.
     * @param numToConvert The integer number that needs to be displayed by the Tiles.
     */
    public void updateTileData(Tile[] tiles, int numToConvert)
    {
        int numOfDigits = tiles.length;

        // Break 'numToConvert' into numbers places and store the numbers places as a list of integers.
        // If the integer list has fewer digits than 'numOfDigits', then fill the remaining digits with 0's.
        ArrayList<Integer> numberInPlaces = Helper.breakNumIntoPlaces(numToConvert);
        if (numberInPlaces.size() < numOfDigits)
        {
            while (numOfDigits - numberInPlaces.size() > 0)
                numberInPlaces.add(0);
        }

        // Reverse the integer list so that the 0's come first.
        Collections.reverse(numberInPlaces);

        // Set each Tile in 'tiles' to the TileType corresponding to the number in its same index in the integer list.
        int digitIndex = 0;
        for (Tile tile : tiles)
        {
            switch (numberInPlaces.get(digitIndex))
            {
                case 0 -> tile.setTileType(TileType.ZERO);
                case 1 -> tile.setTileType(TileType.ONE);
                case 2 -> tile.setTileType(TileType.TWO);
                case 3 -> tile.setTileType(TileType.THREE);
                case 4 -> tile.setTileType(TileType.FOUR);
                case 5 -> tile.setTileType(TileType.FIVE);
                case 6 -> tile.setTileType(TileType.SIX);
                case 7 -> tile.setTileType(TileType.SEVEN);
                case 8 -> tile.setTileType(TileType.EIGHT);
                case 9 -> tile.setTileType(TileType.NINE);
            }

            digitIndex++;
        }
    }

    /**
     * Initialize the Tiles in the game board and next display.
     */
    private void populateBoardTiles()
    {
        int startingX;
        int x;
        int y;
        int rowCount;
        int colCount;

        for (Tile[][] display : tileDisplays)
        {
            if (Arrays.deepEquals(display, gameboard))
            {
                startingX = Constants.GAMEBOARD_STARTING_X;
                x = startingX;
                y = Constants.GAMEBOARD_STARTING_Y;
                rowCount = Constants.GAMEBOARD_ROWS;
                colCount = Constants.GAMEBOARD_COLS;
            }
            else
            {
                startingX = Constants.NEXT_TETROMINO_STARTING_X;
                x = startingX;
                y = Constants.NEXT_TETROMINO_STARTING_Y;
                rowCount = Constants.NEXT_DISPLAY_SIZE;
                colCount = Constants.NEXT_DISPLAY_SIZE;
            }

            for (int row = 0; row < rowCount; row++)
            {
                for (int col = 0; col < colCount; col++)
                {
                    display[row][col] = new Tile(x, y, TileType.EMPTY);
                    x += Constants.TILE_SIZE;

                    if (col == colCount - 1)
                    {
                        x = startingX;
                        y += Constants.TILE_SIZE;
                    }
                }
            }
        }
    }

    /**
     * Clear all Tiles in the game board and next display.
     */
    public void resetBoardTiles()
    {
        // Reset all board tiles back to empty and unplaced.
        for (Tile[] boardRow : gameboard)
        {
            for (Tile boardTile : boardRow)
            {
                boardTile.setTileType(TileType.EMPTY);
                boardTile.setIsPlacedTetrominoTile(false);
            }
        }

        for (Tile[] boardRow : nextDisplay)
        {
            for (Tile boardTile : boardRow)
                boardTile.setTileType(TileType.EMPTY);
        }
    }

    /**
     * @return the stats Tile array.
     */
    public Tile[][] getStats()
    {
        return stats;
    }

    /**
     * @return the lines Tile array.
     */
    public Tile[] getLines()
    {
        return lines;
    }

    /**
     * @return the top score Tile array.
     */
    public Tile[] getTopScore()
    {
        return topScore;
    }

    /**
     * @return the score Tile array.
     */
    public Tile[] getScore()
    {
        return score;
    }

    /**
     * @return the level Tile array.
     */
    public Tile[] getLevel()
    {
        return level;
    }

    /**
     * @return the tetris display Tile array.
     */
    public  Tile[] getTetrisDisplay()
    {
        return tetrisDisplay;
    }

    /**
     * @return the game board Tile array.
     */
    public Tile[][] getGameboard()
    {
        return gameboard;
    }

    /**
     * @return the next display Tile array.
     */
    public Tile[][] getNextDisplay()
    {
        return nextDisplay;
    }
}
