package carcassonne.model;

import java.util.Objects;

/**
 * Created by Andrey on 02/12/15.
 * Features of tile are roads, cities, land and cloisters
 */
public class Feature {
    Feature() {
    }

    private boolean followerPlaced = false;
    private Follower follower;

    static Feature createFeature(FeatureType type) {
        switch (type) {
            case ROAD:
                return new RoadPiece();
            case CLOISTER:
                return new CloisterPiece();
            case LAND:
                return new LandPiece();
            case CITY:
                return new CityPiece();
            case CITY_SHIELD:
                return new CityPieceWithShield();
        }
        return new Feature();
    }

    boolean isFollowerPlaced() {
        return follower == null ? false : true;
    }

    void setFollower(Follower follower) {
        if (isFollowerPlaced())
            throw new RuntimeException("Trying to place follower on feature element that already has one");
        this.follower = follower;
    }

    public boolean isCity() {
        return this instanceof CityPiece;
    }

    boolean hasShield() {
        return false;
    }

    public boolean isSameType(Feature feature) {
        this.getClass();
        feature.getClass();
        return this.getClass().equals(feature.getClass());
    }

    public boolean isRoad() {
        return false;
    }

    public boolean isLand() {
        return false;
    }
}
