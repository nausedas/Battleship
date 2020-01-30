package com.martin;

import java.util.ArrayList;

public class Battle {

    private ArrayList<Coordinates> playerMoves = new ArrayList<>();
    private ArrayList<Coordinates> pcMoves = new ArrayList<>();
    private ArrayList<Coordinates> possiblePlayerShipLocations = new ArrayList<>();
    private ArrayList<Coordinates> successfulShotsOfPcAttack = new ArrayList<>();
    private ArrayList<Coordinates> stackedShipLeads = new ArrayList<>();
    private Coordinates firstPcHitInAttackCoordinates = new Coordinates();
    private boolean playersShipLocationKnown = false;
    private int pcDamageGivenInAttackForSunkShips = 0;
    private int pcSuccessfulShotsCounter = 0;
    private int playerSuccessfulShotsCounter = 0;

    public static void gameSetUp() {
        ShipGenerator pc_carrier = new ShipGenerator(5, "Carrier", true);
        ShipGenerator pc_battleship = new ShipGenerator(4, "Battleship", true);
        ShipGenerator pc_cruiser = new ShipGenerator(3, "Cruiser", true);
        ShipGenerator pc_submarine = new ShipGenerator(3, "Submarine", true);
        ShipGenerator pc_destroyer = new ShipGenerator(2, "Destroyer", true);

        ShipGenerator player_carrier = new ShipGenerator(5, "Carrier", false);
        ShipGenerator player_battleship = new ShipGenerator(4, "Battleship", false);
        ShipGenerator player_cruiser = new ShipGenerator(3, "Cruiser", false);
        ShipGenerator player_submarine = new ShipGenerator(3, "Submarine", false);
        ShipGenerator player_destroyer = new ShipGenerator(2, "Destroyer", false);
    }

    public void startBattle() {
        boolean gameOver = false;
        int coinToss = Helper.getRandomInt(2);
        while (!gameOver) {
            if (coinToss == 1) {
                if (pcTurnWithWinCheck() || playerTurnWithWinCheck()) gameOver = true;
            } else {
                if (playerTurnWithWinCheck() || pcTurnWithWinCheck()) gameOver = true;
            }
        }
    }

    private boolean pcTurnWithWinCheck() {
        pcTurn();
        if (pcSuccessfulShotsCounter == 17) {
            Menu.dataSource.incrementGameCompletedAndGameWon(Menu.userName, false);
            Menu.dataSource.recordGameInfo(Menu.userName, Helper.extractCoordinatesWithShips(ShipGenerator.pcGameBoard), Helper.extractCoordinatesWithShips(ShipGenerator.playerGameBoard), pcMoves, playerMoves);
            System.out.println("Game over, pc won");
            return true;
        } else return false;
    }

    private boolean playerTurnWithWinCheck() {
        playerTurn();
        if (playerSuccessfulShotsCounter == 17) {
            Menu.dataSource.incrementGameCompletedAndGameWon(Menu.userName, true);
            Menu.dataSource.recordGameInfo(Menu.userName, Helper.extractCoordinatesWithShips(ShipGenerator.pcGameBoard), Helper.extractCoordinatesWithShips(ShipGenerator.playerGameBoard), pcMoves, playerMoves);
            System.out.println("Game over, player won.");
            return true;
        }
        return false;
    }

