package carcassonne.model.realestate;

import carcassonne.model.*;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Has data about real estate and its owners
 * Handles union and changing of owner
 */
public class RealEstateManager {
    private final Map<Player, Set<RealEstate.ImmutableRealEstate>> playerToRealEstateSetMap = new HashMap<>();
    private final Map<RealEstate.ImmutableRealEstate, Set<Player>> realEstateMap = new HashMap<>();
    private final Map<Player, Set<RealEstate.ImmutableRealEstate>> playerToFinishedRealEstate = new HashMap<>();
    private final Table table;

    public RealEstateManager(Table table) {
        this.table = table;
    }

    public boolean playerHasAssets(Player player) {
        return playerToRealEstateSetMap.containsKey(player);
    }

    //<editor-fold desc="Getters">
    public Set<RealEstate> getAssets(Player player) {
        return playerToRealEstateSetMap.get(player).stream()
                .map(RealEstate.ImmutableRealEstate::getRealEstate)
                .collect(Collectors.toSet());
    }


    public Map<Player, Set<RealEstate.ImmutableRealEstate>> getPlayerToFinishedRealEstate() {
        return new HashMap<>(playerToFinishedRealEstate);
    }

    public Map<RealEstate.ImmutableRealEstate, Set<Player>> getRealEstateMap() {
        return new HashMap<>(realEstateMap);
    }

    public Set<RealEstate.ImmutableRealEstate> getRealEstateImmutableSet() {
        return new HashSet<>(realEstateMap.keySet());
    }

    public Map<Player, Set<RealEstate.ImmutableRealEstate>> getPlayerToRealEstateSetMap() {
        return new HashMap<>(playerToRealEstateSetMap);
    }
    //</editor-fold>

    public void addAsset(Player player, RealEstate realEstate) {
        Util.addLinkedSetElement(playerToRealEstateSetMap, player, realEstate.getImmutableRealEstate());
        realEstateMap.put(realEstate.getImmutableRealEstate(), new HashSet<>(Collections.singletonList(player)));
    }

    public void createAsset(Player player, Tile tile) {
        RealEstate realEstate = RealEstate.getInstance(tile, table);
        if (playerToRealEstateSetMap.containsKey(player)) {
            Set<RealEstate.ImmutableRealEstate> assets = playerToRealEstateSetMap.get(player);
            assets.add(realEstate.getImmutableRealEstate());
        } else {
            playerToRealEstateSetMap.put(player, new HashSet<>(Collections.singletonList(realEstate.getImmutableRealEstate())));
        }

        realEstateMap.put(realEstate.getImmutableRealEstate(), new HashSet<>(Collections.singletonList(player)));
    }

    /*
     * Update method is called by table object when a new tile is placed on the table
     */
    public void update(Tile tile) {
//        if (tilePlacedLast.hasFollower()) {
//            placedFollowers.add(new PlacedFollower(tilePlacedLast.getCoordinates(), tilePlacedLast.getOccupiedFeature()));
//            realEstateManager.createAsset(tilePlacedLast.getFollowerOwner(), tilePlacedLast);
//        }
        for (RealEstate.ImmutableRealEstate realEstate: realEstateMap.keySet()) {
            realEstate.getRealEstate().update(tile);
        }
        realEstateUnion();
        finishedRealEstate();
    }


    /*
     * This method is run by an instance that implements OwnershipChecker interface to check
     * whether or not a possible follower position should be displayed and
     * by the table instance to check if Runtime Exception should be thrown
     *
     * According to the rules of the game, Cloisters should be excluded from this check.
     *
     */
    public boolean isPartOfRealEstate(Tile tilePlacedLast, TileDirection direction) {
        return
                realEstateMap.keySet().stream()
                        .filter(immutableRE -> ! (immutableRE.getRealEstate() instanceof Cloister))
                        .map(immutableRealEstate -> {
                            RealEstate temporaryRealEstate = RealEstate.getCopy(immutableRealEstate.getRealEstate());
                            temporaryRealEstate.update(tilePlacedLast);
                            return temporaryRealEstate.contains(tilePlacedLast, direction);})
                        .reduce(false, (acc, element) -> acc = element || acc);
    }

    public void addPointsForUnfinishedRealEstate() {
        for (RealEstate.ImmutableRealEstate currentImmutableRE: realEstateMap.keySet()) {
            int points = currentImmutableRE.getRealEstate().getPoints();
            for (Player player: realEstateMap.get(currentImmutableRE)) {
                Util.addLinkedSetElement(playerToFinishedRealEstate, player, currentImmutableRE);
                Util.removeSetElement(playerToRealEstateSetMap, player, currentImmutableRE);
                player.increaseCurrentPoints(points);
            }
        }
    }

