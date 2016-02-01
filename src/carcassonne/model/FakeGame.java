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
    private FollowerPlacingHelper   followerPlacingHelper = new FollowerPlacingHelper();

    @Override
    public void turnActions(int x, int y, Rotation angle, double[] follower) {
        lastFollowerTileDirections = currentTileFollowerMap.getDirection(follower);
        super.turnActions(x, y, angle, follower);
    }

    @Override
    public Set<double[]> getPossibleFollowerLocations(int currentTileX, int currentTileY) {
        currentTileFollowerMap = followerPlacingHelper.getFollowerLocations(getCurrentTile());
        return super.getPossibleFollowerLocations(currentTileX, currentTileY);
    }

    public TileDirections getLastFollowerTileDirections() {
        return lastFollowerTileDirections;
    }
}
