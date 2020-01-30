package com.martin;

import java.util.ArrayList;

public class ShipGenerator {

    public static GameBoard pcGameBoard = new GameBoard();
    public static GameBoard playerGameBoard = new GameBoard();
    private int shipSize;
    private String ShipName;
    private boolean vertical;
    private boolean horizontal;
    private ArrayList<Coordinates> shipCoordinates;

    public ShipGenerator(int shipSize, String name, Boolean isPc) {
        this.shipSize = shipSize;
        this.ShipName = name;
        deployShip(isPc);
    }

    private void setShipCoordinates(boolean isPC) {
        ArrayList<Coordinates> shipCoordinates = new ArrayList<>();
        Coordinates coordinates1 = new Coordinates();
        Direction shipDirection;
        char xAxisMax = 'J';
        char xAxisMin = 'A';
        int yAxisMin = 1;
        int yAxisMax = 10;

        if (!isPC) {
            System.out.println("Enter " + ShipName + " ship first coordinate (A1-J10):");
            String coordinates = Helper.getCoordinatesFromUser();
            coordinates1.setX(coordinates.charAt(0));
            if (coordinates.length() == 2) {
                coordinates1.setY(Character.getNumericValue(coordinates.charAt(1)));
            } else coordinates1.setY(10);

            System.out.println("Please enter direction: RIGHT (1), DOWN (2), LEFT (3), UP (4)");

            shipDirection = Helper.getDirectionFromUser();

        } else {
            coordinates1.setX(Helper.getRandomChar_A_J());
            coordinates1.setY(Helper.getRandomInt(10));
            shipDirection = Helper.getRandomDirection();

            if (shipDirection == Direction.RIGHT || shipDirection == Direction.LEFT) {
                horizontal = true;
            } else vertical = true;
        }
        shipCoordinates.add(coordinates1);

        for (int i = 0; i < (shipSize - 1); i++) {
            if (shipDirection == Direction.RIGHT) {
                if (coordinates1.getX() + i < xAxisMax) {
                    shipCoordinates.add(new Coordinates((char) (coordinates1.getX() + (i + 1)), coordinates1.getY()));
                } else {
                    shipCoordinates.add(new Coordinates((char) ((int) xAxisMax - (i + 1)), coordinates1.getY()));
                }
            } else if (shipDirection == Direction.LEFT) {
                if (coordinates1.getX() - i > xAxisMin) {
                    shipCoordinates.add(new Coordinates((char) (coordinates1.getX() - (i + 1)), coordinates1.getY()));
                } else {
                    shipCoordinates.add(new Coordinates((char) ((int) xAxisMin + (i + 1)), coordinates1.getY()));
                }
            } else if (shipDirection == Direction.UP) {
                if (coordinates1.getY() - i > yAxisMin) {
                    shipCoordinates.add(new Coordinates(coordinates1.getX(), coordinates1.getY() - (i + 1)));
                } else {
                    shipCoordinates.add(new Coordinates(coordinates1.getX(), yAxisMin + (i + 1)));
                }
            } else if (shipDirection == Direction.DOWN) {
                if (coordinates1.getY() + i < yAxisMax) {
                    shipCoordinates.add(new Coordinates(coordinates1.getX(), coordinates1.getY() + (i + 1)));
                } else {
                    shipCoordinates.add(new Coordinates(coordinates1.getX(), yAxisMax - (i + 1)));
                }
            }
        }
        this.shipCoordinates = shipCoordinates;
    }

    private boolean isThereSpace(ArrayList<Coordinates> shipCoordinates, boolean isPC, GameBoard gameBoard) {
        for (Coordinates shipCoordinate : shipCoordinates) {
            int y = shipCoordinate.getY();
            char x = shipCoordinate.getX();

            for (int j = 0; j < 100; j++) {
                if (gameBoard.gridList.get(j).getY() == y && gameBoard.gridList.get(j).getX() == x) {

                    if (isPC && gameBoard.gridList.get(j).isBusy()) return false;
                    else if (!isPC && gameBoard.gridList.get(j).isShip()) return false;
                }
            }
        }
        return true;
    }

    private void deployShip(boolean isPC) {
        setShipCoordinates(isPC);
        if (isPC) {
            if (isThereSpace(shipCoordinates, true, pcGameBoard)) {

                for (int i = 0; i < shipCoordinates.size(); i++) {
                    int y = shipCoordinates.get(i).getY();
                    char x = shipCoordinates.get(i).getX();

                    for (int j = 0; j < 100; j++) {
                        if (pcGameBoard.gridList.get(j).getY() == y && pcGameBoard.gridList.get(j).getX() == x) {
                            pcGameBoard.gridList.get(j).setBusy(true);
                            pcGameBoard.gridList.get(j).setShip(true);
                            pcGameBoard.gridList.get(j).setShipName(ShipName);
                        }
                        if (horizontal) {
                            if (pcGameBoard.gridList.get(j).getY() == y + 1 && pcGameBoard.gridList.get(j).getX() == x || pcGameBoard.gridList.get(j).getY() == y - 1 && pcGameBoard.gridList.get(j).getX() == x) {
                                pcGameBoard.gridList.get(j).setBusy(true);
                            }
                            if ((i == 0 || i == shipCoordinates.size() - 1) && pcGameBoard.gridList.get(j).getY() == y && (pcGameBoard.gridList.get(j).getX() == x - 1 || pcGameBoard.gridList.get(j).getX() == x + 1)) {
                                pcGameBoard.gridList.get(j).setBusy(true);
                            }
                        }
                        if (vertical) {
                            if (pcGameBoard.gridList.get(j).getY() == y && pcGameBoard.gridList.get(j).getX() == x + 1 || pcGameBoard.gridList.get(j).getY() == y && pcGameBoard.gridList.get(j).getX() == x - 1) {
                                pcGameBoard.gridList.get(j).setBusy(true);
                            }
                            if ((i == 0 || i == shipCoordinates.size() - 1) && pcGameBoard.gridList.get(j).getX() == x && (pcGameBoard.gridList.get(j).getY() == y - 1 || pcGameBoard.gridList.get(j).getY() == y + 1)) {
                                pcGameBoard.gridList.get(j).setBusy(true);
                            }
                        }
                    }
                }
            } else deployShip(true);
        } else {
            if (isThereSpace(shipCoordinates, false, playerGameBoard)) {

                for (Coordinates shipCoordinate : shipCoordinates) {
                    int y = shipCoordinate.getY();
                    char x = shipCoordinate.getX();

                    for (int j = 0; j < 100; j++) {
                        if (playerGameBoard.gridList.get(j).getY() == y && playerGameBoard.gridList.get(j).getX() == x) {
                            playerGameBoard.gridList.get(j).setShip(true);
                            playerGameBoard.gridList.get(j).setShipName(ShipName);
                        }
                    }
                }
            } else {
                System.out.println("This place is taken");
                deployShip(false);
            }
        }
    }
}
