package carcassonne.model;

import carcassonne.controller.DataToModel;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 25/01/16.
 */
public class FakeGame implements DataToModel {
    private Rotation currentTileRotation = Rotation.DEG_0;

    @Override
    public void turnActions(int x, int y, Rotation angle, TileDirections direction) {
        this.currentTileRotation = angle;
    }

    @Override
    public void turnActions(int x, int y, Rotation angle) {
        this.currentTileRotation = angle;
    }

    @Override
    public void turnActions(int x, int y) {

    }

    @Override
    public void forceNotify() {

    }

    public Rotation getCurrentTileRotation() {
        return this.currentTileRotation;
    }
}
