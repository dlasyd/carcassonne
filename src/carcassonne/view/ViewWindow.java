package carcassonne.view;

import carcassonne.model.Coordinates;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

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

    void setEndTurnButtonEnabled(boolean b);

    void setNumberOfFollowers(String numberOfFollowers);

    void setCurrentPoints(String currentPoints);

    void addTileOnTable(DrawableTile tile);

    void displayEndgameWindow();

    void setTilePreviewEnabled(boolean b);

    void repaintWindow();

    void setPossibleTileLocations(Set<Coordinates> possibleTileLocations);

    void setCurrentTile(DrawableTile drawableTile);

    DrawableTile getCurrentTile();

    void setPossibleFollowerLocations(Set<double[]> followerLocations);

    void setCurrentFollowerLocation(double[] temporaryFollowerLocation );

    void setDrawablePlacedFollowersSet(Set<DrawablePlacedFollower> drawablePlacedFollowers);
}
