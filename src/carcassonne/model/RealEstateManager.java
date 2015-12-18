package carcassonne.model;

import java.util.*;

/**
 *
 */
public class RealEstateManager {
    private Map<Player, Set<RealEstate>> assetsList = new HashMap<>();
    // TODO make it final
    private Table table;

    RealEstateManager() {}

    RealEstateManager(Table table) {
        this.table = table;
    }

    Set<RealEstate> getAssets(Player player) {
        Set<RealEstate> result = new HashSet<>(assetsList.get(player));
        return result;
    }

    void addAsset(Player player, RealEstate realEstate) {
        if (assetsList.containsKey(player)) {
            Set<RealEstate> assets = assetsList.get(player);
            assets.add(realEstate);
        } else {
            assetsList.put(player, new HashSet<>(Arrays.asList(realEstate)));
        }
    }

    void createAsset(Player player, Tile tile) {
        RealEstate realEstate = new RealEstate(tile, table);
        if (assetsList.containsKey(player)) {
            Set<RealEstate> assets = assetsList.get(player);
            assets.add(realEstate);
        } else {
            assetsList.put(player, new HashSet<>(Collections.singletonList(realEstate)));
        }

    }

    void update(Tile tile) {
        Set<Player> keySet = assetsList.keySet();
        for (Player player: keySet) {
            Set<RealEstate> realEstateSet = assetsList.get(player);
            for (RealEstate realEstate: realEstateSet) {
                realEstate.update(tile);
            }
        }
    }
}
