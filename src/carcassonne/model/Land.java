package carcassonne.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Land is never finished.
 * Owners get 3 points for each finished castle that touches the land
 */
class Land extends RealEstate {
    public Land(Tile tile, Table table) {
        super(tile, table);
    }

    @Override
    boolean isFinished() {
        return false;
    }

    @Override
    int getPoints() {
        /*
         * 1) find all tiles that have city features
         * 2) count finished cities
         */
        Set<Tile> allLandTiles = getTileSet();
        Set<Tile> tilesWithCity = new HashSet<>();

        for (Tile tile: allLandTiles) {
            for (Feature feature: tile.getFeatures()) {
                if (feature.isCity())
                    tilesWithCity.add(tile);
            }
        }

        Set<RealEstate> citiesOnLand = new HashSet<>();

        Player helper = new Player();
        for (Tile tile: tilesWithCity) {
            Map<Tile, Set<TileDirections>> disjointCitiesDirections = getTileToCityDirectionsMap(tile);
            // TODO change the line below to make it work with disjoint castles on the same tile
            tile.placeFollower(helper, Util.any(disjointCitiesDirections.get(tile)));
            citiesOnLand.add(RealEstate.getInstance(tile, table));
            // place follower on every disjoint city tile
        }

        int points = 0;
        for (RealEstate city: citiesOnLand) {
            if (city.isFinished())
                points += 3;
        }
        return points;
    }

    /*
     * TODO make it work with disjointed castles on same tile
     */
    private Map<Tile, Set<TileDirections>> getTileToCityDirectionsMap(Tile tile) {
        Map<Tile, Set<TileDirections>> disjointCitiesDirections = new HashMap<>();

        TileDirections[] directionsToCheck = {TileDirections.WEST, TileDirections.EAST,
                TileDirections.NORTH, TileDirections.SOUTH};

        for (TileDirections tileDirection: directionsToCheck) {
            if (tile.getFeature(tileDirection).isCity()) {
                Util.addSetElement(disjointCitiesDirections, tile, tileDirection);
            }
        }

        return  disjointCitiesDirections;
    }
}
