package com.example.tetris_clone;

import java.util.*;

/**
 * This class represents a Tetromino - an array of 4 Tiles.
 * Tetrominos are able to fall, soft drop, hard drop, move, and rotate.
 * Tetrominos also contain methods to generate a new type, reset their Tile positions, etc.
 */

public class Tetromino
{
    private final Tile[] tetrominoTiles;
    private TileType tetrominoType;
    private int rowsSoftDropped;
    private int rowsHardDropped;
    private boolean movedOutOfBounds;
    private int[][] matrix;
    private final int[][] tileVectors;
    private int[] vector;
    private final int tileSize;
    private int row, col;
    private int distanceToPlacement;

    public Tetromino()
    {
        // create a new ArrayList of Tiles for the Tetromino
        tetrominoTiles = new Tile[4];
        tetrominoType = generateNewType();
        rowsSoftDropped = 0;
        rowsHardDropped = 0;
        distanceToPlacement = 0;
        matrix = new int[tetrominoTiles.length][];
        tileVectors = new int[tetrominoTiles.length][];
        vector = new int[tetrominoTiles.length];
        tileSize = Constants.TILE_SIZE;
        movedOutOfBounds = false;
    }

    /**
     * Initialize Tetromino Tiles
     * @param startingX Starting Tetromino x position.
     * @param startingY Starting Tetromino y position.
     */
    public void generateNewTileList(int startingX, int startingY)
    {
        switch (tetrominoType)
        {
            case T_TET ->
            {
                tetrominoTiles[0] = new Tile(startingX + tileSize, startingY, tetrominoType);
                tetrominoTiles[1] = new Tile(startingX, startingY + tileSize, tetrominoType);
                tetrominoTiles[2] = new Tile(startingX + tileSize, startingY + tileSize, tetrominoType);
                tetrominoTiles[3] = new Tile(startingX + tileSize * 2, startingY + tileSize, tetrominoType);

            }
            case J_TET ->
            {
                tetrominoTiles[0] = new Tile(startingX, startingY, tetrominoType);
                tetrominoTiles[1] = new Tile(startingX, startingY + tileSize, tetrominoType);
                tetrominoTiles[2] = new Tile(startingX + tileSize, startingY + tileSize, tetrominoType);
                tetrominoTiles[3] = new Tile(startingX + tileSize * 2, startingY + tileSize, tetrominoType);
            }
            case Z_TET ->
            {
                tetrominoTiles[0] = new Tile(startingX + tileSize, startingY, tetrominoType);
                tetrominoTiles[1] = new Tile(startingX + tileSize * 2, startingY, tetrominoType);
                tetrominoTiles[2] = new Tile(startingX + tileSize * 2, startingY + tileSize, tetrominoType);
                tetrominoTiles[3] = new Tile(startingX + tileSize * 3, startingY + tileSize, tetrominoType);
            }
            case O_TET ->
            {
                tetrominoTiles[0] = new Tile(startingX + tileSize, startingY, tetrominoType);
                tetrominoTiles[1] = new Tile(startingX + tileSize * 2, startingY, tetrominoType);
                tetrominoTiles[2] = new Tile(startingX + tileSize, startingY + tileSize, tetrominoType);
                tetrominoTiles[3] = new Tile(startingX + tileSize * 2, startingY + tileSize, tetrominoType);
            }
            case S_TET ->
            {
                tetrominoTiles[0] = new Tile(startingX + tileSize, startingY, tetrominoType);
                tetrominoTiles[1] = new Tile(startingX + tileSize * 2, startingY, tetrominoType);
                tetrominoTiles[2] = new Tile(startingX, startingY + tileSize, tetrominoType);
                tetrominoTiles[3] = new Tile(startingX + tileSize, startingY + tileSize, tetrominoType);
            }
            case L_TET ->
            {
                tetrominoTiles[0] = new Tile(startingX + tileSize * 3, startingY, tetrominoType);
                tetrominoTiles[1] = new Tile(startingX + tileSize, startingY + tileSize, tetrominoType);
                tetrominoTiles[2] = new Tile(startingX + tileSize * 2, startingY + tileSize, tetrominoType);
                tetrominoTiles[3] = new Tile(startingX + tileSize * 3, startingY + tileSize, tetrominoType);
            }
            case I_TET ->
            {
                tetrominoTiles[0] = new Tile(startingX, startingY, tetrominoType);
                tetrominoTiles[1] = new Tile(startingX + tileSize, startingY, tetrominoType);
                tetrominoTiles[2] = new Tile(startingX + tileSize * 2, startingY, tetrominoType);
                tetrominoTiles[3] = new Tile(startingX + tileSize * 3, startingY, tetrominoType);
            }
        }
    }

