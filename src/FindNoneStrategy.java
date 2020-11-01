import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindNoneStrategy extends Strategy {
    private Map<String, List<Integer>> index;

    FindNoneStrategy(Map<String, List<Integer>> index) {
        this.index = index;
    }

    /**
     * Searches for people using NONE strategy - looks for matches not containing any of the target words.
     *
     * @param data Map of people.
     * @param target The target data to look for in the people map.
     * @return The search results (people matching the target data)
     */
    @Override
    String find(Map<Integer, String> data, String[] target) {
        StringBuilder sb = new StringBuilder();
        Map<Integer, String> mapCopy = new HashMap<>(data);
        for (int i = 0; i < target.length; i++) {
            String word = target[i].toLowerCase();
            if (index.containsKey(word)) { // ensuring user input is lowercase
                List<Integer> nums = index.get(word);
                for (Integer num : nums) {
                    for (Map.Entry<Integer, String> e : data.entrySet()) {
                        if (e.getKey().equals(num)) {
                            mapCopy.remove(e.getKey());
                        }
                    }
                }
            }
        }
        for (Map.Entry<Integer, String> e : mapCopy.entrySet()) {
            sb.append(e.getValue() + "\n");
        }
        return sb.toString();
    }
}
