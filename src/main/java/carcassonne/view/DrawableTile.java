package carcassonne.view;

import carcassonne.model.tile.Rotation;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileName;

import java.awt.*;

/**
 * This class contains an image of a tile and information about rotation.
 * It stores a copy of a tile object that is passed in constructor and there is no way
 * to change the original tile using class methods.
 */
public class DrawableTile {
    private Tile tile;
    private javaxt.io.Image image;
    private Rotation rotation = Rotation.DEG_0;

    public DrawableTile(Tile tile) {
        assert (tile.getName() != null): "Tile has no TileName";
        if (tile.isNull()) {
            throw new RuntimeException("Cannot create a DrawableTile from null tile");
        }
        this.tile = tile;

        image = new javaxt.io.Image("res/tiles/" + this.getFileName());

        /*
         * ordinal() will return number of 90 degrees rotations
         */
        int numberOfRotations = tile.getCurrentRotation().ordinal();
        for (int i = 0; i < numberOfRotations; i++) {
            this.turnRight();
        }
    }

    public TileName getTileName() {
        return tile.getName();
    }

    public String getFileName() {
        return tile.getName().name().toLowerCase() + ".png";
    }

    public boolean noCoordinates() {
        return tile.getCoordinates() == null;
    }

    int getX() {
        return tile.getX();
    }

    int getY() {
        return tile.getY();
    }

    private void turnRight() {
        image.rotate(90);
        rotation = Rotation.values()[(rotation.ordinal() + 1) % 4];
    }

    public void setRotation(Rotation angle) {
        /*
         * First, rotate the image to initial position
         */
        image.rotate(- (90 * rotation.ordinal()));

        image.rotate(90 * angle.ordinal());
        rotation = angle;
    }

    public Rotation getCurrentRotation() {
        return rotation;
    }

    public Image getBufferedImage() {
        return image.getBufferedImage();
    }

    public Rotation getRotation() {
        return rotation;
    }
}
