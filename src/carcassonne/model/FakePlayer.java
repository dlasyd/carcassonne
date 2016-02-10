package carcassonne.model;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 11/02/16.
 */
public class FakePlayer extends Player {
    void placeFollower() {
        decreaseNumberOfFollowers();
    }

    void returnFollower() {
        increaseNumberOfFollowers();
     }
}
