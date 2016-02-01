package carcassonne.controller;

import carcassonne.model.Coordinates;
import carcassonne.model.Rotation;
import carcassonne.model.Tile;
import carcassonne.view.PlacedFollower;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class GameDataBuilder {
    private String name;
    private String points;
    private String followers;
    private Color playerColor;
    private String tilesLeft;
    private Tile currentTile;
    private Tile previouslyPlacedTile;
    private Set<Coordinates> possibleTileLocations;
    private Set<PlacedFollower> placedFollowers;
    private Map<Coordinates, Set<Rotation>> possibleLocationsAndRotations;

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

    public GameDataBuilder setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
        return this;
    }

    public GameDataBuilder setPreviouslyPlacedTile(Tile previouslyPlacedTile) {
        this.previouslyPlacedTile = previouslyPlacedTile;
        return this;
    }

    public GameDataBuilder setPossibleTileLocations(Set<Coordinates> possibleTileLocations) {
        this.possibleTileLocations = possibleTileLocations;
        return this;
    }

    public GameDataBuilder setPlacedFollowers(Set<PlacedFollower> placedFollowers) {
        this.placedFollowers = placedFollowers;
        return this;
    }

    public GameDataBuilder setPossibleLocationsAndRotations(Map<Coordinates, Set<Rotation>> possibleLocationsAndRotations) {
        this.possibleLocationsAndRotations = possibleLocationsAndRotations;
        return this;
    }

    public GameData createGameData() {
        return new GameData(name, points, followers, playerColor, tilesLeft, currentTile, previouslyPlacedTile, possibleTileLocations, placedFollowers, possibleLocationsAndRotations);
    }
}