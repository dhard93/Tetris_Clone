package com.example.tetris_clone;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This class is where the application is launched.
 */

public class Main extends Application
{
    @Override
    public void start(Stage stage)
    {
        TetrisRenderer renderer = new TetrisRenderer();
        Game newGame = new Game(renderer);
        TetrisController controller = new TetrisController(newGame);
        Scene scene = renderer.getScene();
        stage.setTitle("Tetris");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args){}
}
