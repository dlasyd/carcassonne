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

    public FakeWindow(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }

    /*
     * Simulating window events
     */
    public void pressEndTurnButton() {
        windowLogic.updateTilePlaced(fakeCoordinates[moveCounter][0], fakeCoordinates[moveCounter][1]);
        windowLogic.updateEndTurnButton();
        moveCounter++;
    }

    public void clickOnGamePanel() {
        clickOnGamePanel(1, 0);
    }

    public void clickOnGamePanel(int x, int y) {
        windowLogic.updateTilePlaced(x , y);
    }

    public void clickOnPlacedTile() {
        windowLogic.clickOnPlacedTile();
    }

    @Override
    public DrawableTile getCurrentTile() {
        return currentTile;
    }

    //<editor-fold desc="Overridden setters">
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

    public String getTilePreviewName() {
        return currentTile.getFileName();
    }

    public boolean isPlacedTileSetEmpty() {
        return tilesOnTable.isEmpty();
    }

    public TileName getFirstPlacedTileName() {
        return firstPlacedTileName;
    }

    public boolean isCurrentTilePlaced() {
        return windowLogic.isCurrentTileOnTheTable();
    }

    public boolean canTileBeRelocated() {
        return !windowLogic.isTileConfirmed();
    }

    public void pressConfirmTileButton() {
        windowLogic.updateTileConfirmed();
    }

    public Rotation getCurrentTileRotation() {
        return currentTile.getCurrentRotation();
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public boolean isTilePreviewEnabled() {
        return tilePreviewEnabled;
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

    //</editor-fold>

    public void setTilePreviewEnabled(boolean b) {
        this.tilePreviewEnabled = b;
    }

    public Set<Coordinates> getPossibleTileLocations() {
        return possibleTileLocations;
    }

    public Set<DrawableTile> getTilesOnTable() {
        return tilesOnTable;
    }


}
