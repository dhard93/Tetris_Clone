package com.example.tetris_clone;

/**
 * This class uses keyboard event handlers to accept user input and edits game and menu data based on that input.
 */

import javafx.scene.Scene;

public class TetrisController
{
    private final Game game;
    boolean isLeftHeld = false;
    boolean isRightHeld = false;
    boolean isDownHeld = false;
    boolean isUpHeld = false;
    boolean is_z_held = false;
    boolean is_x_held = false;
    boolean isSpaceHeld = false;

    public TetrisController(Game newGame)
    {
        game = newGame;
        MenuData menuData = game.getMenuData();
        TetrisRenderer renderer = newGame.getRenderer();
        Scene currentScene = renderer.getScene();
        currentScene.setOnKeyPressed(event ->
        {
            // If the active menu is the 'playerNameTiles' menu, then use either keyboard letters or backspace to enter/delete
            // chars for when the player needs to enter their name after getting a high score.
            if (menuData.getActiveMenu() == 4)
            {
                switch (event.getCode())
                {
                    case    A, B, C, D, E, F,
                            G, H, I, J, K, L,
                            M, N, O, P, Q, R,
                            S, T, U, V, W, X,
                            Y, Z    ->
                    {
                        menuData.enterNameChar(event.getCode().getChar().charAt(0));
                        game.setMenuDataHasBeenChanged(true);
                    }

                    case BACK_SPACE ->
                    {
                        menuData.deleteNameChar();
                        game.setMenuDataHasBeenChanged(true);
                    }
                }
            }

            {
                // Left and Right arrow keys are used to move the current tetromino.
                switch (event.getCode())
                {
                    case LEFT  ->
                    {
                        if (game.getIsBeginning() && game.getGameActive() && game.getGamePaused())
                        {
                            if (!isLeftHeld)
                            {
                                isLeftHeld = true;
                                moveTetromino(Direction.LEFT, game.getCurrentTetromino(), game);
                            }
                        }
                    }
                    case RIGHT ->
                    {
                        if (game.getIsBeginning() && game.getGameActive() && game.getGamePaused())
                        {
                            if (!isRightHeld)
                            {
                                isRightHeld = true;
                                moveTetromino(Direction.RIGHT, game.getCurrentTetromino(), game);
                            }
                        }
                    }

                    // Down arrow key is used to navigate menu options when a menu is active, or soft drop the
                    // current tetromino when the game is active.
                    case DOWN ->
                    {
                        if (game.getIsMenuActive())
                        {
                            game.setMenuDataHasBeenChanged(true);

                            switch (menuData.getActiveMenu())
                            {
                                case 0, 1 ->
                                {
                                    menuData.setSelected(menuData.getSelected() + 1, menuData.getMenus()[menuData.getActiveMenu()]);
                                    menuData.updateMenuTileData();
                                }
                            }
                        }

                        if (game.getIsBeginning() && game.getGameActive() && game.getGamePaused())
                        {
                            if (!isDownHeld)
                            {
                                isDownHeld = true;
                                game.setIsDownPressed(true);
                            }
                        }
                    }
                    // The up arrow key is used to navigate menu options when a menu is active and to hard
                    // drop the current tetromino when the game is active.
                    case UP ->
                    {
                        if (game.getIsMenuActive())
                        {
                            game.setMenuDataHasBeenChanged(true);

                            switch (menuData.getActiveMenu())
                            {
                                case 0, 1 ->
                                {
                                    menuData.setSelected(menuData.getSelected() - 1, menuData.getMenus()[menuData.getActiveMenu()]);
                                    menuData.updateMenuTileData();
                                }
                            }
                        }

                        if (game.getGameActive() && game.getGamePaused())
                        {
                            if (!isUpHeld)
                            {
                                isUpHeld = true;
                                hardDropTetromino(game.getCurrentTetromino(), game);
                            }
                        }
                    }
                    // The z key is used to rotate the current tetromino counter-clockwise.
                    case Z ->
                    {
                        if (game.getGameActive() && game.getGamePaused())
                        {
                            if (!is_z_held)
                            {
                                is_z_held = true;
                                rotateTetromino(Direction.LEFT, game.getCurrentTetromino(), game);
                            }
                        }
                    }
                    // The x key is used to rotate the current tetromino clockwise.
                    case X ->
                    {
                        if (game.getGameActive() && game.getGamePaused())
                        {
                            if (!is_x_held)
                            {
                                is_x_held = true;
                                rotateTetromino(Direction.RIGHT, game.getCurrentTetromino(), game);
                            }
                        }
                    }
                    // The space and enter keys can be used to either click on the selected menu option if a menu is active,
                    // or to play/pause the game if the game is active.
                    case SPACE, ENTER ->
                    {
                        if (!isSpaceHeld)
                        {
                            isSpaceHeld = true;

                            if (game.getInBetweenGamesStatus())
                            {
                                // Discard menu 4 (enterName)
                                // Instead, need to use same menu.
                                if (menuData.getActiveMenu() == 4)
                                {
                                    game.setMenuDataHasBeenChanged(true);

                                    if (menuData.getCanSaveName())
                                    {
                                        game.setInBetweenGamesStatus(false);
                                        game.playButtonSound();
                                        game.getDatabase().editEntries(menuData.getPlayerName(), game.getPlace(), game.getTopPlayersData());
                                        menuData.setTopScoresData(game.getTopPlayersData());
                                        renderer.openMenu(4, 0, menuData);
                                        game.resetGame();
                                    }
                                    else
                                        game.playOutOfBoundsSound();
                                }
                                else
                                {
                                    game.playButtonSound();
                                    game.setInBetweenGamesStatus(false);
                                    game.resetGame();
                                    renderer.openMenu(4, 0, menuData);
                                }

                            }
                            else
                            {
                                game.playButtonSound();
                                game.setMenuDataHasBeenChanged(true);

                                switch(menuData.getActiveMenu())
                                {
                                    case 0 ->
                                    {
                                        switch (menuData.getSelected())
                                        {
                                            case 0 ->
                                            {
                                                // close start menu and start new game
                                                if (!game.getGameHasBeenRestarted())
                                                {
                                                    game.resetGame();
                                                    game.setGameHasBeenRestarted(true);
                                                }

                                                game.setGameActive(true);
                                                game.setIsMenuActive(false);
                                                renderer.closeMenu();
                                                menuData.setActiveMenu(1);
                                                game.getSoundPlayer().startMusic();
                                                game.getTetrominoDropper().start();
                                            }
                                            case 1 -> renderer.openMenu(5, 2, menuData);
                                            case 2 -> renderer.openMenu(6, 3, menuData);
                                            case 3 -> game.shutdownGame();
                                        }
                                    }
                                    case 1 ->
                                    {
                                        if (game.getGamePaused())
                                        {
                                            game.pauseGame();
                                            game.getSoundPlayer().stopMusic();
                                            game.getTetrominoDropper().stop();
                                            game.setIsMenuActive(true);
                                            renderer.openMenu(4, 1, menuData);
                                        }
                                        else
                                        {
                                            switch (menuData.getSelected())
                                            {
                                                case 0 ->
                                                {
                                                    game.resumeGame();
                                                    game.getTetrominoDropper().start();
                                                    game.setIsMenuActive(false);
                                                    renderer.closeMenu();
                                                    game.getSoundPlayer().resumeMusic();
                                                }
                                                case 1 -> renderer.openMenu(5, 2, menuData);
                                                case 2 -> renderer.openMenu(6, 3, menuData);
                                                case 3 ->
                                                {
                                                    // End the current game and return to start menu.
                                                    game.setGamePaused(true);
                                                    game.gameOver(true);
                                                    game.setInBetweenGamesStatus(false);
                                                    game.resetGame();
                                                }
                                            }
                                        }
                                    }
                                    case 2, 3 ->
                                    {
                                        if (game.getGameActive())
                                            renderer.openMenu(4, 1, menuData);
                                        else
                                            renderer.openMenu(4, 0, menuData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        currentScene.setOnKeyReleased(event ->
        {
            switch (event.getCode())
            {
                case LEFT  -> isLeftHeld  = false;
                case RIGHT -> isRightHeld = false;
                case DOWN  ->
                {
                    game.setIsDownPressed(false);
                    isDownHeld  = false;
                }
                case UP    -> isUpHeld = false;
                case Z     -> is_z_held   = false;
                case X     -> is_x_held   = false;
                case SPACE, ENTER -> isSpaceHeld = false;
            }
        });
    }

    /**
     * The moveTetromino method moves the tetromino tiles according to direction, updates the game board tiles
       to match the tetromino tiles, and renders the updated sprites.
     * @param dir           Direction of user input
     * @param tetromino     The tetromino being moved
     * @param game          The current game
     */
    public static void moveTetromino(Direction dir, Tetromino tetromino, Game game)
    {
        switch (dir)
        {
            case LEFT  ->
            {
                if (game.isAnimationActive())
                {
                    tetromino.move(Direction.LEFT, game.getGameData().getGameboard());

                    if (tetromino.getMovedOutOfBounds())
                        game.playOutOfBoundsSound();
                }
            }
            case RIGHT ->
            {
                if (game.isAnimationActive())
                {
                    tetromino.move(Direction.RIGHT, game.getGameData().getGameboard());

                    if (tetromino.getMovedOutOfBounds())
                        game.playOutOfBoundsSound();
                }
            }
        }
    }

    /**
     * The rotateTetromino method rotates the tetromino by rotating each of its tiles
       around an axis tile by 90 degrees in the direction specified by user input.
     * @param dir           The direction of user input
     * @param tetromino     The tetromino being rotated
     */
    public static void rotateTetromino(Direction dir, Tetromino tetromino, Game game)
    {
        switch (dir)
        {
            case LEFT  ->
            {
                if (game.isAnimationActive())
                {
                    tetromino.rotate(Direction.LEFT, game.getGameData().getGameboard());
                    game.playRotationSound();
                }
            }
            case RIGHT ->
            {
                if (game.isAnimationActive())
                {

                    tetromino.rotate(Direction.RIGHT, game.getGameData().getGameboard());
                    game.playRotationSound();
                }
            }
        }
    }

    /**
     * The hardDropTetromino method calls the Tetromino's hardDrop method and sets the game's 'isUpPressed' flag to true
       so that the game knows that the tetromino was hard dropped.
     * @param tetromino The game's current tetromino
     * @param game The active game.
     */
    public void hardDropTetromino(Tetromino tetromino, Game game)
    {
        if (game.getGameActive())
        {
            tetromino.hardDrop(game.getGameData().getGameboard());
            game.setIsUpPressed(true);
        }
    }
}
