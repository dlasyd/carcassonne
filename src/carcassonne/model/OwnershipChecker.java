package carcassonne.model;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 03/02/16.
 */
public interface OwnershipChecker {
    boolean locationIsLegal(int currentTileX, int currentTileY, Rotation angle, TileDirections direction);
}
