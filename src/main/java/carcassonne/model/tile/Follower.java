package carcassonne.model.tile;

import carcassonne.model.Player;

public class Follower {
    private final Player player;

    public Follower(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
