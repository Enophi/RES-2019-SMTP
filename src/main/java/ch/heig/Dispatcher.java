package ch.heig;

import java.util.ArrayList;
import java.util.List;

public class Dispatcher {

    private static final int MINIMUM_GROUP_SIZE = 3;

    public static List<List<String>> chunk(List<String> list, int size) {
        int counter = 0;
        List<List<String>> result = new ArrayList<>();
        for (String s : list) {
            if (counter++ % size == 0)
                result.add(new ArrayList<>());

            result.get(result.size() - 1).add(s);
        }

        // Last group below the minium of 3 -> Move into the previous group and remove
        if (result.get(result.size() - 1).size() < MINIMUM_GROUP_SIZE) {
            result.get(result.size() - 2).addAll(result.get(result.size() - 1));
            result.remove(result.size() - 1);
        }

        return result;
    }
}
