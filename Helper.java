package com.martin;

import java.util.ArrayList;
import java.util.Scanner;

enum Direction {
    UP(4), DOWN(2), RIGHT(1), LEFT(3);
    int direction;

    Direction(int direction) {
        this.direction = direction;
    }
}

public class Helper {

    public static Scanner scanner = new Scanner(System.in);

    public static int getRandomInt(int upTo) {
        return (int) (Math.random() * upTo) + 1;
    }

    public static char getRandomChar_A_J() {
        return (char) ((int) (Math.random() * 10) + 65);
    }

    public static String getCoordinatesFromUser() {
        boolean correctValuePassed = false;
        String coordinates;
        int i = 0;

        do {
            if (i > 0) System.out.println("Please enter correct coordinates.");
            i++;
            coordinates = scanner.nextLine();

            if (coordinates.length() < 2 || coordinates.length() > 3) continue;

            char a = coordinates.charAt(0);
            char a1 = coordinates.charAt(1);
            char a2;

            if (coordinates.length() == 3) {
                a2 = coordinates.charAt(2);
                if (a2 != '0') continue;
            }
            if (a < 'A' || a > 'j' || (a > 'J' && a < 'a') || a1 < '1' || a1 > '9') continue;
            correctValuePassed = true;
        } while (!correctValuePassed);

        return coordinates.toUpperCase();
    }

    public static int getIntegerFromUser(int upTo) {
        int value = -1;
        boolean correctValuePassed = false;
        while (!correctValuePassed) {
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value >= 0 && value <= upTo) {
                    correctValuePassed = true;
                } else {
                    System.out.println("Wrong value entered, try again.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Wrong value entered, try again.");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return value;
    }

    public static Direction getRandomDirection() {

        switch (getRandomInt(4)) {
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.DOWN;
            case 3:
                return Direction.LEFT;
            case 4:
                return Direction.UP;
        }
        return null;
    }

    public static Direction getDirectionFromUser() {
        int i = 0;
        boolean correctInput = false;
        Direction directionToReturn = null;

        do {
            if (i > 0) System.out.println("Please enter correct direction: RIGHT (1), DOWN (2), LEFT (3), UP (4)");
            i++;
            String direction = scanner.nextLine().toUpperCase();

            switch (direction) {
                case "1":
                case "RIGHT":
                    directionToReturn = Direction.RIGHT;
                    correctInput = true;
                    break;
                case "2":
                case "DOWN":
                    directionToReturn = Direction.DOWN;
                    correctInput = true;
                    break;
                case "3":
                case "LEFT":
                    directionToReturn = Direction.LEFT;
                    correctInput = true;
                    break;
                case "4":
                case "UP":
                    directionToReturn = Direction.UP;
                    correctInput = true;
                    break;
            }

        } while (!correctInput);

        return directionToReturn;
    }

    public static String convertCoordinatesToString(ArrayList<Coordinates> coordinatesList) {

        StringBuilder coordinates = new StringBuilder();

        for (Coordinates coordinate : coordinatesList) {
            String x = String.valueOf(coordinate.getX());
            String y = String.valueOf(coordinate.getY());

            coordinates.append(x);
            coordinates.append(y);
            coordinates.append(" ");
        }
        return coordinates.toString();
    }

    public static ArrayList<Coordinates> extractCoordinatesWithShips(GameBoard gameBoard) {
        ArrayList<Coordinates> toReturn = new ArrayList<>();

        for (int i = 0; i < gameBoard.gridList.size(); i++) {
            if (gameBoard.gridList.get(i).isShip()) {
                toReturn.add(gameBoard.gridList.get(i));
            }
        }
        return toReturn;
    }
}
