package carcassonne.model;

/**
 * Created by Andrey on 02/12/15.
 * Property in the sense of something that can be own, as a legal term. Not "property" in the sense
 * of important characteristics of something.
 */
class Property {
    private boolean followerPlaced = false;
    private Follower follower;

    boolean isFollowerPlaced() {
        return follower == null ? false : true;
    }

    void setFollower(Follower follower) {
        if (isFollowerPlaced())
            throw new RuntimeException("Trying to place follower on property element that already has one");
        this.follower = follower;
    }
}
