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
 * Tests data flow from model to controller and then to view
 * and all controller behaviour
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
        assertEquals("Current player in controller is correct", "Anton", fakeWindow.getCurrentPlayerName());
        game.turnActions(1, 0);
        assertEquals("Current player in controller is correct", "Andrey", fakeWindow.getCurrentPlayerName());
        windowLogic.setGameWindow(viewWindow);
    }

    @Test
    public void controllerCanSendInformation() {
        assertEquals("Current player in controller is correct", "Anton", fakeWindow.getCurrentPlayerName());
        fakeWindow.pressEndTurnButton();
        assertEquals("Current player in controller is correct", "Andrey", fakeWindow.getCurrentPlayerName());
        windowLogic.setGameWindow(viewWindow);
    }

    @Test
    public void beforeFirsMoveAllInfoIsDisplayedInGameWindow() {
        assertEquals("Current player in controller is correct", "Anton", fakeWindow.getCurrentPlayerName());
        assertEquals("Score in controller is correct", "0", fakeWindow.getCurrentPoints());
        assertEquals("Followers number is correct", "7", fakeWindow.getNumberOfFollwers());
        assertEquals("Tiles left", "4", fakeWindow.getTilesLeft());
    }

    @Test
    public void endGameWindowRunsWhenGameEnds() {
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        assertEquals("End game window not displayed", false, fakeWindow.isEndGameWindowDisplayed());
        fakeWindow.pressEndTurnButton();
        assertEquals("Game is finished", true, game.isFinished());
        assertEquals("Tile place button disabled", false, fakeWindow.isPlaceTileButtonEnabled());
        assertEquals("End turn button disabled", false, fakeWindow.isPlaceEndTurnButtonEnabled());
        assertEquals("End game window displayed", true, fakeWindow.isEndGameWindowDisplayed());
    }

    @Test
    public void lastTileCorrectButtonBehaviour() {
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        assertEquals("Game is finished", true, game.isFinished());
        assertEquals("Tile place button disabled", false, fakeWindow.isPlaceTileButtonEnabled());
        assertEquals("End turn button disabled", false, fakeWindow.isPlaceEndTurnButtonEnabled());
        fakeWindow.clickOnGamePanel();
        assertEquals("Tile place button enabled", false, fakeWindow.isPlaceTileButtonEnabled());
        assertEquals("End turn button disabled", false, fakeWindow.isPlaceEndTurnButtonEnabled());
    }

    /*
     * User should see tile preview in the top right corner only until he places
     */
    @Test
    public void tilePreviewIsDisplayedCorrectly() {
        assertEquals("Tile preview is enabled", true, fakeWindow.isTilePreviewEnabled());
        fakeWindow.clickOnGamePanel();
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

    @Test
    public void windowGetsFirstTile() {
        assertEquals("Placed tiles set is not empty", false, fakeWindow.isPlacedTileSetEmpty());
    }

    @Test
    public void fistTileIsCorrect() {
        assertEquals("First tile is correct", TileName.CITY1RWE, fakeWindow.getFirstPlacedTileName());
    }

    @Test
    public void coordinatesOfLegalTilePlacement() {
        Set<Coordinates> result = new HashSet<>();

        /*
         * This only tests the fact that data came through, correctness of
         * data is tested in TilePlacementHelperTest
         */
        assertEquals("Window has displayed possible tile position",
                false, fakeWindow.getPossibleTileLocations().isEmpty());
    }

    @Test
    public void endGameLastTileIsDisplayed() {
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        assertEquals ("Window displays 6 tiles", 6, fakeWindow.getTilesOnTable().size());
    }

    @Test
    public void currentTileDrawnOnlyAfterPlaced() {
        assertEquals("Current tile not placed before game area is clicked", false, fakeWindow.isCurrentTilePlaced());
        fakeWindow.clickOnGamePanel();
        assertEquals("Current tile placed after game area is clicked", true, fakeWindow.isCurrentTilePlaced());
        fakeWindow.pressEndTurnButton();
        assertEquals("Current tile not placed before game area is clicked", false, fakeWindow.isCurrentTilePlaced());
    }

    @Test
    public void confirmTileButtonStopsTileRelocation() {
        assertEquals("Tile can be relocated", true, fakeWindow.canTileBeRelocated());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressConfirmTileButton();
        assertEquals("Tile cannot be relocated", false, fakeWindow.canTileBeRelocated());
        fakeWindow.pressConfirmTileButton();
        assertEquals("Tile can be relocated", true, fakeWindow.canTileBeRelocated());
    }

    @Test
    public void rotateTileWhenClickOnPlacedTile() {
        assertEquals("Tile isn't rotated", Rotation.DEG_0, fakeWindow.getCurrentTileRotation());
        fakeWindow.clickOnGamePanel();
        fakeWindow.clickOnPlacedTile();
        assertEquals("Tile is rotated", Rotation.DEG_90, fakeWindow.getCurrentTileRotation());
    }

    @Test
    public void endGamePreviewCurrentTileDisabled() {
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        assertEquals("Tile preview is disabled", false, fakeWindow.isTilePreviewEnabled());
    }
}