    public void playerTurn() {
        boolean hitSuccess = false;
        Coordinates shot = new Coordinates();

        System.out.println("\nPlease enter your shot coordinates (A1-J10):");
        String coordinates = Helper.getCoordinatesFromUser();
        char x = coordinates.charAt(0);
        int y;
        if (coordinates.length() == 2) y = Character.getNumericValue(coordinates.charAt(1));
        else y = 10;

        for (int i = 0; i < playerMoves.size(); i++) {
            if (playerMoves.get(i).getX() == x && playerMoves.get(i).getY() == y) {
                System.out.println("You have already tried this spot. Please enter your shot coordinates (A1-J10):");
                coordinates = Helper.getCoordinatesFromUser();
                x = coordinates.charAt(0);
                if (coordinates.length() == 2) y = Character.getNumericValue(coordinates.charAt(1));
                else y = 10;
                i = -1;
            }
        }
        shot.setY(y);
        shot.setX(x);
        playerMoves.add(shot);

        for (int i = 0; i < ShipGenerator.pcGameBoard.gridList.size(); i++) {
            if (ShipGenerator.pcGameBoard.gridList.get(i).getX() == x && ShipGenerator.pcGameBoard.gridList.get(i).getY() == y && ShipGenerator.pcGameBoard.gridList.get(i).isShip()) {
                playerSuccessfulShotsCounter++;
                System.out.println("PC ship was hit");
                hitSuccess = true;
                shipHealthCounter(ShipGenerator.pcGameBoard, ShipGenerator.pcGameBoard.gridList.get(i).getShipName());

                if (wasShipSunk(ShipGenerator.pcGameBoard, ShipGenerator.pcGameBoard.gridList.get(i).getShipName())) {
                    System.out.println(ShipGenerator.pcGameBoard.gridList.get(i).getShipName() + " was sunk.");
                }
            }
        }
        if (!hitSuccess) {
            System.out.println("Shot unsuccessful.");
        }
    }

