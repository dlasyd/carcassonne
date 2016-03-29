package carcassonne.model;

import carcassonne.model.tile.Rotation;
import carcassonne.model.tile.TileDirection;

public interface OwnershipChecker {
    boolean locationIsLegal(int currentTileX, int currentTileY, Rotation angle, TileDirection direction);
}
