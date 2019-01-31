package features.preprocessing;

import features.analyse.UseTerminal;
import features.utilities.DeleteFiles;
import features.utilities.TemporaryFiles;
import features.utilities.FindRepositoryName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class PreviousStatus {
    // dependent on the answer
    private boolean restart = false;
    private boolean answered = false;
    // auto-detect whether the previous execution was irrupted
    public PreviousStatus(LinkedList<String> links, String gitPath) {
        // check user on how to proceed
        String[] command = {"ls"};
        String fileReader = TemporaryFiles.analysing.EXECUTIONSTATUS.getString(); // saves output in this file
        new UseTerminal(command, "", fileReader);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileReader));
            String line = bufferedReader.readLine();
            // initialise the same identifiers as in AnalysedLink
            String[] identifiers = new String[3];
            identifiers[0] = TemporaryFiles.analysing.GITDIFF.getString();
            identifiers[1] = TemporaryFiles.analysing.COMMITSLIST.getString();
            identifiers[2] = TemporaryFiles.analysing.PMDALERTS.getString();
            // compare what was found
            while (line != null) {
                for (String identifier:identifiers) {
                    if (line.length()==identifier.length()) {
                        if (line.substring(0,identifier.length()).equals(identifier)) {
                            if (!answered) {
                                restart = askPrevExecutionStatus();
                                if (restart) { // if true, then delete
                                    new DeleteFiles(new File(identifier));
                                }
                                answered = true;
                            } else {
                                if (restart) { // if true, then delete
                                    new DeleteFiles(new File(identifier));
                                } else {
                                    break; // break if answered "continue"
                                }
                            }
                        }
                    }
                }
                line = bufferedReader.readLine();
                if (answered && !restart){
                    break; // break from the loop if wish to continue
                }
            }
            if (restart || !answered){
                System.out.println("Deleting old repositories...");
                deleteRepositories(links, gitPath);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("*** Main: Error in prevExecutionStatus.");
            e.printStackTrace();
        }
        new DeleteFiles(new File(fileReader));
    }
    // repeat the question responded properly
    private boolean askPrevExecutionStatus() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n*** Your respond is required to proceed.\n" +
                "It has been detected that the previous termination of this script was abrupt.\n" +
                "If you wish to restart, make sure projects that were downloaded during the latest execution are deleted now.\n" +
                "If you wish to continue, then the project should not have been deleted.\n" +
                "Otherwise, it will influence your results.\n" +
                "Do you with to continue the previous analysis (c) or restart (r)?");
        char answer = scan.nextLine().charAt(0);
        if (answer == 'c' || answer == 'C') {
            System.out.println("Continuing the analysis...");
            return false;
        } else if (answer == 'r' || answer == 'R') {
            System.out.println("Restarting the analysis...");
            return true;
        } else {
            return askPrevExecutionStatus();
        }
    }
    // delete all repositories found that can interfere the analysis
    private void deleteRepositories(LinkedList<String> links, String gitPath) {
        for (String link:links) {
            String repositoryName = new FindRepositoryName(link).getFolderName();
            new DeleteFiles(new File(gitPath + "/" + repositoryName));
        }
    }
    // needed getter for appending or creating new Excel
    public boolean isRestart() {
        return restart;
    }
    public boolean isAnswered() {
        return answered;
    }
}
