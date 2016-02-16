package carcassonne.model;

import carcassonne.model.tile.Rotation;
import carcassonne.model.tile.TileDirections;

public interface OwnershipChecker {
    boolean locationIsLegal(int currentTileX, int currentTileY, Rotation angle, TileDirections direction);
}
