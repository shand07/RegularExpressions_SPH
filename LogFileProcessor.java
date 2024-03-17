import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LogFileProcessor class to process a Unix log file.
 * This class searches the log file for unique IP addresses and usernames,
 * and stores them in separate hashmaps.
 * It also provides methods to print the contents of each hashmap and get their sizes.
 * @author Sean Hand
 * @version 1.0
 * Assignment 4
 * CS322 - Compiler Construction
 * Spring 2024
 */
public class LogFileProcessor {
    private Map<String, Integer> ipAddressMap;
    private Map<String, Integer> usernameMap;

    /**
     * Constructor for LogFileProcessor.
     * Initializes the hashmaps.
     */
    public LogFileProcessor() {
        ipAddressMap = new HashMap<>();
        usernameMap = new HashMap<>();
    }

    /**
     * Process the log file and populate the hashmaps with IP addresses and usernames.
     * @param filename The filename of the log file to process.
     * @throws IOException If an error occurs while reading the file.
     */
    public void processLogFile(String filename) throws IOException {
        int linesParsed = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            Pattern ipPattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
            Pattern usernamePattern = Pattern.compile("username=(\\w+)");

            while ((line = br.readLine()) != null) {
                linesParsed++;
                Matcher ipMatcher = ipPattern.matcher(line);
                Matcher usernameMatcher = usernamePattern.matcher(line);

                while (ipMatcher.find()) {
                    String ipAddress = ipMatcher.group();
                    ipAddressMap.put(ipAddress, ipAddressMap.getOrDefault(ipAddress, 0) + 1);
                }

                while (usernameMatcher.find()) {
                    String username = usernameMatcher.group(1);
                    usernameMap.put(username, usernameMap.getOrDefault(username, 0) + 1);
                }
            }
        }
        System.out.println(linesParsed + " lines in the log file were parsed.");
    }

    /**
     * Get the size of the IP address hashmap.
     * @return The number of unique IP addresses.
     */
    public int getIpMapSize() {
        return ipAddressMap.size();
    }

    /**
     * Get the size of the username hashmap.
     * @return The number of unique usernames.
     */
    public int getUsernameMapSize() {
        return usernameMap.size();
    }

    /**
     * Print the contents of the IP address hashmap.
     */
    public void printIpMap() {
        for (Map.Entry<String, Integer> entry : ipAddressMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Print the contents of the username hashmap.
     */
    public void printUsernameMap() {
        for (Map.Entry<String, Integer> entry : usernameMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Main method to test the LogFileProcessor class.
     * Prompts the user for a filename and print flag.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the filename of the log file: ");
        String filename = scanner.nextLine();

        System.out.print("Enter the print flag (0 for default, 1 for IP addresses, 2 for usernames): ");
        int printFlag = scanner.nextInt();

        scanner.close();

        LogFileProcessor processor = new LogFileProcessor();
        try {
            processor.processLogFile(filename);
            System.out.println("There are " + processor.getIpMapSize() + " unique IP addresses in the log.");
            System.out.println("There are " + processor.getUsernameMapSize() + " unique users in the log.");

            switch (printFlag) {
                case 1:
                    processor.printIpMap();
                    break;
                case 2:
                    processor.printUsernameMap();
                    break;
                default:
                    // Display default output
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
