package com.example.tetris_clone;

/**
 * This class stores Tiles for all menus.
 * Each menu has a list of selectable options that can be navigated using arrow keys.
 * When a menu option is selected, its Tiles will be replaced with a different TileType to indicate that it is selected.
 * Tiles are stored in a 2D array, where each 1D array index corresponds to a particular menu option.
 */

public class MenuData
{
    // The Tile arrays are used to store the Tiles for each menu.
    // Each Tile array has a corresponding String array that contains the chars of the menu options that need to be displayed for each menu.
    // When a menu is opened, its Tiles are set to the TileTypes corresponding to the chars of its menu options.
    private final Tile[][][] menus;
    private final String[][] menuOptions;
    private final Tile[][] start;
    private final Tile[][] pause;
    private final Tile[][] controls;
    private final Tile[][] topScores;
    private final Tile[][] playerNameTiles;
    private final String[] startOptions;
    private final String[] pauseOptions;
    private final String[] controlsOptions;
    private final String[] topScoresOptions;
    private String[] playerName;
    private int nameMenuOption = 0;
    private int nameChar = 0;
    private int selectedMenuOption;
    private int activeMenu;
    private boolean canSaveName;

    /**
     * Constructor initializes all Tiles arrays and String arrays.
     */
    public MenuData()
    {
        startOptions = new String[]{" NEW GAME ", " CONTROLS ", "TOP SCORES", "   QUIT   "};
        pauseOptions = new String[]{"  RESUME  ", " CONTROLS ", "TOP SCORES", "   MENU   "};
        controlsOptions = new String[]{"BACK"};
        topScoresOptions = new String[]{"NAME1", "SCORE1", "NAME2", "SCORE2", "NAME3", "SCORE3", "          ","          ", "   BACK   "};
        playerName = new String[]{"    ", "SAVE"};

        start = new Tile[startOptions.length][];
        pause = new Tile[pauseOptions.length][];
        controls = new Tile[controlsOptions.length][];
        topScores = new Tile[topScoresOptions.length][];
        playerNameTiles = new Tile[playerName.length][];

        menus = new Tile[][][]{start, pause, controls, topScores, playerNameTiles};
        menuOptions = new String[][]{startOptions, pauseOptions, controlsOptions, topScoresOptions, playerName};
        activeMenu = 0;
        selectedMenuOption = 0;
        canSaveName = false;
    }

    /**
     * Initialize the Tiles in 'menu'.
     * @param menuOptions The menu options for 'menu' - used to determine the number of Tiles in 'menu'.
     * @param menu The menu whose Tiles are being initialized.
     * @param startX The x position of the first Tile in each Tile row.
     * @param endX The x position of the last Tile in each Tile row.
     * @param startY The y position of the first Tile in each Tile row.
     * @param endY The y position that, when reached, ends the loop.
     * @param xIncrement The horizontal distance between Tiles.
     * @param yIncrement The vertical distance between Tiles.
     */
    public void initializeMenuTileData(String[] menuOptions, Tile[][] menu, int startX, int endX, int startY, int endY, int xIncrement, int yIncrement)
    {
        int r = 0;
        int c = 0;

        for (String option : menuOptions)
        {
            menu[r] = new Tile[option.length()];
            r++;
        }

        r = 0;

        for (int y = startY; y < endY; y += yIncrement)
        {
            for (int x = startX; x <= endX; x += xIncrement)
            {
                menu[r][c] = new Tile(x, y, TileType.EMPTY);
                c++;

                if (c == menu[r].length)
                {
                    c = 0;
                    r++;
                }
            }
        }
    }

