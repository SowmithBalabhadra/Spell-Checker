import java.io.*;
import java.util.*;

public class SpellChecker {
   
    public static Map<String, Integer> dictionary = new HashMap<>();
    public static String dictionaryPath = "C:\\Users\\sowmi\\Desktop\\spellcheck\\dictionary.txt";

    public static void main(String[] args) {
        loadDictionary(dictionaryPath);
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a sentence: ");
        String input = scanner.nextLine();  
        input = input.toLowerCase();
        String[] words = input.split(" "); 
        
        for (String word : words) {
            if (!dictionary.containsKey(word)) {
                System.out.println("The word '" + word + "' is not in the dictionary.");
                List<String> suggestions = getSimilarWords(word);
                if (!suggestions.isEmpty()) {
                    System.out.println("Did you mean: " + suggestions);
                } else {
                    System.out.println("No suggestions found.");
                }
                
                System.out.println("Would you like to add '" + word + "' to the dictionary? (yes/no): ");
                String response = scanner.nextLine();
                if (response.equals("yes")) {
                    addWordToDictionary(word);
                    System.out.println("'" + word + "' has been added to the dictionary.");
                }
            }
        }
        
        scanner.close();
    }

public static void loadDictionary(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.endsWith(",")) {
                String word = line.replaceAll("\"", "").replace(",", "").trim();
                dictionary.put(word, 1);
            }
        }
        System.out.println("Dictionary loaded with " + dictionary.size() + " words.");
    } catch (IOException e) {
        System.out.println("Error");
    }
}

    public static void addWordToDictionary(String word) {
        dictionary.put(word, 1);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryPath, true))) {
            writer.write("\"" + word + "\": 1,\n");
        } catch (IOException e) {
            System.out.println("Error writing to dictionary file: " + e.getMessage());
        }
    }

    public static List<String> getSimilarWords(String word) {
        List<String> similarWords = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;
        
        for (String dictWord : dictionary.keySet()) {
            int distance = levenshteinDistance(word, dictWord);
            if (distance < minDistance) {
                minDistance = distance;
                similarWords.clear();
                similarWords.add(dictWord);
            } else if (distance == minDistance) {
                similarWords.add(dictWord);
            }
        }
        return similarWords;
    }

    public static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[a.length()][b.length()];
    }
}
    