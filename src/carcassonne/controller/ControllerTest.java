package carcassonne.controller;

import carcassonne.model.Game;
import carcassonne.model.Table;
import carcassonne.model.Tile;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import static org.junit.Assert.*;

/**
 * Created by Andrey on 01/12/15.
 */
public class ControllerTest {
    Game game;
    Table table;

    @Before
    public final void before() {
        game = Game.getInstance();
        game.loadTestTiles();
        game.addPlayer("Anton" , Color.RED);
        game.addPlayer("Andrey", Color.YELLOW);

        int[][] confirmTileCoordinates = {{0, 1}, {1,1}, {1, 2}};
    }

    /*
     * TODO pass arguments to confirmTileMethod and check final game field in the end
     * TODO get Follower Possible Locations and place a follower for one player
     */
    @Test
    public void playGame() {
        // preparing game
        game.nextPlayer();
        game.dragTile();

        for (int i = 0; i < 3; i++) {
            confirmTile();
            confirmFollower();
        }

        assertEquals ("Game is finished", true, game.isFinished());

    }

    public void confirmTile() {

    }

    public void confirmFollower() {
        game.nextPlayer();
        game.dragTile();
    }
}