    /**
     * Initialize all menu Tiles.
     */
    public void mapInitialMenuData()
    {
        initializeMenuTileData(startOptions, start, Constants.GAMEBOARD_STARTING_X, Constants.GAMEBOARD_ENDING_X, Constants.GAMEBOARD_STARTING_Y + Constants.TILE_SIZE * 4, Constants.GAMEBOARD_ENDING_Y - Constants.TILE_SIZE * 2, Constants.TILE_SIZE, Constants.TILE_SIZE * 4);
        initializeMenuTileData(pauseOptions, pause, Constants.GAMEBOARD_STARTING_X, Constants.GAMEBOARD_ENDING_X, Constants.GAMEBOARD_STARTING_Y + Constants.TILE_SIZE * 4, Constants.GAMEBOARD_ENDING_Y - Constants.TILE_SIZE * 2, Constants.TILE_SIZE, Constants.TILE_SIZE * 4);
        initializeMenuTileData(controlsOptions, controls, Constants.GAMEBOARD_STARTING_X + Constants.TILE_SIZE * 3, Constants.GAMEBOARD_ENDING_X - Constants.TILE_SIZE * 3, Constants.GAMEBOARD_ENDING_Y, Constants.GAMEBOARD_ENDING_Y + Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE * 4);
        initializeMenuTileData(topScoresOptions, topScores, Constants.GAMEBOARD_STARTING_X, Constants.GAMEBOARD_ENDING_X, Constants.GAMEBOARD_STARTING_Y + Constants.TILE_SIZE * 3, Constants.GAMEBOARD_ENDING_Y + Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE * 2);
        initializeMenuTileData(playerName, playerNameTiles, Constants.PLAYER_NAME_STARTING_X, Constants.PLAYER_NAME_ENDING_X, Constants.PLAYER_NAME_STARTING_Y, Constants.PLAYER_NAME_ENDING_Y, Constants.PLAYER_NAME_X_INCREMENT, Constants.PLAYER_NAME_Y_INCREMENT);
        for (int i = 0; i < 4; i++)
            playerNameTiles[1][i].setX(playerNameTiles[1][i].getX() - Constants.PLAYER_NAME_SAVE_OFFSET);

        activeMenu = 0;
        setSelected(0, menus[activeMenu]);
        updateMenuTileData();
    }

    /**
     * Update the TileTypes of the Tiles in the active menu to reflect the chars of the menu options of the active menu.
     */
    public void updateMenuTileData()
    {
        int r = 0;
        int stringIndex = 0;
        int c = 0;
        int typeSelector;
        char[] charsToConvert;

        // If active menu is the player name Tiles menu, then only update one Tile at a time - otherwise, update all menu Tiles.
        if (activeMenu == 4)
            playerNameTiles[nameMenuOption][nameChar].setTileType(Helper.convertCharToTileType(playerName[nameMenuOption].charAt(nameChar), 0));
        else
        {
            for (Tile[] row : menus[activeMenu])
            {
                charsToConvert = menuOptions[activeMenu][stringIndex].toCharArray();
                stringIndex++;

                for (Tile tile : row)
                {
                    if (r != selectedMenuOption)
                    {
                        if (activeMenu != 3)
                            typeSelector = 0;
                        else
                        {
                            switch (charsToConvert[c])
                            {
                                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> typeSelector = 0;
                                default -> typeSelector = 3;
                            }
                        }
                    }
                    else
                        typeSelector = 2;

                    tile.setTileType(Helper.convertCharToTileType(charsToConvert[c], typeSelector));

                    c++;

                    if (c == menus[activeMenu][r].length)
                    {
                        c = 0;
                        r++;
                    }
                }
            }
        }
    }

    /**
     * Update 'topScoresOptions' to reflect the current top 3 player names and scores using the passed in PlayerData.
     * @param topPlayers PlayerData object containing top 3 names and scores.
     */
    public void setTopScoresData(PlayerData[] topPlayers)
    {
        int index = 0;
        int nameCounter = 0;
        int scoreCounter = 0;

        for (PlayerData player : topPlayers)
        {
            while (scoreCounter < 1)
            {
                if (nameCounter == 0)
                {
                    topScoresOptions[index] = player.getName();
                    nameCounter++;

                }
                else
                {
                    topScoresOptions[index] = String.valueOf(player.getScore());
                    scoreCounter++;

                }
                while (topScoresOptions[index].length() < Constants.GAMEBOARD_COLS)
                    topScoresOptions[index] += " ";

                index++;
            }

            nameCounter = 0;
            scoreCounter = 0;
        }
    }

