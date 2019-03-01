package second.process.data;

import java.util.LinkedList;

public class Tokens {
    private LinkedList<String> tokens = new LinkedList<>();
    private LinkedList<String> bugzillaBugs = new LinkedList<>(); // potential Bugzilla bugs
    private LinkedList<Integer> bugzillaBugs_id = new LinkedList<>(); // ids for the records.xlsx
    private LinkedList<String> bugzillaReport_history = new LinkedList<>(); // output from the website
    private LinkedList<String> bugzillaReport_overview = new LinkedList<>(); // output from the website
    // constructor
    public Tokens() { }
    // getters
    public LinkedList<String> getTokens() {
        return tokens;
    }
    public LinkedList<String> getBugzillaBugs() {
        return bugzillaBugs;
    }
    public LinkedList<Integer> getBugzillaBugs_id() { return bugzillaBugs_id; }
    public LinkedList<String> getBugzillaReport_history() { return bugzillaReport_history; }
    public LinkedList<String> getBugzillaReport_overview() { return bugzillaReport_overview; }
    // adders
    public void addToken(String token) {
        this.tokens.add(token);
    }
    public void addBugzillaBugs(String bugzillaBugs) {
        this.bugzillaBugs.add(bugzillaBugs);
    }
    public void addBugzillaBugs_id(Integer bugzillaBugs_id) { this.bugzillaBugs_id.add(bugzillaBugs_id); }
    public void addBugzillaReport_history(String bugzillaReport) {
        this.bugzillaReport_history.add(bugzillaReport);
    }
    public void addBugzillaReport_overview(String bugzillaReport) { this.bugzillaReport_overview.add(bugzillaReport); }
}
