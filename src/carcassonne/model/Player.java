package carcassonne.model;

import java.awt.Color;

public final class Player {
    private String name;
    private Color color;
    private int numberOfFollowers = 7;
    private int numberOfProperties;
    private int currentPoints = 0;

    public Player(String name, Color color) {
        this.color = color;
        this.name = name;
    }

    Player() {}

    void increaseNumberOfProperties() {
        numberOfProperties++;
    }

    String getName() {
        return name;
    }

    Color getColor() {
        return color;
    }

    int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    void placeFollower() {
        numberOfFollowers--;
        if (numberOfFollowers < 0)
            throw new RuntimeException("Number of followers that a player has is less than 0");
    }

    void returnFollower() {
        numberOfFollowers++;
        if (numberOfFollowers > 7)
            throw new RuntimeException("Number of followers that a player has is more than 7");
    }

    public int getNumberOfProperties() {
        return numberOfProperties;
    }

    public boolean hasAvaliableFollowers() {
        if (numberOfFollowers > 0)
            return true;
        else
            return false;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void increaseCurrentPoints(int points) {
        this.currentPoints +=  points;
    }
}
