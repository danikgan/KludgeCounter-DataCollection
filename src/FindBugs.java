import common.AskInputPath;
import common.TemporaryFiles;
import second.preprocessing.CheckInputExists;
import second.process.*;
import second.process.data.BugzillaOverview;
import second.process.data.BugzillaRestOutput;
import second.process.data.ProjectsData;
import second.process.data.Tokens;

import java.util.LinkedList;
import java.util.Scanner;

class FindBugs {
	/** This class is doing (pseudocode):
	 *
	 * read the records file to get projects from it
	 * ask the user which project to analyse for bugs or allow to analyse all
	 * get comments of the project
	 *      tokenise each comment
	 *      for each token, which suits the model (defined)
	 *          use GET to download JSON file from Bugzilla
	 *          read attributes from the JSON
	 *          save
	 * save all info collected (record to separate XLSX or the same?)
	 *
	 * 1. FindProjects
	 * 2. GetProjectInfo -> Tokens
	 * 3. CommentTokenisation -> Tokens
	 * 4. BugzillaChecker -> Tokens
	 * 5. SaveIdentifiedBugs
	 * 6. BugzillaLaunch
	 * 7. RetrieveBugzillaData -> Tokens
	 * 8. RetrieveBugzillaDataOverview
	 * 9. SaveBugzillaHistory
	 */
	FindBugs() {
		String inputPath = askInput(); // getting the path to the input file
		boolean inputFound = new CheckInputExists(inputPath).isResult();
		if (inputFound) {
			// proceed
			System.out.println("Finding existing projects...");
			LinkedList<String> projects = new FindProjects(inputPath).getProjectNames();
			if (projects.size() > 0) {
				// it will execute information retrieval upon answering
				String projectSelected = askWhichProjectToAnalyse(projects); // returns projects' info collected
				// saving returned data
				LinkedList<ProjectsData> projectsData = new LinkedList<>();
				// need to be initialised earlier
				GetProjectInfo getProjectInfo;
				LinkedList<Tokens> listTokens = new LinkedList<>();
				try {
					if (projectSelected.equals("A L L")) {
						getProjectInfo = new GetProjectInfo(inputPath, projects); // all projects
					} else {
						getProjectInfo = new GetProjectInfo(inputPath, projectSelected); // only one project
					}
					// gets additional information from the records.xlsx file
					projectsData = getProjectInfo.getProjectsData();
					// saved data
					LinkedList<BugzillaRestOutput> bugzillaRestOutputs = new LinkedList<>();
					LinkedList<BugzillaOverview> bugzillaOverviews = new LinkedList<>();
					for (ProjectsData projectData:projectsData) {
						System.out.println("\nProject: " + projectData.getProject());
						// tokenise
						listTokens = new CommentTokenisation(projectData).getListTokens();
						// check if good for Bugzilla
						new BugzillaChecker(listTokens);
						System.out.println("Adding identified bugs to "
								+ TemporaryFiles.analysing.OUTPUT_ONE.getString() + "...");
						new SaveIdentifiedBugs(projectData.getProject(), listTokens, inputPath);
						if (!listTokens.isEmpty()) {
							// remove duplicated data
							removeDuplicateData(listTokens);
							// accessing REST and Bugzilla
							System.out.println("Getting URL reports...");
							new BugzillaLaunch(listTokens);
							// retrieving valuable data from the reports + saving project name
							System.out.println("Retrieving history information...");
							bugzillaRestOutputs.addAll(
									new RetrieveBugzillaData(listTokens, projectData.getProject())
											.getBugzillaRestOutputs());
							System.out.println("Retrieving overview information...");
							bugzillaOverviews.addAll(
									new RetrieveBugzillaDataOverview(listTokens, projectData.getProject())
											.getBugzillaOverviews());
						}
						listTokens.clear(); // resetting
					}
					// save what was collected
					System.out.println("\nSaving histories...");
					new SaveBugzillaHistory(bugzillaRestOutputs, inputPath);
					System.out.println("\nSaving overviews...");
					new SaveBugzillaOverview(bugzillaOverviews, inputPath);
					// finishing
					closing(bugzillaRestOutputs, bugzillaOverviews, inputPath, projectsData);
				} catch (Exception e) {
					System.out.println("*** FindBugs: Error processing projects.");
					e.printStackTrace();
				}
			} else {
				System.out.println("*** No projects were found in the input file.");
			}
		} else {
			System.out.println("*** Input file was not found. Check the path.");
		}
	}
	// needed to remove duplicate bugzilla bugs
	private void removeDuplicateData(LinkedList<Tokens> listTokens) {
		LinkedList<String> bugzillaBugs = new LinkedList<>();
		// remove duplicate data
		for (Tokens tokens:listTokens) {
			int index_temp = tokens.getBugzillaBugs().size();
			for (int i = 0; i < index_temp; i++) {
				String bug = tokens.getBugzillaBugs().get(i);
				if (bugzillaBugs.contains(bug)) {
					// removes the duplicate
					tokens.getBugzillaBugs().remove(i);
					// when an item is removed, the list shrinks, thus there are less indices
					index_temp--;
					i--;
				} else {
					// saves unique
					bugzillaBugs.add(bug);
				}
			}
		}
		// clearing for the sake of clearing
		bugzillaBugs.clear();
	}
	// asking for the path of input
	private String askInput() {
		String asking = "\nSpecify the path to the input file, which was the output of the previous operation named as "
				+ TemporaryFiles.analysing.OUTPUT_ONE.getString() + ":";
		return new AskInputPath(asking).getInputPath();
//        return "/Users/danikgan/Desktop";
	}
	// asks the user and returns the selected project(s)
	private String askWhichProjectToAnalyse(LinkedList<String> projects) {
		Scanner scan = new Scanner(System.in);
		System.out.println("\nIn which project would you want to find bugs in?" +
				"\nAlternatively, state \"all\" to find bugs in all the projects listed.\n");
		// printing available projects to the user
		for (String project:projects) {
//            if (project.equals(projects.getLast())) {
//                System.out.print("\"" + project + "\".\n");
//            } else {
//                System.out.print("\"" + project + "\"" + ", ");
//            }
			System.out.print(project + "\n");
		}
		String answer = scan.nextLine().trim();
		if (projects.contains(answer)){
			System.out.println("Proceeding to finding bugs in " + answer +"...");
			// GetProjectInfo(String inputPath, String projectName)
			return answer; // return the project
		} else if (answer.equals("all") || answer.equals("All") || answer.equals("ALL")) {
			System.out.println("Proceeding to finding bugs in all projects...");
			// GetProjectInfo(String inputPath, LinkedList<String> projectNames)
			return "A L L"; // return the projects
		} else {
			System.out.println("*** The entered project is not listed. Try again.");
			return askWhichProjectToAnalyse(projects);
		}
//        System.out.println("Proceeding to finding bugs in all projects...");
//        // GetProjectInfo(String inputPath, LinkedList<String> projectNames)
//        return "A L L"; // return the projects
	}
	// for debugging
	private void printProjects(LinkedList<ProjectsData> projectsData, LinkedList<Tokens> listTokens) {
		for (ProjectsData oneProject:projectsData) {
//            System.out.println("Project: " + oneProject.getProject());
//            for (String comment:oneProject.getComments()) {
//                System.out.println("C: " + comment);
//            }
//            for (String checkout:oneProject.getCommitNumbers()) {
//                System.out.println("E: " + checkout);
//            }
		}
		for (Tokens tokens:listTokens) {
//            System.out.println("Tokens: " + tokens.getTokens());
			System.out.println("Bugs found: " + tokens.getBugzillaBugs());
//            System.out.println("Reports found: " + tokens.getBugzillaReport_history());
		}
	}
	private void closing(LinkedList<BugzillaRestOutput> bugzillaRestOutputs,
						 LinkedList<BugzillaOverview> bugzillaOverviews,
						 String inputPath,
						 LinkedList<ProjectsData> projectsData) {
		System.out.println("\n!!! These projects were analysed: ");
		for (ProjectsData projectData:projectsData) {
			System.out.println("\t - " + projectData.getProject());
		}
		System.out.println("[" + bugzillaOverviews.size() + "] " +
		"Bugzilla overview records were saved in \""
				+ TemporaryFiles.analysing.OUTPUT_THREE.getString() + "\".");
		int counter = 0;
		for (BugzillaRestOutput bugzillaRestOutput : bugzillaRestOutputs) {
			counter += bugzillaRestOutput.getChanges().size();
		}
		System.out.println("[" + counter + "] " +
				"Bugzilla history records were saved in \""
				+ TemporaryFiles.analysing.OUTPUT_TWO.getString() + "\".");
		System.out.println("Saved under \"" + inputPath + "\".");
		System.out.println("\nDone.");
	}
}
