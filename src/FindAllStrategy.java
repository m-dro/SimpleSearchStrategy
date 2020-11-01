import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindAllStrategy extends Strategy {
    private static Map<String, List<Integer>> index;

    public FindAllStrategy(Map<String, List<Integer>> index) {
        this.index = index;
    }

    /**
     * Searches for people using ALL strategy - looks for match with all of the target words.
     *
     * @param data Map of people.
     * @param target The target data to look for in the people map.
     * @return The search results (people matching the target data)
     */
    @Override
    String find(Map<Integer, String> data, String[] target) {
        StringBuilder sb = new StringBuilder();
        List<Integer> indicesForFirstWord = new ArrayList<>();
        List<Integer> indicesForSecondWord = new ArrayList<>();
        List<Integer> indicesForThirdWord = new ArrayList<>();
        Pattern pattern = Pattern.compile("^(\\w+) (\\w+) (\\w+)?$");
        Matcher matcher;
        for (int i = 0; i < target.length; i++) {
            matcher = pattern.matcher(target[i]);
            if (matcher.matches()) {
                if (matcher.group(1) != null) {
                    indicesForFirstWord = index.get(target[0].toLowerCase());
                }
                if (matcher.group(2) != null) {
                    indicesForSecondWord = index.get(target[1].toLowerCase());
                }
                if (matcher.group(3) != null) {
                    indicesForThirdWord = index.get(target[2].toLowerCase());
                }
            }
        }

        indicesForFirstWord.retainAll(indicesForSecondWord);
        indicesForFirstWord.retainAll(indicesForThirdWord);
        for (int i = 0; i < indicesForFirstWord.size(); i++) {
            sb.append(data.get(indicesForFirstWord.get(i)) + "\n");
        }

        return sb.toString();
    }
}
