package carcassonne.model;

import carcassonne.controller.DataToModel;
import carcassonne.controller.GameDataBuilder;
import carcassonne.controller.WindowLogic;
import carcassonne.view.DrawableTile;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.*;

import static carcassonne.model.TileDirections.*;

public class Game implements DataToModel{
    private static Game game;
    private Table               table;
    private WindowLogic         windowLogic;
    private TilePile            tilePile = new TilePile();
    private ArrayList<Player>   players = new ArrayList<>();
    private Player              currentPlayer;
    private FollowerPlacingHelper followerPlacingHelper = new FollowerPlacingHelper();
    private boolean             finished;
    private boolean             currentTileConfirmed;
    private boolean             followerFriendly;           //determines if a tile has a vacant place for follower

    public static Game getInstance() {
        if (game == null){
            game = new Game();
            game.table = new Table();
        }
        return game;
    }

    @Override
    public void turnActions(int x, int y) {
        turnActions(x, y, Rotation.DEG_0);
    }

    @Override
    public void turnActions(int x, int y, Rotation angle) {
        Tile tile = getCurrentTile();
        tile.setCoordinates(x, y);
        tile.turnRight(angle);
        table.placeTile(tile);
        if (tilePile.hasTiles()) {
            nextPlayer();
            dragTile();
            notifyController();
    } else {
            finished = true;
            windowLogic.finishGame();
            notifyController();
        }
    }

    @Override
    public void turnActions(int x, int y, Rotation angle,TileDirections direction) {
        turnActions(x, y);
        table.placeFollower(getCurrentPlayer(), direction);
    }

    public void notifyController() {
        windowLogic.update(new GameDataBuilder().setName(getCurrentPlayer().getName()).
                setPoints("" + getCurrentPlayer().getCurrentPoints()).
                setFollowers("" + getCurrentPlayer().getNumberOfFollowers()).
                setPlayerColor(getCurrentPlayer().getColor()).
                setTilesLeft("" + tilePile.getNumberOfTiles()).
                setCurrentTile(table.getCurrentTile()).
                setPreviouslyPlacedTile(table.getPreviouslyPlacedTile()).
                setPossibleTileLocations(table.getPossibleTileLocations()).
                setPossibleLocationsAndRotations(table.getPossibleTileLocationsAndRotations()).
                createGameData());
    }

    @Override
    public void forceNotify() {
        notifyController();
    }

    @Override
    public Set<double[]> getPossibleFollowerLocations(int currentTileX, int currentTileY) {
        return followerPlacingHelper.getFollowerLocations(getCurrentTile());
    }

    //<editor-fold desc="Getters">
    int getNumberOfPlayers() {
        return players.size();
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }
    boolean isCurrentTileConfirmed() {
        return currentTileConfirmed;
    }

    public Tile getCurrentTile() {
        return table.getCurrentTile();
    }

    public TilePile getTilePile() {
        return tilePile;
    }

    public boolean isFinished() {
        return finished;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">
    void setFollowerFriendly(boolean followerFriendly) {
        this.followerFriendly = followerFriendly;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void setWindowLogic(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }
    //</editor-fold>

    public void nextPlayer() {
        if (currentPlayer == null) {
            currentPlayer = players.get(0);
        } else {
            currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        }
    }

    public boolean addPlayer(String name, Color color) {
        if (5 > getNumberOfPlayers()) {
            players.add(new Player(name, color));
            return true;
        } else {
            return false;
        }
    }

    /*
     * Adding no param player is used only in testing
     */
    boolean addPlayer() {
        return addPlayer("Default", Color.BLACK);
    }

    void confirmCurrentTile(int x, int y) {
        currentTileConfirmed = true;
    }

    void confirmFollower() {
        if (!currentTileConfirmed)
            throw new RuntimeException("Cannot place follower if tile placement is not confirmed");
        if (!followerFriendly)
            throw new RuntimeException("Not followerFriendly (no vacant space for a follower)");
        currentPlayer.increaseNumberOfProperties();
        currentPlayer.placeFollower();
        nextPlayer();
    }


    public void dragTile() {
        if (tilePile.hasTiles())
            table.setCurrentTile(tilePile.dragTile());
        else
            throw new RuntimeException("Trying to drag a tile from an empty pile");
    }

    //TODO rename,
    //TODO where is it used
    public boolean followerCanBePlaced() {
        return true;
    }

    /*
     * This function was not tested with JUnit
     * Used in testing
     */
    public void loadTestTiles() {
        game.getTilePile().addTile(Tile.getInstance());
        game.getTilePile().addTile(Tile.getInstance());
        game.getTilePile().addTile(Tile.getInstance());
    }

    FollowerPlacingHelper getFollowerPlacingHelper() {
        return followerPlacingHelper;
    }

    /**
     * An instance of this class has a method that returns all legal locations
     * of possible follower placement on the current tile. Legal is:
     * 1) 1 per feature
     * 2) exclude occupied real estate
     */
    class FollowerPlacingHelper {

        Set<double[]> getFollowerLocations(Tile tile) {
            Set<double[]> result = new HashSet<>();
            Set<Feature> features = tile.getFeatures();
            for (Feature feature: features) {
                result.add(getTileSizeRelativeMultipliers(tile, feature));
            }
            return result;
        }

        private double[] getTileSizeRelativeMultipliers(Tile tile, Feature feature) {
            double[] xyMultipliers = new double[2];
            /*
             * get 1 direction pre feature
             */
            Set<TileDirections> directions= tile.getFeatureTileDirections(feature);

            if (feature.isCity()) {
                directions.removeAll(Arrays.asList(NNE, NNW, EES, EEN, WWS, WWN, SSE, SSW));
            } else if (feature.isLand()) {
                if (directions.size() > 3) {
                    directions.removeAll(Arrays.asList(NNE, NNW, EES, EEN, WWS, WWN, SSE, SSW));
                }
            }

            TileDirections direction = Util.any(directions);




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
                    if(feature.isLand()) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.6;
                    }
                case CITY1RSE:
                case CITY1RSW:
                case CITY1RWE:
                case CITY1RSWE:
                    if (feature.isRoad() && directions.size() == 2) {
                        if (directions.contains(TileDirections.WEST) && directions.contains(TileDirections.EAST)) {
                            xyMultipliers[0] = 0.5;
                            xyMultipliers[1] = 0.5;
                        } else if (directions.contains(TileDirections.WEST)) {
                            xyMultipliers[0] = 0.4;
                            xyMultipliers[1] = 0.6;
                        } else {
                            xyMultipliers[0] = 0.6;
                            xyMultipliers[1] = 0.6;
                        }
                    } else if((feature.isRoad() && directions.size() == 1) ) {
                        if (directions.contains(TileDirections.WEST)) {
                            xyMultipliers[0] = 0.3;
                            xyMultipliers[1] = 0.6;
                        } else if (directions.contains(TileDirections.EAST)) {
                            xyMultipliers[0] = 0.8;
                            xyMultipliers[1] = 0.6;
                        }

                    } else if(feature.isLand() && directions.contains(TileDirections.WWN)) {
                        xyMultipliers[0] = 0.15;
                        xyMultipliers[1] = 0.35;
                    }
                    break;
                case CITY2WE:
                case CITY2WES:
                    if (feature.isCity()) {
                        xyMultipliers[0] = 0.5;
                        xyMultipliers[1] = 0.45;
                    } else if (directions.contains(TileDirections.NORTH)) {
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
                    } else if (directions.contains(SSW)){
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
                        } else if (directions.contains(TileDirections.SSW)) {
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
            }
            return xyMultipliers;
        }
    }
}
