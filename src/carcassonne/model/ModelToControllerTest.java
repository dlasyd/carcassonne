package carcassonne.model;

import carcassonne.controller.DataCollector;
import carcassonne.controller.DummyWindowLogic;
import carcassonne.controller.WindowLogic;
import carcassonne.view.FakeWindow;
import carcassonne.view.GameWindow;
import carcassonne.view.ViewWindow;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 14/01/16.
 */
public class ModelToControllerTest {
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
    public void dataIsPushed() {
        WindowLogic windowLogic = new DummyWindowLogic();
        ViewWindow viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        assertEquals("Current player in controller is correct", "Anton", ((FakeWindow)viewWindow).getCurrentPlayerName());
        game.turnActions(1, 0);
        assertEquals("Current player in controller is correct", "Andrey", ((FakeWindow)viewWindow).getCurrentPlayerName());
        windowLogic.setGameWindow(viewWindow);
    }

    @Test
    public void controllerCanSendInformation() {
        WindowLogic windowLogic = new DummyWindowLogic();
        ViewWindow viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(game);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        assertEquals("Current player in controller is correct", "Anton", ((FakeWindow)viewWindow).getCurrentPlayerName());
        ((FakeWindow) viewWindow).pressEndTurnButton();
        assertEquals("Current player in controller is correct", "Andrey", ((FakeWindow)viewWindow).getCurrentPlayerName());
        windowLogic.setGameWindow(viewWindow);
    }

    @Test
    public void beforeFirsMoveAllInfoIsDisplayedInGameWindow() {
        WindowLogic windowLogic = new DummyWindowLogic();
        ViewWindow viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(game);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        assertEquals("Current player in controller is correct", "Anton", ((FakeWindow)viewWindow).getCurrentPlayerName());
        assertEquals("Score in controller is correct", "0", ((FakeWindow)viewWindow).getCurrentPoints());
        assertEquals("Followers number is correct", "7", ((FakeWindow) viewWindow).getNumberOfFollwers());
        assertEquals("Tiles left", "4", ((FakeWindow) viewWindow).getTilesLeft());
    }

    @Test
    public void endGameWindowRunsWhenGameEnds() {
        WindowLogic windowLogic = new DummyWindowLogic();
        ViewWindow viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(game);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        assertEquals("Game is finished", true, game.isFinished());
        assertEquals("Tile place button disabled", false, ((FakeWindow) viewWindow).isPlaceTileButtonEnabled());
        assertEquals("End turn button disabled", false, ((FakeWindow) viewWindow).isPlaceEndTurnButtonEnabled());
        assertEquals("End game window displayed", true, ((FakeWindow) viewWindow).isEndGameWindowDisplayed());
    }

}
