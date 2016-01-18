package carcassonne.model;

import carcassonne.controller.GameWindowLogic;
import carcassonne.controller.WindowLogic;
import carcassonne.view.FakeWindow;
import carcassonne.view.ViewWindow;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 14/01/16.
 */
public class ModelToControllerTest {
    Game game;
    Table table;
    WindowLogic windowLogic;
    ViewWindow viewWindow;
    FakeWindow fakeWindow;

    @Before
    public final void setUp() {
        game = new Game();
        table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        game.setTable(table);
        game.addPlayer("Anton" , Color.RED);
        game.addPlayer("Andrey", Color.YELLOW);
        game.getTilePile().add5DifferentTiles();

        windowLogic = new GameWindowLogic();
        viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(game);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        fakeWindow = (FakeWindow) viewWindow;

    }

    @Test
    public void dataIsPushed() {
        assertEquals("Current player in controller is correct", "Anton", ((FakeWindow)viewWindow).getCurrentPlayerName());
        game.turnActions(1, 0);
        assertEquals("Current player in controller is correct", "Andrey", ((FakeWindow)viewWindow).getCurrentPlayerName());
        windowLogic.setGameWindow(viewWindow);
    }

    @Test
    public void controllerCanSendInformation() {
        assertEquals("Current player in controller is correct", "Anton", ((FakeWindow)viewWindow).getCurrentPlayerName());
        ((FakeWindow) viewWindow).pressEndTurnButton();
        assertEquals("Current player in controller is correct", "Andrey", ((FakeWindow)viewWindow).getCurrentPlayerName());
        windowLogic.setGameWindow(viewWindow);
    }

    @Test
    public void beforeFirsMoveAllInfoIsDisplayedInGameWindow() {
        assertEquals("Current player in controller is correct", "Anton", ((FakeWindow)viewWindow).getCurrentPlayerName());
        assertEquals("Score in controller is correct", "0", ((FakeWindow)viewWindow).getCurrentPoints());
        assertEquals("Followers number is correct", "7", ((FakeWindow) viewWindow).getNumberOfFollwers());
        assertEquals("Tiles left", "4", ((FakeWindow) viewWindow).getTilesLeft());
    }

    @Test
    public void endGameWindowRunsWhenGameEnds() {
        ((FakeWindow) viewWindow).pressEndTurnButton();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        ((FakeWindow) viewWindow).pressEndTurnButton();
        assertEquals("Game is finished", true, game.isFinished());
        assertEquals("Tile place button disabled", false, ((FakeWindow) viewWindow).isPlaceTileButtonEnabled());
        assertEquals("End turn button disabled", false, ((FakeWindow) viewWindow).isPlaceEndTurnButtonEnabled());
        assertEquals("End game window displayed", true, fakeWindow.isEndGameWindowDisplayed());
    }

    /*
     * User should see tile preview in the top right corner only until he places
     */
    @Test
    public void tilePreviewIsDisplayedCorrectly() {
        assertEquals("Tile preview is enabled", true, fakeWindow.isTilePreviewEnabled());
        ((FakeWindow) viewWindow).clickOnGamePanel();
        assertEquals("Tile preview is disabled", false, fakeWindow.isTilePreviewEnabled());
    }

    @Test
    public void tilePreviewDisplaysDifferentTiles() {
        Set<String> tileNames = new HashSet<>();
        Set<String> expected = new HashSet<>(Arrays.asList("road4.png", "road3.png",
                "city1.png", "city1rwe.png", "city11ne.png"));
        tileNames.add(fakeWindow.getTilePreviewName());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressEndTurnButton();
        tileNames.add(fakeWindow.getTilePreviewName());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressEndTurnButton();
        tileNames.add(fakeWindow.getTilePreviewName());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressEndTurnButton();
        tileNames.add(fakeWindow.getTilePreviewName());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressEndTurnButton();
        tileNames.add(fakeWindow.getTilePreviewName());
        assertEquals("Tiles displayed correctly", expected, tileNames);
    }
}
