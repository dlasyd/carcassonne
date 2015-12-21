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
        Set<RealEstate.ImmutableRealEstate> duplicateRealEstate = new HashSet<>();

        for (int i = 0; i < MAXIMUM_UNION_PER_TILE; i++) {
            allRealEstateObjects = new ArrayList<>(realEstateMap.keySet());
            if (!(allRealEstateObjects.size() > i))
                    break;

            RealEstate.ImmutableRealEstate checkedRealEstate = allRealEstateObjects.get(i);

            for (RealEstate.ImmutableRealEstate realEstateToCompare: allRealEstateObjects) {
                if (checkedRealEstate.getRealEstate().equals(realEstateToCompare.getRealEstate())) {
                    duplicateRealEstate.add(checkedRealEstate);
                    duplicateRealEstate.add(realEstateToCompare);
                }
            }

            Set<Player> ownersOfDuplicateRealEstate = new HashSet<>();
            for (RealEstate.ImmutableRealEstate sameRealEstate: duplicateRealEstate) {
                ownersOfDuplicateRealEstate.addAll(realEstateMap.get(sameRealEstate));
            }

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

            for (Player player: playerToRealEstateSetMap.keySet()) {
                for (RealEstate.ImmutableRealEstate toRemove: duplicateRealEstate) {
                    if (playerToRealEstateSetMap.get(player).contains(toRemove)) {
                        Set<RealEstate.ImmutableRealEstate> set = playerToRealEstateSetMap.get(player);
                        set.remove(toRemove);
                        set.add(masterRealEstate);
                    }
                }
            }

        }


    }

    private void removeDuplicateRealEstate(Set<RealEstate.ImmutableRealEstate> immutableRealEstateSet) {

    }
}