    public void pcTurn() {
        boolean hitSuccess = false;
        Coordinates shot = new Coordinates();

        if (playersShipLocationKnown) {
            int randomShotPicker = Helper.getRandomInt(possiblePlayerShipLocations.size()) - 1;
            char x = possiblePlayerShipLocations.get(randomShotPicker).getX();
            int y = possiblePlayerShipLocations.get(randomShotPicker).getY();

            shot.setY(y);
            shot.setX(x);
            pcMoves.add(shot);

            System.out.println("\nPC shoots at " + shot.getX() + shot.getY());

            for (int i = 0; i < ShipGenerator.playerGameBoard.gridList.size(); i++) {
                if (ShipGenerator.playerGameBoard.gridList.get(i).getX() == x && ShipGenerator.playerGameBoard.gridList.get(i).getY() == y && ShipGenerator.playerGameBoard.gridList.get(i).isShip()) {
                    pcSuccessfulShotsCounter++;
                    playersShipLocationKnown = true;
                    System.out.println("Players ship was hit");
                    hitSuccess = true;
                    successfulShotsOfPcAttack.add(new Coordinates(x, y));
                    shipHealthCounter(ShipGenerator.playerGameBoard, ShipGenerator.playerGameBoard.gridList.get(i).getShipName());

                    //PRIDEDA PO VIENA KOORDINATE I SONA NUO PATAIKYTOS (NEPRIDEDA TU, I KURIAS JAU BUVO SAUTA)
                    if (shot.getX() + 1 <= 'J' && hasPcShotAt(shot.getY(), (char) (shot.getX() + 1)) && firstPcHitInAttackCoordinates.getY() == shot.getY()) {
                        possiblePlayerShipLocations.add(new Coordinates((char) (shot.getX() + 1), shot.getY()));
                    }
                    if (shot.getX() - 1 >= 'A' && hasPcShotAt(shot.getY(), (char) (shot.getX() - 1)) && firstPcHitInAttackCoordinates.getY() == shot.getY()) {
                        possiblePlayerShipLocations.add(new Coordinates((char) (shot.getX() - 1), shot.getY()));
                    }
                    if (shot.getY() + 1 <= 10 && hasPcShotAt(shot.getY() + 1, shot.getX()) && firstPcHitInAttackCoordinates.getX() == shot.getX()) {
                        possiblePlayerShipLocations.add(new Coordinates(shot.getX(), shot.getY() + 1));
                    }
                    if (shot.getY() - 1 >= 1 && hasPcShotAt(shot.getY() - 1, shot.getX()) && firstPcHitInAttackCoordinates.getX() == shot.getX()) {
                        possiblePlayerShipLocations.add(new Coordinates(shot.getX(), shot.getY() - 1));
                    }
                    //ISTRINA NEBEAKTUALIAS KOORDINATES IS POSSIBLE SHIP LOCATIONS, KURIU Y NESUTAMPA SU PIRMO SEKMINGO SUVIO
                    if (shot.getY() == firstPcHitInAttackCoordinates.getY()) {
                        for (int j = 0; j < possiblePlayerShipLocations.size(); j++) {
                            if (possiblePlayerShipLocations.get(j).getY() != shot.getY()) {
                                possiblePlayerShipLocations.remove(j);
                                j = -1;
                            }
                        }
                    }
                    //ISTRINA NEBEAKTUALIAS KOORDINATES IS POSSIBLE SHIP LOCATIONS, KURIU X NESUTAMPA SU PIRMO SEKMINGO SUVIO
                    if (shot.getX() == firstPcHitInAttackCoordinates.getX()) {
                        for (int j = 0; j < possiblePlayerShipLocations.size(); j++) {
                            if (possiblePlayerShipLocations.get(j).getX() != shot.getX()) {
                                possiblePlayerShipLocations.remove(j);
                                j = -1;
                            }
                        }
                    }
                    for (int j = 0; j < possiblePlayerShipLocations.size(); j++) {
                        if (possiblePlayerShipLocations.get(j).getX() == shot.getX() && possiblePlayerShipLocations.get(j).getY() == shot.getY()) {
                            possiblePlayerShipLocations.remove(j);
                            break;
                        }
                    }
                    if (wasShipSunk(ShipGenerator.playerGameBoard, ShipGenerator.playerGameBoard.gridList.get(i).getShipName())) {
                        System.out.println(ShipGenerator.playerGameBoard.gridList.get(i).getShipName() + " was sunk.");
                        possiblePlayerShipLocations.clear();

                        pcDamageGivenInAttackForSunkShips += getMaxShipSize(ShipGenerator.playerGameBoard.gridList.get(i).getShipName());

                        if (stackedShipLeads.size() != 0) {
                            stackedShipTracker();
                        } else if (successfulShotsOfPcAttack.size() != pcDamageGivenInAttackForSunkShips) {
                            for (Coordinates coordinates : successfulShotsOfPcAttack) {
                                for (int k = 0; k < ShipGenerator.playerGameBoard.gridList.size(); k++) {
                                    if (coordinates.getX() == ShipGenerator.playerGameBoard.gridList.get(k).getX() &&
                                            coordinates.getY() == ShipGenerator.playerGameBoard.gridList.get(k).getY()) {
                                        if (!wasShipSunk(ShipGenerator.playerGameBoard, ShipGenerator.playerGameBoard.gridList.get(k).getShipName())) {
                                            stackedShipLeads.add(new Coordinates(coordinates.getX(), coordinates.getY()));
                                        }
                                    }
                                }
                            }
                            stackedShipTracker();
                        } else {
                            playersShipLocationKnown = false;
                            possiblePlayerShipLocations.clear();
                            firstPcHitInAttackCoordinates = new Coordinates();
                            successfulShotsOfPcAttack.clear();
                            pcDamageGivenInAttackForSunkShips = 0;
                        }
                    }
                }
            }

            for (int i = 0; i < possiblePlayerShipLocations.size(); i++) {
                if (possiblePlayerShipLocations.get(i).getY() == y && possiblePlayerShipLocations.get(i).getX() == x) {
                    possiblePlayerShipLocations.remove(i);
                    break;
                }
            }
            if (!hitSuccess) {
                System.out.println("Pc Shot unsuccessful");
            }
            //PAGAUNA VIENA SALIA KITO SUSTATYTUS LAIVUS
            if (playersShipLocationKnown && possiblePlayerShipLocations.size() == 0 && stackedShipLeads.size() == 0) {
                stackedShipLeads.addAll(successfulShotsOfPcAttack);
                stackedShipTracker();
            }

        } else {
            char x = Helper.getRandomChar_A_J();
            int y = Helper.getRandomInt(10);

            for (int i = 0; i < pcMoves.size(); i++) {
                if (pcMoves.get(i).getX() == x && pcMoves.get(i).getY() == y) {
                    x = Helper.getRandomChar_A_J();
                    y = Helper.getRandomInt(10);
                    i = -1;
                }
            }
            shot.setY(y);
            shot.setX(x);
            pcMoves.add(shot);
            System.out.println("\nPC shoots at " + shot.getX() + shot.getY());

            for (int i = 0; i < ShipGenerator.playerGameBoard.gridList.size(); i++) {
                if (ShipGenerator.playerGameBoard.gridList.get(i).getX() == x && ShipGenerator.playerGameBoard.gridList.get(i).getY() == y && ShipGenerator.playerGameBoard.gridList.get(i).isShip()) {
                    pcSuccessfulShotsCounter++;
                    playersShipLocationKnown = true;
                    System.out.println("Players ship was hit.");
                    hitSuccess = true;
                    shipHealthCounter(ShipGenerator.playerGameBoard, ShipGenerator.playerGameBoard.gridList.get(i).getShipName());
                    successfulShotsOfPcAttack.add(new Coordinates(x, y));
                    generatePossibleShipLocations(shot);
                    firstPcHitInAttackCoordinates.setX(x);
                    firstPcHitInAttackCoordinates.setY(y);
                }
            }

            if (!hitSuccess) {
                System.out.println("Pc shot unsuccessful.");
            }
        }
    }

