package carcassonne.model;

import java.util.ArrayList;

import static carcassonne.model.FeatureType.*;
import static carcassonne.model.TileDirections.*;

public class TilePile {
    private static TilePile tilePile;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();

    public static Tile getReferenceTile(TileName tileName) {
        Tile tile = Tile.getInstance();
        switch (tileName) {
            case CITY2WE:
                tile.addFeature(Feature.createFeature(CITY), WWN, WEST, WWS, EAST, EEN, EES);
                tile.addFeature(Feature.createFeature(LAND), NNW, NORTH, NNE);
                tile.addFeature(Feature.createFeature(LAND), SSW, SOUTH, SSE);
                break;
            case CITY2NW:
                tile.addFeature(Feature.createFeature(CITY), NNW, NNE, NORTH, WWN, WWS, WEST);
                tile.addFeature(Feature.createFeature(LAND), tile.getUnoccupiedDirections());
                break;
            case CITY2NWR:
                tile.addFeature(Feature.createFeature(CITY), NNW, NNE, NORTH, WWN, WWS, WEST);
                tile.addFeature(Feature.createFeature(ROAD), EAST, SOUTH);
                tile.addFeature(Feature.createFeature(LAND), SSW, EEN);
                tile.addFeature(Feature.createFeature(LAND), tile.getUnoccupiedDirections());
                break;
            case ROAD4:
                tile.addFeature(Feature.createFeature(ROAD), EAST);
                tile.addFeature(Feature.createFeature(ROAD), WEST);
                tile.addFeature(Feature.createFeature(ROAD), SOUTH);
                tile.addFeature(Feature.createFeature(ROAD), NORTH);
                tile.addFeature(Feature.createFeature(LAND), WWN, NNW);
                tile.addFeature(Feature.createFeature(LAND), NNE, EEN);
                tile.addFeature(Feature.createFeature(LAND), WWS, SSW);
                tile.addFeature(Feature.createFeature(LAND), EES, SSE);
                break;
            case CITY1:
                tile.addFeature(Feature.createFeature(CITY), NNW, NORTH, NNE);
                tile.addFeature(Feature.createFeature(LAND), tile.getUnoccupiedDirections());
                break;
            case ROAD2NS:
                tile.addFeature(Feature.createFeature(LAND), WEST, WWN, WWS, SSW, NNW);
                tile.addFeature(Feature.createFeature(ROAD), NORTH, SOUTH);
                tile.addFeature(Feature.createFeature(LAND), tile.getUnoccupiedDirections());
                break;
            case ROAD2SW:
                tile.addFeature(Feature.createFeature(ROAD), SOUTH, WEST);
                tile.addFeature(Feature.createFeature(LAND), WWS, SSW);
                tile.addFeature(Feature.createFeature(LAND), tile.getUnoccupiedDirections());
                break;
            case CITY3:
                tile.addFeature(Feature.createFeature(LAND), SOUTH, SSW, SSE);
                tile.addFeature(Feature.createFeature(CITY), tile.getUnoccupiedDirections());
                break;
            case CITY1RWE:
                tile.addFeature(Feature.createFeature(CITY), NORTH, NNW, NNE);
                tile.addFeature(Feature.createFeature(LAND), EEN, WWN);
                tile.addFeature(Feature.createFeature(ROAD), EAST, WEST);
                tile.addFeature(Feature.createFeature(LAND), EES, WWS, SSE, SSW, SOUTH);
                break;
            case CITY4:
                tile.addFeature(Feature.createFeature(CITY), NORTH, NNW, NNE, EAST, EEN, EES, SOUTH, SSE,SSW, WEST, WWN, WWS);
                break;
            case CLOISTER:
                tile.addFeature(Feature.createFeature(CLOISTER), CENTER);
                tile.addFeature(Feature.createFeature(LAND), tile.getUnoccupiedDirections());
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
