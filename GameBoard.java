package com.martin;

import java.util.ArrayList;

public class GameBoard {
    public ArrayList<Coordinates> gridList = new ArrayList<>();
    private int carrierHealth = 5;
    private int battleShipHealth = 4;
    private int cruiserHealth = 3;
    private int submarineHealth = 3;
    private int destroyerHealth = 2;

    public GameBoard() {
        for (int i = 1; i <= 10; i++) {
            for (char j = 'A'; j <= 'J'; j++) {
                Coordinates gridCoordinates = new Coordinates(j, i);
                gridList.add(gridCoordinates);
            }
        }
    }

    public int getCarrierHealth() {
        return carrierHealth;
    }

    public void setCarrierHealth(int carrierHealth) {
        this.carrierHealth = carrierHealth;
    }

    public int getBattleShipHealth() {
        return battleShipHealth;
    }

    public void setBattleShipHealth(int battleShipHealth) {
        this.battleShipHealth = battleShipHealth;
    }

    public int getCruiserHealth() {
        return cruiserHealth;
    }

    public void setCruiserHealth(int cruiserHealth) {
        this.cruiserHealth = cruiserHealth;
    }

    public int getSubmarineHealth() {
        return submarineHealth;
    }

    public void setSubmarineHealth(int submarineHealth) {
        this.submarineHealth = submarineHealth;
    }

    public int getDestroyerHealth() {
        return destroyerHealth;
    }

    public void setDestroyerHealth(int destroyerHealth) {
        this.destroyerHealth = destroyerHealth;
    }
}