    /**
     * Change 'playerName' so that its char at the index of 'nameChar' is changed to the char 'c' that is passed in and then update the Tiles
       to reflect the change.
     * Used when entering player name after achieving a high score and player is entering name chars using keyboard.
     * If 3 chars have been entered (nameChar == 3), then canSaveName is toggled to true, allowing the player to enter the name.
     * @param c The char that will replace the existing char at the index of 'nameChar' in 'playerName'.
     */
    public void enterNameChar(char c)
    {
        if (nameChar < 3)
        {
            if (playerNameTiles[nameMenuOption][nameChar].getType() == TileType.EMPTY)
            {
                char[] nameChars = playerName[nameMenuOption].toCharArray();
                nameChars[nameChar] = c;
                playerName[nameMenuOption] = String.valueOf(nameChars);
                updateMenuTileData();
            }
        }

        if (nameChar < 3)
            nameChar++;

        if (nameChar > 2)
            canSaveName = true;

        showOrHideSave(canSaveName);
    }

    /**
     * Deletes the last char entered into 'playerName' and updates the Tiles to reflect the change.
     * Toggle 'canSaveName' to false if there are less than 3 chars entered.
     */
    public void deleteNameChar()
    {
        if (playerNameTiles[nameMenuOption][nameChar].getType() == TileType.EMPTY && nameChar > 0)
            nameChar--;

        char[] nameChars = playerName[nameMenuOption].toCharArray();
        nameChars[nameChar] = ' ';
        playerName[nameMenuOption] = String.valueOf(nameChars);
        updateMenuTileData();

        if (nameChar <= 2)
            canSaveName = false;

        showOrHideSave(canSaveName);
    }

    /**
     * Set the menu option in 'menu' that is selected.
     * The selected menu option will have cyan Tiles, while unselected menu options will have white Tiles.
     * @param newSelected The new selected menu option.
     * @param menu The active menu.
     */
    public void setSelected(int newSelected, Tile[][] menu)
    {
        if (newSelected < 0)
            selectedMenuOption = menu.length - 1;
        else if (newSelected > menu.length - 1)
            selectedMenuOption = 0;
        else
            selectedMenuOption = newSelected;
    }

    /**
     * Set the active menu to the menu at the index passed in.
     * @param menuIndex The index of the new active menu.
     */
    public void setActiveMenu(int menuIndex)
    {
        activeMenu = menuIndex;
    }

    /**
     * @return The 3D array containing all menus.
     */
    public Tile[][][] getMenus()
    {
        return menus;
    }

    /**
     * @return The index of the active menu.
     */
    public int getActiveMenu()
    {
        return activeMenu;
    }

    /**
     * @return The index of the selected menu option.
     */
    public int getSelected()
    {
        return selectedMenuOption;
    }

    /**
     * @return The boolean flag indicating whether the playerName can be saved or not.
     */
    public boolean getCanSaveName()
    {
        return canSaveName;
    }

    /**
     * @return player name
     */
    public String getPlayerName()
    {
        return playerName[0];
    }

    /**
     * Delete all name chars entered and reset player name tiles to EMPTY.
     */
    public void resetPlayerName()
    {
        deleteNameChar();
        deleteNameChar();
        deleteNameChar();

        for (int i = 0; i < 4; i++)
            playerNameTiles[0][i].setTileType(TileType.EMPTY);
    }

    /**
     * Update player name Tiles to show the SAVE Tiles if canSaveStatus is true - otherwise, clear the SAVE Tiles.
     * The name can only be saved if SAVE is visible.
     * @param canSaveStatus flag indicating whether name can be saved or not.
     */
    private void showOrHideSave(boolean canSaveStatus)
    {
        nameMenuOption++;

        for (int i = 0; i < playerName[nameMenuOption].length(); i++)
        {
            if (canSaveStatus)
                playerNameTiles[nameMenuOption][i].setTileType(Helper.convertCharToTileType(playerName[nameMenuOption].charAt(i), 2));
            else
                playerNameTiles[nameMenuOption][i].setTileType(TileType.EMPTY);
        }

        nameMenuOption--;
    }
}
