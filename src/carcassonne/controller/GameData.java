package carcassonne.controller;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class GameData {
    public GameData(String name, int points, int followers) {
        this.name = name;
        this.points = points;
        this.followers = followers;
    }

    private String name;
    private int points;
    private int followers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
