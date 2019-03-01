package second.process;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import second.process.data.BugzillaRestOutput;
import second.process.data.Tokens;

import java.util.LinkedList;
import java.util.List;

/** This class handles the output received from REST
 *
 * Done by tokenising the input.
 */
public class RetrieveBugzillaData {
	// the main returned information
	private LinkedList<BugzillaRestOutput> bugzillaRestOutputs = new LinkedList<>();
	private BugzillaRestOutput bugzillaRestOutput; // used in processing
	// constructor
	public RetrieveBugzillaData(LinkedList<Tokens> listTokens, String project) { // commence the parsing!
		for (Tokens tokens:listTokens) {
			for (String bugzillaReport : tokens.getBugzillaReport_history()) {
//				System.out.println("ID: " + tokens.getBugzillaBugs());
				// initialise new for each report
                bugzillaRestOutput = new BugzillaRestOutput();
                // save project name
                bugzillaRestOutput.setProject(project);
				// running the JSON parser
				retrieveData(bugzillaReport);
                // saving the analysed entry
                bugzillaRestOutputs.add(bugzillaRestOutput);
			}
		}
	}
	// using GSON lib, analyse the JSON report
	private void retrieveData(String bugzillaReport) {
//		System.out.println("BR: " + bugzillaReport);
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		// allocation of information
		BugsHistory bugs = gson.fromJson(bugzillaReport, BugsHistory.class);
		// saving the collected data
		int changes = 0;
		for (insideHistory instanceHistory:bugs.getBugs().get(0).getHistory()) {
			for (insideChanges instanceChanges:instanceHistory.getChanges()) {
				bugzillaRestOutput.addWho(instanceHistory.getWho());
				bugzillaRestOutput.addWhen(instanceHistory.getWhen());
				// inside of history
				bugzillaRestOutput.addAdded(instanceChanges.getAdded());
				bugzillaRestOutput.addField_name(instanceChanges.getField_name());
				bugzillaRestOutput.addRemoved(instanceChanges.getRemoved());
				bugzillaRestOutput.addChanges(changes);
			}
			changes++;
		}
		bugzillaRestOutput.setId(bugs.getBugs().get(0).getId());
	}

	public LinkedList<BugzillaRestOutput> getBugzillaRestOutputs() {
		return bugzillaRestOutputs;
	}
}
// the root
class BugsHistory {
	private List<insideBugs> bugs;
	// getters
	List<insideBugs> getBugs() {
		return bugs;
	}
	@Override
	public String toString() {
		return "BugsHistory{" +
				"BugsHistory=" + bugs +
				'}';
	}
}
// inside of BugsHistory
class insideBugs {
	private Integer id;
	private List<insideHistory> history;
	// getters
	Integer getId() {
		return id;
	}
	List<insideHistory> getHistory() {
		return history;
	}
	@Override
	public String toString() {
		return "insideBugs{" +
				"id=" + id +
				", history='" + history + '\'' +
				'}';
	}
}
// inside of history
class insideHistory {
	private String when;
	private String who;
	private List<insideChanges> changes;
	// getters
	String getWhen() {
		return when;
	}
	String getWho() {
		return who;
	}
	List<insideChanges> getChanges() {
		return changes;
	}
	@Override
	public String toString() {
		return "insideHistory{" +
				"when='" + when + '\'' +
				", who='" + who + '\'' +
				", changes='" + changes + '\'' +
				'}';
	}
}
// inside of changes
class insideChanges {
	private String added;
	private String field_name;
	private String removed;
	// getters
	String getAdded() {
		return added;
	}
	String getField_name() {
		return field_name;
	}
	String getRemoved() {
		return removed;
	}
	@Override
	public String toString() {
		return "insideChanges{" +
				"added='" + added + '\'' +
				", field_name='" + field_name + '\'' +
				", removed='" + removed + '\'' +
				'}';
	}
}