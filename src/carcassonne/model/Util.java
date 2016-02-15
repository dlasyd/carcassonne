package carcassonne.model;

import java.util.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 27/12/15.
 */
public class Util {
    public static <V, K> void addLinkedSetElement(Map<K, Set<V>> map, K key, V value) {
        if (map.containsKey(key)) {
            Set<V> set = map.get(key);
            set.add(value);
        } else {
            map.put(key, new LinkedHashSet<V>(Collections.singletonList(value)));
        }
    }

    public static <V, K> void addLinkedSetElement(Map<K, Set<V>> map, K key, V... values) {
        for (V value: values) {
            addLinkedSetElement(map, key, value);
        }
    }

    public static <V, K> void addLinkedSetElement(Map<K, Set<V>> map, K key, Set<V> values) {
        for (V value: values) {
            addLinkedSetElement(map, key, value);
        }
    }

    public static <V, K> void addAllSetElements(Map<K, Set<V>> receiver, Map<K, Set<V>> donor) {
        for (Map.Entry<K, Set<V>> element: donor.entrySet()) {
            addLinkedSetElement(receiver, element.getKey(), element.getValue());
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
        throw new RuntimeException("This should not be used");
    }
}
