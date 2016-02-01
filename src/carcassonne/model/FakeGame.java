package carcassonne.model;

import carcassonne.controller.DataToModel;

import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 25/01/16.
 */
public class FakeGame extends Game {
    private TileDirections lastFollowerTileDirections;

    @Override
    public void turnActions(int x, int y, Rotation angle, TileDirections direction) {
        lastFollowerTileDirections = direction;
        super.turnActions(x, y, angle, direction);
    }

    public TileDirections getLastFollowerTileDirections() {
        return lastFollowerTileDirections;
    }
}
