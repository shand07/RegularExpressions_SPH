import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This program processes a novel file and a file containing regex patterns, searches for each pattern in the novel,
 * and writes the pattern along with its count to an output file.
 * Assignment 4
 * CS322 - Compiler Construction
 * Spring 2024
 * @author Sean Hand
 * @version 1.0
 */
public class NovelProcessor {

    /**
     * Main method to execute the program.
     * @param args Command line arguments - not used in this version.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for novel file name
        System.out.print("Enter the full path of the novel file: ");
        String novelFileName = scanner.nextLine();

        // Prompt user for patterns file name
        System.out.print("Enter the full path of the patterns file: ");
        String patternsFileName = scanner.nextLine();

        HashMap<String, Integer> patternCounts = new HashMap<>();
        
        try (BufferedReader novelReader = new BufferedReader(new FileReader(novelFileName))) {
            StringBuilder novelContent = new StringBuilder();
            String line;
            while ((line = novelReader.readLine()) != null) {
                novelContent.append(line).append("\n");
            }
            
            String novelText = novelContent.toString();
            
            try (BufferedReader patternsReader = new BufferedReader(new FileReader(patternsFileName))) {
                String pattern;
                while ((pattern = patternsReader.readLine()) != null) {
                    int count = countPatternOccurrences(novelText, pattern);
                    patternCounts.put(pattern, count);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
            return;
        }
        
        String novelName = novelFileName.substring(0, novelFileName.lastIndexOf('.'));
        String outputFileName = novelName + "_wc.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String pattern : patternCounts.keySet()) {
                writer.write(pattern + "|" + patternCounts.get(pattern) + "\n");
            }
            System.out.println("Word count data written to " + outputFileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        scanner.close();
    }
    
    /**
     * Counts the occurrences of a pattern in a text.
     * @param text The text to search for pattern occurrences.
     * @param pattern The regex pattern to search for.
     * @return The count of occurrences of the pattern in the text.
     */
    private static int countPatternOccurrences(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }
}
