package carcassonne.model;

import java.util.Collections;
import java.util.HashSet;

/**
 * Represents cloister real estate. It consist of a cloister tile and
 * optionally up to 8 tiles around cloister.
 * Is finished if there are 8 tiles around cloister.
 * 1 point for each tile that is part of this real estate. (9 is max)
 */
class Cloister extends RealEstate {
    public Cloister(Tile tile, Table table) {
        super(tile, table);
    }

    boolean isFinished() {
        return false;
    }

    @Override
    int getPoints() {
        return super.getTilesAndFeatureTileDirections().keySet().size();
    }

    @Override
    void addTileAndConnectedTiles(Tile tile) {
        //TODO super might be redundant
        super.tilesAndFeatureTileDirections.put(tile, new HashSet<>(Collections.singletonList(TileDirections.CENTER)));
    }

    @Override
    void update(Tile tile) {
        for (int x = super.getFirstX() - 1; x < super.getFirstX() + 2; x++) {
            for (int y = super.getFirstY() - 1; y < super.getFirstY() + 2; y++) {
                if (tile.getX() == x && tile.getY() == y)
                    //TODO add using method, not variable
                    super.tilesAndFeatureTileDirections.put(tile, new HashSet<>(Collections.singletonList(TileDirections.CENTER)));
            }
        }
    }
}
