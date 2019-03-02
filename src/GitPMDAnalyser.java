import common.AskInputPath;
import first.AnalysedLink;
import first.ReadTXTInput;
import first.data.Records;
import first.SaveGitCommits;
import first.preprocessing.IdentifyPMD;
import first.preprocessing.PreviousStatus;
import common.TemporaryFiles;

import java.util.LinkedList;

public class GitPMDAnalyser {
	// in case of errors
	private boolean failureToProceed = false;
	// pre-processing
	private LinkedList<Integer> commitsQuantity = new LinkedList<>();
	private LinkedList<String> links = new LinkedList<>();
	// getString each link
	private LinkedList<Records> records = new LinkedList<>();
	//    private String gitPath = "/Users/" + System.getProperty("user.name") + "/";
	private String gitPath; // path of the input file
	// needed to save on execution
	private boolean executionComplete = false;
	private AnalysedLink analysedLink;
	private PreviousStatus previousStatus;
	// Main method
	GitPMDAnalyser() {
		try {
			// register Message as shutdown hook
			Runtime.getRuntime().addShutdownHook(new Termination());
			// commence the execution
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
	private void askGitPath() {
		gitPath = new AskInputPath("\nSpecify the path for the text document, containing links and their corresponding number of commits to download. State the folders after: ").getInputPath();
	}
	// reading the input file, doing auto-detections
	private void preProcessing() { // Pre-processing includes the reading of links and quantity of commits
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
		failureToProceed = !(new IdentifyPMD().isPmdFound()); // PMD must be found, otherwise downloaded manually
		// check the previous status of execution
		if (!failureToProceed) {
			previousStatus = new PreviousStatus(links, gitPath);
		}
	}
	// the main analysis of the script
	// Analysing includes downloading the links pre-processed, and getting the unique PMD alerts
	private void analysing() {
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
	private void savingData() {
		System.out.println("\nSaving...");
		SaveGitCommits saveGitCommits = new SaveGitCommits(records, gitPath);
//        saveGitCommits.createNewRecords();
		if (previousStatus.isAnswered() && !previousStatus.isRestart()) {
			saveGitCommits.checkRecordsExist();
		} else {
			saveGitCommits.createNewRecords();
		}
	}
	// a class that extends thread that is to be called when program is exiting
	class Termination extends Thread {
		public void run() {
//			System.out.println("Execution completed: " + executionComplete);
			if (!executionComplete) {
				try {
					analysedLink.saveData();
					// adding the last project being analysed
					if (analysedLink.isProjectExists()) {
						// in case this is the first link to be analysed
						records.add(analysedLink.getRecord());
						System.out.println("*** The latest project is added.");
					}
				} catch (Exception e ) {
					System.out.println("*** Main: Error returning projectExists.");
					e.printStackTrace();
				}
			}
			// saving to Excel
			if (records.size() > 0) {
				savingData();
				// final information printout
				System.out.println("\n!!! Projects analysed [total number of commits]: ");
				for (Records record:records) {
					System.out.println(" - " + record.getProjectName()
							+ " [" + record.getUniqueAlerts_count().size() + "]");
//                    if (record.getProjectName().equals(records.getLast().getProjectName())) {
//                        System.out.println(". ");
//                    } else {
//                        System.out.println(", ");
//                    }
				}
				System.out.println("Saved in Excel file \"" + TemporaryFiles.analysing.OUTPUT_ONE.getString() + "\""
						+ " in repository \"" + gitPath + "\".");
			}
			// print that the program is closing
			System.out.println("\nDone.");
		}
	}
}
