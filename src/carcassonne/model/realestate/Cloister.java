package carcassonne.model.realestate;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirections;

import java.util.Collections;
import java.util.HashSet;

/**
 * Represents cloister real estate. It consist of a cloister tile and
 * optionally up to 8 tiles around cloister.
 * Is finished if there are 8 tiles around cloister.
 * 1 point for each tile that is part of this real estate. (9 is max)
 */
class Cloister extends RealEstate {
    public Cloister(Tile tile) {
        super(tile);
    }

    boolean isFinished() {
        return getNumberOfTiles() == 9;
    }

    @Override
    int getPoints() {
        return getTilesAndFeatureTileDirections().keySet().size();
    }

    @Override
    void addTileAndConnectedTiles(Tile tile) {
        putTile(tile, new HashSet<>(Collections.singletonList(TileDirections.CENTER)));
        for (int x = tile.getX() - 1; x < tile.getX() + 2; x++) {
            for (int y = tile.getY() - 1; y < tile.getY() + 2; y++) {
                if (!super.getTable().getTile(x, y).isNull()) {
                    putTile(super.getTable().getTile(x, y), new HashSet<>(Collections.singletonList(TileDirections.CENTER)));
                }
            }
        }
    }

    @Override
    void update(Tile tile) {
        for (int x = getFirstX() - 1; x < getFirstX() + 2; x++) {
            for (int y = getFirstY() - 1; y < getFirstY() + 2; y++) {
                if (tile.getX() == x && tile.getY() == y)
                    putTile(tile, new HashSet<>(Collections.singletonList(TileDirections.CENTER)));
            }
        }
    }
}
