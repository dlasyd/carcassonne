package carcassonne.view;

import carcassonne.controller.WindowLogic;
import carcassonne.model.Coordinates;
import carcassonne.model.TileName;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 14/01/16.
 */
public class FakeWindow implements ViewWindow {
    String currentPlayerName = "";
    int moveCounter = 0;
    private WindowLogic windowLogic;
    private String currentPoints;
    private String numberOfFollwers;
    private String tilesLeft;
    private int[][] fakeCoordinates = {{1,0}, {2,0}, {3, 0}, {4, 0}, {5, 0}};
    private boolean endTurnEnabled, nextTurnEnabled;
    private boolean endGameWindowDisplayed;
    private boolean tilePreviewEnabled;
    private String currentTileFileName;

    public Set<DrawableTile> getTilesOnTable() {
        return tilesOnTable;
    }

    private Set<DrawableTile> tilesOnTable = new HashSet<>();
    private TileName firstPlacedTileName;
    private Set<Coordinates> possibleTileLocations;

    public FakeWindow(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }

    @Override
    public void setConfirmTileButtonEnabled(boolean b) {
        this.nextTurnEnabled = b;
    }

    @Override
    public void setConfirmTileButtonText(String text) {
    }

    @Override
    public void setPlayerColorRemainder(Color color) {
    }

    @Override
    public void setTilesNumber(String tilesNumber) {
        this.tilesLeft = tilesNumber;
    }

    @Override
    public void setEndTurnEnabled(boolean b) {
        this.endTurnEnabled = b;
    }

    @Override
    public void displayEndgameWindow() {
        this.endGameWindowDisplayed = true;
    }

    @Override
    public void repaintWindow() {

    }

    @Override
    public void setCurrentTileFileName(String currentTileFileName) {
        this.currentTileFileName = currentTileFileName;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    @Override
    public void setCurrentPlayerName(String currentPlayer) {
        this.currentPlayerName = currentPlayer;
    }

    public void pressEndTurnButton() {
        windowLogic.updateTilePlaced(fakeCoordinates[moveCounter][0], fakeCoordinates[moveCounter][1]);
        windowLogic.updateEndTurnButton();
        moveCounter++;
    }

    public String getCurrentPoints() {
        return currentPoints;
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

    public String getNumberOfFollwers() {
        return numberOfFollwers;
    }

    @Override
    public void setNumberOfFollowers(String numberOfFollowers) {
        this.numberOfFollwers = numberOfFollowers;
    }

    @Override
    public void setPossibleTileLocations(Set<Coordinates> possibleTileLocations) {
        this.possibleTileLocations = possibleTileLocations;
    }

    public String getTilesLeft() {
        return tilesLeft;
    }

    public boolean isPlaceTileButtonEnabled() {
        return this.nextTurnEnabled;
    }

    public boolean isPlaceEndTurnButtonEnabled() {
        return this.endTurnEnabled;
    }

    public boolean isEndGameWindowDisplayed() {
        return this.endGameWindowDisplayed;
    }

    public boolean isTilePreviewEnabled() {
        return tilePreviewEnabled;
    }

    public void setTilePreviewEnabled(boolean b) {
        this.tilePreviewEnabled = b;
    }

    public void clickOnGamePanel() {
        windowLogic.updateTilePlaced(1, 0);
    }

    public String getTilePreviewName() {
        return currentTileFileName;
    }

    public boolean isPlacedTileSetEmpty() {
        return tilesOnTable.isEmpty();
    }

    public TileName getFirstPlacedTileName() {
        return firstPlacedTileName;
    }

    public Set<Coordinates> getDisplayedPossibleTileCoordinatesSet() {
        return new HashSet<>();
    }

    public Set<Coordinates> getPossibleTileLocations() {
        return possibleTileLocations;
    }

}
