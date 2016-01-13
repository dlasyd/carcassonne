package carcassonne.model;

import carcassonne.controller.GameData;

public class GameDataBuilder {
    private String name;
    private int points;
    private int followers;

    public GameDataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public GameDataBuilder setPoints(int points) {
        this.points = points;
        return this;
    }

    public GameDataBuilder setFollowers(int followers) {
        this.followers = followers;
        return this;
    }

    public GameData createGameData() {
        return new GameData(name, points, followers);
    }
}