package second.process;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import second.process.data.BugzillaOverview;
import second.process.data.Tokens;

import java.util.LinkedList;

public class RetrieveBugzillaDataOverview {
	// initialise the linked list to store values
	private LinkedList<BugzillaOverview> bugzillaOverviews = new LinkedList<>();
	public RetrieveBugzillaDataOverview(LinkedList<Tokens> listTokens, String project) {
		// processing
		for (Tokens tokens:listTokens) {
			for (String bugzillaReport : tokens.getBugzillaReport_overview()) {
//				System.out.println("ID: " + tokens.getBugzillaBugs());
//				System.out.println("R: " + bugzillaReport);
				bugzillaOverviews.add(analyseJSON(bugzillaReport, project)); // saving the one entry
			}
		}
	}
	// saving JSON data for each Bugzilla Overview report
	private BugzillaOverview analyseJSON(String bugzillaReport, String project) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		// allocation of information
		BugsOverview bugsOverview = gson.fromJson(bugzillaReport, BugsOverview.class);
		// saving the collected data
		bugsOverview.getBugs().getFirst().setProject(project);
		return bugsOverview.getBugs().getFirst();
	}
	public LinkedList<BugzillaOverview> getBugzillaOverviews() {
		return bugzillaOverviews;
	}
}

class BugsOverview {
	private LinkedList<BugzillaOverview> bugs;
	LinkedList<BugzillaOverview> getBugs() {
		return bugs;
	}
	@Override
	public String toString() {
		return "BugsOverview{" +
				"bugs=" + bugs +
				'}';
	}
}
