package carcassonne.controller;

import carcassonne.view.DrawableTile;

import java.awt.*;

public class GameDataBuilder {
    private String name;
    private String points;
    private String followers;
    private Color playerColor;
    private String tilesLeft;
    private DrawableTile currentTile;

    public GameDataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public GameDataBuilder setPoints(String points) {
        this.points = points;
        return this;
    }

    public GameDataBuilder setFollowers(String followers) {
        this.followers = followers;
        return this;
    }

    public GameDataBuilder setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
        return this;
    }

    public GameDataBuilder setTilesLeft(String tilesLeft) {
        this.tilesLeft = tilesLeft;
        return this;
    }

    public GameData createGameData() {
        return new GameData(name, points, followers, playerColor, tilesLeft, currentTile);
    }

    public GameDataBuilder setCurrentTile(DrawableTile currentTile) {
        this.currentTile = currentTile;
        return this;
    }
}