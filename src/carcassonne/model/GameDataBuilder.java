package carcassonne.model;

import carcassonne.controller.GameData;

public class GameDataBuilder {
    private String name;
    private String points;
    private String followers;

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

    public GameData createGameData() {
        return new carcassonne.controller.GameDataBuilder().setName(name).setPoints(points).setFollowers(followers).createGameData();
    }
}