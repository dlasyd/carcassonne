package carcassonne.controller;

import java.awt.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class GameData {
    public GameData(String name, String points, String followers, Color playerColor, String tilesLeft) {
        this.name = name;
        this.points = points;
        this.followers = followers;
        this.playerColor = playerColor;
        this.tilesLeft = tilesLeft;
    }

    private String name;
    private String points;
    private String followers;
    private Color playerColor;
    private String tilesLeft;

    public String getName() {
        return name;
    }

    public String getPoints() {
        return "" + points;
    }

    public String getFollowers() {
        return "" + followers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public String getTilesLeft() {
        return tilesLeft;
    }

    public void setTilesLeft(String tilesLeft) {
        this.tilesLeft = tilesLeft;
    }
}
