package carcassonne.model;

import carcassonne.controller.DataToModel;
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
    }

    public void add5DifferentTiles() {
        game.getTilePile().add5DifferentTiles();
    }

    public void add5CrossRoads() {
        game.getTilePile().addXCrossroads(5);
    }

    public void prepareGame() {
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
        add5DifferentTiles();
        prepareGame();
        assertEquals("Current player in controller is correct", "Anton", fakeWindow.getCurrentPlayerName());
        game.turnActions(1, 0);
        assertEquals("Current player in controller is correct", "Andrey", fakeWindow.getCurrentPlayerName());
        windowLogic.setGameWindow(viewWindow);
    }

    @Test
    public void controllerCanSendInformation() {
        add5CrossRoads();
        prepareGame();
        assertEquals("Current player in controller is correct", "Anton", fakeWindow.getCurrentPlayerName());
        fakeWindow.pressEndTurnButton();
        assertEquals("Current player in controller is correct", "Andrey", fakeWindow.getCurrentPlayerName());
        windowLogic.setGameWindow(viewWindow);
    }

    @Test
    public void beforeFirsMoveAllInfoIsDisplayedInGameWindow() {
        add5DifferentTiles();
        prepareGame();
        assertEquals("Current player in controller is correct", "Anton", fakeWindow.getCurrentPlayerName());
        assertEquals("Score in controller is correct", "0", fakeWindow.getCurrentPoints());
        assertEquals("Followers number is correct", "7", fakeWindow.getNumberOfFollwers());
        assertEquals("Tiles left", "4", fakeWindow.getTilesLeft());
    }

    @Test
    public void endGameWindowRunsWhenGameEnds() {
        add5CrossRoads();
        prepareGame();
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
        add5CrossRoads();
        prepareGame();
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
        add5CrossRoads();
        prepareGame();
        assertEquals("Tile preview is enabled", true, fakeWindow.isTilePreviewEnabled());
        fakeWindow.clickOnGamePanel();
        assertEquals("Tile preview is disabled", false, fakeWindow.isTilePreviewEnabled());
    }

    @Test
    public void tilePreviewDisplaysDifferentTiles() {
        game.getTilePile().addTile(TileName.CITY1RWE);
        game.getTilePile().addTile(TileName.ROAD3);
        prepareGame();
        Set<String> tileNames = new HashSet<>();
        Set<String> expected = new HashSet<>(Arrays.asList("road3.png", "city1rwe.png"));
        tileNames.add(fakeWindow.getTilePreviewName());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressEndTurnButton();
        tileNames.add(fakeWindow.getTilePreviewName());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressEndTurnButton();

        assertEquals("Tiles displayed correctly", expected, tileNames);
    }

    @Test
    public void windowGetsFirstTile() {
        add5DifferentTiles();
        prepareGame();
        assertEquals("Placed tiles set is not empty", false, fakeWindow.isPlacedTileSetEmpty());
    }

    @Test
    public void fistTileIsCorrect() {
        add5DifferentTiles();
        prepareGame();
        assertEquals("First tile is correct", TileName.CITY1RWE, fakeWindow.getFirstPlacedTileName());
    }

    @Test
    public void coordinatesOfLegalTilePlacement() {
        Set<Coordinates> result = new HashSet<>();
        result.addAll(Arrays.asList(new Coordinates(-1, 0), new Coordinates(1, 0),new Coordinates(0, 1) ));

        game = new Game();
        table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        game.setTable(table);
        game.addPlayer("Anton" , Color.RED);
        game.addPlayer("Andrey", Color.YELLOW);
        game.getTilePile().addTile(TileName.ROAD3);

        windowLogic = new GameWindowLogic();
        viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(game);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        fakeWindow = (FakeWindow) viewWindow;

        /*
         * This only tests the fact that data came through, correctness of
         * data is tested in TilePlacementHelperTest
         */
        assertEquals("Window has displayed possible tile position",
                result, fakeWindow.getPossibleTileLocations());
    }

    @Test
    public void endGameLastTileIsDisplayed() {
        add5CrossRoads();
        prepareGame();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        assertEquals ("Window displays 6 tiles", 6, fakeWindow.getTilesOnTable().size());
    }

    @Test
    public void currentTileDrawnOnlyAfterPlaced() {
        add5CrossRoads();
        prepareGame();
        assertEquals("Current tile not placed before game area is clicked", false, fakeWindow.isCurrentTilePlaced());
        fakeWindow.clickOnGamePanel();
        assertEquals("Current tile placed after game area is clicked", true, fakeWindow.isCurrentTilePlaced());
        fakeWindow.pressEndTurnButton();
        assertEquals("Current tile not placed before game area is clicked", false, fakeWindow.isCurrentTilePlaced());
    }

    @Test
    public void confirmTileButtonStopsTileRelocation() {
        add5CrossRoads();
        prepareGame();
        assertEquals("Tile can be relocated", true, fakeWindow.canTileBeRelocated());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressConfirmTileButton();
        assertEquals("Tile cannot be relocated", false, fakeWindow.canTileBeRelocated());
        fakeWindow.pressConfirmTileButton();
        assertEquals("Tile can be relocated", true, fakeWindow.canTileBeRelocated());
    }

    @Test
    public void rotateTileWhenClickOnPlacedTile() {
        add5CrossRoads();
        prepareGame();
        assertEquals("Tile isn't rotated", Rotation.DEG_0, fakeWindow.getCurrentTileRotation());
        fakeWindow.clickOnGamePanel();
        fakeWindow.clickOnPlacedTile();
        assertEquals("Tile is rotated", Rotation.DEG_90, fakeWindow.getCurrentTileRotation());
    }

    @Test
    public void endGamePreviewCurrentTileDisabled() {
        add5CrossRoads();
        prepareGame();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        fakeWindow.pressEndTurnButton();
        assertEquals("Tile preview is disabled", false, fakeWindow.isTilePreviewEnabled());
    }

    @Test
    public void tileRotationIsPassedToModel() {
        add5CrossRoads();
        prepareGame();
        DataToModel game = new FakeGame();
        FakeGame fakeGame = (FakeGame) game;
        windowLogic.setDataToModel(game);
        assertEquals("Tile isn't rotated", Rotation.DEG_0, fakeGame.getCurrentTileRotation());
        fakeWindow.clickOnGamePanel();
        fakeWindow.clickOnPlacedTile();
        fakeWindow.pressEndTurnButton();
        assertEquals("Tile is rotated", Rotation.DEG_90, fakeGame.getCurrentTileRotation());
    }

    @Test
    public void currentTileIsLegallyRotatedWhenPlaced() {
        game = new Game();
        table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        game.setTable(table);
        game.addPlayer("Anton" , Color.RED);
        game.addPlayer("Andrey", Color.YELLOW);
        game.getTilePile().addTile(TileName.CITY1);

        windowLogic = new GameWindowLogic();
        viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(game);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        fakeWindow = (FakeWindow) viewWindow;

        fakeWindow.clickOnGamePanel(0, -1);
        assertEquals("Tile is rotated legally when placed", Rotation.DEG_180, fakeWindow.getCurrentTileRotation());
    }

}
