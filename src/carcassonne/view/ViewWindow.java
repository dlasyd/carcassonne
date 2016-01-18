package carcassonne.view;

import java.awt.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 14/01/16.
 */
public interface ViewWindow {
    void setConfirmTileButtonEnabled(boolean b);

    void setConfirmTileButtonText(String text);

    void setPlayerColorRemainder(Color color);

    void setTilesNumber(String tilesNumber);

    void setCurrentPlayerName(String currentPlayerName);

    void setEndTurnEnabled(boolean b);

    void setNumberOfFollwers(String numberOfFollwers);

    void setCurrentPoints(String currentPoints);

    void displayEndgameWindow();

    void setTilePreviewEnabled(boolean b);

    void repaintWindow();
}
