package org.error_reader.ErrorOperations;

import java.util.Comparator;
import java.util.HashMap;

public class HashMapComparator implements Comparator<String> {
    HashMap<String, Integer> map;

    public HashMapComparator(HashMap<String, Integer> map) {
        this.map = map;
    }

    public int compare(String a, String b) {
        if (map.get(a) > map.get(b)) {
            return -1;
        } else if (map.get(a).equals(map.get(b))) {
            return 0;
        } else {
            return 1;
        }
    }
}
