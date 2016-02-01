package carcassonne.controller;

import carcassonne.model.Coordinates;
import carcassonne.model.Rotation;
import carcassonne.model.Tile;
import carcassonne.view.PlacedFollower;

import java.awt.*;
import java.util.Map;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class GameData {
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

    public GameData(String name, String points, String followers, Color playerColor, String tilesLeft,
                    Tile currentTile, Tile previouslyPlacedTile, Set<Coordinates> possibleTileLocations,
                    Set<PlacedFollower> placedFollowers, Map<Coordinates, Set<Rotation>> possibleLocationsAndRotations) {
        this.name = name;
        this.points = points;
        this.followers = followers;
        this.playerColor = playerColor;
        this.tilesLeft = tilesLeft;
        this.currentTile = currentTile;
        this.previouslyPlacedTile = previouslyPlacedTile;
        this.possibleTileLocations = possibleTileLocations;
        this.placedFollowers = placedFollowers;
        this.possibleLocationsAndRotations = possibleLocationsAndRotations;
    }

    public String getName() {
        return name;
    }

    public String getPoints() {
        return "" + points;
    }

    public String getFollowers() {
        return "" + followers;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public String getTilesLeft() {
        return tilesLeft;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public Tile getPreviouslyPlacedTile() {
        return previouslyPlacedTile;
    }

    public Set<Coordinates> getPossibleTileLocations() {
        return possibleTileLocations;
    }

    public Map<Coordinates, Set<Rotation>> getPossibleLocationsAndRotations() {
        return possibleLocationsAndRotations;
    }

    public Set<PlacedFollower> getPlacedFollowers() {
        return placedFollowers;
    }
}
