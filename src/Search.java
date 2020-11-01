import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Course: JetBrains Academy, Java Developer Track
 * Project: Simple Search Engine
 * Purpose: A console-based program to search for people by name or email in a list imported from file.
 *
 * @author Mirek Drozd
 * @version 1.1
 */
public class Search {
    private Strategy strategy;

    public static Scanner in = new Scanner(System.in);
    public static String[] people;
    public static Map<String, List<Integer>> index;

    public String executeStrategy(Map<Integer, String> data, String[] target) {
        return this.strategy.find(data, target);
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void programSetup(String argument) {
        importData(argument);
        index = buildInvertedIndex(people);
        displayMenu();
        selectOption();
    }

    /**
     * Imports data from file and saves to a list.
     *
     * @param pathToFile Path to the file where people data are stored.
     * @return Array of strings representing people data (name, email).
     */
    public String[] importData(String pathToFile) {
        File file = new File(pathToFile);
        try(Scanner fromFile = new Scanner(file)){
            ArrayList<String> list = new ArrayList();
            while (fromFile.hasNext()) {
                list.add(fromFile.nextLine());
            }
            people = new String[list.size()];
            people = list.toArray(people);

        } catch(FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return people;
    }

    /**
     * Displays user menu.
     */
    public void displayMenu(){
        System.out.println("\n=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit\n");
    }

    /**
     * Reads menu option selected by user.
     */
    public void selectOption() {
        String option = in.nextLine();
        while (!option.equals("0")) {
            switch(option) {
                case "1":
                    System.out.println("Select a matching strategy: ALL, ANY, NONE");
                    String strategy = in.nextLine();
                    if (!strategy.toUpperCase().equals("ALL") && !strategy.toUpperCase().equals("ANY") && !strategy.toUpperCase().equals("NONE")) {
                        System.out.println("Unknown strategy.");
                        continue;
                    }
                    System.out.printf("\nEnter a name or email to search all suitable people.%n");
                    String[] target = in.nextLine().split(" ");
                    String result = findPeople(people, target, strategy);
                    System.out.println(result.isEmpty() ? "No matching people found." : result.toString());
                    break;
                case "2":
                    printAllPeople(people);
                    break;
                default:
                    System.out.println("Incorrect option! Try again");
            }
            displayMenu();
            option = in.nextLine();
        }
        System.out.println("Bye!");
    }

    /**
     * Reads search strategy preferred by user and executes the corresponding method.
     *
     * @param people The people data in the form of String array.
     * @param target The target data to look for in the people array.
     * @param strategy The strategy selected for searching.
     * @return The search results (people matching the target data)
     */
    public String findPeople(String[] people, String[] target, String strategy) {
        Map<Integer, String> data = convertArrayToMap(people);
        String result = "";
        switch (strategy.toUpperCase()) {
            case "ANY" :
                this.setStrategy(new FindAnyStrategy(index));
                break;
            case "ALL" :
                this.setStrategy(new FindAllStrategy(index));
                break;
            case "NONE" :
                this.setStrategy(new FindNoneStrategy(index));
                break;
            default :
                result = "UNKNOWN STRATEGY";
        }

        result =  this.executeStrategy(data, target);

        return result;
    }


    /**
     * Prints data of all people from the imported file.
     *
     * @param people The array of strings representing people data.
     */
    public static void printAllPeople(String[] people) {
        System.out.println("=== List of people ===");
        for(String person : people){
            System.out.println(person);
        }
    }

    /**
     * Creates inverted index for the array of strings representing people data.
     *
     * @param array The array of strings representing people data.
     * @return The map representing the inverted index.
     */
    public static Map<String, List<Integer>> buildInvertedIndex(String[] array) {
        Map<String, List<Integer>> map = new HashMap<>();
        Pattern pattern = Pattern.compile("^(\\w+) (\\w+)(\\s?\\w+@\\w+.com)?$");
        Matcher matcher;
        for (int i = 0; i < array.length; i++) {
            matcher = pattern.matcher(array[i]);
            if (matcher.matches()) {
                int numOfGroups;
                if (matcher.group(3) != null) {
                    numOfGroups = 3;
                } else {
                    numOfGroups = 2;
                }
                for (int j = 1; j <= numOfGroups; j++) {
                    String key = matcher.group(j).toLowerCase().trim(); // making keys lowercase
                    if (map.containsKey(key)) {
                        map.get(key).add(i);
                    } else {
                        map.put(key, new ArrayList<>());
                        map.get(key).add(i);
                    }
                }
            }

        }
        return map;
    }

    /**
     * Utility method for converting String array to Map for easier searching.
     *
     * @param array The string array representing people data.
     * @return The resulting Map.
     */
    public static Map<Integer, String> convertArrayToMap(String[] array) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            map.put(i, array[i]);
        }
        return map;
    }
}
