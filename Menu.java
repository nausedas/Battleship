package com.martin;

public class Menu {

    public static String userName = "";
    public static DataSource dataSource = new DataSource();

    public static void startMainMenu() {
        dataSource.createDatabaseWithTablesIfNotExists();
        boolean exit = false;
        int gameCounter = 0;

        while (!exit) {
            System.out.println("\nMAIN MENU \n\n" +
                    "Enter number to select: \n"
                    + "1 - Start game. \n"
                    + "2 - Rules.\n"
                    + "3 - User data.\n"
                    + "0 - Exit. \n\n");

            switch (Helper.getIntegerFromUser(4)) {
                case 1:
                    System.out.println("Enter user name. If entered user name does not exist, it will be created. \nTo go back to main menu - enter 0.");
                    do {
                        String input = Helper.scanner.nextLine();
                        if (input.length() > 0 && input.length() <= 10) {
                            userName = input;
                        } else System.out.println("Username length must be between 1 and 10 characters.");
                    } while (userName.equals(""));

                    if (userName.equals("0")) break;
                    dataSource.createNewPlayer(userName);

                    if (gameCounter>0) {
                        ShipGenerator.playerGameBoard = new GameBoard();
                        ShipGenerator.pcGameBoard = new GameBoard();
                    }
                    gameCounter++;
                    Battle battle = new Battle();
                    Battle.gameSetUp();
                    dataSource.incrementGamesPlayed(userName);
                    battle.startBattle();
                    break;

                case 2:
                    System.out.println("Game is played with 5 ships: \n" +
                            "Carrier size 5 \n" +
                            "Battleship size 4 \n" +
                            "Cruiser size 3 \n" +
                            "Submarine size 3 \n" +
                            "Destroyer size 2. \n" +
                            "At the start of the game player has to position its ships either vertically or horizontally on the board.\n" +
                            "Board has 100 slots referred to by coordinates -  X axis: A-J, Y axis: 1-10. To position a ship enter its first \n" +
                            "coordinate and then specify the direction to which it should be pointed from the first. (Use words or digits for shortcut. If there wasn't enough space in \n" +
                            "the selected direction due to the boundaries of the board - your ship will be placed on the edge of the board and to the opposite \n" +
                            "direction of the first coordinate.If ship does not fit because another ship is in the way, you will be prompted to select another location. \n" +
                            "Ships may touch, but must not overlap. Once all ships are placed, game begins. First turn is randomly allocated between user and pc. \n" +
                            "You will be notified when each ship is sunk, so keep in mind the sizes of each ship to not waste any more shots than needed. Whoever sinks all 5 ships, wins the game.\n\n");
                    break;

                case 3:
                    boolean exitUserInfo = false;

                    while (!exitUserInfo) {
                        System.out.println("\n");
                        if (!dataSource.printUserInfo()) {
                            break;
                        } else {
                            System.out.println("\n1 - Delete player data.\n" +
                                    "2 - View detailed player data\n" +
                                    "0 - Exit to Main Menu.");
                            int choice = Helper.getIntegerFromUser(3);

                            switch (choice) {
                                case 1:
                                    System.out.println("Enter player name to delete: \nTo go back enter 0.");
                                    String input = Helper.scanner.nextLine();
                                    if (input.equals("0")) break;
                                    dataSource.deletePlayerData(input);
                                    break;
                                case 2:
                                    boolean exitDetailedPlayerData = false;
                                    System.out.println("Enter player name to retrieve game history: \nTo go back enter 0.");
                                    String name = Helper.scanner.nextLine();
                                    if (name.equals("0")) break;

                                    while (!exitDetailedPlayerData) {
                                        if(!dataSource.returnGameHistory(name)) break;
                                        System.out.println("\nEnter game number to retrieve detailed information:\nTo go back enter 0.");
                                        int gameToRetrieve = Helper.getIntegerFromUser(Integer.MAX_VALUE);
                                        if (gameToRetrieve == 0) exitDetailedPlayerData = true;
                                        else if (!dataSource.returnGameMoves(name, gameToRetrieve)) {
                                            System.out.println("\nSuch game does not exist.");
                                        }
                                    }
                                    break;

                                case 0:
                                    exitUserInfo = true;
                                    break;
                            }
                        }
                    }
                    break;
                case 0:
                    Helper.scanner.close();
                    exit = true;
                    break;
            }
        }
    }
}
