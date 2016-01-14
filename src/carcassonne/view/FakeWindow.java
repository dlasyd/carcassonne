package carcassonne.view;

import carcassonne.controller.WindowLogic;

import java.awt.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 14/01/16.
 */
public class FakeWindow implements ViewWindow {
    String currentPlayerName = "";
    private WindowLogic windowLogic;

    public FakeWindow(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }

    @Override
    public void setConfirmTileButtonEnabled(boolean b) {

    }

    @Override
    public void setConfirmTileButtonText(String text) {

    }

    @Override
    public void setPlayerColorRemainder(Color color) {

    }

    @Override
    public void setTilesNumber(String tilesNumber) {

    }

    @Override
    public void setCurrentPlayerName(String currentPlayer) {
        this.currentPlayerName = currentPlayer;
    }

    @Override
    public void setEndTurnEnabled(boolean b) {

    }

    @Override
    public void setNumberOfFollwers(String numberOfFollwers) {

    }

    @Override
    public void setCurrentPoints(String currentPoints) {

    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void pressEndTurnButton() {
        windowLogic.updateEndTurnButton();
    }
}
