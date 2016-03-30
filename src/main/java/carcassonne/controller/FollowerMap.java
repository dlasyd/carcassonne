package carcassonne.controller;

import carcassonne.model.tile.TileDirection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 01/02/16.
 */
public class FollowerMap {
    private final Map<FollowerMultipliers, TileDirection> multipliersToFeatureMap = new HashMap<>();

    public void put(double[] xyMultipliers, TileDirection direction) {
        multipliersToFeatureMap.put(new FollowerMultipliers(xyMultipliers), direction);
    }

    public Set<double[]> getMultipliers() {
        HashSet<double []> result = multipliersToFeatureMap.keySet().stream()
                .map(FollowerMultipliers::getMultipliers)
                .collect(Collectors.toCollection(HashSet::new));
        return result;
    }

    public TileDirection getDirection(double[] multipliers0Rotation) {
        return multipliersToFeatureMap.get(new FollowerMultipliers(multipliers0Rotation));
    }

    class FollowerMultipliers {
        private final double[] multipliers;

        FollowerMultipliers(double[] multipliers) {
            this.multipliers = multipliers;
        }

        double[] getMultipliers() {
            return multipliers;
        }

        @Override
        public int hashCode() {
            return (int) (multipliers[0] * 25 - multipliers[1]);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof FollowerMultipliers && Arrays.equals(((FollowerMultipliers) obj).multipliers, multipliers);

        }
    }
}
