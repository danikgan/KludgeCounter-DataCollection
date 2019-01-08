import features.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    private static boolean failureToProceed = false;

    // pre-processing
    private static LinkedList<Integer> commitsQuantity = new LinkedList<>();
    private static LinkedList<String> links = new LinkedList<>();

    // analysing each link
    private static LinkedList<Records> records = new LinkedList<>();

    private static String gitPath = "/Users/" + System.getProperty("user.name") + "/";

    public static void main(String[] args) {

        System.out.println("Hello World!\n");
        askGitPath();
//        gitPath += "Desktop/tests";
        preProcessing();
        if (!failureToProceed) {
            analysing();
        }

//        for (Records record:records) {
//            System.out.println(record.toString());
//        }

        if (records.size() > 0) {
            postProcessing();
        }

        System.out.println("\nDone.");
    }

    private static void askGitPath() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Specify the path for the text document, containing links and their corresponding number of commits to download. State the folders after: ");
        System.out.printf(gitPath);
        gitPath += scan.nextLine();
    }

    private static void preProcessing() { // Pre-processing includes the reading of links and quantity of commits
        System.out.println("\nPre-processing...");
        ReadTXTInput readTXTInput = new ReadTXTInput(gitPath);
        links = readTXTInput.getLinks();
        commitsQuantity = readTXTInput.getCommitsQuantity();

        if (links == null || commitsQuantity == null || !readTXTInput.getTxtExists()) {
            System.out.println("*** Failed to proceed. Check the txt file.");
            failureToProceed = true;

        } System.out.println("");
    }

    private static void analysing() { // Analysing includes downloading the links pre-processed, and getting the unique PMD alerts
        System.out.println("Analysing...");
        for (int i = 0; i < links.size() && i < commitsQuantity.size(); i++) {
//            System.out.println("Link: " + links.get(i));
//            System.out.println("Quantity: " + commitsQuantity.get(i));
            AnalysedLink analysedLink = new AnalysedLink(gitPath, links.get(i), commitsQuantity.get(i));
            if (analysedLink.isProjectExists()){
                records.add(analysedLink.getRecord());
            }
        }
    }

    private static void postProcessing() {
        System.out.println("\nSaving...");
        try {
            new SaveRecordsXLSX(records, gitPath);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
