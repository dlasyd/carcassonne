package carcassonne.model;

import carcassonne.controller.GameWindowLogic;
import carcassonne.controller.WindowLogic;
import carcassonne.view.DrawablePlacedFollower;
import carcassonne.view.FakeWindow;
import carcassonne.view.ViewWindow;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests data flow from model to controller and then to view
 * and all controller behaviour
 * Created by Andrey on 14/01/16.
 */
public class ModelToControllerTest {
    Game    game;
    Table   table;
    WindowLogic windowLogic;
    ViewWindow  viewWindow;
    FakeWindow  fakeWindow;
    FakeGame    fakeGame;


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

    public void setUpFakeGame() {
        fakeGame = new FakeGame();

        table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        fakeGame.setTable(table);
        fakeGame.addPlayer("Anton" , Color.RED);
        fakeGame.addPlayer("Andrey", Color.YELLOW);
    }

    public void prepareFakeGame() {
        windowLogic = new GameWindowLogic();
        viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(fakeGame);
        fakeGame.setWindowLogic(windowLogic);
        fakeGame.nextPlayer();
        fakeGame.dragTile();
        fakeGame.notifyController();
        fakeWindow = (FakeWindow) viewWindow;
    }

    public void add5DifferentTiles() {
        game.getTilePile().add5DifferentTiles();
    }

    public void add5CrossRoads() {
        game.getTilePile().addXCrossroads(5);
    }

    public void turnActions(int x, int y, double mx, double my) {
        fakeWindow.clickOnGamePanel(x, y);
        fakeWindow.pressConfirmTileButton();
        fakeWindow.clickOnCurrentTile(mx, my);
        fakeWindow.pressEndTurnButton();
    }

