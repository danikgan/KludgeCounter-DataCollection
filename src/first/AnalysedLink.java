package first;

import first.analyse.*; // additional classes for AnalysedLink
import common.DeleteFiles;
import common.TemporaryFiles;
import first.data.Records;

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
	// returned to main
	private Records record = new Records();
	private boolean projectExists = true;
	//    private boolean projectFinished = false;
	// various file created for the execution
	private String gitDiffTXT_string = TemporaryFiles.analysing.GIT_DIFF.getString();
	private String commitsListTXT_string = TemporaryFiles.analysing.COMMITS_LIST.getString();
	private String pmdAlertsTXT_string = TemporaryFiles.analysing.PMD_ALERTS.getString();
	// attributes needed
	private String projectName;
	private String gitPath;
	private String fullPath;
	private int commitQuantity;
	// adding alerts one-by-one for each commit
	private LinkedList<String> uniqueAlerts = new LinkedList<>();
	private LinkedList<Integer> uniqueAlerts_count = new LinkedList<>();
	private LinkedList<Integer> numberOfLinesModified = new LinkedList<>();
	private LinkedList<Integer> numberOfFilesModified = new LinkedList<>();
	// constructor
	public AnalysedLink(String gitPath, int commitQuantity) {
		// passed from main
		this.gitPath = gitPath;
		this.commitQuantity = commitQuantity;
		// launching the analyses
	}
	public void analysing(String link) {
		// in case commit quantity is negative do not proceed
		if (commitQuantity < 0) {
			projectExists = false;
			projectName = null;
		} else {
			DownloadGit downloadGit = new DownloadGit();
			projectName = downloadGit.DownloadGit(link, gitPath);
		}
		// project was downloaded
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
		// exiting analysis of the current project
		System.out.println("Project Exists: " + projectExists);
		System.out.println("Projected assessed.\n");
	}
	// gets initial information on the project repository
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
	// main getString process
	/** This is how usePMDonCommit() works:
	 *
	 * 1. Checks if it's the first iteration of the loop
	 *  1.1 If true, then assign a temp variable the commitNumber
	 *  1.2 Else, use git-diff and read git-diff txt against pmd txt (which is supposed to reflect the later commit)
	 * 2. Checkout
	 * 3. Record alerts in PMD txt
	 */
	private void usePMDonCommit() {
		String commitNumber_next = ""; // this is going to be "next" commit, cuz it goes from latest to earliest
		int i_temp = 1; // to keep track of checkouts during runtime
		for (String commitNumber:record.getCommitNumber()) {
			System.out.println(i_temp + ". This checkout: " + commitNumber);
			// checking for the first iteration
			if (commitNumber_next.equals("") || commitNumber_next.equals(commitNumber)) {
				commitNumber_next = commitNumber;
			} else {
				// if it's not the first iteration
				// git-diff is in the form of "prevCommit nextCommit" for correct positive information
				String[] commandGitDiff = {"git", "diff", commitNumber, commitNumber_next};
				new UseTerminal(commandGitDiff, fullPath, gitDiffTXT_string);
				// getting unique errors and their count
				ComparePMDandDIFF comparePMDandDIFF = new ComparePMDandDIFF(fullPath, gitDiffTXT_string, pmdAlertsTXT_string);
				uniqueAlerts.add(comparePMDandDIFF.getUniqueAlerts()); // add multiple strings, per commit
				uniqueAlerts_count.add(comparePMDandDIFF.getUniqueAlerts_count()); // add one value, per commit
				numberOfLinesModified.add(comparePMDandDIFF.getNumberOfLinesModified()); // saving how many lines were edited
				numberOfFilesModified.add(comparePMDandDIFF.getNumberOfFilesModified()); // saving how many files were edited
			}
			// do not more operations on the last commit as it is useless
			if (!commitNumber.equals(record.getCommitNumber().getLast())) { // checking if it's the last commit
				String[] commandCheckout = {"git", "checkout", commitNumber};
				new UseTerminal(commandCheckout, fullPath, ""); // fileName zero means no record
				// apply PMD and record the alerts in txt
				new ApplyPMD(projectName, gitPath);
			}
			i_temp++;
			commitNumber_next = commitNumber; // from latest to earliest
		}
		// after all uniqueAlerts were collected
		saveData();
	}
	// delete temporary files
	private void deleteTemporaryFiles() {
		System.out.println("Deleting: " + projectName);
		new DeleteFiles(new File(fullPath));
        // delete txt temporary files
		new DeleteFiles(new File(gitDiffTXT_string));
		new DeleteFiles(new File(commitsListTXT_string));
		new DeleteFiles(new File(pmdAlertsTXT_string));
	}
	// used by Main to check if project exists or there were errors
	public boolean isProjectExists() {
		return projectExists;
	}
	// return the data of the current record
	public Records getRecord() {
		return record;
	}
	// in case of the urgent exit, save the current errors found
	public void saveData() {
		record.setUniqueAlerts(uniqueAlerts);
		record.setUniqueAlerts_count(uniqueAlerts_count);
		record.setNumberOfLinesModified(numberOfLinesModified);
		record.setNumberOfFilesModified(numberOfFilesModified);
	}
}
