import java.util.List;
import java.util.Map;

abstract class Strategy {
    Map<String, List<Integer>> index; // what types of data can abstract class contain???
    abstract String find(Map<Integer, String> data, String[] target);
}
