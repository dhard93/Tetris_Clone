package com.example.tetris_clone;

import java.util.*;

/**
 * This class contains static helper methods used throughout the program.
 */

public class Helper
{
    /**
     * Convert the int parameter into an ArrayList of Integers where each index is a number's place.
     * @param num The number to break into places.
     * @return The ArrayList of Integers.
     */
    public static ArrayList<Integer> breakNumIntoPlaces(int num)
    {
        ArrayList<Integer> places = new ArrayList<>();

        if (num >= 10)
        {
            while (num / 10 > 0)
            {
                places.add(num % 10);
                num /= 10;

                if (num < 10)
                    places.add(num);
            }
        }
        else
            places.add(num);

        return places;
    }

    /**
     * Calculate the distance from the 'tilesToCheck' to its placement position.
     * @param tilesToCheck The Tiles for which the placement distance is being calculated.
     * @param gameTiles Game board Tiles.
     * @return The distance to placement.
     */
    public static int getTetrominoDropDistance(Tile[] tilesToCheck, Tile[][] gameTiles)
    {
        ArrayList<Integer> tileDistances = new ArrayList<>();
        int row, col;
        boolean rowHasPlacedTiles;

        // Check the rows underneath each Tile to find any placed Tiles, if any exist.
        // When a placed Tile below the Tile being checked that is in the same column is found,
        // add the distance between the two to tile distances.
        for (Tile tile : tilesToCheck)
        {
            row = getRow(tile);
            col = getCol(tile);
            rowHasPlacedTiles = false;

            while (row < Constants.GAMEBOARD_ROWS - 1)
            {
                if (gameTiles[row + 1][col].getIsPlacedTetrominoTile())
                {
                    tileDistances.add(gameTiles[row][col].getY() - tile.getY());
                    rowHasPlacedTiles = true;
                    break;
                }

                row++;
            }

            // If the row has no placed Tiles, add the distance between the Tile being checked and the bottom of the
            // game board to tile distances.
            if (!rowHasPlacedTiles)
                tileDistances.add(Constants.GAMEBOARD_ENDING_Y - tile.getY());
        }

        // Sort distances in ascending order so that the shortest distance is first.
        sortAscending(tileDistances);

        return tileDistances.get(0);
    }

    /**
     * bubble sort arraylist in ascending order
     * @param arrayList The ArrayList to sort.
     */
    public static void sortAscending(ArrayList<Integer> arrayList)
    {
        int temp;

        for (int i = 0; i < arrayList.size(); i++)
        {
            for (int j = 1; j < arrayList.size(); j++)
            {
                if (arrayList.get(j - 1) > arrayList.get(j))
                {
                    temp = arrayList.get(j - 1);
                    arrayList.set(j - 1, arrayList.get(j));
                    arrayList.set(j, temp);
                }
            }
        }
    }

    /**
     * Bubble sort ArrayList in descending order.
     * @param arrayList The ArrayList to sort.
     */
    public static void sortDescending(ArrayList<Integer> arrayList)
    {
        int temp;

        for (int i = 0; i < arrayList.size(); i++)
        {
            for (int j = 1; j < arrayList.size(); j++)
            {
                if (arrayList.get(j - 1) < arrayList.get(j))
                {
                    temp = arrayList.get(j - 1);
                    arrayList.set(j - 1, arrayList.get(j));
                    arrayList.set(j, temp);
                }
            }
        }
    }

