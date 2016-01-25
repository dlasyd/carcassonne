package carcassonne.view;

import carcassonne.model.*;

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
        assert (tile.getName() != null);
        if (tile.isNull()) {
            throw new RuntimeException("Cannot create a DrawableTile from null tile");
        }

        if (tile.hasCoordinates()) {
            this.tile = Tile.getInstance(tile.getX(), tile.getY(), tile.getName());
        } else {
            this.tile = Tile.getInstance(tile.getName());
        }
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
        if (tile.getCoordinates() == null)
            return true;
        else
            return false;
    }

    int getX() {
        return tile.getX();
    }

    int getY() {
        return tile.getY();
    }

    public void turnRight() {
        image.rotate(90);
        rotation = Rotation.values()[(rotation.ordinal() + 1) % 4];
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
