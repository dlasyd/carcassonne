package carcassonne.model;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Created by Andrey on 01/12/15.
 */
public class GameFunctionalTest {
    Game game;
    Table table;

    @Before
    public final void before() {
        game = new Game();
        table = new Table();
        game.setTable(table);
        game.addPlayer("Anton" , Color.RED);
        game.addPlayer("Andrey", Color.YELLOW);
        game.getTilePile().addTile(Tile.getInstance());
        game.getTilePile().addTile(Tile.getInstance());
        game.getTilePile().addTile(Tile.getInstance());
    }

    @Test
    public void playGame() {
        while (!game.isFinished()) {
            game.dragTile();
        }
        assertEquals ("\n true, gameFinished", game.isFinished(), true);
    }
}