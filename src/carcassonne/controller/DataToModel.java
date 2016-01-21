package carcassonne.controller;

import carcassonne.model.Rotation;
import carcassonne.model.TileDirections;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 13/01/16.
 */
public interface DataToModel {
    void turnActions(int x, int y, Rotation rotation);

    public void turnActions(int x, int y, TileDirections direction);
    public void turnActions(int x, int y);


    void forceNotify();
}
