package com.martin;

public class Coordinates {
    private char x;
    private int y;
    private boolean busy;
    private boolean ship;
    private String ShipName;

    public Coordinates(char x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {
    }

    public String getShipName() {
        return ShipName;
    }

    public void setShipName(String shipName) {
        ShipName = shipName;
    }

    public boolean isShip() {
        return ship;
    }

    public void setShip(boolean ship) {
        this.ship = ship;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public char getX() {
        return x;
    }

    public void setX(char x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
