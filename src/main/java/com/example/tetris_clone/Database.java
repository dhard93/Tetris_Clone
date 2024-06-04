package com.example.tetris_clone;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * This class is used to create and interact with the database containing the top 3 player names and scores.
 * It can be used to edit and retrieve database entries.
 */

public class Database
{
    private final String FILENAME = "tetrisDatabase.db";

    /**
     * The Database constructor checks to see if a top players database already exists, and if not, creates one
     * with default entry values.
     */
    public Database()
    {
        int defaultFirstPlace  = 10000;
        int defaultSecondPlace = 5000;
        int defaultThirdPlace  = 2500;

        String dirPath = System.getProperty("user.dir");
        File dbFile = new File(dirPath, FILENAME);

        if (!dbFile.exists())
        {
            try
            {
                if (dbFile.createNewFile())
                    System.out.println("Database file created successfully.");
                else
                    System.out.println("ERROR! Database file could not be created.");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
            System.out.println("Database file exists.");

        //File dbFile = new File(FILENAME);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath()))
        {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS top_players " +
                    "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "high_score BIGINT" +
                    ")";
            String existenceCheckSQL = "SELECT COUNT(*) FROM top_players";
            String insertSQL = "INSERT INTO top_players (name, high_score) VALUES (?,?) ";

            PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);
            preparedStatement.executeUpdate();

            System.out.println("Table 'top_players' created successfully.");

            preparedStatement = connection.prepareStatement(existenceCheckSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                int rowCount = resultSet.getInt(1);
                System.out.println("Number of rows in the table: " + rowCount);

                if (rowCount == 0)
                {
                    preparedStatement = connection.prepareStatement(insertSQL);

                    // First Entry
                    preparedStatement.setString(1, "AAA");
                    preparedStatement.setInt(2, defaultFirstPlace);
                    preparedStatement.executeUpdate();

                    // Second Entry
                    preparedStatement.setString(1, "BBB");
                    preparedStatement.setInt(2, defaultSecondPlace);
                    preparedStatement.executeUpdate();

                    // Third Entry
                    preparedStatement.setString(1, "CCC");
                    preparedStatement.setInt(2, defaultThirdPlace);
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e)
        {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Replace the name and score of an existing database entry using 'place' to determine
     * which entry to edit.
     * @param name The new name of the database entry.
     * @param place What place the current player came in. Used to determine which entry to edit.
     * @param topPlayers PlayerData for each of the top 3 players in the database.
     */
    public void editEntries(String name, int place, PlayerData[] topPlayers)
    {
        File dbFile = new File(FILENAME);
        topPlayers[place - 1].setName(name);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath()))
        {
            // Update the database to reflect changes to player data.
            String updateSQL = "UPDATE top_players SET name = ?, high_score = ? WHERE id = ?";

            for (int i = place; i <= topPlayers.length; i++)
            {
                try (PreparedStatement prepStatement = connection.prepareStatement(updateSQL))
                {
                    System.out.println("DB: " + topPlayers[i - 1].getName() + ", " + topPlayers[i - 1].getScore());
                    prepStatement.setString(1, topPlayers[i - 1].getName());
                    prepStatement.setInt(2, topPlayers[i - 1].getScore());
                    prepStatement.setInt(3, i);
                    prepStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve all database entries in the form of a PlayerData object.
     * @return A PlayerData object containing the names and scores of all database entries.
     */
    public PlayerData[] retrieveEntries()
    {
        PlayerData[] playerData = new PlayerData[3];
        int counter = 0;

        File dbFile = new File(FILENAME);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath()))
        {
            String selectSQL = "SELECT * FROM top_players ORDER BY high_score DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String name = resultSet.getString("name");
                int score = resultSet.getInt("high_score");

                playerData[counter] = new PlayerData(name, score);
                counter++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playerData;
    }

}
