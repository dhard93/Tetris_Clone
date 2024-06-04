package com.example.tetris_clone;

/**
 * This class contains all constants used in the program.
 */

public class Constants
{
    // Frames
    public static final float FRAME_RATE = 1/60f;
    public static final int DELAY_FRAME_COUNT = 30;
    public static final int ANIMATION_FRAME_COUNT = 3;

    // GUI size
    public static final int GUI_WIDTH = 1280;
    public static final int GUI_HEIGHT = 960;

    // Tile size
    public static final int TILE_SIZE = 32;

    // Tetromino constants
    public static final int NUM_OF_TETROMINOS = 7;
    public static final int CURRENT_TETROMINO_STARTING_X = 576;
    public static final int CURRENT_TETROMINO_STARTING_Y = 224;
    public static final int NEXT_TETROMINO_STARTING_X = 1024;
    public static final int NEXT_TETROMINO_STARTING_Y = 384;
    public static final int LINES_REQ_FOR_TETRIS = 4;
    public static final float TIME_BTWN_SOFT_DROPS = 0.03f;

    // Tile Board constants
    public static final int GAMEBOARD_STARTING_X = 480;
    public static final int GAMEBOARD_STARTING_Y = 224;
    public static final int GAMEBOARD_ENDING_X = 768;
    public static final int GAMEBOARD_ENDING_Y = 832;
    public static final int GAMEBOARD_ROWS = 20;
    public static final int GAMEBOARD_COLS = 10;
    public static final int NEXT_DISPLAY_STARTING_X = 1024;
    public static final int NEXT_DISPLAY_STARTING_Y = 384;
    public static final int NEXT_DISPLAY_SIZE = 4;

    // Constants for statistics display
    public static final int STATS_PLACE_NUMBER = 4;
    public static final int STATS_STARTING_X   = 224;
    public static final int STATS_ENDING_X     = 352;
    public static final int STATS_STARTING_Y   = 320;
    public static final int STATS_ENDING_Y     = 928;
    public static final int STATS_X_INCREMENT  = TILE_SIZE;
    public static final int STATS_Y_INCREMENT  = TILE_SIZE * 3;

    // Constants for lines display
    public static final int LINES_PLACE_NUMBER = 4;
    public static final int LINES_STARTING_X   = 672;
    public static final int LINES_ENDING_X     = 800;
    public static final int LINES_STARTING_Y   = 64;
    public static final int LINES_ENDING_Y     = 65;
    public static final int LINES_X_INCREMENT  = TILE_SIZE;
    public static final int LINES_Y_INCREMENT  = 1;

    // Constants for top score display
    public static final int TOP_SCORE_PLACE_NUMBER = 8;
    public static final int TOP_SCORE_STARTING_X   = 992;
    public static final int TOP_SCORE_ENDING_X     = 1248;
    public static final int TOP_SCORE_STARTING_Y   = 128;
    public static final int TOP_SCORE_ENDING_Y     = 129;
    public static final int TOP_SCORE_X_INCREMENT  = TILE_SIZE;
    public static final int TOP_SCORE_Y_INCREMENT  = 1;

    // Constants for score display
    public static final int SCORE_PLACE_NUMBER = 8;
    public static final int SCORE_STARTING_X   = 992;
    public static final int SCORE_ENDING_X     = 1248;
    public static final int SCORE_STARTING_Y   = 224;
    public static final int SCORE_ENDING_Y     = 225;
    public static final int SCORE_X_INCREMENT  = TILE_SIZE;
    public static final int SCORE_Y_INCREMENT  = 1;

    // Constants for level display
    public static final int LEVEL_PLACE_NUMBER = 3;
    public static final int LEVEL_STARTING_X   = 992;
    public static final int LEVEL_ENDING_X     = 1088;
    public static final int LEVEL_STARTING_Y   = 640;
    public static final int LEVEL_ENDING_Y     = 641;
    public static final int LEVEL_X_INCREMENT  = TILE_SIZE;
    public static final int LEVEL_Y_INCREMENT  = 1;

    // Constants for TETRIS display
    public static final int NUM_OF_CHARS_IN_TETRIS = 6;
    public static final int TETRIS_STARTING_X = 96;
    public static final int TETRIS_STARTING_Y = 96;
    public static final int TETRIS_ENDING_X = 288;
    public static final int TETRIS_ENDING_Y = 97;
    public static final int TETRIS_X_INCREMENT = TILE_SIZE;
    public static final int TETRIS_Y_INCREMENT = 1;
    public static final float TETRIS_CHAR_ROTATE_SPEED = 1/4f;

    // Constants for player name display
    public static final int PLAYER_NAME_STARTING_X = 593;
    public static final int PLAYER_NAME_ENDING_X = 689;
    public static final int PLAYER_NAME_STARTING_Y = 384;
    public static final int PLAYER_NAME_ENDING_Y = 512;
    public static final int PLAYER_NAME_X_INCREMENT = TILE_SIZE;
    public static final int PLAYER_NAME_Y_INCREMENT = TILE_SIZE * 3;
    public static final int PLAYER_NAME_SAVE_OFFSET = 15;
}
