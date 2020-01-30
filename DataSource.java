package com.martin;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataSource {
    public static final String TABLE_PLAYER_LIST = "playerList";
    public static final String PLAYER_LIST_COLUMN_NAME = "name";
    public static final String PLAYER_LIST_COLUMN_GAMES_PLAYED = "gamesPlayed";
    public static final String PLAYER_LIST_COLUMN_GAMES_COMPLETED = "gamesCompleted";
    public static final String PLAYER_LIST_COLUMN_GAMES_WON = "gamesWon";

    public static final String TABLE_COMPLETED_GAMES_HISTORY = "completedGamesHistory";
    public static final String COMPLETED_GAMES_HISTORY_COLUMN_ID = "_id";
    public static final String COMPLETED_GAMES_HISTORY_COLUMN_NAME = "name";
    public static final String COMPLETED_GAMES_HISTORY_COLUMN_GAME_NUMBER_OF_PLAYER = "gameNumberOfPlayer";
    public static final String COMPLETED_GAMES_HISTORY_COLUMN_DATE_TIME = "date_time";
    public static final String COMPLETED_GAMES_HISTORY_COLUMN_PC_SHIP_LOCATIONS = "pcShipLocations";
    public static final String COMPLETED_GAMES_HISTORY_COLUMN_PLAYER_SHIP_LOCATIONS = "playerShipLocations";

    public static final String TABLE_GAME_MOVES = "gameMoves";
    public static final String GAME_MOVES_COLUMN_TURN = "turn";
    public static final String GAME_MOVES_COLUMN_PC_MOVE = "pcMove";
    public static final String GAME_MOVES_COLUMN_PLAYER_MOVE = "playerMove";
    public static final String GAME_MOVES_COLUMN_ID = "_id_foreign";

    public static final String DB_NAME = "battleship.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/home/martynas/JavaPrograms/Battleship/" + DB_NAME;
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";

    public static final String QUERY_GAME_HISTORY = "SELECT * FROM " + TABLE_COMPLETED_GAMES_HISTORY + " WHERE " + COMPLETED_GAMES_HISTORY_COLUMN_NAME + " = ? ORDER BY gameNumberOfPlayer ASC";
    public static final String QUERY_COMPLETED_GAMES_BY_USER = "SELECT count (*) FROM " + TABLE_COMPLETED_GAMES_HISTORY + " WHERE " + COMPLETED_GAMES_HISTORY_COLUMN_NAME + " = ?";
    public static final String QUERY_PLAYER = "SELECT " + PLAYER_LIST_COLUMN_NAME + " FROM " + TABLE_PLAYER_LIST + " WHERE " + PLAYER_LIST_COLUMN_NAME + " = ?";
    public static final String QUERY_PLAYER_LIST = "SELECT * FROM " + TABLE_PLAYER_LIST;
    public static final String QUERY_GAMES_PLAYED_PLAYER_LIST = "SELECT " + PLAYER_LIST_COLUMN_GAMES_PLAYED + " FROM " + TABLE_PLAYER_LIST + " WHERE " + TABLE_PLAYER_LIST + "." + PLAYER_LIST_COLUMN_NAME + " = ?";
    public static final String QUERY_GAMES_WON_PLAYER_LIST = "SELECT " + PLAYER_LIST_COLUMN_GAMES_WON + " FROM " + TABLE_PLAYER_LIST + " WHERE " + TABLE_PLAYER_LIST + "." + PLAYER_LIST_COLUMN_NAME + " = ?";
    public static final String QUERY_GAMES_COMPLETED_PLAYER_LIST = "SELECT " + PLAYER_LIST_COLUMN_GAMES_COMPLETED + " FROM " + TABLE_PLAYER_LIST + " WHERE " + TABLE_PLAYER_LIST + "." + PLAYER_LIST_COLUMN_NAME + " = ?";
    public static final String QUERY_GAME_MOVES = "SELECT " + TABLE_GAME_MOVES + "." + GAME_MOVES_COLUMN_TURN + ", " + TABLE_GAME_MOVES + "." + GAME_MOVES_COLUMN_PC_MOVE + ", " + TABLE_GAME_MOVES + "."
            + GAME_MOVES_COLUMN_PLAYER_MOVE + " FROM " + TABLE_COMPLETED_GAMES_HISTORY + " INNER JOIN " + TABLE_GAME_MOVES + " ON " + TABLE_COMPLETED_GAMES_HISTORY + "." + COMPLETED_GAMES_HISTORY_COLUMN_ID
            + " = " + TABLE_GAME_MOVES + "." + GAME_MOVES_COLUMN_ID + " WHERE " + COMPLETED_GAMES_HISTORY_COLUMN_NAME + " = ? AND " + COMPLETED_GAMES_HISTORY_COLUMN_GAME_NUMBER_OF_PLAYER + " =?";

    public static final String INSERT_PLAYER_LIST = "INSERT INTO " + TABLE_PLAYER_LIST + '(' + PLAYER_LIST_COLUMN_NAME + ", " + PLAYER_LIST_COLUMN_GAMES_PLAYED + ", " + PLAYER_LIST_COLUMN_GAMES_COMPLETED + ", " + PLAYER_LIST_COLUMN_GAMES_WON + ") VALUES (?, ?, ?, ?)";
    public static final String INSERT_GAME_MOVES = "INSERT INTO " + TABLE_GAME_MOVES + " VALUES ( ?, ?, ?, ?)";
    public static final String INSERT_GAME_HISTORY = "INSERT INTO " + TABLE_COMPLETED_GAMES_HISTORY + " (" + COMPLETED_GAMES_HISTORY_COLUMN_GAME_NUMBER_OF_PLAYER + ", " + COMPLETED_GAMES_HISTORY_COLUMN_NAME + ", " + COMPLETED_GAMES_HISTORY_COLUMN_DATE_TIME + ", "
            + COMPLETED_GAMES_HISTORY_COLUMN_PC_SHIP_LOCATIONS + ", " + COMPLETED_GAMES_HISTORY_COLUMN_PLAYER_SHIP_LOCATIONS + ") VALUES (?, ?, ?, ?,? )";

    public static final String UPDATE_GAMES_PLAYED_PLAYER_LIST = "UPDATE " + TABLE_PLAYER_LIST + " SET " + PLAYER_LIST_COLUMN_GAMES_PLAYED + " = ?" + " WHERE " + PLAYER_LIST_COLUMN_NAME + " = ?";
    public static final String UPDATE_GAMES_COMPLETED_PLAYER_LIST = "UPDATE " + TABLE_PLAYER_LIST + " SET " + PLAYER_LIST_COLUMN_GAMES_COMPLETED + " = ? " + " WHERE " + TABLE_PLAYER_LIST + "." + PLAYER_LIST_COLUMN_NAME + " = ?";
    public static final String UPDATE_GAMES_WON_PLAYER_LIST = "UPDATE " + TABLE_PLAYER_LIST + " SET " + PLAYER_LIST_COLUMN_GAMES_WON + " = ? " + " WHERE " + TABLE_PLAYER_LIST + "." + PLAYER_LIST_COLUMN_NAME + " = ?";

    public static final String DELETE_PLAYER_LIST = "DELETE FROM " + TABLE_PLAYER_LIST + " WHERE " + PLAYER_LIST_COLUMN_NAME + " = ?";

    public void createDatabaseWithTablesIfNotExists() {
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement statement = conn.createStatement()) {
            statement.execute(CREATE_TABLE + " " + TABLE_PLAYER_LIST + " (" + PLAYER_LIST_COLUMN_NAME + " text PRIMARY KEY, " + PLAYER_LIST_COLUMN_GAMES_PLAYED + " integer, " +
                    PLAYER_LIST_COLUMN_GAMES_COMPLETED + " integer, " + PLAYER_LIST_COLUMN_GAMES_WON + " integer)");

            statement.execute(CREATE_TABLE + TABLE_COMPLETED_GAMES_HISTORY + " (" + COMPLETED_GAMES_HISTORY_COLUMN_ID + " integer PRIMARY KEY, " + COMPLETED_GAMES_HISTORY_COLUMN_GAME_NUMBER_OF_PLAYER +
                    " integer, " + COMPLETED_GAMES_HISTORY_COLUMN_NAME + " text, " + COMPLETED_GAMES_HISTORY_COLUMN_DATE_TIME + " text, " + COMPLETED_GAMES_HISTORY_COLUMN_PC_SHIP_LOCATIONS +
                    " text, " + COMPLETED_GAMES_HISTORY_COLUMN_PLAYER_SHIP_LOCATIONS + " text, FOREIGN KEY (" + COMPLETED_GAMES_HISTORY_COLUMN_NAME + ") REFERENCES " + TABLE_PLAYER_LIST + " (" + PLAYER_LIST_COLUMN_NAME + ") ON DELETE CASCADE)");

            statement.execute(CREATE_TABLE + TABLE_GAME_MOVES + " (" + GAME_MOVES_COLUMN_ID + " integer, " + GAME_MOVES_COLUMN_TURN + " integer, " + GAME_MOVES_COLUMN_PC_MOVE + " text, " +
                    GAME_MOVES_COLUMN_PLAYER_MOVE + " text, FOREIGN KEY (" + GAME_MOVES_COLUMN_ID + ") REFERENCES " + TABLE_COMPLETED_GAMES_HISTORY + " (" + COMPLETED_GAMES_HISTORY_COLUMN_ID + ") ON DELETE CASCADE)");
        } catch (SQLException e) {
            System.out.println("Could not create database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createNewPlayer(String playerName) {
        String name = playerName.toLowerCase();

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement queryPlayer = conn.prepareStatement(QUERY_PLAYER);
             PreparedStatement insertIntoPlayerList = conn.prepareStatement(INSERT_PLAYER_LIST)) {
            queryPlayer.setString(1, name);
            ResultSet result = queryPlayer.executeQuery();
            if (result.next()) {
                System.out.println(name + " account selected.");
            } else {
                System.out.println(name + " account created.");
                insertIntoPlayerList.setString(1, name);
                insertIntoPlayerList.setInt(2, 0);
                insertIntoPlayerList.setInt(3, 0);
                insertIntoPlayerList.setInt(4, 0);
                insertIntoPlayerList.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error while creating a user " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void incrementGamesPlayed(String playerName) {
        String name = playerName.toLowerCase();
        int gamesPlayed;
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement queryGamesPlayed = conn.prepareStatement(QUERY_GAMES_PLAYED_PLAYER_LIST);
             PreparedStatement updateGamesPlayed = conn.prepareStatement(UPDATE_GAMES_PLAYED_PLAYER_LIST)) {

            queryGamesPlayed.setString(1, name);
            ResultSet result = queryGamesPlayed.executeQuery();
            gamesPlayed = result.getInt(1) + 1;
            updateGamesPlayed.setInt(1, gamesPlayed);
            updateGamesPlayed.setString(2, name);
            updateGamesPlayed.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while incrementing gamesPlayed" + e.getMessage());
            e.printStackTrace();
        }
    }
    public boolean printUserInfo() {
        String format = "%-12s" + " " + "%5s" + " " + "%12s" + " " + "%7s";

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement statement = conn.createStatement()) {
            ResultSet result = statement.executeQuery(QUERY_PLAYER_LIST);
            if (!result.isBeforeFirst()) {
                System.out.println("No data available.");
                return false;
            } else {
                System.out.format(format, "Username", "Games", "Completed", "Wins");
                System.out.println();
            }
            while (result.next()) {
                System.out.format(format, result.getString(1), result.getInt(2), result.getInt(3), result.getInt(4));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching data from database: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }
    public void incrementGameCompletedAndGameWon(String playerName, boolean playerWon) {
        String name = playerName.toLowerCase();
        int gamesCompleted;
        int gamesWon;

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement queryGamesCompleted = conn.prepareStatement(QUERY_GAMES_COMPLETED_PLAYER_LIST);
             PreparedStatement updateGamesCompleted = conn.prepareStatement(UPDATE_GAMES_COMPLETED_PLAYER_LIST);
             PreparedStatement queryGamesWon = conn.prepareStatement(QUERY_GAMES_WON_PLAYER_LIST);
             PreparedStatement updateGamesWon = conn.prepareStatement(UPDATE_GAMES_WON_PLAYER_LIST)
        ) {
            queryGamesCompleted.setString(1, name);
            ResultSet result = queryGamesCompleted.executeQuery();
            gamesCompleted = result.getInt(1) + 1;
            updateGamesCompleted.setInt(1, gamesCompleted);
            updateGamesCompleted.setString(2, name);
            updateGamesCompleted.executeUpdate();

            if (playerWon) {
                queryGamesWon.setString(1, name);
                ResultSet result2 = queryGamesWon.executeQuery();
                gamesWon = result2.getInt(1) + 1;
                updateGamesWon.setInt(1, gamesWon);
                updateGamesWon.setString(2, name);
                updateGamesWon.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error while updating playerList table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void recordGameInfo(String playerName, ArrayList<Coordinates> pcShipLocations, ArrayList<Coordinates> playerShipLocations, ArrayList<Coordinates> pcMoves, ArrayList<Coordinates> playerMoves) {
        String name = playerName.toLowerCase();
        int generated_id = 0;
        int gameNumber;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime currentTime = LocalDateTime.now();
        String time = dtf.format(currentTime);
        String playerShipLocationsString = Helper.convertCoordinatesToString(playerShipLocations);
        String pcShipLocationsString = Helper.convertCoordinatesToString(pcShipLocations);

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement updateCompletedGameHistory = conn.prepareStatement(INSERT_GAME_HISTORY, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement queryCompletedGamesByUser = conn.prepareStatement(QUERY_COMPLETED_GAMES_BY_USER)) {

            queryCompletedGamesByUser.setString(1, name);
            ResultSet result = queryCompletedGamesByUser.executeQuery();
            gameNumber = result.getInt(1) + 1;

            updateCompletedGameHistory.setInt(1, gameNumber);
            updateCompletedGameHistory.setString(2, name);
            updateCompletedGameHistory.setString(3, time);
            updateCompletedGameHistory.setString(4, pcShipLocationsString);
            updateCompletedGameHistory.setString(5, playerShipLocationsString);
            updateCompletedGameHistory.executeUpdate();

            ResultSet key = updateCompletedGameHistory.getGeneratedKeys();
            generated_id = key.getInt(1);

        } catch (SQLException e) {
            System.out.println("Unexpected error occurred while saving data " + e.getMessage());
            e.printStackTrace();
        }
        recordGameMoves(generated_id, pcMoves, playerMoves);
    }


    private void recordGameMoves(int gameNumber, ArrayList<Coordinates> pcMoves, ArrayList<Coordinates> playerMoves) {

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement recordGameMoves = conn.prepareStatement(INSERT_GAME_MOVES)
        ) {
            recordGameMoves.setInt(1, gameNumber);
            int lastRecursion = 0;
            int repeat = Math.min(pcMoves.size(), playerMoves.size());

            for (int i = 1; i <= repeat; i++) {
                String pcCoordinates = "";
                recordGameMoves.setInt(2, i);
                String x = String.valueOf(pcMoves.get(i - 1).getX());
                String y = String.valueOf(pcMoves.get(i - 1).getY());

                pcCoordinates += x;
                pcCoordinates += y;
                recordGameMoves.setString(3, pcCoordinates);

                String playerCoordinates = "";
                String x1 = String.valueOf(playerMoves.get(i - 1).getX());
                String y1 = String.valueOf(playerMoves.get(i - 1).getY());

                playerCoordinates += x1;
                playerCoordinates += y1;
                recordGameMoves.setString(4, playerCoordinates);
                recordGameMoves.executeUpdate();
                lastRecursion = i;
            }

            if (pcMoves.size() > playerMoves.size()) {

                String pcCoordinates = "";
                String x = String.valueOf(pcMoves.get(lastRecursion).getX());
                String y = String.valueOf(pcMoves.get(lastRecursion).getY());

                pcCoordinates += x;
                pcCoordinates += y;
                recordGameMoves.setInt(2, lastRecursion + 1);
                recordGameMoves.setString(3, pcCoordinates);
                recordGameMoves.setString(4, "N/A");
                recordGameMoves.executeUpdate();

            } else if (pcMoves.size() < playerMoves.size()) {

                String playerCoordinate = "";
                String x = String.valueOf(playerMoves.get(lastRecursion).getX());
                String y = String.valueOf(playerMoves.get(lastRecursion).getY());

                playerCoordinate += x;
                playerCoordinate += y;
                recordGameMoves.setInt(2, lastRecursion + 1);
                recordGameMoves.setString(4, playerCoordinate);
                recordGameMoves.setString(3, "N/A");
                recordGameMoves.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Error while recording game moves: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean returnGameHistory(String name) {
        String format = "%-9s" + " " + "%-20s" + " " + "%-65s" + " " + "%-65s";

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement queryPlayer = conn.prepareStatement(QUERY_PLAYER);
             PreparedStatement returnGameHistory = conn.prepareStatement(QUERY_GAME_HISTORY)) {
            queryPlayer.setString(1, name);
            ResultSet playerExistence = queryPlayer.executeQuery();
            if (!playerExistence.next()) {
                System.out.println("Such player does not exist.");
                return false;
            } else {
                returnGameHistory.setString(1, name);
                ResultSet result = returnGameHistory.executeQuery();
                System.out.println();
                System.out.format(format, "Game ID", "Date and time", "PC ship Locations", "Player Ship Locations");
                System.out.println();
                while (result.next()) {
                    System.out.format(format, result.getString(2), result.getString(4), result.getString(5), result.getString(6));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving game data: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    public boolean returnGameMoves(String name, int game) {

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement returnGameMoves = conn.prepareStatement(QUERY_GAME_MOVES)) {

            returnGameMoves.setString(1, name);
            returnGameMoves.setInt(2, game);
            ResultSet result = returnGameMoves.executeQuery();
            if (!result.isBeforeFirst()) {
                return false;
            } else {
                System.out.println("Turn" + "\t\t" + "PC move" + "\t" + "\t" + "Player move");
                while (result.next()) {
                    System.out.println(
                            result.getInt(1) + "\t" + "\t\t" +
                                    result.getString(2) + "\t" + "\t\t" +
                                    result.getString(3) + "\t");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving game data: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    public void deletePlayerData(String playerName) {
        String name = playerName.toLowerCase();
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement queryPlayer = conn.prepareStatement(QUERY_PLAYER);
             PreparedStatement deleteFromPlayerList = conn.prepareStatement(DELETE_PLAYER_LIST);
             Statement statement = conn.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            queryPlayer.setString(1, name);
            ResultSet result = queryPlayer.executeQuery();
            if (!result.next()) {
                System.out.println("Such player does not exist.");
            } else {
                deleteFromPlayerList.setString(1, name);
                deleteFromPlayerList.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting a user" + e.getMessage());
            e.printStackTrace();
        }
    }
}


