package com.example.tetris_clone;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is used to render graphics using the Tiles of the gameData, menuData, and Tetrominos using Images.
 */

public class TetrisRenderer
{
    private final Scene scene;
    private final GraphicsContext gameGC;
    private final GraphicsContext menuGC;
    private final GraphicsContext tetrominoGC;
    private final int tileSize;
    private final ArrayList<Image> gameImages;                // Stores all Images used in the game.

    public TetrisRenderer()
    {
        tileSize = Constants.TILE_SIZE;

        // Create components and containers
        gameImages = new ArrayList<>();
        Canvas gameCanvas = new Canvas(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        Canvas menuCanvas = new Canvas(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        Canvas tetrominoCanvas = new Canvas(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        gameGC = gameCanvas.getGraphicsContext2D();
        menuGC = menuCanvas.getGraphicsContext2D();
        tetrominoGC = tetrominoCanvas.getGraphicsContext2D();
        Pane root = new Pane();
        root.getChildren().addAll(gameCanvas, tetrominoCanvas, menuCanvas);

        // Create a scene and connect it to the stylesheet
        scene = new Scene(root);

        // Load game images and store in list.
        loadImages();

        // draw game screen.
        gameGC.drawImage(gameImages.get(0), 0,0);
    }

    /**
     * Change the image displayed on the game canvas.
     * @param index The index of the desired Images in 'gameImages'.
     */
    public void changeBackgroundImage(int index)
    {
        gameGC.drawImage(gameImages.get(index), 0, 0);
    }

    /**
     * Display the active menu on the menu canvas.
     * @param imageIndex The index of the image for the active menu in 'gameImages'.
     * @param menuIndex The index of the active menu in 'menuData's' list of menus.
     * @param menuData The menuData instance of the current game.
     */
    public void openMenu(int imageIndex, int menuIndex, MenuData menuData)
    {
        // Set the active menu of 'menuData' to the value of 'menuIndex'.
        menuData.setActiveMenu(menuIndex);

        // Set the selected menu option (the option that is highlighted initially) to the first option, unless the active menu is
        // the 'top scores' menu, in which case the last menu option (BACK) should be highlighted.
        if (menuData.getActiveMenu() == 3)
            menuData.setSelected(menuData.getMenus()[menuData.getActiveMenu()].length - 1, menuData.getMenus()[menuData.getActiveMenu()]);
        else
            menuData.setSelected(0, menuData.getMenus()[menuData.getActiveMenu()]);

        // Update the Tiles for the menu unless the active menu is the 'playerNameTiles' menu.
        if (menuData.getActiveMenu() != 4)
            menuData.updateMenuTileData();

        // Draw the menu to the menu canvas.
        menuGC.drawImage(gameImages.get(imageIndex), Constants.GAMEBOARD_STARTING_X, Constants.GAMEBOARD_STARTING_Y);
    }

    /**
     * Clear the menu canvas.
     */
    public void closeMenu()
    {
        menuGC.clearRect(Constants.GAMEBOARD_STARTING_X, Constants.GAMEBOARD_STARTING_Y, Constants.GAMEBOARD_ENDING_X - Constants.GAMEBOARD_STARTING_X + Constants.TILE_SIZE, Constants.GAMEBOARD_ENDING_Y - Constants.GAMEBOARD_STARTING_Y + Constants.TILE_SIZE);
    }

    /**
     * Draw all Tiles contained in 'gameData' to the screen at once.
     * @param gameData The current game's GameData.
     */
    public void renderAllGameGraphics(GameData gameData)
    {
        for (int statsRow = 0; statsRow < gameData.getStats().length; statsRow++)
            drawGameDataSprites(gameData, gameData.getStats()[statsRow]);

        drawGameDataSprites(gameData, gameData.getLines());
        drawGameDataSprites(gameData, gameData.getTopScore());
        drawGameDataSprites(gameData, gameData.getScore());
        drawGameDataSprites(gameData, gameData.getLevel());
        drawGameSprites(gameData.getGameboard());
        drawGameSprites(gameData.getNextDisplay());
    }

    /**
     * Draw the correct images to the screen for each Tile's TileType.
     * @param gameData The current game's GameData.
     * @param tiles The Tiles to draw images for.
     */
    public void drawGameDataSprites(GameData gameData, Tile[] tiles)
    {
        Image spriteSheet;

        if (Arrays.equals(tiles, gameData.getTetrisDisplay()))
            spriteSheet = gameImages.get(3);
        else
            spriteSheet = gameImages.get(1);

        PixelReader reader = spriteSheet.getPixelReader();
        WritableImage sprite;
        int x;
        int y = 64;

        // identify the correct image for each TileType
        for (Tile tile : tiles)
        {
            x = 0;

            switch (tile.getType())
            {
                case ZERO -> x += 0;
                case ONE -> x = 32;
                case TWO -> x = 64;
                case THREE -> x = 96;
                case FOUR -> x = 128;
                case FIVE -> x = 160;
                case SIX -> x = 192;
                case SEVEN -> x = 224;
                case EIGHT -> x = 256;
                case NINE -> x = 288;
                case    T, T90, T180, T240,
                        E, E90, E180, E240,
                        S, S180 ->
                {
                    y = 64;

                    switch (tile.getType())
                    {
                        case T -> x += 0;
                        case T90 -> x = 32;
                        case T180 -> x = 64;
                        case T240 -> x = 96;
                        case E -> x = 128;
                        case E90 -> x = 160;
                        case E180 -> x = 192;
                        case E240 -> x = 224;
                        case S -> x = 256;
                        case S180 -> x = 288;
                    }
                }
                case R, R90, R180, R240, I, I180 ->
                {
                    y = 96;

                    switch (tile.getType())
                    {
                        case R -> x += 0;
                        case R90 -> x = 32;
                        case R180 -> x = 64;
                        case R240 -> x = 96;
                        case I -> x = 128;
                        case I180 -> x = 160;
                    }
                }
                case EMPTY ->
                {
                    x = 224;
                    y = 96;
                }
            }

            // draw the image at the specified location
            sprite = new WritableImage(reader, x, y, tileSize, tileSize);
            gameGC.drawImage(sprite, tile.getX(), tile.getY());
        }
    }

    /**
     * Draw the correct images for each Tile in the menu passed in.
     * @param menu The menu to draw images for.
     */
    public void drawMenuSprites(Tile[][] menu)
    {
        // menuSpriteSheet uses the image with white letters.
        // menuSpriteSheet2 uses the image with gold letters.
        // selectedSpriteSheet uses the image with cyan letters.
        Image menuSpriteSheet = gameImages.get(1);
        Image menuSpriteSheet2 = gameImages.get(3);
        Image selectedSpriteSheet = gameImages.get(2);

        PixelReader menuReader = menuSpriteSheet.getPixelReader();
        PixelReader menuReader2 = menuSpriteSheet2.getPixelReader();
        PixelReader selectedReader = selectedSpriteSheet.getPixelReader();
        WritableImage sprite;
        int x;
        int y;

        for (Tile[] menuOption : menu)
        {
            for (Tile tile : menuOption)
            {
                x = 0;

                // TileTypes with just a letter are for white letters, TileTypes ending in 2 are cyan letters,
                // and TileTypes ending in 3 are gold letters.
                switch (tile.getType())
                {
                    case    A, A2, A3, B, B2, B3, C, C2, C3,
                            I, I2, I3, L, L2, L3, R, R2, R3,
                            S, S2, S3, T, T2, T3, N, N2, N3,
                            O, O2, O3, P, P2, P3, V, V2, V3,
                            X, X2, X3 ->
                    {
                        y = 0;

                        switch (tile.getType())
                        {
                            case A, A2, A3 -> x += 0;
                            case B, B2, B3 -> x = 32;
                            case C, C2, C3 -> x = 64;
                            case I, I2, I3 -> x = 96;
                            case L, L2, L3 -> x = 128;
                            case R, R2, R3 -> x = 160;
                            case S, S2, S3 -> x = 192;
                            case T, T2, T3 -> x = 224;
                            case N, N2, N3 -> x = 256;
                            case O, O2, O3 -> x = 288;
                            case P, P2, P3 -> x = 320;
                            case V, V2, V3 -> x = 352;
                            case X, X2, X3 -> x = 384;
                        }
                    }

                    case    U, U2, U3, M, M2, M3, W, W2, W3,
                            G, G2, G3, Q, Q2, Q3, E, E2, E3,
                            D, D2, D3, F, F2, F3, H, H2, H3,
                            J, J2, J3, K, K2, K3, Y, Y2, Y3,
                            Z, Z2, Z3 ->
                    {
                        y = 32;

                        switch (tile.getType())
                        {
                            case M, M2, M3 -> x = 32;
                            case W, W2, W3 -> x = 64;
                            case G, G2, G3 -> x = 96;
                            case Q, Q2, Q3 -> x = 128;
                            case E, E2, E3 -> x = 160;
                            case D, D2, D3 -> x = 192;
                            case F, F2, F3 -> x = 224;
                            case H, H2, H3 -> x = 256;
                            case J, J2, J3 -> x = 288;
                            case K, K2, K3 -> x = 320;
                            case Y, Y2, Y3 -> x = 352;
                            case Z, Z2, Z3 -> x = 384;
                        }
                    }

                    case    ZERO, ONE, TWO,
                            THREE, FOUR, FIVE,
                            SIX, SEVEN, EIGHT,
                            NINE ->
                    {
                        y = 64;

                        switch (tile.getType())
                        {
                            case ONE -> x = 32;
                            case TWO -> x = 64;
                            case THREE -> x = 96;
                            case FOUR -> x = 128;
                            case FIVE -> x = 160;
                            case SIX -> x = 192;
                            case SEVEN -> x = 224;
                            case EIGHT -> x = 256;
                            case NINE -> x = 288;
                        }
                    }
                    default ->
                    {
                        x = 224;
                        y = 96;
                    }
                }

                // draw the image at the specified location
                if (tile.getType().toString().endsWith("2"))
                    sprite = new WritableImage(selectedReader, x, y, tileSize, tileSize);
                else if (tile.getType().toString().endsWith("3"))
                    sprite = new WritableImage(menuReader2, x, y, tileSize, tileSize);
                else
                    sprite = new WritableImage(menuReader, x, y, tileSize, tileSize);

                menuGC.drawImage(sprite, tile.getX(), tile.getY());
            }
        }
    }

    /**
     * Clear the Tetromino and Preview canvases.
     */
    public void clearTetromino()
    {
        tetrominoGC.clearRect(Constants.GAMEBOARD_STARTING_X, Constants.GAMEBOARD_STARTING_Y, Constants.GAMEBOARD_ENDING_X - Constants.GAMEBOARD_STARTING_X + Constants.TILE_SIZE, Constants.GAMEBOARD_ENDING_Y - Constants.GAMEBOARD_STARTING_Y + Constants.TILE_SIZE);
    }

    /**
     * Draw the images for the tetromino Tiles to the tetromino canvas and preview Tiles to the preview canvas.
     * @param tetromino The game's current Tetromino
     */
    public void drawTetromino(Tetromino tetromino)
    {
        Image spriteSheet = gameImages.get(1);
        PixelReader reader = spriteSheet.getPixelReader();
        WritableImage sprite;

        int x = 0;
        int y = 96;

        clearTetromino();

        for (Tile tile : tetromino.getTiles())
        {
            switch (tile.getType())
            {
                case T_TET              -> x = 0;
                case J_TET              -> x = 32;
                case Z_TET              -> x = 64;
                case O_TET              -> x = 96;
                case S_TET              -> x = 128;
                case L_TET              -> x = 160;
                case I_TET              -> x = 192;
            }

            sprite = new WritableImage(reader, x, y, tileSize, tileSize);
            tetrominoGC.drawImage(sprite, tile.getX(), tile.getY());
        }
    }

    /**
     * Draw the images for all Tiles placed on the game board to the game canvas.
     * @param boardTiles The game board Tiles.
     */
    public void drawGameSprites(Tile[][] boardTiles)
    {
        Image spriteSheet = gameImages.get(1);
        PixelReader reader = spriteSheet.getPixelReader();
        WritableImage sprite;

        int x = 0;
        int y = 96;

        for (Tile[] row : boardTiles)
        {
            for (Tile tile : row)
            {
                switch (tile.getType())
                {
                    case T_TET              -> x = 0;
                    case J_TET              -> x = 32;
                    case Z_TET              -> x = 64;
                    case O_TET              -> x = 96;
                    case S_TET              -> x = 128;
                    case L_TET              -> x = 160;
                    case I_TET              -> x = 192;
                    case EMPTY              -> x = 224;
                    case PREVIEW            -> x = 320;
                    case WHITE              -> x = 352;
                }
                sprite = new WritableImage(reader, x, y, tileSize, tileSize);
                gameGC.drawImage(sprite, tile.getX(), tile.getY());
            }
        }
    }

    /**
     * Load image files as inputStreams and store as Images in a list for use by the TetrisRenderer.
     */
    private void loadImages()
    {
        int imgCount = 9;
        URL imgUrl = TetrisRenderer.class.getResource("/com/example/tetris_clone/Tetris_Images");
        String filePath;

        for (int i = 0; i < imgCount; i++)
        {
            assert imgUrl != null;
            filePath = String.format("%s/img%d.png", imgUrl, i);
            gameImages.add(new Image(filePath));
        }
    }

    /**
     * @return the TetrisRenderer's scene.
     */
    public Scene getScene()
    {
        return scene;
    }
}
