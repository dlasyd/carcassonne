package carcassonne.view;

import carcassonne.model.Rotation;
import carcassonne.model.Tile;
import carcassonne.model.TileDirections;
import carcassonne.model.TileName;

import java.awt.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 18/01/16.
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
        tile.turnRight(Rotation.DEG_90);
        image.rotate(90);
        rotation = Rotation.values()[(rotation.ordinal() + 1) % 4];
    }

    public Rotation getCurrentRotation() {
        return tile.getCurrentRotation();
    }

    public Image getBufferedImage() {
        return image.getBufferedImage();
    }

    public Rotation getRotation() {
        return rotation;
    }
}
