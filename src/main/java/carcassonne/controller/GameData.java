package carcassonne.controller;

import carcassonne.model.tile.Coordinates;
import carcassonne.model.tile.Rotation;
import carcassonne.model.tile.Tile;
import carcassonne.view.PlacedFollower;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class GameData {
    private final String name;
    private final String points;
    private final int followers;
    private final Color playerColor;
    private final String tilesLeft;
    private final Tile currentTile;
    private final Tile previouslyPlacedTile;
    private final Set<PlacedFollower> placedFollowers;
    private final Map<Coordinates, Set<Rotation>> possibleLocationsAndRotations;
    private final ArrayList<String[]> playersStats;

    public GameData(String name, String points, int followers, Color playerColor, String tilesLeft,
                    Tile currentTile, Tile previouslyPlacedTile, Set<Coordinates> possibleTileLocations,
                    Set<PlacedFollower> placedFollowers, Map<Coordinates, Set<Rotation>> possibleLocationsAndRotations,
                    ArrayList<String[]> playersStats) {
        this.name = name;
        this.points = points;
        this.followers = followers;
        this.playerColor = playerColor;
        this.tilesLeft = tilesLeft;
        this.currentTile = currentTile;
        this.previouslyPlacedTile = previouslyPlacedTile;
        this.placedFollowers = placedFollowers;
        this.possibleLocationsAndRotations = possibleLocationsAndRotations;
        this.playersStats = playersStats;
    }

    public String getName() {
        return name;
    }

    public String getPoints() {
        return "" + points;
    }

    public int getFollowers() {
        return followers;
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

    public Map<Coordinates, Set<Rotation>> getPossibleLocationsAndRotations() {
        return possibleLocationsAndRotations;
    }

    public Set<PlacedFollower> getPlacedFollowers() {
        return placedFollowers;
    }

    public ArrayList<String[]> getPlayersStats() {
        return playersStats;
    }
}
