package carcassonne.model;

import java.util.*;

/**
 *
 */
public class RealEstateManager {
    private Map<Player, Set<RealEstate.ImmutableRealEstate>> assetsList = new HashMap<>();
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
        for (RealEstate.ImmutableRealEstate realEstate: assetsList.get(player)) {
            result.add(realEstate.getRealEstate());
        }
        return result;
    }

    void addAsset(Player player, RealEstate realEstate) {
        if (assetsList.containsKey(player)) {
            Set<RealEstate.ImmutableRealEstate> assets = assetsList.get(player);
            assets.add(realEstate.getImmutableRealEstate());
        } else {
            assetsList.put(player, new HashSet<>(Arrays.asList(realEstate.getImmutableRealEstate())));
        }
    }

    void createAsset(Player player, Tile tile) {
        RealEstate realEstate = new RealEstate(tile, table);
        if (assetsList.containsKey(player)) {
            Set<RealEstate.ImmutableRealEstate> assets = assetsList.get(player);
            assets.add(realEstate.getImmutableRealEstate());
        } else {
            assetsList.put(player, new HashSet<>(Collections.singletonList(realEstate.getImmutableRealEstate())));
        }

        realEstateMap.put(realEstate.getImmutableRealEstate(), new HashSet<>(Collections.singletonList(player)));
    }

    void update(Tile tile) {
        Set<Player> keySet = assetsList.keySet();
        for (Player player: keySet) {
            Set<RealEstate.ImmutableRealEstate> realEstateSet = assetsList.get(player);
            for (RealEstate.ImmutableRealEstate realEstate: realEstateSet) {
                realEstate.getRealEstate().update(tile);
            }
        }
        checkRealEstateUnion();
    }

    Map<RealEstate.ImmutableRealEstate, Set<Player>> getRealEstateMap() {
        return new HashMap<>(realEstateMap);
    }

    Set<RealEstate.ImmutableRealEstate> getRealEstateImmutableSet() {
        return new HashSet<>(realEstateMap.keySet());
    }

    private void checkRealEstateUnion() {
        ArrayList<RealEstate.ImmutableRealEstate> allRealEstateObjects = new ArrayList<>(realEstateMap.keySet());

        Set<RealEstate.ImmutableRealEstate> duplicateRealEstate = new HashSet<>();

        for (int i = 0; i < MAXIMUM_UNION_PER_TILE; i++) {
            allRealEstateObjects = new ArrayList<>(realEstateMap.keySet());
            if (!(allRealEstateObjects.size() > i))
                    break;

            RealEstate.ImmutableRealEstate realEstate = allRealEstateObjects.get(i);

            for (RealEstate.ImmutableRealEstate realEstateToCompare: allRealEstateObjects) {
                if (realEstate.getRealEstate().equals(realEstateToCompare.getRealEstate())) {
                    duplicateRealEstate.add(realEstate);
                    duplicateRealEstate.add(realEstateToCompare);
                }
            }
            // TODO remove duplicate real estate
            Set<Player> owners = new HashSet<>();
            for (RealEstate.ImmutableRealEstate sameRealEstate: duplicateRealEstate) {
                owners.addAll(realEstateMap.get(sameRealEstate));
            }

            boolean added = false;
            /*
             * masterRealEstate - the one that will replace copies in assetsList
             * TODO rename assetsList
             */
            RealEstate.ImmutableRealEstate masterRealEstate = null;

            for (RealEstate.ImmutableRealEstate sameRealEstate: duplicateRealEstate) {
                if (added == false) {
                    realEstateMap.put(sameRealEstate, owners);
                    masterRealEstate = sameRealEstate;
                    added = true;
                } else {
                    realEstateMap.remove(sameRealEstate);
                }
            }

            assert (masterRealEstate != null);
            duplicateRealEstate.remove(masterRealEstate);

            for (Player player: assetsList.keySet()) {
                for (RealEstate.ImmutableRealEstate toRemove: duplicateRealEstate) {
                    if (assetsList.get(player).contains(toRemove)) {
                        Set<RealEstate.ImmutableRealEstate> set = assetsList.get(player);
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
