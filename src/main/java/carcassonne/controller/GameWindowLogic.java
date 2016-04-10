package carcassonne.controller;

import carcassonne.model.*;
import carcassonne.model.Feature.Feature;
import carcassonne.model.tile.Coordinates;
import carcassonne.model.tile.Rotation;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;
import carcassonne.view.DrawablePlacedFollower;
import carcassonne.view.DrawableTile;
import carcassonne.view.PlacedFollower;
import carcassonne.view.ViewWindow;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

import static carcassonne.model.tile.TileDirection.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class GameWindowLogic implements WindowLogic {
    private boolean tilePreviewEnabled = true;
    private boolean currentTileOnTheTable;
    private boolean tileConfirmed;
    private boolean gameEnded = false;
    private int currentTileX, currentTileY;
    private GameData gameData;
    private ViewWindow gameWindow;
    private DataToModel dataToModel;
    private Rotation currentTileRotation = Rotation.DEG_0;
    private Set<Rotation> possibleCurrentTileRotations;
    private double[] currentFollowerLocation;
    private boolean temporaryFollowerDisplayed;
    private boolean canFollowerBePlaced;
    private final FollowerPlacingHelper followerPlacingHelper = new FollowerPlacingHelper();
    private FollowerMap currentTileFollowerMap;
    private final Set<DrawablePlacedFollower> drawablePlacedFollowers = new HashSet<>();
    private final boolean LOG = true;
    private int clickOnCurrentTile;
    private final ArrayList<String> debugTileNames = new ArrayList<>();
    private final ArrayList<String> debugTurnActions = new ArrayList<>();

    @Override
    public void setGameWindow(ViewWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void update(GameData gameData) {
        this.gameData = gameData;

        if (isGameUnfinished()) {
            tilePreviewEnabled = true;
        }

        updateUI();
        updateDetailedResultTable();
        tileConfirmed = false;
        gameWindow.setConfirmTileButtonText("Confirm tile");
        setConfirmTileButtonEnabled(false);
    }

    /**
     * This method must be invoked when an area that represents a tile possible location
     * is clicked.
     */
    @Override
    public void clickOnPossibleLocation(int x, int y) {
        /*
         * Things that are checked as part of window logic:
         * 1) if the tile is already on the table and logical coordinates are the same,
         * should not do anything. Otherwise the rotation of tile will be changed;
         */
        if (tileShouldBePlaced(x, y)) {
            updateCurrentTileCoordinates(x, y);
            currentTileOnTheTable = true;
            setConfirmTileButtonEnabled(true);
            currentTileRotation = getNewRotation(x, y);
            getCurrentTile().setRotation(currentTileRotation);
        }
        disableTilePreview();
        repaintWindow();
    }

    private void updateCurrentTileCoordinates(int x, int y) {
        currentTileX = x;
        currentTileY = y;
    }

    protected void repaintWindow() {
        gameWindow.repaintWindow();
    }

    private boolean tileShouldBePlaced(int x, int y) {
        if (clickedOnPlacedTile(x, y))
            return false;
        if (isTileConfirmed())
            return false;
        if (isGameUnfinished())
            return true;
        else
            return false;
    }

    protected boolean isGameUnfinished() {
        return !gameEnded;
    }

    private Rotation getNewRotation(int x, int y) {
        possibleCurrentTileRotations = getPossibleLocationsAndRotations().get(new Coordinates(x, y));
        if (shouldBeRotatedBeforePlaced())
            return possibleCurrentTileRotations.iterator().next();
        else
            return Rotation.DEG_0;
    }

    private boolean shouldBeRotatedBeforePlaced() {
        return !possibleCurrentTileRotations.contains(Rotation.DEG_0);
    }

    protected void setConfirmTileButtonEnabled(boolean b) {
        gameWindow.setConfirmTileButtonEnabled(b);
    }

    protected DrawableTile getCurrentTile() {
        return gameWindow.getCurrentTile();
    }

    protected Map<Coordinates, Set<Rotation>> getPossibleLocationsAndRotations() {
        return gameData.getPossibleLocationsAndRotations();
    }


    private void disableTilePreview() {
        gameWindow.setTilePreviewEnabled(false);
        tilePreviewEnabled = false;
    }

    private boolean clickedOnPlacedTile(int x, int y) {
        return isCurrentTileOnTheTable() && currentTileX == x && currentTileY == y;
    }

    @Override
    public void updateTileConfirmedButton() {
        assert (currentTileOnTheTable);
        if (tileConfirmed) {
            tileConfirmed = false;
            canFollowerBePlaced = false;
            temporaryFollowerDisplayed = false;
            gameWindow.setConfirmTileButtonText("Confirm tile");
            gameWindow.setEndTurnButtonEnabled(false);
        } else {
            tileConfirmed = true;
            canFollowerBePlaced = true;
            currentTileFollowerMap = followerPlacingHelper.getFollowerLocations(gameData.getCurrentTile());
            gameWindow.setConfirmTileButtonText("Relocate tile");
            if (gameData.getFollowers() != 0)
                gameWindow.setPossibleFollowerLocations(currentTileFollowerMap.getMultipliers());
            else
                gameWindow.setPossibleFollowerLocations(new HashSet<>());
            gameWindow.setEndTurnButtonEnabled(true);
        }
        repaintWindow();
    }

    @Override
    public void updateEndTurnButton() {
        if (LOG) {
            System.out.println("\n--------------");
            System.out.println("--------------");
            if (temporaryFollowerDisplayed)
                debugTurnActions.add("turnActions(" + currentTileX + ", " + currentTileY + ", " + clickOnCurrentTile + ", " +
                        currentFollowerLocation[0] + ", " + currentFollowerLocation[1] + ");");
            else
                debugTurnActions.add("turnActions(" + currentTileX + ", " + currentTileY +
                        ", " + clickOnCurrentTile + ");");

            debugTileNames.add(gameData.getCurrentTile().getName().toString() + ", ");

            debugTileNames.forEach(System.out::print);

            System.out.println("--");

            debugTurnActions.stream().forEach(System.out::println);


        }
        if (temporaryFollowerDisplayed) {
            drawablePlacedFollowers.add(new DrawablePlacedFollower(new Coordinates(currentTileX, currentTileY),
                    currentFollowerLocation, gameData.getPlayerColor(), currentTileRotation));
            TileDirection direction = currentTileFollowerMap.getDirection(currentFollowerLocation);
            if (direction == null)
                throw new RuntimeException("Trying to place follower on illegal position in controller");

            TileDirection rotatedDirection = direction.turnRight(currentTileRotation);
            dataToModel.turnActions(currentTileX, currentTileY, currentTileRotation, rotatedDirection);
        } else
            dataToModel.turnActions(currentTileX, currentTileY, currentTileRotation);

        gameWindow.setEndTurnButtonEnabled(false);
        currentTileOnTheTable = false;
        canFollowerBePlaced = false;
        temporaryFollowerDisplayed = false;
        currentTileRotation = Rotation.DEG_0;
        if (gameEnded)
            gameWindow.displayEndgameWindow();

        clickOnCurrentTile = 0;
    }

    /**
     * Should be invoked when click on temporarily placed tile happens.
     * <p>Parameters determine a position of a follower within temporary tile in the coordinate system where
     * top left corner of a tile is the origin.</p>
     * <p>If follower cannot be placed in the current moment, method will invoke clickOnCurrentTile()</p>
     *
     * @param xMultiplier Tile numberOfTiles relative X multiplier such that relativeX = xMultiplier * tileSize;
     * @param yMultiplier Tile numberOfTiles relative Y multiplier such that relativeY = yMultiplier * tileSize;
     */
    @Override
    public void clickOnCurrentTile(double xMultiplier, double yMultiplier) {
        if (canFollowerBePlaced()) {
            placeFollower(xMultiplier, yMultiplier);
        } else {
            clickOffCurrentTile();
        }
    }

    @Override
    public void clickOnCurrentTile() {
        if (!isCurrentTileOnTheTable()) {
            return;
        }

        if (isTileConfirmed()) {
            return;
        }

        while (true) {
            currentTileRotation = Rotation.values()[(currentTileRotation.ordinal() + 1) % 4];
            if (possibleCurrentTileRotations.contains(currentTileRotation))
                break;
        }
        getCurrentTile().setRotation(currentTileRotation);
        repaintWindow();
        clickOnCurrentTile++;
    }

    @Override
    public void clickOffCurrentTile() {
        if (temporaryFollowerDisplayed) {
            temporaryFollowerDisplayed = false;
        }
        clickOnCurrentTile = 0;
    }
    /*
     * Methods that are invoked by action listeners of GameWindow
     * END
     */

    @Override
    public void finishGame() {
        setConfirmTileButtonEnabled(false);
        gameWindow.setEndTurnButtonEnabled(false);
        gameWindow.setTilePreviewEnabled(false);
        gameEnded = true;
    }

    @Override
    public void pull() {
        dataToModel.forceNotify();
    }

    @Override
    public boolean displayPossibleLocations() {
        return isGameUnfinished();
    }

    private void placeFollower(double xM, double yM) {
        currentFollowerLocation = new double[]{xM, yM};
        temporaryFollowerDisplayed = true;
    }

    //<editor-fold desc="Getters and setters">
    @Override
    public int getCurrentTileX() {
        return currentTileX;
    }

    @Override
    public int getCurrentTileY() {
        return currentTileY;
    }

    @Override
    public boolean isCurrentTileOnTheTable() {
        return currentTileOnTheTable;
    }

    @Override
    public boolean isTileConfirmed() {
        return tileConfirmed;
    }

    @Override
    public boolean isFollowerPlaceDisplayed() {
        return tileConfirmed;
    }

    @Override
    public Color getCurrentPlayerColor() {
        return gameData.getPlayerColor();
    }

    @Override
    public boolean isTemporaryFollowerDisplayed() {
        return temporaryFollowerDisplayed;
    }

    @Override
    public boolean canFollowerBePlaced() {
        return canFollowerBePlaced;
    }

    @Override
    public double[] getCurrentFollowerLocation() {
        return currentFollowerLocation;
    }

    public void setDataToModel(DataToModel dataToModel) {
        this.dataToModel = dataToModel;
    }

    //</editor-fold>

    private void updateUI() {
        gameWindow.setCurrentPlayerName(gameData.getName());
        gameWindow.setNumberOfFollowers("" + gameData.getFollowers());
        gameWindow.setCurrentPoints(gameData.getPoints());
        gameWindow.setPlayerColorRemainder(gameData.getPlayerColor());
        gameWindow.setTilesNumber(gameData.getTilesLeft());
        gameWindow.setCurrentTile(new DrawableTile(gameData.getCurrentTile()));
        gameWindow.setTilePreviewEnabled(tilePreviewEnabled);
        gameWindow.setPossibleTileLocations(getPossibleLocationsAndRotations().keySet());
        gameWindow.addTileOnTable(new DrawableTile(gameData.getPreviouslyPlacedTile()));
        gameWindow.setDrawablePlacedFollowersSet(createDrawablePlacedFollowersSet(gameData.getPlacedFollowers()));
    }

    private void updateDetailedResultTable() {
        int rowCounter = 0;
        for (String[] values : gameData.getPlayersStats()) {
            gameWindow.setTableRowValues(rowCounter, values);
            rowCounter++;
        }
    }

    /*
     * This method removes placed followers from drawablePlacedFollowers
     * Followers are added during end turn
     */
    private Set<DrawablePlacedFollower> createDrawablePlacedFollowersSet(Set<PlacedFollower> placedFollowers) {
        Set<DrawablePlacedFollower> toRetain = new HashSet<>();
        placedFollowers.stream().forEach(placedFollower -> {
            toRetain.addAll(drawablePlacedFollowers.stream()
                    .filter(drawableFollower -> placedFollower.getCoordinates().equals(drawableFollower.getCoordinates()))
                    .collect(Collectors.toList()));
        });
        drawablePlacedFollowers.retainAll(toRetain);
        return new HashSet<>(drawablePlacedFollowers);
    }

    /*
     * Methods that are invoked by action listeners of GameWindow
     * BEGINNING
     */

    /**
     * An instance of this class has a method that returns all legal locations
     * of possible follower placement on the current tile. Legal is:
     * 1) 1 per Feature
     * 2) exclude occupied real estate
     */
    class FollowerPlacingHelper {
        private OwnershipChecker ownershipChecker;

        private OwnershipChecker getOwnershipChecker() {
            if (ownershipChecker == null)
                ownershipChecker = dataToModel.getOwnershipChecker();
            return ownershipChecker;
        }

        FollowerMap getFollowerLocations(Tile tile) {
            FollowerMap result = new FollowerMap();
            Set<Feature> features = tile.getFeatures();
            for (Feature feature : features) {
                TileDirection direction = tile.getFeatureTileDirections(feature)
                        .iterator().next();
                if (getOwnershipChecker().locationIsLegal(currentTileX, currentTileY, currentTileRotation, direction))
                    result.put(getTileSizeRelativeMultipliers(tile, feature), direction);
            }
            return result;
        }

        private double[] getTileSizeRelativeMultipliers(Tile tile, Feature feature) {
            double[] xyMultipliers = new double[2];
            /*
             * get 1 direction pre Feature
             */
            LinkedHashSet<TileDirection> directions = tile.getFeatureTileDirections(feature);

            if (feature.isCity()) {
                directions.removeAll(Arrays.asList(NNE, NNW, EES, EEN, WWS, WWN, SSE, SSW));
            } else if (feature.isLand()) {
                if (directions.size() > 3) {
                    directions.removeAll(Arrays.asList(NNE, NNW, EES, EEN, WWS, WWN, SSE, SSW));
                }
            }

            TileDirection direction = directions.iterator().next();

            /*
             * Basic rule
             */
            switch (direction) {
                case NNE:
                    xyMultipliers[0] = 0.85;
                    xyMultipliers[1] = 0.15;
                    break;
                case NNW:
                    xyMultipliers[0] = 0.15;
                    xyMultipliers[1] = 0.15;
                    break;
                case NORTH:
                    xyMultipliers[0] = 0.5;
                    xyMultipliers[1] = 0.15;
                    break;
                case SOUTH:
                    xyMultipliers[0] = 0.5;
                    xyMultipliers[1] = 0.85;
                    break;
                case SSE:
                    xyMultipliers[0] = 0.85;
                    xyMultipliers[1] = 0.85;
                    break;
                case SSW:
                    xyMultipliers[0] = 0.15;
                    xyMultipliers[1] = 0.85;
                    break;
                case WEST:
                    xyMultipliers[0] = 0.15;
                    xyMultipliers[1] = 0.5;
                    break;
                case WWN:
                    xyMultipliers[0] = 0.15;
                    xyMultipliers[1] = 0.15;
                    break;
                case WWS:
                    xyMultipliers[0] = 0.15;
                    xyMultipliers[1] = 0.85;
                    break;
                case EAST:
                    xyMultipliers[0] = 0.85;
                    xyMultipliers[1] = 0.5;
                    break;
                case EEN:
                    xyMultipliers[0] = 0.85;
                    xyMultipliers[1] = 0.15;
                    break;
                case EES:
                    xyMultipliers[0] = 0.85;
                    xyMultipliers[1] = 0.85;
                    break;
                case CENTER:
                    xyMultipliers[0] = 0.5;
                    xyMultipliers[1] = 0.5;
                    break;

            }

            /*
             * Tile specific rules
             */
            switch (tile.getName()) {
                case CITY1:
                    if (feature.isLand()) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.6;
                    }
                case CITY1RSE:
                case CITY1RSW:
                case CITY1RWE:
                case CITY1RSWE:
                    if (feature.isRoad() && directions.size() == 2) {
                        if (directions.contains(TileDirection.WEST) && directions.contains(TileDirection.EAST)) {
                            xyMultipliers[0] = 0.5;
                            xyMultipliers[1] = 0.5;
                        } else if (directions.contains(TileDirection.WEST)) {
                            xyMultipliers[0] = 0.4;
                            xyMultipliers[1] = 0.6;
                        } else {
                            xyMultipliers[0] = 0.6;
                            xyMultipliers[1] = 0.6;
                        }
                    } else if ((feature.isRoad() && directions.size() == 1)) {
                        if (directions.contains(TileDirection.WEST)) {
                            xyMultipliers[0] = 0.3;
                            xyMultipliers[1] = 0.6;
                        } else if (directions.contains(TileDirection.EAST)) {
                            xyMultipliers[0] = 0.8;
                            xyMultipliers[1] = 0.6;
                        }

                    } else if (feature.isLand() && directions.contains(TileDirection.WWN)) {
                        xyMultipliers[0] = 0.15;
                        xyMultipliers[1] = 0.35;
                    }
                    break;
                case CITY11WE:
                    if (feature.isLand()) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.15;
                    }
                    break;
                case CITY2WE:
                case CITY2WES:
                    if (feature.isCity()) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.45;
                    } else if (directions.contains(TileDirection.NORTH)) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.0;
                    } else {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.9;
                    }
                    break;
                case CITY2NW:
                case CITY2NWS:
                    if (feature.isLand()) {
                        xyMultipliers[0] = 0.65;
                        xyMultipliers[1] = 0.65;
                    }
                case CITY2NWR:
                case CITY2NWSR:
                    if (feature.isCity()) {
                        xyMultipliers[0] = 0.25;
                        xyMultipliers[1] = 0.25;
                    } else if (feature.isRoad()) {
                        xyMultipliers[0] = 0.85;
                        xyMultipliers[1] = 0.55;
                    } else if (directions.contains(SSW)) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.65;
                    }
                    break;
                case CITY3:
                case CITY3R:
                case CITY3S:
                case CITY3SR:
                    if (feature.isCity()) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.4;
                    }
                    if (feature.isLand()) {
                        if (directions.size() == 3) {
                            xyMultipliers[0] = 0.6;
                            xyMultipliers[1] = 0.85;
                        } else if (directions.contains(TileDirection.SSW)) {
                            xyMultipliers[0] = 0.25;
                            xyMultipliers[1] = 0.95;
                        } else {
                            xyMultipliers[0] = 0.75;
                            xyMultipliers[1] = 0.85;
                        }
                    }
                    break;

                case CITY4:
                    xyMultipliers[0] = 0.5;
                    xyMultipliers[1] = 0.5;
                    break;

                case ROAD2NS:
                    if (feature.isRoad()) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.15;
                    }
                    break;

                case ROAD2SW:
                    if (feature.isRoad()) {
                        xyMultipliers[0] = 0.4;
                        xyMultipliers[1] = 0.4;
                    } else if (feature.isLand() && directions.contains(TileDirection.SSW)) {
                        xyMultipliers[0] = 0.15;
                        xyMultipliers[1] = 0.85;
                    } else {
                        xyMultipliers[0] = 0.85;
                        xyMultipliers[1] = 0.15;
                    }
                    break;

                case CLOISTER:
                    if (feature.isLand()) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.15;
                    }
                    break;
                case CLOISTERR:
                    if (feature.isLand()) {
                        xyMultipliers[0] = 0.85;
                        xyMultipliers[1] = 0.15;
                    }
            }
            return xyMultipliers;
        }
    }
}

