package carcassonne.model;

import carcassonne.controller.GameWindowLogic;
import carcassonne.controller.WindowLogic;
import carcassonne.view.FakeWindow;
import carcassonne.view.ViewWindow;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.*;

import static org.junit.Assert.*;

public class GameTest {
    @Rule
    public ExpectedException exception =
            ExpectedException.none();
    Game game;
    Table table;

    @Before
    public final void setUp() {
        game = new Game();
        table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        game.setTable(table);
        game.addPlayer("Anton" , Color.RED);
        game.addPlayer("Andrey", Color.YELLOW);
        game.getTilePile().addXCrossroads(5);
    }

    @Test
    public void gameStaticFactoryObjectIsCreated() {
        Game game = Game.getInstance();
        assertFalse(game == null);
        Game game2 = Game.getInstance();
        assert (game == game2);
    }

    @Test
    public void numberOfPlayersShouldBe2to5() {
        int numberOfP = game.getNumberOfPlayers();
        assertTrue (numberOfP > 1 && numberOfP < 6);
    }

    @Test
    public void ifNextPlayerThenCurrentPlayer() {
        game.nextPlayer();
        Player player = game.getCurrentPlayer();
        assertNotEquals ("CurrentPlayer", player, null);
    }

    @Test
    public void nextPlayerIsDifferent() {
        Player player1 = game.getCurrentPlayer();
        game.nextPlayer();
        assertFalse (player1 == game.getCurrentPlayer());
    }

    @Test
    public void backToFirstPlayerAfterLast() {
        game.nextPlayer();
        assertTrue (game.getNumberOfPlayers() == 2);
        Player player1 = game.getCurrentPlayer();
        game.nextPlayer();
        game.nextPlayer();
        Player player2 = game.getCurrentPlayer();
        assertTrue (player1 == player2);
    }

    @Test
    public void addPlayerWhenLessThen6() {
        Game game = new Game();
        for (int i = 0; i < 5; i++) {
            assertTrue ("\nShould be possible to add player number " + i, game.addPlayer());
        }
        assertFalse (game.addPlayer());
    }

    @Test
    public void currentPlayerDoesNotExistInTheBeginning() {
        assertEquals ("Current player before first method invocation", null, game.getCurrentPlayer());
    }

    @Test
    public void confirmTilePlacementPlacesTileOnTable() {
        Tile currentTile = Tile.getInstance(1, 0, TileName.CITY1);
        table.placeTile(currentTile);
        assertEquals (currentTile, table.getTile(1,0));
    }

    @Test
    public void confirmTilePlacement() {
        assertEquals ("Current Tile is not confirmed", false, game.isCurrentTileConfirmed());
        game.confirmCurrentTile(1, 2);
        assertEquals ("Current Tile is confirmed", true, game.isCurrentTileConfirmed());
    }

    @Test
    public void ifTileIsNotPlacedRuntimeExceptionWhenCommittingFollower() {
        exception.expect(RuntimeException.class);
        game.confirmFollower();
    }

    @Test
    public void ifTileNotFollowerFriendlyAndPlaceFollowerThenException() {
        exception.expect(RuntimeException.class);
        game.confirmCurrentTile(1,1);
        game.confirmFollower();
    }

    @Test
    public void ifFollowerConfirmedPlayerOwnsOneMoreProperty() {
        game.nextPlayer();
        Player player = game.getCurrentPlayer();
        assertEquals ("\n Number of feature objects in the beginning", 0, player.getNumberOfProperties());
        game.confirmCurrentTile(1,1);
        game.setFollowerFriendly(true);
        game.confirmFollower();
        assertEquals ("\n Number of feature objects after 1 placed follower", 1, player.getNumberOfProperties());
    }

    @Test
    public void followerConfirmedThenPickNextPlayer() {
        game.nextPlayer();
        Player player = game.getCurrentPlayer();
        game.confirmCurrentTile(1,1);
        game.setFollowerFriendly(true);
        game.confirmFollower();

        assertEquals ("Should be second player", "Andrey", game.getCurrentPlayer().getName());
        game.confirmCurrentTile(1,1);
        game.setFollowerFriendly(true);
        game.confirmFollower();

        assertEquals ("Back to first player", "Anton", game.getCurrentPlayer().getName());
    }

    @Test
    public void nextPlayerPicksNewTile() {
        game.dragTile();
        Tile currentTile = game.getCurrentTile();
        assertNotEquals ("\nCurrent tile should not be null", currentTile, null);
        assertEquals ("\nCurrent tile should not change before player", game.getCurrentTile(), currentTile);
        game.nextPlayer();
    }

    @Test
    public void noCurrentTileWithoutImplicitInvocation() {
        assertEquals ("currentTile before it is picked must be null", game.getCurrentTile(), null);
    }

    @Test
    public void ifNoTilesLeftThenFinishedTrue() {
        /*
         * added later because previous test implementation stopped working
         */
        WindowLogic windowLogic = new GameWindowLogic();
        ViewWindow viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        /*
         * end of added later
         */


        game.turnActions(1,0);
        game.turnActions(2,0);
        game.turnActions(3,0);
        game.turnActions(4,0);
        game.turnActions(5,0);
        assertEquals("\nNo tiles left. game.isFinished()", true, game.isFinished());
        /*
         * This is not tested, but the dragTile method is intended to invoke gameFinished() method
         * that makes boolean finished = true; and later will perform other actions
         */
    }

    @Test
    public void playerCanPlaceFollowerIFFhasFollowersLeft() {
        game.nextPlayer();
        assertEquals ("\nPlayer has avaliable followers, can place a follower",
                game.getCurrentPlayer().hasAvaliableFollowers(), game.followerCanBePlaced());
    }

    @Test
    public void ifFollowerIsPlacedThenNumberOfAvaliableFollowersDecrease() {
        game.nextPlayer();
        Player player = game.getCurrentPlayer();
        game.confirmCurrentTile(1,0);
        game.setFollowerFriendly(true);
        game.confirmFollower();
        assertEquals("\n 6, number of followers after one is placed", 6, player.getNumberOfFollowers());
    }

    @Test
    public void ifFollowerIsReturnedThenNumberOfAvaliableFollowersIncrease() {
        game.nextPlayer();
        Player player = game.getCurrentPlayer();
        game.confirmCurrentTile(1,0);
        game.setFollowerFriendly(true);
        game.confirmFollower();
        player.returnFollower();
        assertEquals("\n 7, number of followers after one is returned", 7, player.getNumberOfFollowers());
    }

    @Test
    public void firstPlayerNameIsAnton() {
        game.nextPlayer();
        assertEquals ("first player name", "Anton", game.getCurrentPlayer().getName());
    }
}























