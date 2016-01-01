package carcassonne.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 27/12/15.
 */
public class Util {
    public static <V, K> void addSetElement(Map<K, Set<V>> map, K key, V value) {
        if (map.containsKey(key)) {
            Set<V> set = map.get(key);
            set.add(value);
        } else {
            map.put(key, new HashSet<V>(Collections.singletonList(value)));
        }
    }

    public static <V, K> void removeSetElement(Map<K, Set<V>> map, K key, V value) {
        if (map.containsKey(key)) {
            Set<V> set = map.get(key);
            set.remove(value);
            if (set.isEmpty())
                map.remove(key);
        }
    }

    public static <T> T any(Set<T> set) {
        if (set.isEmpty())
            throw new RuntimeException("Cannot return element of empty set");
        for (T t: set) {
            return t;
        }
        assert false;
        return null;
    }
}
