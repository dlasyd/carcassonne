package carcassonne.controller;


import carcassonne.view.ViewWindow;

import java.awt.*;

/**
 * Created by Andrey on 01/12/15.
 */
public interface WindowLogic {
    void setGameWindow(ViewWindow gameWindow);

    void update(GameData gameData);

    GameData getLatestGameData();

    void clickOnPossibleLocation(int x, int y);

    void updateTileConfirmedButton();

    void setDataToModel(DataToModel dataToModel);

    void pull();

    void updateEndTurnButton();

    void finishGame();

    boolean displayPossibleLocations();

    int getCurrentTileX();

    int getCurrentTileY();

    boolean isCurrentTileOnTheTable();

    boolean isTileConfirmed();

    /**
     * Should be invoked when click on temporarily placed tile happens.
     *<p>Parameters determine a position within temporary tile in the coordinate system where
     * top left corner of a tile is the origin.</p>
     * @param xMultiplier Tile size relative X multiplier such that relativeX = xMultiplier * tileSize;
     * @param yMultiplier Tile size relative Y multiplier such that relativeY = yMultiplier * tileSize;
     */
    void clickOnCurrentTile(double xMultiplier, double yMultiplier);

    boolean isFollowerPlaceDisplayed();

    Color getCurrentPlayerColor();

    boolean isTemporaryFollowerDisplayed();

    boolean canFollowerBePlaced();

    double[] getCurrentFollowerLocation();

    void clickOffCurrentTile();
}
