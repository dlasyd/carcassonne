package carcassonne.model;

import carcassonne.controller.DataToModel;
import carcassonne.controller.FollowerMap;

import java.util.Set;

/**
 * Class that extends Game with methods used for testing
 */
public class FakeGame extends Game {
    private TileDirections      lastFollowerTileDirections;
    private FollowerMap         currentTileFollowerMap;

    @Override
    public void turnActions(int x, int y, Rotation angle, TileDirections direction) {
        lastFollowerTileDirections = direction;
        super.turnActions(x, y, angle, direction);
    }

    public TileDirections getLastFollowerTileDirections() {
        return lastFollowerTileDirections;
    }
}