    public void turnActions(int x, int y) {
        fakeWindow.clickOnGamePanel(x, y);
        fakeWindow.pressConfirmTileButton();
        fakeWindow.pressEndTurnButton();
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
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressConfirmTileButton();
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

        for (int i = 0; i < 4; i ++) {
            fakeWindow.clickOnGamePanel();
            fakeWindow.pressConfirmTileButton();
            fakeWindow.pressEndTurnButton();
        }

        assertEquals("End game window not displayed", false, fakeWindow.isEndGameWindowDisplayed());
        fakeWindow.clickOnGamePanel();
        fakeWindow.pressConfirmTileButton();
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
        for (int i = 0; i < 5; i ++) {
            fakeWindow.clickOnGamePanel();
            fakeWindow.pressConfirmTileButton();
            fakeWindow.pressEndTurnButton();
        }

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

        for (int i = 0; i < 5; i ++) {
            fakeWindow.clickOnGamePanel();
            fakeWindow.pressConfirmTileButton();
            fakeWindow.pressEndTurnButton();
        }

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

        for (int i = 0; i < 5; i ++) {
            fakeWindow.clickOnGamePanel();
            fakeWindow.pressConfirmTileButton();
            fakeWindow.pressEndTurnButton();
        }
        assertEquals("Tile preview is disabled", false, fakeWindow.isTilePreviewEnabled());
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

    @Test
    public void whenClickOnTileOnlyLegalRotation() {
        game.getTilePile().addTile(TileName.CITY1);
        prepareGame();
        fakeWindow.clickOnGamePanel(0, 1);
        fakeWindow.clickOnPlacedTile();
        assertEquals("Current tile rotation is correct", Rotation.DEG_180, fakeWindow.getCurrentTileRotation());
        fakeWindow.clickOnPlacedTile();
        assertEquals("Current tile rotation is correct", Rotation.DEG_270, fakeWindow.getCurrentTileRotation());
        fakeWindow.clickOnPlacedTile();
        assertEquals("Incorrect rotation is skipped", Rotation.DEG_90, fakeWindow.getCurrentTileRotation());
    }

    @Test
    public void placesForFollowersAreDisplayedWhenNecessary() {
        game.getTilePile().addTile(TileName.ROAD3);
        prepareGame();
        assertEquals("Spaces for followers are displayed", false, fakeWindow.areFollowersLocationsDisplayed());
        fakeWindow.clickOnGamePanel(0, 1);
        assertEquals("Spaces for followers are displayed", false, fakeWindow.areFollowersLocationsDisplayed());
        fakeWindow.pressConfirmTileButton();
        assertEquals("Spaces for followers are displayed", true, fakeWindow.areFollowersLocationsDisplayed());
        fakeWindow.pressEndTurnButton();
        assertEquals("Spaces for followers are displayed", false, fakeWindow.areFollowersLocationsDisplayed());
    }

    @Test
    public void windowReceivesFollowerLocationData() {
        game.getTilePile().addTile(TileName.CITY1);
        prepareGame();
        fakeWindow.clickOnGamePanel(0, -1);
        fakeWindow.pressConfirmTileButton();
        assertEquals("Window has follower locations coordinates", 2, fakeWindow.getFollowerLocations().size());
    }

    @Test
    public void followerCantBePlacedBeforeTileIsConfirmed() {
        game.getTilePile().addTile(TileName.CITY1RWE);
        prepareGame();
        fakeWindow.clickOnGamePanel(1, 0);
        assertEquals("Follower cannot be placed", false, fakeWindow.canFollowerBePlaced());
    }

    @Test
    public void followerCanBePlacedAfterTileIsConfirmed() {
        game.getTilePile().addTile(TileName.CITY1RWE);
        prepareGame();
        fakeWindow.clickOnGamePanel(1, 0);
        fakeWindow.pressConfirmTileButton();
        assertEquals("Follower can be placed", true, fakeWindow.canFollowerBePlaced());
    }

    @Test
    public void followerCantBePlacedAfterUnconfirmed() {
        game.getTilePile().addTile(TileName.CITY1RWE);
        prepareGame();
        fakeWindow.clickOnGamePanel(1, 0);
        fakeWindow.pressConfirmTileButton();
        fakeWindow.pressConfirmTileButton();
        assertEquals("Follower cannot be placed", false, fakeWindow.canFollowerBePlaced());
    }

    @Test
    public void followerCantBePlacedAfterEndTurnButton() {
        game.getTilePile().addTile(TileName.CITY1RWE);
        prepareGame();
        fakeWindow.clickOnGamePanel(1, 0);
        fakeWindow.pressConfirmTileButton();
        fakeWindow.pressEndTurnButton();
        assertEquals("Follower cannot be placed", false, fakeWindow.canFollowerBePlaced());
    }

    @Test
    public void temporaryFollowerIsPlaced() {
        game.getTilePile().addTile(TileName.CITY1RWE);
        prepareGame();
        fakeWindow.clickOnGamePanel(1, 0);
        fakeWindow.pressConfirmTileButton();
        fakeWindow.clickOnCurrentTile(0.5, 0.5);
        assertEquals("Temporary follower is placed", true, fakeWindow.isTemporaryFollowerPlaced());
    }

    //TODO refactor
    @Test
    public void followerCanBePlacedOnOneOfCorrectPositions() {
        Set<double[]> expected = new HashSet<>();
        expected.add(new double[] {0.5, 0.6});
        expected.add(new double[] {0.5, 0.15});

        game.getTilePile().addTile(TileName.CITY1);
        prepareGame();
        fakeWindow.clickOnGamePanel(0, -1);
        fakeWindow.pressConfirmTileButton();
        boolean result = false;
        for (double[] i: fakeWindow.getPossibleFollowerLocationsSet()) {
            for (double[] j: expected) {
                if (Arrays.equals(i, j)) {
                    result = true;
                    break;
                }
            }
        }
        assertEquals(true, result);
    }

    @Test
    public void temporaryFollowerLocationIsSavedInController() {
        /*
         * expected location differed from previous test because tile will be turn upside down automatically
         */
        double[] expected = new double[]{0.5, 0.85};
        game.getTilePile().addTile(TileName.CITY1);
        prepareGame();
        fakeWindow.clickOnGamePanel(0, -1);
        fakeWindow.pressConfirmTileButton();
        fakeWindow.clickOnCurrentTile(0.5, 0.85);
        assertTrue("Window should get correct temporary follower location", Arrays.equals(expected, fakeWindow.getCurrentFollowerLocation()));
    }

    @Test
    public void clickAwayFromCurrentTileRemovesTemporaryFollower() {
        game.getTilePile().addTile(TileName.CITY1RWE);
        prepareGame();
        fakeWindow.clickOnGamePanel(1, 0);
        fakeWindow.pressConfirmTileButton();
        fakeWindow.clickOnCurrentTile(0.5, 0.5);
        assertEquals("Temporary follower should be placed", true, fakeWindow.isTemporaryFollowerPlaced());
        fakeWindow.clickOffCurrentTile();
        assertEquals("Temporary follower should be removed", false, fakeWindow.isTemporaryFollowerPlaced());
    }

    @Test
    public void submittingFollowerModelHasTileDirection() {
        setUpFakeGame();
        fakeGame.getTilePile().addTile(TileName.CITY1);
        prepareFakeGame();
        turnActions(0, -1, 0.5, 0.15);
        assertTrue("Follower placed to right position", Arrays.asList(TileDirections.SOUTH, TileDirections.SSE,
                TileDirections.SSW).contains(fakeGame.getLastFollowerTileDirections()));
    }

    @Test
    public void followerNotSubmittedThenModelGetsNoTileDirection() {
        setUpFakeGame();
        fakeGame.getTilePile().addTile(TileName.CITY1);
        prepareFakeGame();
        turnActions(0, -1);
        assertEquals("Follower placed to right position", null, fakeGame.getLastFollowerTileDirections());
    }

    @Test
    public void placedFollowersAreDisplayed() {
        Set<DrawablePlacedFollower> expected = new HashSet();
        expected.add(new DrawablePlacedFollower(new Coordinates(0, -1), new double[] {0.5, 0.5}, Color.RED, Rotation.DEG_0));
        game.getTilePile().addTile(TileName.CITY4, TileName.CITY4);
        prepareGame();
        turnActions(0, -1, 0.5, 0.5);
        assertEquals("Placed follower is displayed on correct tile and position", expected, fakeWindow.getPlacedFollowers());
    }

    @Test
    public void smallFinishedCastleGives4Points() {
        game.getTilePile().addTile(TileName.CITY1, TileName.CITY1, TileName.CITY1);
        prepareGame();
        turnActions(0, -1, 0.5, 0.15);
        turnActions(0, 1);
        assertEquals("Follower placed to right position", "4", fakeWindow.getCurrentPoints());
    }

    @Test
    public void smallFinishedCastleFollowerNotOnTable() {
        Set<DrawablePlacedFollower> expected = new HashSet();
        game.getTilePile().addTile(TileName.CITY1, TileName.CITY1);
        prepareGame();
        turnActions(0, -1, 0.5, 0.15);
        assertEquals("Placed follower is displayed on correct tile and position", expected, fakeWindow.getPlacedFollowers());
    }

    @Test
    public void notDisplayingPossibleFollowerLocationIfRealEstateOccupied() {
        Set<Double> expected = new HashSet();
        expected.add(0.6);
        expected.add(0.85);
        game.getTilePile().addTile(TileName.CITY3, TileName.CITY3, TileName.CITY3);
        prepareGame();


        turnActions(0, -1, 0.5, 0.4);
        Set<Double> result = new HashSet<>();
        fakeWindow.clickOnGamePanel(0, -2);
        fakeWindow.pressConfirmTileButton();
        for (double[] i: fakeWindow.getPossibleFollowerLocationsSet()) {
            result.add(i[0]);
            result.add(i[1]);
        }
        assertEquals("Correct follower possible locations", expected, result);
    }

    @Test
    /*
     * Each turn number of points and followers should be updated for all players
     */
    public void importantInformationIsDisplayedForAllPlayers() {
        ArrayList<Map<Integer, String>> expected = new ArrayList<>();
        expected.add(new HashMap());
        expected.add(new HashMap());
        expected.get(0).put(0, "Anton");
        expected.get(0).put(1, "6");
        expected.get(0).put(2, "4");
        expected.get(1).put(0, "Andrey");
        expected.get(1).put(1, "7");
        expected.get(1).put(2, "4");

        game.getTilePile().addTile(TileName.ROAD4, TileName.ROAD4, TileName.ROAD4, TileName.ROAD4,
                TileName.ROAD4, TileName.ROAD4);
        prepareGame();

        turnActions(1, 0, 0.15, 0.5);
        turnActions(2, 0, 0.15, 0.5);
        turnActions(3, 0, 0.15, 0.5);
        turnActions(4, 0, 0.15, 0.5);
        turnActions(5, 0, 0.15, 0.5);

        assertEquals("Correct information is displayed in table", expected, fakeWindow.getCurrentTableData());
    }

    @Test
    public void tableHasDataInTheBeginning() {
        ArrayList<Map<Integer, String>> expected = new ArrayList<>();
        expected.add(new HashMap());
        expected.add(new HashMap());
        expected.get(0).put(0, "Anton");
        expected.get(0).put(1, "7");
        expected.get(0).put(2, "0");
        expected.get(1).put(0, "Andrey");
        expected.get(1).put(1, "7");
        expected.get(1).put(2, "0");

        game.getTilePile().addTile(TileName.ROAD4, TileName.ROAD4, TileName.ROAD4, TileName.ROAD4,
                TileName.ROAD4, TileName.ROAD4);
        prepareGame();

        assertEquals("Correct information is displayed in table", expected, fakeWindow.getCurrentTableData());
    }
}
