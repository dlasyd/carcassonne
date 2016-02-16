package carcassonne.model.tile;

import carcassonne.model.Player;

/**
 * Created by Andrey on 01/12/15.
 */
public class Follower {
    private final Player player;

    public Follower(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