    private void generatePossibleShipLocations(Coordinates lead) {
        if (lead.getX() + 1 <= 'J' && hasPcShotAt(lead.getY(), (char) (lead.getX() + 1))) {
            possiblePlayerShipLocations.add(new Coordinates((char) (lead.getX() + 1), lead.getY()));
        }
        if (lead.getX() - 1 >= 'A' && hasPcShotAt(lead.getY(), (char) (lead.getX() - 1))) {
            possiblePlayerShipLocations.add(new Coordinates((char) (lead.getX() - 1), lead.getY()));
        }
        if (lead.getY() + 1 <= 10 && hasPcShotAt(lead.getY() + 1, lead.getX())) {
            possiblePlayerShipLocations.add(new Coordinates(lead.getX(), lead.getY() + 1));
        }
        if (lead.getY() - 1 >= 1 && hasPcShotAt(lead.getY() - 1, lead.getX())) {
            possiblePlayerShipLocations.add(new Coordinates(lead.getX(), lead.getY() - 1));
        }
    }

    public void stackedShipTracker() {
        firstPcHitInAttackCoordinates.setX(stackedShipLeads.get(0).getX());
        firstPcHitInAttackCoordinates.setY(stackedShipLeads.get(0).getY());
        generatePossibleShipLocations(stackedShipLeads.get(0));
        stackedShipLeads.remove(0);
    }

    // Checks if Pc has already tried given coordinates.
    public boolean hasPcShotAt(int y, char x) {
        for (Coordinates pcMove : pcMoves) {
            if (pcMove.getX() == x && pcMove.getY() == y) {
                return false;
            }
        }
        return true;
    }

    // Adjusts ship health counters in either pc or player gameBoard;
    public void shipHealthCounter(GameBoard gameBoard, String shipName) {
        switch (shipName) {
            case "Carrier":
                gameBoard.setCarrierHealth(gameBoard.getCarrierHealth() - 1);
                break;
            case "Battleship":
                gameBoard.setBattleShipHealth(gameBoard.getBattleShipHealth() - 1);
                break;
            case "Cruiser":
                gameBoard.setCruiserHealth(gameBoard.getCruiserHealth() - 1);
                break;
            case "Submarine":
                gameBoard.setSubmarineHealth(gameBoard.getSubmarineHealth() - 1);
                break;
            case "Destroyer":
                gameBoard.setDestroyerHealth(gameBoard.getDestroyerHealth() - 1);
                break;
        }
    }

    // Checks if given ship's health is down to 0 in a given grid;
    public boolean wasShipSunk(GameBoard gameBoard, String shipName) {
        return (shipName.equals("Carrier") && gameBoard.getCarrierHealth() == 0 ||
                shipName.equals("Battleship") && gameBoard.getBattleShipHealth() == 0 ||
                shipName.equals("Cruiser") && gameBoard.getCruiserHealth() == 0 ||
                shipName.equals("Submarine") && gameBoard.getSubmarineHealth() == 0 ||
                shipName.equals("Destroyer") && gameBoard.getDestroyerHealth() == 0);
    }

    private int getMaxShipSize(String name) {
        int size = -1;
        switch (name) {
            case "Carrier":
                size = 5;
                break;
            case "Battleship":
                size = 4;
                break;
            case "Cruiser":
            case "Submarine":
                size = 3;
                break;
            case "Destroyer":
                size = 2;
                break;
        }
        return size;
    }
}
