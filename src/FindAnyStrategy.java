import java.util.List;
import java.util.Map;

public class FindAnyStrategy extends Strategy {
    private static Map<String, List<Integer>> index;

    public FindAnyStrategy(Map<String, List<Integer>> index) {
        this.index = index;
    }

    /**
     * Searches for people using ANY strategy - looks for match with any of the target words.
     *
     * @param data Map of people.
     * @param target The target data to look for in the people map.
     * @return The search results (people matching the target data)
     */
    @Override
    public String find(Map<Integer, String> data, String[] target) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < target.length; i++) {
            String word = target[i].toLowerCase();
            if (index.containsKey(word)) { // ensuring user input is lowercase
                for (Integer num : index.get(word)) {
                    sb.append(data.get(num) + "\n");
                }
            }
        }
        return sb.toString();
    }
}
