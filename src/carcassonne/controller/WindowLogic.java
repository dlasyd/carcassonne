package carcassonne.controller;


import carcassonne.model.Rotation;
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

    void clickOnCurrentTile();

    boolean isFollowerPlaceDisplayed();

    Color getCurrentPlayerColor();

    void placeFollower(double xM, double yM);

    boolean isTemporaryFollowerDisplayed();

    boolean canFollowerBePlaced();

    double[] getCurrentFollowerLocation();

    void clickOffCurrentTile();
}
