package carcassonne.model;

/**
 * Created by Andrey on 02/12/15.
 * Features of tile are roads, cities, land and cloisters
 */
class Feature {
    private boolean followerPlaced = false;
    private Follower follower;

    boolean isFollowerPlaced() {
        return follower == null ? false : true;
    }

    void setFollower(Follower follower) {
        if (isFollowerPlaced())
            throw new RuntimeException("Trying to place follower on feature element that already has one");
        this.follower = follower;
    }
}
