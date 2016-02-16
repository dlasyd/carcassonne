package carcassonne.model;

import java.util.*;

/**
 *
 */
public class RealEstateManager {
    private Map<Player, Set<RealEstate.ImmutableRealEstate>> playerToRealEstateSetMap = new HashMap<>();
    private Map<RealEstate.ImmutableRealEstate, Set<Player>> realEstateMap = new HashMap<>();
    private Map<Player, Set<RealEstate.ImmutableRealEstate>> playerToFinishedRealEstate = new HashMap<>();
    private final Table table;

    public RealEstateManager(Table table) {
        this.table = table;
        RealEstate.setTable(table);
    }

    boolean playerHasAssets(Player player) {
        return playerToRealEstateSetMap.containsKey(player);
    }

    //<editor-fold desc="Getters">
    Set<RealEstate> getAssets(Player player) {
        Set<RealEstate> result = new HashSet<>();
        for (RealEstate.ImmutableRealEstate realEstate: playerToRealEstateSetMap.get(player)) {
            result.add(realEstate.getRealEstate());
        }
        return result;
    }


    Map<Player, Set<RealEstate.ImmutableRealEstate>> getPlayerToFinishedRealEstate() {
        return new HashMap<>(playerToFinishedRealEstate);
    }

    Map<RealEstate.ImmutableRealEstate, Set<Player>> getRealEstateMap() {
        return new HashMap<>(realEstateMap);
    }

    Set<RealEstate.ImmutableRealEstate> getRealEstateImmutableSet() {
        return new HashSet<>(realEstateMap.keySet());
    }

    Map<Player, Set<RealEstate.ImmutableRealEstate>> getPlayerToRealEstateSetMap() {
        return playerToRealEstateSetMap;
    }
    //</editor-fold>

    void addAsset(Player player, RealEstate realEstate) {
        Util.addLinkedSetElement(playerToRealEstateSetMap, player, realEstate.getImmutableRealEstate());
        realEstateMap.put(realEstate.getImmutableRealEstate(), new HashSet<>(Collections.singletonList(player)));
    }

    void createAsset(Player player, Tile tile) {
        RealEstate realEstate = RealEstate.getInstance(tile);
        if (playerToRealEstateSetMap.containsKey(player)) {
            Set<RealEstate.ImmutableRealEstate> assets = playerToRealEstateSetMap.get(player);
            assets.add(realEstate.getImmutableRealEstate());
        } else {
            playerToRealEstateSetMap.put(player, new HashSet<>(Collections.singletonList(realEstate.getImmutableRealEstate())));
        }

        realEstateMap.put(realEstate.getImmutableRealEstate(), new HashSet<>(Collections.singletonList(player)));
        realEstateUnion();
        finishedRealEstate();
    }

    /*
     * Update method is called by table object when a new tile is placed on the table
     */
    void update(Tile tile) {
        for (RealEstate.ImmutableRealEstate realEstate: realEstateMap.keySet()) {
            realEstate.getRealEstate().update(tile);
        }
        realEstateUnion();
        finishedRealEstate();
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
        for (RealEstate.ImmutableRealEstate currentImmutableRE: allRealEstate) {
            if (currentImmutableRE.getRealEstate().isFinished()) {
                int points = currentImmutableRE.getRealEstate().getPoints();
                for (Player player: realEstateMap.get(currentImmutableRE)) {
                    Util.addLinkedSetElement(playerToFinishedRealEstate, player, currentImmutableRE);
                    Util.removeSetElement(playerToRealEstateSetMap, player, currentImmutableRE);
                    player.increaseCurrentPoints(points);
                    for (Tile tile: currentImmutableRE.getRealEstate().getTileSet()) {
                        if (tile.hasFollower() &&
                                tile.getFollowerOwner() == player &&
                                currentImmutableRE.getRealEstate()
                                        .getTilesAndFeatureTileDirections().get(tile)
                                        .contains(tile.getFollowerTileDirection())) {
                            tile = tile.returnFollowerToPlayer();
                            table.removeFollowerFromTile(tile.getCoordinates());
                        }
                    }
                }
                realEstateMap.remove(currentImmutableRE);
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

        for (Set<RealEstate.ImmutableRealEstate> set: duplicatesSets) {
            mergeRealEstate(set);
        }

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
     * Used by realEstateUnion()
     * Uses recursion
     */
    private Set<LinkedHashSet<RealEstate.ImmutableRealEstate>> findDuplicatesInSet(Set<RealEstate.ImmutableRealEstate> data) {
        Set<LinkedHashSet<RealEstate.ImmutableRealEstate>> result = new LinkedHashSet<>();
        LinkedHashSet<RealEstate.ImmutableRealEstate> duplicatesFound = new LinkedHashSet<>();
        for (RealEstate.ImmutableRealEstate comparedRealEstate: data) {
            for (RealEstate.ImmutableRealEstate someRealEstate: data) {
                if (comparedRealEstate.getRealEstate().equals(someRealEstate.getRealEstate()))
                    duplicatesFound.add(someRealEstate);
            }
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

    /*
     * Used by mergeRealEstate()
     */
    public static boolean assetSetContainsRealEstateWithTileSet(Set<RealEstate> assets, HashSet<Tile> tiles) {
        for (RealEstate realEstate: assets) {
            if (realEstate.getTileSet().equals(tiles))
                return true;
        }
        return false;
    }

    /*
     * This method is run by an instance that implements OwnershipChecker interface to check
     * whether or not a possible follower position should be displayed and
     * by the table instance to check if Runtime Exception should be thrown
     *
     * According to the rules of ther game, Cloisters should be excluded from this check.
     *
     */
    public boolean isPartOfRealEstate(Tile tilePlacedLast, TileDirections direction) {
        boolean result = false;
        for (RealEstate.ImmutableRealEstate iRealEstate: realEstateMap.keySet()) {
            if (iRealEstate.getRealEstate() instanceof Cloister)
                continue;
            RealEstate temporaryRealEstate = RealEstate.getCopy(iRealEstate.getRealEstate());
            temporaryRealEstate.update(tilePlacedLast);
            result = temporaryRealEstate.contains(tilePlacedLast, direction) || result;
        }
        return result;
    }

    void addPointsForUnfinishedRealEstate() {
        for (RealEstate.ImmutableRealEstate currentImmutableRE: realEstateMap.keySet()) {
            int points = currentImmutableRE.getRealEstate().getPoints();
            for (Player player: realEstateMap.get(currentImmutableRE)) {
                Util.addLinkedSetElement(playerToFinishedRealEstate, player, currentImmutableRE);
                Util.removeSetElement(playerToRealEstateSetMap, player, currentImmutableRE);
                player.increaseCurrentPoints(points);
            }
        }
    }
}
