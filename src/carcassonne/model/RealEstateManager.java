package carcassonne.model;

import java.util.*;

/**
 *
 */
public class RealEstateManager {
    private Map<Player, Set<RealEstate.ImmutableRealEstate>> playerToRealEstateSetMap = new HashMap<>();
    private Map<RealEstate.ImmutableRealEstate, Set<Player>> realEstateMap = new HashMap<>();
    private Map<Player, Set<RealEstate.ImmutableRealEstate>> playerToFinishedRealEstate = new HashMap();
    private final int MAXIMUM_UNION_PER_TILE = 4;
    // TODO make it final
    private Table table;

    RealEstateManager() {}

    public RealEstateManager(Table table) {
        this.table = table;
    }

    boolean playerHasAssets(Player player) {
        return playerToRealEstateSetMap.containsKey(player);
    }

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
    /*
     * used for testing purposes only
     */
    void addAsset(Player player, RealEstate realEstate) {
        if (playerToRealEstateSetMap.containsKey(player)) {
            Set<RealEstate.ImmutableRealEstate> assets = playerToRealEstateSetMap.get(player);
            assets.add(realEstate.getImmutableRealEstate());
        } else {
            playerToRealEstateSetMap.put(player, new HashSet<>(Arrays.asList(realEstate.getImmutableRealEstate())));
        }
        realEstateMap.put(realEstate.getImmutableRealEstate(), new HashSet<>(Collections.singletonList(player)));
    }

    void createAsset(Player player, Tile tile) {
        RealEstate realEstate = RealEstate.getInstance(tile, table);
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

    void update(Tile tile) {
        for (RealEstate.ImmutableRealEstate realEstate: realEstateMap.keySet()) {
            realEstate.getRealEstate().update(tile);
        }
        realEstateUnion();
        finishedRealEstate();
    }

    private void temporaryUpdateRealEstate(Tile tile) {
        for (RealEstate.ImmutableRealEstate realEstate: realEstateMap.keySet()) {
            realEstate.getRealEstate().update(tile);
        }
    }

    private void removeTemporaryUpdate(Tile tile) {
        for (RealEstate.ImmutableRealEstate realEstate: realEstateMap.keySet()) {
            realEstate.getRealEstate().rollBack(tile);
        }
    }

    /*
     * If real estate is finished the following happens:
     * 1) Real estate is removed from playerToRealEstateSetMap and realEstateMap
     * 2) RE is added to playerToFinishedRealEstateSetMap
     * 3) The number of points is counted
     * 4) All owners current points increase by that number
     * TODO 5) Placed followers return to players hands
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
                    Util.addSetElement(playerToFinishedRealEstate, player, currentImmutableRE);
                    Util.removeSetElement(playerToRealEstateSetMap, player, currentImmutableRE);
                    player.increaseCurrentPoints(points);
                    for (Tile tile: currentImmutableRE.getRealEstate().getTileSet()) {
                        if (!tile.isNoFollower()) {
                            tile.returnFollowerToPlayer();
                            table.removeFollowerFromTile(tile.getCoordinates());
                        }
                    }
                }
                realEstateMap.remove(currentImmutableRE);
            }
        }
    }

    Map<RealEstate.ImmutableRealEstate, Set<Player>> getRealEstateMap() {
        return new HashMap<>(realEstateMap);
    }

    Set<RealEstate.ImmutableRealEstate> getRealEstateImmutableSet() {
        return new HashSet<>(realEstateMap.keySet());
    }

    private void realEstateUnion() {
        Set<RealEstate.ImmutableRealEstate> allRealEstateObjects = new HashSet<>(realEstateMap.keySet());

        if (allRealEstateObjects.isEmpty())
            return;

        Set<Set<RealEstate.ImmutableRealEstate>> duplicatesSets = findDuplicatesInSet(allRealEstateObjects);

        for (Set<RealEstate.ImmutableRealEstate> set: duplicatesSets) {
            mergeRealEstate(set);
        }

    }

    private void mergeRealEstate(Set<RealEstate.ImmutableRealEstate> duplicateRealEstate) {
        Set<Player> ownersOfDuplicateRealEstate = Util.any(duplicateRealEstate).getRealEstate().getLegitimateOwners();
        RealEstate.ImmutableRealEstate masterRealEstate = Util.any(duplicateRealEstate).getRealEstate().getImmutableRealEstate();

        realEstateMap.keySet().removeAll(duplicateRealEstate);
        realEstateMap.put(masterRealEstate, ownersOfDuplicateRealEstate);

        for (Player player: new HashSet<>(playerToRealEstateSetMap.keySet())) {
            for (RealEstate.ImmutableRealEstate toRemove: duplicateRealEstate) {
                Util.removeSetElement(playerToRealEstateSetMap, player, toRemove);
            }
        }

        for (Player player: ownersOfDuplicateRealEstate)
            Util.addSetElement(playerToRealEstateSetMap, player, masterRealEstate);
    }

    private Set<Set<RealEstate.ImmutableRealEstate>> findDuplicatesInSet(Set<RealEstate.ImmutableRealEstate> data) {
        Set<Set<RealEstate.ImmutableRealEstate>> result = new HashSet<>();
        Set<RealEstate.ImmutableRealEstate> duplicatesFound = new HashSet<>();
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


    private void removeDuplicateRealEstate(Set<RealEstate.ImmutableRealEstate> immutableRealEstateSet) {

    }

    public static boolean assetSetContainsRealEstateWithTileSet(Set<RealEstate> assets, HashSet<Tile> tiles) {
        for (RealEstate realEstate: assets) {
            if (realEstate.getTileSet().equals(tiles))
                return true;
        }
        return false;
    }

    Map<Player, Set<RealEstate.ImmutableRealEstate>> getPlayerToRealEstateSetMap() {
        return playerToRealEstateSetMap;
    }

    public boolean isPartOfRealEstate(Tile tilePlacedLast, TileDirections direction) {
        boolean result = false;
        for (RealEstate.ImmutableRealEstate iRealEstate: realEstateMap.keySet()) {
            temporaryUpdateRealEstate(tilePlacedLast);
            result = iRealEstate.getRealEstate().contains(tilePlacedLast, direction);
            if (result == true) {
                removeTemporaryUpdate(tilePlacedLast);
                break;
            }
        }
        return result;
    }

    void addPointsForUnfinishedRealEstate() {
        for (RealEstate.ImmutableRealEstate currentImmutableRE: realEstateMap.keySet()) {
            int points = currentImmutableRE.getRealEstate().getPoints();
            for (Player player: realEstateMap.get(currentImmutableRE)) {
                Util.addSetElement(playerToFinishedRealEstate, player, currentImmutableRE);
                Util.removeSetElement(playerToRealEstateSetMap, player, currentImmutableRE);
                player.increaseCurrentPoints(points);
            }
        }
    }
}
