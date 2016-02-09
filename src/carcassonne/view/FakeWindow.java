package carcassonne.view;

import carcassonne.controller.WindowLogic;
import carcassonne.model.Coordinates;
import carcassonne.model.Rotation;
import carcassonne.model.TileName;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class simulates a game window. It is used for testing controller.
 * Data is saved into instance variables via ViewWindow implemented methods.
 * Data can be retrieved by FakeWindow getters
 */
public class FakeWindow implements ViewWindow {
    String currentPlayerName = "";
    int moveCounter = 0;
    private WindowLogic windowLogic;
    private String currentPoints;
    private String numberOfFollowers;
    private String tilesLeft;
    private int[][] fakeCoordinates = {{1,0}, {2,0}, {3, 0}, {4, 0}, {5, 0}};
    private boolean endTurnEnabled, nextTurnEnabled;
    private boolean endGameWindowDisplayed;
    private boolean tilePreviewEnabled;
    private DrawableTile currentTile;
    private Set<DrawableTile> tilesOnTable = new HashSet<>();
    private TileName firstPlacedTileName;
    private Set<Coordinates> possibleTileLocations;
    private Set<double[]> followerLocations;
    private double[] temporaryFollowerLocation;
    private Set<DrawablePlacedFollower> placedFollowers = new HashSet<>();

    public FakeWindow(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }

    /*
     * Simulating window events
     * BEGINNING
     */
    public void pressEndTurnButton() {
        //windowLogic.clickOnPossibleLocation(fakeCoordinates[moveCounter][0], fakeCoordinates[moveCounter][1]);
        moveCounter++;
        windowLogic.updateEndTurnButton();
    }

    public void clickOnCurrentTile(double xM, double yM) {
        windowLogic.clickOnCurrentTile(xM, yM);
    }

    public void pressConfirmTileButton() {
        windowLogic.updateTileConfirmedButton();
    }

    public void clickOnGamePanel() {
        clickOnGamePanel(moveCounter + 1, 0);
    }

    public void clickOnGamePanel(int x, int y) {
        windowLogic.clickOnPossibleLocation(x , y);
    }

    public void clickOnPlacedTile() {
        windowLogic.clickOnCurrentTile();
    }

    public void clickOffCurrentTile() {
        windowLogic.clickOffCurrentTile();
    }
    /*
     * Simulating window events
     * END
     */

    @Override
    public DrawableTile getCurrentTile() {
        return currentTile;
    }

    //<editor-fold desc="Overridden setters">
    @Override
    public void setPossibleFollowerLocations(Set<double[]> followerLocations) {
        this.followerLocations = followerLocations;
    }


    @Override
    public void setDrawablePlacedFollowersSet(Set<DrawablePlacedFollower> drawablePlacedFollowers) {
        placedFollowers = drawablePlacedFollowers;
    }

    @Override
    public void setConfirmTileButtonEnabled(boolean b) {
        this.nextTurnEnabled = b;
    }

    @Override
    public void setCurrentPoints(String currentPoints) {
        this.currentPoints = currentPoints;
    }

    @Override
    public void addTileOnTable(DrawableTile tile) {
        tilesOnTable.add(tile);
        firstPlacedTileName = tile.getTileName();
    }

    @Override
    public void setTableValue(String value, int col, int row) {

    }

    @Override
    public void setNumberOfFollowers(String numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    @Override
    public void setPossibleTileLocations(Set<Coordinates> possibleTileLocations) {
        this.possibleTileLocations = possibleTileLocations;
    }

    @Override
    public void setCurrentTile(DrawableTile drawableTile) {
        this.currentTile = drawableTile;
    }

    @Override
    public void setTilesNumber(String tilesNumber) {
        this.tilesLeft = tilesNumber;
    }

    @Override
    public void setEndTurnButtonEnabled(boolean b) {
        this.endTurnEnabled = b;
    }

    @Override
    public void displayEndgameWindow() {
        this.endGameWindowDisplayed = true;
    }

    @Override
    public void setCurrentPlayerName(String currentPlayer) {
        this.currentPlayerName = currentPlayer;
    }

    @Override
    public void setTilePreviewEnabled(boolean b) {
        this.tilePreviewEnabled = b;
    }
    //</editor-fold>

    //<editor-fold desc="Empty implementations">
    @Override
    public void setConfirmTileButtonText(String text) {
    }

    @Override
    public void setPlayerColorRemainder(Color color) {
    }

    @Override
    public void repaintWindow() {
    }
    //</editor-fold>

    //<editor-fold desc="FakeWindow Getters">
    public boolean isPlaceTileButtonEnabled() {
        return this.nextTurnEnabled;
    }

    public boolean isPlaceEndTurnButtonEnabled() {
        return this.endTurnEnabled;
    }

    public boolean isEndGameWindowDisplayed() {
        return this.endGameWindowDisplayed;
    }

    public boolean isPlacedTileSetEmpty() {
        return tilesOnTable.isEmpty();
    }

    public boolean isTemporaryFollowerPlaced() {
        return windowLogic.isTemporaryFollowerDisplayed();
    }

    public boolean isCurrentTilePlaced() {
        return windowLogic.isCurrentTileOnTheTable();
    }

    public boolean isTilePreviewEnabled() {
        return tilePreviewEnabled;
    }

    public boolean canTileBeRelocated() {
        return !windowLogic.isTileConfirmed();
    }

    public boolean areFollowersLocationsDisplayed() {
        return windowLogic.isFollowerPlaceDisplayed();
    }

    public boolean canFollowerBePlaced() {
        return windowLogic.canFollowerBePlaced();
    }

    public String getTilePreviewName() {
        return currentTile.getFileName();
    }

    public TileName getFirstPlacedTileName() {
        return firstPlacedTileName;
    }

    public Rotation getCurrentTileRotation() {
        return currentTile.getCurrentRotation();
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public String getCurrentPoints() {
        return currentPoints;
    }

    public String getNumberOfFollwers() {
        return numberOfFollowers;
    }

    public String getTilesLeft() {
        return tilesLeft;
    }

    public Set<Coordinates> getPossibleTileLocations() {
        return possibleTileLocations;
    }

    public Set<DrawableTile> getTilesOnTable() {
        return tilesOnTable;
    }

    public Set<double[]> getFollowerLocations() {
        return followerLocations;
    }

    public Set<double[]> getPossibleFollowerLocationsSet() {
        return followerLocations;
    }

    public double[] getCurrentFollowerLocation() {
        return windowLogic.getCurrentFollowerLocation();
    }

    public Set getPlacedFollowers() {
        return placedFollowers;
    }

    //</editor-fold>




}
