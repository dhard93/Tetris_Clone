package com.example.tetris_clone;

/**
 * This class represents a 2D, 32x32 px square that has an x,y position and a type, and can either be placed or not.
 */

public class Tile
{
    private int x;                                          // x position of the Tile
    private int y;                                          // y position of the Tile
    private TileType type;                                  // tells renderer what image to draw for the Tile
    private boolean isPlacedTetrominoTile;                  // placed Tiles are drawn onto the game board

    public Tile(int xPos, int yPos, TileType tiletype)
    {
        x = xPos;
        y = yPos;
        type = tiletype;
        isPlacedTetrominoTile = false;
    }

    /**
     * Assign a new x position to the Tile.
     * @param newX The Tile's new x position.
     */
    public void setX(int newX)
    {
        x = newX;
    }

    /**
     * Assign a new y position to the Tile.
     * @param newY The Tile's new y position.
     */
    public void setY(int newY)
    {
        y = newY;
    }

    /**
     * Assign a new TileType to the Tile.
     * @param newTileType The Tile's new TileTYpe..
     */
    public void setTileType(TileType newTileType)
    {
        type = newTileType;
    }

    /**
     * Set the placement status to the Tile.
     * @param status The Tile's new placement status.
     */
    public void setIsPlacedTetrominoTile(boolean status)
    {
        isPlacedTetrominoTile = status;
    }

    /**
     * @return The Tile's x position.
     */
    public int getX()
    {
        return x;
    }

    /**
     * @return The Tile's y position.
     */
    public int getY()
    {
        return y;
    }

    /**
     * @return The Tile's TileType.
     */
    public TileType getType()
    {
        return type;
    }

    /**
     * @return The Tile's placement status.
     */
    public boolean getIsPlacedTetrominoTile()
    {
        return isPlacedTetrominoTile;
    }
}
