package carcassonne.model;

import java.util.*;

/**
 * Created by Andrey on 02/12/15.
 */
public class RealEstateManager {
    private Map<Player, Set<RealEstate>> assetsList = new HashMap<>();

    public Set<RealEstate> getAssets(Player player) {
        Set<RealEstate> result = new HashSet<>(assetsList.get(player));
        return result;
    }

    public void addAsset(Player player, RealEstate realEstate) {
        if (assetsList.containsKey(player)) {
            Set<RealEstate> assets = assetsList.get(player);
            assets.add(realEstate);
        } else {
            assetsList.put(player, new HashSet<>(Arrays.asList(realEstate)));
        }
    }
}
