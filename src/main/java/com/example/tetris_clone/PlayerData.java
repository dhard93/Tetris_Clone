package com.example.tetris_clone;

/**
 * This class represents the data of a player in the top 3 and is used to access a top player's name and score.
 */

public class PlayerData
{
    private String name;
    private int score;

    public PlayerData(String playerName, int highScore)
    {
        name = playerName;
        score = highScore;
    }

    public void setName(String newName)
    {
        name = newName;
    }

    public void setScore(int newScore)
    {
        score = newScore;
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }
}
