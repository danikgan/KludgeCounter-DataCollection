import features.*;
import features.preprocessing.IdentifyOS;
import features.preprocessing.IdentifyPMD;
import features.preprocessing.PreviousStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    // in case of errors
    private static boolean failureToProceed = false;
    // pre-processing
    private static LinkedList<Integer> commitsQuantity = new LinkedList<>();
    private static LinkedList<String> links = new LinkedList<>();
    // getString each link
    private static LinkedList<Records> records = new LinkedList<>();
//    private static String gitPath = "/Users/" + System.getProperty("user.name") + "/";
    private static String gitPath; // path of the input file
    // needed to save on execution
    private static boolean executionComplete = false;
    private static AnalysedLink analysedLink;
    private static PreviousStatus previousStatus;
    // Main method
    public static void main(String[] args) {
        try {
            // register Message as shutdown hook
            Runtime.getRuntime().addShutdownHook(new Termination());
            // commence the execution
            System.out.println("Hello World!\n");
            askGitPath();
//            gitPath = "/Users/" + System.getProperty("user.name") + "/Desktop"; // used for testing
            preProcessing();
            if (!failureToProceed) {
                analysing();
            }
//            for (Records record:records) { System.out.println(record.toString()); } // used for testing
            executionComplete = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // asking for the path of input
    private static void askGitPath() {
        Scanner scan = new Scanner(System.in);
        // detecting the operating system
        IdentifyOS identifyOS = new IdentifyOS();
        gitPath = identifyOS.getGitPath();
        // getting the path to the text input file
        System.out.println("Specify the path for the text document, containing links and their corresponding number of commits to download. State the folders after: ");
        System.out.printf(gitPath);
        gitPath += scan.nextLine();
    }
    // reading the input file, doing auto-detections
    private static void preProcessing() { // Pre-processing includes the reading of links and quantity of commits
        System.out.println("\nPre-processing...");
        // read the input with the links and commits
        ReadTXTInput readTXTInput = new ReadTXTInput(gitPath);
        links = readTXTInput.getLinks();
        commitsQuantity = readTXTInput.getCommitsQuantity();
        // if there are problems
        if (links == null || commitsQuantity == null || !readTXTInput.getTxtExists()) {
            System.out.println("*** Failed to proceed. Check the txt file.");
            failureToProceed = true;
        }
        // getting the latest PMD version with the repository
        new IdentifyPMD();
        // check the previous status of execution
        previousStatus = new PreviousStatus(links, gitPath);
    }
    // the main analysis of the script
    // Analysing includes downloading the links pre-processed, and getting the unique PMD alerts
    private static void analysing() {
        System.out.println("\nAnalysing...");
        for (int i = 0; i < links.size() && i < commitsQuantity.size(); i++) {
            // executes the analysis
            analysedLink = new AnalysedLink(gitPath, commitsQuantity.get(i));
            analysedLink.analysing(links.get(i));
//            analysedLink = analysedLink_temp;
            if (analysedLink.isProjectExists()){
                records.add(analysedLink.getRecord());
            }
        }
    }
    // saving the analysed data
    private static void savingData() {
        System.out.println("\nSaving...");
        SaveRecordsXLSX saveRecordsXLSX = new SaveRecordsXLSX(records, gitPath);
//        saveRecordsXLSX.createNewRecords();
        if (previousStatus.isAnswered() && !previousStatus.isRestart()) {
            saveRecordsXLSX.checkRecordsExist();
        } else {
            saveRecordsXLSX.createNewRecords();
        }
    }
    // a class that extends thread that is to be called when program is exiting
    static class Termination extends Thread {
        public void run() {
            System.out.println("Execution completed: " + executionComplete);
            if (!executionComplete) {
                boolean projectExists = false;
                try {
                    projectExists = analysedLink.isProjectExists();
                    analysedLink.saveData();
                } catch (Exception e ) {
                    System.out.println("*** Main: Error returning projectExists.");
                    e.printStackTrace();
                }
                // adding the last project being analysed
                if (records.size() > 0 && projectExists) {
                    // checking item wasn't added on termination
                    // this is to avoid duplicated data
                    if (!records.getLast().getProjectName().equals(analysedLink.getRecord().getProjectName())){
                        records.add(analysedLink.getRecord());
                        System.out.println("*** The latest project is added.");
                    } else {
                        System.out.println("*** The latest project was added.");
                    }
                } else if (projectExists) {
                    // in case this is the first link to be analysed
                    records.add(analysedLink.getRecord());
                    System.out.println("*** The latest project is added.");
                }
            }
            // saving to Excel
            if (records.size() > 0) {
                savingData();
            }
            // print that the program is closing
            System.out.println("\nDone.");
        }
    }
}
