package carcassonne.controller;

import carcassonne.model.Rotation;
import carcassonne.model.TileDirections;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 13/01/16.
 */
public interface DataToModel {
    void turnActions(int x, int y, Rotation angle, TileDirections direction);

    void turnActions(int x, int y, Rotation angle, double[] currentFollower);

    void turnActions(int x, int y);

    void turnActions(int x, int y, Rotation angle);


    void forceNotify();

    Set<double[]> getPossibleFollowerLocations(int currentTileX, int currentTileY);
}