    /*
     * If real estate is finished the following happens:
     * 1) Real estate is removed from playerToRealEstateSetMap and realEstateMap
     * 2) RE is added to playerToFinishedRealEstateSetMap
     * 3) The number of points is counted
     * 4) All owners current points increase by that number
     * 5) Placed followers return to players hands
     */
    private void finishedRealEstate() {
        /*
         * Finished conditions:
         * 1) Tiles around monastery
         * 2) Road has 2 ends
         * 3) Each tile of a castle has an end or a CITY4 - wrong
         */
        Set<RealEstate.ImmutableRealEstate> allRealEstate = new HashSet<>(realEstateMap.keySet());
        allRealEstate.stream()
                .filter(currentImmutableRE -> currentImmutableRE.getRealEstate().isFinished())
                .forEach(currentImmutableRE -> {
                    int points = currentImmutableRE.getRealEstate().getPoints();
                    for (Player player : realEstateMap.get(currentImmutableRE)) {
                        Util.addLinkedSetElement(playerToFinishedRealEstate, player, currentImmutableRE);
                        Util.removeSetElement(playerToRealEstateSetMap, player, currentImmutableRE);
                        player.increaseCurrentPoints(points);
                        removeFollowersFromFinishedRealEstate(currentImmutableRE.getRealEstate(), player);
                    }
            realEstateMap.remove(currentImmutableRE);
        });
    }

    /*
     * Different rules cloister and for road/city
     */
    private void removeFollowersFromFinishedRealEstate(RealEstate realEstate, Player player) {
        if (realEstate.isCloister()) {
            realEstate.getFirstTile().returnFollowerToPlayer();
            table.removeFollowerFromTile(realEstate.getFirstTile().getCoordinates());
            return;
        }
        for (Tile tile: realEstate.getTileSet()) {
            if (tile.hasFollower() &&
                    tile.getFollowerOwner() == player &&
                    realEstate.getTilesAndFeatureTileDirections().get(tile)
                            .contains(tile.getFollowerTileDirection())) {
                tile = tile.returnFollowerToPlayer();
                table.removeFollowerFromTile(tile.getCoordinates());
            }
        }
    }

    /*
     * If several separate real estate objects become connected by recently placed tile, they will have same
     * features and same tiles. This method finds such "equal" real estate objects and invokes
     * mergeRealEstate() method to make one real estate out of several.
     *
     */
    private void realEstateUnion() {
        Set<RealEstate.ImmutableRealEstate> allRealEstateObjects = new HashSet<>(realEstateMap.keySet());

        if (allRealEstateObjects.isEmpty())
            return;

        Set<LinkedHashSet<RealEstate.ImmutableRealEstate>> duplicatesSets = findDuplicatesInSet(allRealEstateObjects);

        duplicatesSets.forEach(this::mergeRealEstate);

    }

    /*
     * Checks if separate real estate object became one as a result of placing latest tile.
     * Then the new real estate should be treated like one object. Method removes separate real estate
     * objects from Collections and adds new (united) real estate.
     * Used by realEstateUnion()
     */
    private void mergeRealEstate(Set<RealEstate.ImmutableRealEstate> duplicateRealEstate) {
        Set<Player> ownersOfDuplicateRealEstate = duplicateRealEstate
                .iterator().next()
                .getRealEstate().getLegitimateOwners();
        RealEstate.ImmutableRealEstate masterRealEstate = duplicateRealEstate
                .iterator().next()
                .getRealEstate().getImmutableRealEstate();

        realEstateMap.keySet().removeAll(duplicateRealEstate);
        realEstateMap.put(masterRealEstate, ownersOfDuplicateRealEstate);

        for (Player player: new HashSet<>(playerToRealEstateSetMap.keySet())) {
            for (RealEstate.ImmutableRealEstate toRemove: duplicateRealEstate) {
                Util.removeSetElement(playerToRealEstateSetMap, player, toRemove);
            }
        }

        for (Player player: ownersOfDuplicateRealEstate)
            Util.addLinkedSetElement(playerToRealEstateSetMap, player, masterRealEstate);
    }

    /*
     * Used by mergeRealEstate()
     */
    private boolean assetSetContainsRealEstateWithTileSet(Set<RealEstate> assets, HashSet<Tile> tiles) {
        return assets.stream()
                .map(realEstate -> realEstate.getTileSet().equals(tiles))
                .reduce(false, (acc, element) -> element || acc);
    }

    /*
     * Used by realEstateUnion()
     */
    private Set<LinkedHashSet<RealEstate.ImmutableRealEstate>> findDuplicatesInSet(Set<RealEstate.ImmutableRealEstate> data) {
        Set<LinkedHashSet<RealEstate.ImmutableRealEstate>> result = new LinkedHashSet<>();
        LinkedHashSet<RealEstate.ImmutableRealEstate> duplicatesFound = new LinkedHashSet<>();
        for (RealEstate.ImmutableRealEstate comparedRealEstate: data) {
            duplicatesFound.addAll(data.stream()
                    .filter(someRealEstate -> comparedRealEstate.getRealEstate().equals(someRealEstate.getRealEstate()))
                    .collect(Collectors.toList()));
            if (duplicatesFound.size() > 1)
                break;
            else
                duplicatesFound.clear();
        }
        if (duplicatesFound.isEmpty()) {
            return result;
        } else {
            Set<RealEstate.ImmutableRealEstate> newData = new HashSet<>(data);
            newData.removeAll(duplicatesFound);
            result.add(duplicatesFound);
            result.addAll(findDuplicatesInSet(newData));
            return result;
        }
    }

}