    /**
     * Reset Tetromino Tiles to starting positions based on its type.
     * @param startingX Starting Tetromino x position.
     * @param startingY Starting Tetromino y position.
     */
    public void resetTetromino(int startingX, int startingY)
    {
        switch (tetrominoType)
        {
            case T_TET ->
            {
                Helper.resetTileToDefault(startingX + tileSize, startingY, tetrominoType, tetrominoTiles, 0);
                Helper.resetTileToDefault(startingX, startingY + tileSize, tetrominoType, tetrominoTiles, 1);
                Helper.resetTileToDefault(startingX + tileSize, startingY + tileSize, tetrominoType, tetrominoTiles, 2);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY + tileSize, tetrominoType, tetrominoTiles, 3);
            }
            case J_TET ->
            {
                Helper.resetTileToDefault(startingX, startingY, tetrominoType, tetrominoTiles, 0);
                Helper.resetTileToDefault(startingX, startingY + tileSize, tetrominoType, tetrominoTiles, 1);
                Helper.resetTileToDefault(startingX + tileSize, startingY + tileSize, tetrominoType, tetrominoTiles, 2);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY + tileSize, tetrominoType, tetrominoTiles, 3);
            }
            case Z_TET ->
            {
                Helper.resetTileToDefault(startingX + tileSize, startingY, tetrominoType, tetrominoTiles, 0);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY, tetrominoType, tetrominoTiles, 1);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY + tileSize, tetrominoType, tetrominoTiles, 2);
                Helper.resetTileToDefault(startingX + tileSize * 3, startingY + tileSize, tetrominoType, tetrominoTiles, 3);
            }
            case O_TET ->
            {
                Helper.resetTileToDefault(startingX + tileSize, startingY, tetrominoType, tetrominoTiles, 0);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY, tetrominoType, tetrominoTiles, 1);
                Helper.resetTileToDefault(startingX + tileSize, startingY + tileSize, tetrominoType, tetrominoTiles, 2);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY + tileSize, tetrominoType, tetrominoTiles, 3);
            }
            case S_TET ->
            {
                Helper.resetTileToDefault(startingX + tileSize, startingY, tetrominoType, tetrominoTiles, 0);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY, tetrominoType, tetrominoTiles, 1);
                Helper.resetTileToDefault(startingX, startingY + tileSize, tetrominoType, tetrominoTiles, 2);
                Helper.resetTileToDefault(startingX + tileSize, startingY + tileSize, tetrominoType, tetrominoTiles, 3);
            }
            case L_TET ->
            {
                Helper.resetTileToDefault(startingX + tileSize * 3, startingY, tetrominoType, tetrominoTiles, 0);
                Helper.resetTileToDefault(startingX + tileSize, startingY + tileSize, tetrominoType, tetrominoTiles, 1);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY + tileSize, tetrominoType, tetrominoTiles, 2);
                Helper.resetTileToDefault(startingX + tileSize * 3, startingY + tileSize, tetrominoType, tetrominoTiles, 3);
            }
            case I_TET ->
            {
                Helper.resetTileToDefault(startingX, startingY, tetrominoType, tetrominoTiles, 0);
                Helper.resetTileToDefault(startingX + tileSize, startingY, tetrominoType, tetrominoTiles, 1);
                Helper.resetTileToDefault(startingX + tileSize * 2, startingY, tetrominoType, tetrominoTiles, 2);
                Helper.resetTileToDefault(startingX + tileSize * 3, startingY, tetrominoType, tetrominoTiles, 3);
            }
        }
    }

    /**
     * Drop the Tetromino by 'distanceToFall' if there is room.
     * If not, then place the Tetromino.
     * @param boardTiles Game board Tiles - used to check for placed Tiles on the game board.
     */
    public void fall(Tile[][] boardTiles)
    {
        // Calculate the distance to the Tetromino's placement location.
        distanceToPlacement =  Helper.getTetrominoDropDistance(tetrominoTiles, boardTiles);

        // If there is room, drop the Tetromino by 1 tile space.
        if (distanceToPlacement >= Constants.TILE_SIZE)
        {
            for (Tile tile : tetrominoTiles)
                tile.setY(tile.getY() + Constants.TILE_SIZE);
        }

        // If there is no room to drop, then place the Tetromino.
        if (distanceToPlacement <= Constants.TILE_SIZE)
        {
            for (Tile tetrominoTile : tetrominoTiles)
                tetrominoTile.setIsPlacedTetrominoTile(true);
        }
    }

    /**
     * Instantly drop the Tetromino to its placement position, indicated by the preview Tiles on the game board.
     * Calculate the number of rows hard dropped and increment accordingly in order to accurately calculate score.
     * @param boardTiles Game board Tiles - used to determine placement location.
     */
    public void hardDrop(Tile[][] boardTiles)
    {
        // Calculate Tetromino placement location and number of rows hard dropped, and then update Tetromino position
        // to the placement position.
        incrementRowsHardDropped(boardTiles);

        // After dropping Tetromino, place it.
        for (Tile tile : tetrominoTiles)
            tile.setIsPlacedTetrominoTile(true);
    }

    /**
     * Move the Tetromino in the specified direction by 1 tile space.
     * @param dir The direction of input.
     * @param boardTiles Game board Tiles - used to check if movement was legal or not.
     */
    public void move(Direction dir, Tile[][] boardTiles)
    {
        movedOutOfBounds = false;
        int distanceToBorder = Helper.getDistanceToBorder(dir, tetrominoTiles);
        int offset = 0;

        switch (dir)
        {
            case LEFT -> offset = -1;
            case RIGHT -> offset = 1;
        }

        // Check to see if desired movement is legal.
        for (Tile tile : tetrominoTiles)
        {
            row = Helper.getRow(tile);
            col = Helper.getCol(tile);

            if (col > 0 && col < Constants.GAMEBOARD_COLS - 1)
            {
                if (boardTiles[row][col + offset].getIsPlacedTetrominoTile())
                {
                    movedOutOfBounds = true;
                    break;
                }
            }

            if (distanceToBorder < Constants.TILE_SIZE)
            {
                movedOutOfBounds = true;
                break;
            }
        }

        // If movement is legal, then move tetromino by 1 tile space in the desired direction.
        if (!movedOutOfBounds)
        {
            for (Tile tile : tetrominoTiles)
                tile.setX(tile.getX() + offset * Constants.TILE_SIZE);
        }
    }

    /**
     * Rotate the Tetromino Tiles around its axis Tile in the desired direction using matrices.
     * @param dir The direction of input.
     * @param boardTiles Game board Tiles.
     */
    public void rotate(Direction dir, Tile[][] boardTiles)
    {
        if (!tetrominoType.equals(TileType.O_TET))
        {
            // Create a list that holds the homogeneous position vector of each tile.
            // Also create lists to hold initial x and y positions in case rotation is illegal and
            // the Tetromino needs to be returned to its initial position.
            int[] initial_x_positions = new int[tetrominoTiles.length];
            int[] initial_y_positions = new int[tetrominoTiles.length];
            int i = 0;

            for (Tile tile : tetrominoTiles)
            {
                vector = new int[]{tile.getX(), tile.getY(), 1};
                tileVectors[i] = vector;
                initial_x_positions[i] = tile.getX();
                initial_y_positions[i] = tile.getY();
                i++;
            }

            // translation
            assembleMatrix("t");
            reassignVectors();

            // rotation
            assembleMatrix(dir);
            reassignVectors();

            // detranslation
            assembleMatrix("d");
            reassignVectors();

            // Lastly, update the positions of each Tile in the tetromino to the new position
            int listIndex = 0;
            int xOffset = 0;
            int yOffset = 0;
            int xDifference = 0;
            int yDifference = 0;

            for (Tile tile : tetrominoTiles)
            {
                // Set each tile to its new rotated position
                tile.setX(tileVectors[listIndex][0]);
                tile.setY(tileVectors[listIndex][1]);

                // If any of the tiles have been rotated off the board, get the distance between the furthest one and the board
                if (tile.getX() < Constants.GAMEBOARD_STARTING_X)
                    xOffset = Constants.GAMEBOARD_STARTING_X - tile.getX();
                else if (tile.getX() > Constants.GAMEBOARD_ENDING_X)
                    xOffset = Constants.GAMEBOARD_ENDING_X - tile.getX();
                else if (tile.getY() < Constants.GAMEBOARD_STARTING_Y)
                    yOffset = Constants.GAMEBOARD_STARTING_Y - tile.getY();
                else if (tile.getY() > Constants.GAMEBOARD_ENDING_Y)
                    yOffset = Constants.GAMEBOARD_ENDING_Y - tile.getY();

                listIndex++;
            }

            // Add the distance between the furthest tile away from the board and the board to each tile
            for (Tile tile : tetrominoTiles)
            {
                tile.setX(tile.getX() + xOffset);
                tile.setY(tile.getY() + yOffset);
            }

            xOffset = 0;
            yOffset = 0;

            boolean isRotationIllegal = false;

            // If the rotation results in a collision with a placed Tile, move the Tetromino over accordingly.
            // checkForCollision method
            for (Tile tile : tetrominoTiles)
            {
                row = Helper.getRow(tile);
                col = Helper.getCol(tile);

                if (boardTiles[row][col].getIsPlacedTetrominoTile())
                {
                    // get direction of axis tile
                    switch (tetrominoType)
                    {
                        case T_TET, J_TET, Z_TET, L_TET ->
                        {
                            xDifference = tetrominoTiles[2].getX() - tile.getX();
                            yDifference = tetrominoTiles[2].getY() - tile.getY();
                        }
                        case S_TET ->
                        {
                            xDifference = tetrominoTiles[3].getX() - tile.getX();
                            yDifference = tetrominoTiles[3].getY() - tile.getY();
                        }
                        case I_TET ->
                        {
                            xDifference = tetrominoTiles[1].getX() - tile.getX();
                            yDifference = tetrominoTiles[1].getY() - tile.getY();
                        }
                    }

                    if (xDifference != -yDifference)
                    {
                        if (xDifference < 0)
                            xOffset += -tileSize;
                        else if (xDifference > 0)
                            xOffset += tileSize;
                        else if (yDifference < 0)
                            yOffset += -tileSize;
                        else yOffset += tileSize;
                    }
                }
            }

            // If rotation results in Tetromino leaving the game board boundaries, move it accordingly.
            // checkForOutOfBounds method
            for (Tile tile : tetrominoTiles)
            {
                tile.setX(tile.getX() + xOffset);
                tile.setY(tile.getY() + yOffset);

                if (tile.getX() < Constants.GAMEBOARD_STARTING_X || tile.getX() > Constants.GAMEBOARD_ENDING_X || tile.getY() < Constants.GAMEBOARD_STARTING_Y || tile.getY() > Constants.GAMEBOARD_ENDING_Y)
                    isRotationIllegal = true;
                else
                {
                    row = Helper.getRow(tile);
                    col = Helper.getCol(tile);

                    if (boardTiles[row][col].getIsPlacedTetrominoTile())
                        isRotationIllegal = true;
                }
            }

            // If rotation was illegal, then reset Tetromino back to its initial position.
            if (isRotationIllegal)
            {
                int index = 0;

                for (Tile tile : tetrominoTiles)
                {
                    tile.setX(initial_x_positions[index]);
                    tile.setY(initial_y_positions[index]);
                    index++;
                }
            }
        }
    }

    /**
     * Set the Tetromino's type to the TileType parameter.
     * @param newType New TileType.
     */
    public void setTetrominoType(TileType newType)
    {
        tetrominoType = newType;
    }

    /**
     * Generate a random TileType and assign the Tetromino to that new type.
     * @return
     */
    public TileType generateNewType()
    {
        Random rand = new Random();

        switch (rand.nextInt(7))
        {
            case 0 ->
            {
                return TileType.T_TET;
            }

            case 1 ->
            {
                return TileType.J_TET;
            }
            case 2 ->
            {
                return TileType.Z_TET;
            }
            case 3 ->
            {
                return TileType.O_TET;
            }
            case 4 ->
            {
                return TileType.S_TET;
            }
            case 5 ->
            {
                return TileType.L_TET;
            }
            case 6 ->
            {
                return TileType.I_TET;
            }
            default ->
            {
                return null;
            }
        }
    }

    /**
     * @return The Tetromino's TileType.
     */
    public TileType getTetrominoType()
    {
        return tetrominoType;
    }

    /**
     * @return The Tetromino's Tile array.
     */
    public Tile[] getTiles()
    {
        return tetrominoTiles;
    }

    /**
     * Increment number of rows soft dropped.
     */
    public void incrementRowsSoftDropped()
    {
        rowsSoftDropped++;
    }

    /**
     * @return Number of rows soft dropped
     */
    public int getRowsSoftDropped()
    {
        return rowsSoftDropped;
    }

    /**
     * Calculate distance to Tetromino placement location, move the Tetromino to that position, and increment the number
       of rows on the game board that were hard dropped.
     * @param boardTiles Game board Tiles.
     */
    public void incrementRowsHardDropped(Tile[][] boardTiles)
    {
        // Create lists to store the y-position of each tile before and after hard dropping, as well as a list to store the distance
        // from each tile to either the bottom of the board or the closest placed tile on the board.
        int[] preDropTileRows = new int[tetrominoTiles.length];
        int[] postDropTileRows = new int[tetrominoTiles.length];

        // Get the row of each tetromino tile.
        int index = 0;
        for (Tile tile : tetrominoTiles)
        {
            preDropTileRows[index] = Helper.getRow(tile);
            index++;
        }

        // Sort current rows into descending order to get the largest row first.
        Helper.sortDescending(preDropTileRows);

        // Calculate the distance to placement for the tetromino.
        distanceToPlacement = Helper.getTetrominoDropDistance(tetrominoTiles, boardTiles);

        // Get the row of the placement location for each tetromino tile.
        index = 0;
        for (Tile tile : tetrominoTiles)
        {
            postDropTileRows[index] = Helper.getRow(tile, distanceToPlacement);
            index++;
        }

        // Sort placement location rows into descending order so we have the largest first.
        Helper.sortDescending(postDropTileRows);

        // Add the distance to placement to each tetromino tile.
        for (Tile tile : tetrominoTiles)
        {
            if (distanceToPlacement >= Constants.TILE_SIZE)
                tile.setY(tile.getY() + distanceToPlacement);
        }

        // Calculate rows hard dropped.
        rowsHardDropped = postDropTileRows[0] - preDropTileRows[0];
    }

    /**
     * Reset number of rows soft and hard dropped.
     */
    public void resetRowsHardAndSoftDropped()
    {
        rowsHardDropped = 0;
        rowsSoftDropped = 0;
    }

    /**
     * @return Number of rows hard dropped.
     */
    public int getRowsHardDropped()
    {
        return rowsHardDropped;
    }

    /**
     * @return Status of whether the Tetromino has moved out of bounds or not.
     */
    public boolean getMovedOutOfBounds()
    {
        return movedOutOfBounds;
    }

    private void assembleMatrix(String matrixType)
    {
        int offset;

        if (matrixType.equals("t"))
            offset = -1;
        else
            offset = 1;

        switch (tetrominoType)
        {
            case T_TET, J_TET, L_TET, Z_TET -> matrix = new int[][]{
                    {1, 0, offset * tetrominoTiles[2].getX()},
                    {0, 1, offset * tetrominoTiles[2].getY()},
                    {0, 0, 1}
            };
            case S_TET -> matrix = new int[][]{
                    {1, 0, offset * tetrominoTiles[3].getX()},
                    {0, 1, offset * tetrominoTiles[3].getY()},
                    {0, 0, 1}
            };
            case I_TET -> matrix = new int[][]{
                    {1, 0, offset * tetrominoTiles[1].getX()},
                    {0, 1, offset * tetrominoTiles[1].getY()},
                    {0, 0, 1}
            };
        }
    }

    private void assembleMatrix(Direction dir)
    {
        switch (dir)
        {
            case LEFT -> matrix = new int[][]{
                    {(int)Math.round(Math.cos(Math.PI / 2)), (int)Math.round(Math.sin(Math.PI / 2)), 0},
                    {-(int)Math.round(Math.sin(Math.PI / 2)), (int)Math.round(Math.cos(Math.PI / 2)), 0},
                    {0, 0, 1}
            };
            case RIGHT -> matrix = new int[][]{
                    {(int)Math.round(Math.cos(Math.PI / 2)), -(int)Math.round(Math.sin(Math.PI / 2)), 0},
                    {(int)Math.round(Math.sin(Math.PI / 2)), (int)Math.round(Math.cos(Math.PI / 2)), 0},
                    {0, 0, 1}
            };
        }
    }

    private void reassignVectors()
    {
        int i = 0;

        for (int[] vector : tileVectors)
        {
            int[] newVector = new int[3];
            newVector[0] = vector[0] * matrix[0][0] + vector[1] * matrix[0][1] + vector[2] * matrix[0][2];
            newVector[1] = vector[0] * matrix[1][0] + vector[1] * matrix[1][1] + vector[2] * matrix[1][2];
            newVector[2] = vector[0] * matrix[2][0] + vector[1] * matrix[2][1] + vector[2] * matrix[2][2];
            tileVectors[i] = newVector;
            i++;
        }
    }

    public int getDistanceToPlacement()
    {
        return distanceToPlacement;
    }
}
