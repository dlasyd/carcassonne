package carcassonne.controller;

import carcassonne.model.OwnershipChecker;
import carcassonne.model.tile.Rotation;
import carcassonne.model.tile.TileDirection;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 13/01/16.
 */
public interface DataToModel {
    void turnActions(int x, int y, Rotation angle, TileDirection direction);

    void turnActions(int x, int y);

    void turnActions(int x, int y, Rotation angle);

    void forceNotify();

    OwnershipChecker getOwnershipChecker();
}
