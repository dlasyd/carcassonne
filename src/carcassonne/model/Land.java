package carcassonne.model;

import carcassonne.model.feature.Feature;

import java.util.*;

/**
 * Land is never finished.
 * Owners get 3 points for each finished castle that touches the land
 */
class Land extends RealEstate {
    public Land(Tile tile) {
        super(tile);
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
                if (feature.isCity() &&
                        tile.featureBordersWith(feature, this.getTilesAndFeatureTileDirections().get(tile)))
                    tilesWithCity.add(tile);

            }
        }

        Set<RealEstate> citiesOnLand = new HashSet<>();

        Player helper = new FakePlayer();
        for (Tile tile: tilesWithCity) {
            Set<TileDirections> disjointCitiesDirections = oneDirectionPerDisjointCity(tile);

            for (TileDirections direction: disjointCitiesDirections) {
                tile = tile.placeFollower(helper, direction);
                citiesOnLand.add(RealEstate.getInstance(tile));
            }
        }

        int points = 0;
        for (RealEstate city: citiesOnLand) {
            if (city.isFinished())
                points += 3;
        }
        return points;
    }

    /*
     * Returns a set of TileDirections, one TileDirection per disjoint city
     */
    private Set<TileDirections> oneDirectionPerDisjointCity(Tile tile) {
        Set<TileDirections> disjointCitiesDirections = new HashSet<>();

        Set<TileDirections> directionsToCheck = new HashSet<>(Arrays.asList(
                TileDirections.WEST, TileDirections.EAST, TileDirections.NORTH, TileDirections.SOUTH));

        /*
         * One tile can have two disjoint city feature sets, so every tile should be checked twice
         */
        Set<TileDirections> remainingDirectionsToCheck = new HashSet<>(directionsToCheck);
        disjointCitiesDirections = addOneCityDirection(tile, directionsToCheck);
        remainingDirectionsToCheck.removeAll(disjointCitiesDirections);
        disjointCitiesDirections.addAll(addOneCityDirection(tile, remainingDirectionsToCheck));
        disjointCitiesDirections.retainAll(directionsToCheck);
        return  disjointCitiesDirections;
    }

    private Set<TileDirections> addOneCityDirection(Tile tile, Set<TileDirections> directionsToCheck) {
        Set<TileDirections> disjointCitiesDirections = new HashSet<>();
        for (TileDirections tileDirection: directionsToCheck) {
            if (tile.getFeature(tileDirection).isCity()) {
                disjointCitiesDirections.add(tileDirection);
                for (TileDirections cityDirections: tile.getDestinations(tileDirection)) {
                    disjointCitiesDirections.add(cityDirections);
                }
                break;
            }
        }
        return disjointCitiesDirections;
    }
}
