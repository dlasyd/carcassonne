package carcassonne.model;

import java.util.*;

/**
 *
 */
public class RealEstateManager {
    private Map<Player, Set<RealEstate.ImmutableRealEstate>> playerToRealEstateSetMap = new HashMap<>();
    private Map<RealEstate.ImmutableRealEstate, Set<Player>> realEstateMap = new HashMap<>();
    private final int MAXIMUM_UNION_PER_TILE = 4;
    // TODO make it final
    private Table table;

    RealEstateManager() {}

    RealEstateManager(Table table) {
        this.table = table;
    }

    Set<RealEstate> getAssets(Player player) {
        Set<RealEstate> result = new HashSet<>();
        for (RealEstate.ImmutableRealEstate realEstate: playerToRealEstateSetMap.get(player)) {
            result.add(realEstate.getRealEstate());
        }
        return result;
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
        RealEstate realEstate = new RealEstate(tile, table);
        if (playerToRealEstateSetMap.containsKey(player)) {
            Set<RealEstate.ImmutableRealEstate> assets = playerToRealEstateSetMap.get(player);
            assets.add(realEstate.getImmutableRealEstate());
        } else {
            playerToRealEstateSetMap.put(player, new HashSet<>(Collections.singletonList(realEstate.getImmutableRealEstate())));
        }

        realEstateMap.put(realEstate.getImmutableRealEstate(), new HashSet<>(Collections.singletonList(player)));
    }

    void update(Tile tile) {
        for (RealEstate.ImmutableRealEstate realEstate: realEstateMap.keySet()) {
            realEstate.getRealEstate().update(tile);
        }
        realEstateUnion();
    }

    Map<RealEstate.ImmutableRealEstate, Set<Player>> getRealEstateMap() {
        return new HashMap<>(realEstateMap);
    }

    Set<RealEstate.ImmutableRealEstate> getRealEstateImmutableSet() {
        return new HashSet<>(realEstateMap.keySet());
    }

    private void realEstateUnion() {
        ArrayList<RealEstate.ImmutableRealEstate> allRealEstateObjects;
        HashSet<RealEstate.ImmutableRealEstate> duplicateRealEstate;
        allRealEstateObjects = new ArrayList<>(realEstateMap.keySet());
        for (int i = 0; i < MAXIMUM_UNION_PER_TILE; i++) {
            duplicateRealEstate = new HashSet<>();

            if (!(allRealEstateObjects.size() > i))
                    break;

            RealEstate.ImmutableRealEstate checkedRealEstate = allRealEstateObjects.get(i);

            for (RealEstate.ImmutableRealEstate realEstateToCompare: allRealEstateObjects) {
                if (checkedRealEstate.getRealEstate().equals(realEstateToCompare.getRealEstate())) {
                    duplicateRealEstate.add(checkedRealEstate);
                    duplicateRealEstate.add(realEstateToCompare);
                }
            }

            // TODO create function to get any item of HashSet and use it
            Set<Player> ownersOfDuplicateRealEstate = new ArrayList<>(duplicateRealEstate)
                    .get(0).getRealEstate().getLegitimateOwners();

            boolean added = false;
            /*
             * masterRealEstate - the one that will replace copies in playerToRealEstateSetMap
             */
            RealEstate.ImmutableRealEstate masterRealEstate = null;
            for (RealEstate.ImmutableRealEstate sameRealEstate: duplicateRealEstate) {
                if (added == false) {
                    realEstateMap.put(sameRealEstate, ownersOfDuplicateRealEstate);
                    masterRealEstate = sameRealEstate;
                    added = true;
                } else {
                    realEstateMap.remove(sameRealEstate);
                }
            }

            assert (masterRealEstate != null);
            duplicateRealEstate.remove(masterRealEstate);

            for (Player player: new HashSet<>(playerToRealEstateSetMap.keySet())) {
                for (RealEstate.ImmutableRealEstate toRemove: duplicateRealEstate) {
                    if (playerToRealEstateSetMap.get(player).contains(toRemove)) {
                        Set<RealEstate.ImmutableRealEstate> playersRealEstate = playerToRealEstateSetMap.get(player);
                        playersRealEstate.remove(toRemove);

                        //TODO only add if a player owns it
                        if (ownersOfDuplicateRealEstate.contains(player))
                            playersRealEstate.add(masterRealEstate);
                        if (playersRealEstate.isEmpty())
                            playerToRealEstateSetMap.remove(player);
                    }
                }
            }

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

}
