package carcassonne.model;

import java.util.ArrayList;

import static carcassonne.model.TileDirections.*;

public class TilePile {
    private static TilePile tilePile;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();

    public static Tile getReferenceTile(TileName tileName) {
        Tile tile = Tile.getInstance();
        switch (tileName) {
            case CITY2WE:
                tile.addFeature(new Feature(), WWN, WEST, WWS, EAST, EEN, EES);
                tile.addFeature(new Feature(), NNW, NORTH, NNE, END);
                tile.addFeature(new Feature(), SSW, SOUTH, SSE, END);
                break;
            case CITY2NW:
                tile.addFeature(new Feature(), NNW, NNE, NORTH, WWN, WWS, WEST);
                tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
                break;
            case ROAD4:
                tile.addFeature(new Feature(), EAST);
                tile.addFeature(new Feature(), WEST);
                tile.addFeature(new Feature(), SOUTH);
                tile.addFeature(new Feature(), NORTH);
                tile.addFeature(new Feature(), CENTER);
                tile.addFeature(new Feature(), WWN, NNW);
                tile.addFeature(new Feature(), NNE, EEN);
                tile.addFeature(new Feature(), WWS, SSW);
                tile.addFeature(new Feature(), EES, SSE);
                break;
            case CITY1:
                tile.addFeature(new Feature(), NNW, NORTH, NNE, END);
                tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
                break;
            case ROAD2NS:
                tile.addFeature(new Feature(), WEST, WWN, WWS, SSW, NNW);
                tile.addFeature(new Feature(), NORTH, SOUTH);
                tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
                break;
            case ROAD2SW:
                tile.addFeature(new Feature(), SOUTH, WEST);
                tile.addFeature(new Feature(), WWS, SSW);
                tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
                break;
            case CITY3:
                tile.addFeature(new Feature(), SOUTH, SSW, SSE, END);
                tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
                break;
            case CITY1RWE:
                tile.addFeature(new Feature(), NORTH, NNW, NNE, END);
                tile.addFeature(new Feature(), EEN, WWN);
                tile.addFeature(new Feature(), EAST, WEST);
                tile.addFeature(new Feature(), EES, WWS, SSE, SSW, SOUTH);

                break;
            default:
                throw new RuntimeException("Not supported yet");
        }
        return tile;

    /*
     * # # #
     *   # #
     * # # #
     *
    public void threeSideCastleWithWestLand(Tile tile) {
        tile.addFeature(new Feature(), WEST, WWN, WWS, END);
        tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
    }

    /*
     * # # #
     *   #
     * # # #
     *
    public void verticalTCastle(Tile tile) {
        tile.addFeature(new Feature(), EAST, EEN, EES, END );
        tile.addFeature(new Feature(), WEST, WWN, WWS, END);
        tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
    }

    /*
     *     #
     *   # #
     * # # #
     *
    public void bottomRightAngleCastle(Tile tile) {
        tile.addFeature(new Feature(), NNW, NNE, NORTH, WWN, WWS, WEST);
        tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
    }

    /*
     * # # #
     * # #
     * #
     *
    public void topLeftAngleCastle(Tile tile) {
        tile.addFeature(new Feature(), NNW, NNE, NORTH, WWN, WWS, WEST);
        tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
    }


    /*
     * # # #
     *
     *
     *
    public void topCap(Tile tile) {
        tile.addFeature(new Feature(), NNW, NORTH, NNE, END);
        tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
    }

    /*
     *
     * - - -
     *
     *
    public void simpleRoad(Tile tile) {
        tile.addFeature(new Feature(), WWN, NNW, NORTH, NNE, EEN);
        tile.addFeature(new Feature(), WEST, EAST);
        tile.addFeature(new Feature(), WWS, SSW, SOUTH, SSE, EES);
    }

    /*
     * w w w
     * w + -
     * w | w
     *
    public void roadTurn(Tile tile) {
        tile.addFeature(new Feature(), SOUTH, EAST);
        tile.addFeature(new Feature(), EES, SSE);
        tile.addFeature(new Feature(), tile.getUnoccupiedDirections());
    } */
    }

    public static TilePile getInstance() {
        if (tilePile == null)
            tilePile = new TilePile();
        return tilePile;
    }

    public int getNumberOfTiles() {
        return tiles.size();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    public Tile dragTile() {
        if (tiles.isEmpty())
            throw new RuntimeException("Trying to drag a tile from an empty pile");
        int randomIndex = (int) (Math.random() * tiles.size());
        Tile tile = tiles.get(randomIndex);
        tiles.remove(randomIndex);
        return tile;
    }

    public boolean hasTiles() {
        return tiles.isEmpty() != true;
    }
}