    /**
     * Bubble sort array in ascending order.
     * @param array The array to sort.
     */
    public static void sortAscending(int[] array)
    {
        int temp;

        for (int i = 0; i < array.length; i++)
        {
            for (int j = 1; j < array.length; j++)
            {
                if (array[j - 1] > array[j])
                {
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    /**
     * Bubble sort array in descending order.
     * @param array The array to sort.
     */
    public static void sortDescending(int[] array)
    {
        int temp;

        for (int i = 0; i < array.length; i++)
        {
            for (int j = 1; j < array.length; j++)
            {
                if (array[j - 1] < array[j])
                {
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    /**
     * Calculate the distance between the Tetromino and either the left or right game board border, depending on direction
       of input.
     * @param direction Direction of input.
     * @param tetrominoTiles Tetromino Tiles to check.
     * @return The distance between Tetromino and border.
     */
    public static int getDistanceToBorder(Direction direction, Tile[] tetrominoTiles)
    {
        int tileIndex = 0;
        int[] tileDistances = new int[tetrominoTiles.length];

        switch (direction) {
            case LEFT:
                for (Tile tile : tetrominoTiles) {
                    tileDistances[tileIndex] = tile.getX() - Constants.GAMEBOARD_STARTING_X;
                    tileIndex++;
                }
                break;
            case RIGHT:
                for (Tile tile : tetrominoTiles) {
                    tileDistances[tileIndex] = Constants.GAMEBOARD_ENDING_X - tile.getX();
                    tileIndex++;
                }
                break;
        }

        // Sort the distance array in ascending order so we can get the shortest distance of all the tiles
        sortAscending(tileDistances);
        return tileDistances[0];
    }

    /**
     * Reset a Tile to its default position depending on its TileType.
     * @param x Default x position.
     * @param y Default y position.
     * @param type TileType
     * @param tiles The Tetromino that the Tile belongs to.
     * @param index The index in the Tetromino that the Tile is in.
     */
    public static void resetTileToDefault(int x, int y, TileType type, Tile[] tiles, int index)
    {
        tiles[index].setX(x);
        tiles[index].setY(y);
        tiles[index].setTileType(type);
    }

    /**
     * Calculate the game board row number that a Tile is in based on its y position.
     * @param tile The Tile being checked.
     * @return The row number.
     */
    public static int getRow(Tile tile)
    {
        if (tile.getX() < Constants.NEXT_TETROMINO_STARTING_X)
            return (tile.getY() - Constants.GAMEBOARD_STARTING_Y) / Constants.TILE_SIZE;
        else
            return (tile.getY() - Constants.NEXT_DISPLAY_STARTING_Y) / Constants.TILE_SIZE;
    }

    /**
     * Calculate the game board column number that a Tile is in based on its x position.
     * @param tile The Tile being checked.
     * @return The column number.
     */
    public static int getCol(Tile tile)
    {
        if (tile.getX() < Constants.NEXT_TETROMINO_STARTING_X)
            return (tile.getX() - Constants.GAMEBOARD_STARTING_X) / Constants.TILE_SIZE;
        else
            return (tile.getX() - Constants.NEXT_DISPLAY_STARTING_X) / Constants.TILE_SIZE;
    }

    /**
     * Calculate the game board row number of the placement position of a Tile.
     * Used for Tetromino preview Tiles.
     * @param tile The Tile being checked.
     * @param distance The distance to placement of the Tile.
     * @return The row number of the placement position of the Tile.
     */
    public static int getRow(Tile tile, int distance)
    {
        return (tile.getY() + distance - Constants.GAMEBOARD_STARTING_Y) / Constants.TILE_SIZE;
    }

    /**
     * Convert a char to its corresponding TileType.
     * @param letter The char to convert.
     * @param typeSelector Indicates whether the TileType should be a white, cyan, or gold letter.
     * @return The corresponding TileType.
     */
    public static TileType convertCharToTileType(char letter, int typeSelector)
    {
        String type;

        switch (letter)
        {
            case 'A' -> type = TileType.A.toString();
            case 'B' -> type = TileType.B.toString();
            case 'C' -> type = TileType.C.toString();
            case 'D' -> type = TileType.D.toString();
            case 'E' -> type = TileType.E.toString();
            case 'F' -> type = TileType.F.toString();
            case 'G' -> type = TileType.G.toString();
            case 'H' -> type = TileType.H.toString();
            case 'I' -> type = TileType.I.toString();
            case 'J' -> type = TileType.J.toString();
            case 'K' -> type = TileType.K.toString();
            case 'L' -> type = TileType.L.toString();
            case 'M' -> type = TileType.M.toString();
            case 'N' -> type = TileType.N.toString();
            case 'O' -> type = TileType.O.toString();
            case 'P' -> type = TileType.P.toString();
            case 'Q' -> type = TileType.Q.toString();
            case 'R' -> type = TileType.R.toString();
            case 'S' -> type = TileType.S.toString();
            case 'T' -> type = TileType.T.toString();
            case 'U' -> type = TileType.U.toString();
            case 'V' -> type = TileType.V.toString();
            case 'W' -> type = TileType.W.toString();
            case 'X' -> type = TileType.X.toString();
            case 'Y' -> type = TileType.Y.toString();
            case 'Z' -> type = TileType.Z.toString();
            case '0' -> type = TileType.ZERO.toString();
            case '1' -> type = TileType.ONE.toString();
            case '2' -> type = TileType.TWO.toString();
            case '3' -> type = TileType.THREE.toString();
            case '4' -> type = TileType.FOUR.toString();
            case '5' -> type = TileType.FIVE.toString();
            case '6' -> type = TileType.SIX.toString();
            case '7' -> type = TileType.SEVEN.toString();
            case '8' -> type = TileType.EIGHT.toString();
            case '9' -> type = TileType.NINE.toString();
            default -> type = TileType.EMPTY.toString();
        }

        switch (typeSelector)
        {
            case 2, 3 ->
            {
                if (!Objects.equals(type, TileType.EMPTY.toString()))
                    type += typeSelector;
            }
        }

        return TileType.valueOf(type);
    }
}
