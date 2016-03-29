package carcassonne.model.realestate;

import carcassonne.model.FakePlayer;
import carcassonne.model.Player;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;

import java.util.*;
import java.util.stream.Collectors;

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
            tilesWithCity.addAll(tile.getFeatures().stream()
                    .filter(feature -> feature.isCity() &&
                        tile.featureBordersWith(feature, this.getTilesAndFeatureTileDirections().get(tile)))
                    .map(feature -> tile)
                    .collect(Collectors.toList()));
        }

        Set<RealEstate> citiesOnLand = new HashSet<>();

        Player helper = new FakePlayer();
        for (Tile tile: tilesWithCity) {
            Set<TileDirection> disjointCitiesDirections = oneDirectionPerDisjointCity(tile);

            for (TileDirection direction: disjointCitiesDirections) {
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
     * Returns a set of TileDirection, one TileDirection per disjoint city
     */
    private Set<TileDirection> oneDirectionPerDisjointCity(Tile tile) {
        Set<TileDirection> directionsToCheck = new HashSet<>(Arrays.asList(
                TileDirection.WEST, TileDirection.EAST, TileDirection.NORTH, TileDirection.SOUTH));

        /*
         * One tile can have two disjoint city Feature sets, so every tile should be checked twice
         */
        Set<TileDirection> remainingDirectionsToCheck = new HashSet<>(directionsToCheck);
        Set<TileDirection> disjointCitiesDirections = addOneCityDirection(tile, directionsToCheck);

        remainingDirectionsToCheck.removeAll(disjointCitiesDirections);
        disjointCitiesDirections.addAll(addOneCityDirection(tile, remainingDirectionsToCheck));
        disjointCitiesDirections.retainAll(directionsToCheck);
        return  disjointCitiesDirections;
    }

    private Set<TileDirection> addOneCityDirection(Tile tile, Set<TileDirection> directionsToCheck) {
        Set<TileDirection> disjointCitiesDirections = new HashSet<>();
        for (TileDirection tileDirection: directionsToCheck) {
            if (tile.getFeature(tileDirection).isCity()) {
                disjointCitiesDirections.add(tileDirection);
                disjointCitiesDirections.addAll(tile.getDestinations(tileDirection));
                break;
            }
        }
        return disjointCitiesDirections;
    }
}
