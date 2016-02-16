package carcassonne.model;

import java.awt.Color;

public class Player {
    private String name;
    private Color color;
    private int numberOfFollowers = 7;
    private int numberOfProperties;
    private int currentPoints = 0;

    public Player(String name, Color color) {
        this.color = color;
        this.name = name;
    }

    public Player() {}

    String getName() {
        return name;
    }

    Color getColor() {
        return color;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    void increaseNumberOfFollowers() {
        numberOfFollowers++;
    }

    void decreaseNumberOfFollowers() {
        numberOfFollowers--;
    }

    public void placeFollower() {
        decreaseNumberOfFollowers();
        if (numberOfFollowers < 0)
            throw new RuntimeException("Number of followers that a player has is less than 0 " + name);
    }

    public void returnFollower() {
        increaseNumberOfFollowers();
        if (numberOfFollowers > 7)
            throw new RuntimeException("Number of followers that a player has is more than 7");
    }

    public int getNumberOfProperties() {
        return numberOfProperties;
    }

    public boolean hasAvailableFollowers() {
        return numberOfFollowers > 0;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void increaseCurrentPoints(int points) {
        this.currentPoints +=  points;
    }
}
