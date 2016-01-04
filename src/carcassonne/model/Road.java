package carcassonne.model;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 04/01/16.
 */
class Road extends RealEstate {

    Road(Tile tile, Table table) {
        super(tile, table);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
