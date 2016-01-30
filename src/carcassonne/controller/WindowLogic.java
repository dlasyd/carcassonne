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

    void updateTilePlaced(int x, int y);

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

    void clickOnCurrentTile(double xMultiplier, double yMultiplier);

    boolean isFollowerPlaceDisplayed();

    Color getCurrentPlayerColor();

    boolean isTemporaryFollowerDisplayed();

    boolean canFollowerBePlaced();

    double[] getCurrentFollowerLocation();

    void clickOffCurrentTile();
}
