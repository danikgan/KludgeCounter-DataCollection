package features;

import features.analyse.*; // additional classes for AnalysedLink
import features.utilities.DeleteFiles;

import java.io.File;
import java.util.LinkedList;

/** Analyse each commit of links using PMD for unique quality alerts.
 *
 * 1. Download a commit.
 * 2. Apply PMD, record the quality alerts in a txt.
 * 3. Find the unique (pre commit) quality alerts.
 * 4. Record all the corresponding information.
 * 5. Delete the commit.
 */

public class AnalysedLink {
    private Records record = new Records();
    private boolean projectExists = true;

    private String gitDiffTXT_string = "git-diff.txt";
    private String commitsListTXT_string = "commits-list.txt";
    private String pmdAlertsTXT_string = "pmd-alerts.txt";

    private String projectName;
    private String gitPath;
    private String fullPath;
    private int commitQuantity;

    private LinkedList<String> uniqueAlerts = new LinkedList<>();
    private LinkedList<Integer> uniqueAlerts_count = new LinkedList<>();

    public AnalysedLink(String gitPath, String link, int commitQuantity) {
        this.gitPath = gitPath;
        this.commitQuantity = commitQuantity;

        if (commitQuantity < 0) {
            projectExists = false;
            projectName = null;
        } else {
            DownloadGit downloadGit = new DownloadGit();
            projectName = downloadGit.DownloadGit(link, gitPath);
        }

        if (projectName != null) {
            this.fullPath = gitPath + "/" + projectName; // used throughout the class

            System.out.println("Finding additional information...");
            setProjectInformation();
            System.out.println("Applying PMD...");
            usePMDonCommit();

            // deleting the temporary files
            deleteTemporaryFiles();
        } else {
            projectExists = false;
        }

        System.out.println("Project Exists: " + projectExists);
        System.out.println("Projected assessed.\n");
    }

    private void setProjectInformation() {
        record.setProjectName(projectName);

        String fileName_LC = commitsListTXT_string;
        String[] commandListCommits;
        if (commitQuantity==0){ // means download all commits
            commandListCommits = new String[] {"git", "log"};
        } else {
            commandListCommits = new String[] {"git", "log", "-" + commitQuantity};
        }
        new UseTerminal(commandListCommits, fullPath, fileName_LC);

        System.out.println("Reading " + fileName_LC + "...");
        ReadTXTCommits readTXTCommits = new ReadTXTCommits(fileName_LC);
        if (readTXTCommits.isTxtExists()) {
            record.setAuthorName(readTXTCommits.getAuthorName());
            record.setCommitNumber(readTXTCommits.getCommitNumber());
            record.setCommitDate(readTXTCommits.getCommitDate());
            record.setCommitComment(readTXTCommits.getCommitComment());
        } else {
            projectExists = false;
        }
    }

    /** This is how usePMDonCommit() works:
     *
     * 1. Checks if it's the first iteration of the loop
     *  1.1 If true, then assign a temp variable the commitNumber
     *  1.2 Else, use git-diff and read git-diff txt against pmd txt (which is supposed to reflect the later commit)
     * 2. Checkout
     * 3. Record alerts in PMD txt
     */
    private void usePMDonCommit() {
        // there are no alerts for the first commit
        uniqueAlerts.add("N/A");
        uniqueAlerts_count.add(-1);

        String commitNumber_next = ""; // this is going to be "next" commit, cuz it goes from latest to earliest

        int i_temp = 1; // to keep track of checkouts during runtime
        for (String commitNumber:record.getCommitNumber()) {
            System.out.println(i_temp + ". This checkout: " + commitNumber);

            // checking for the first iteration
            if (commitNumber_next.equals("") || commitNumber_next.equals(commitNumber)) {
//                System.out.println("First commit.");
                commitNumber_next = commitNumber;
            } else { // if it's not the first iteration
                // git-diff is in the form of "prevCommit nextCommit" for correct positive information
                String[] commandGitDiff = {"git", "diff", commitNumber, commitNumber_next};
                new UseTerminal(commandGitDiff, fullPath, gitDiffTXT_string);

                // getting unique errors and their count
                ComparePMDandDIFF comparePMDandDIFF = new ComparePMDandDIFF(fullPath, gitDiffTXT_string, pmdAlertsTXT_string);
                uniqueAlerts.add(comparePMDandDIFF.getUniqueAlerts()); // add multiple strings, per commit
                uniqueAlerts_count.add(comparePMDandDIFF.getUniqueAlerts_count()); // add one value, per commit
            }

            // do not more operations on the last commit as it is useless
            if (!commitNumber.equals(record.getCommitNumber().getLast())) { // checking if it's the last commit
                String[] commandCheckout = {"git", "checkout", commitNumber};
                new UseTerminal(commandCheckout, fullPath, ""); // fileName zero means no record

                // apply PMD and record the alerts in txt
//                System.out.println("Applying PMD...");
                new ApplyPMD(projectName, gitPath);
            }

            i_temp++;
            commitNumber_next = commitNumber; // from latest to earliest
        }

        // after all uniqueAlerts and uniqueAlerts_count were collected
        record.setUniqueAlerts(uniqueAlerts);
        record.setUniqueAlerts_count(uniqueAlerts_count);
    }

    private void deleteTemporaryFiles() {
        System.out.println("Deleting: " + projectName);
        new DeleteFiles(new File(fullPath)); // tools.DeleteFiles deletes all files within the directory
//
//        // delete txt temporary files
//        new DeleteFiles(new File(gitDiffTXT_string));
        new DeleteFiles(new File(commitsListTXT_string));
//        new DeleteFiles(new File(pmdAlertsTXT_string));
    }

    // used by Main
    public boolean isProjectExists() {
        return projectExists;
    }
    public Records getRecord() {
        return record;
    }
}
