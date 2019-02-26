package second.process.data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Tokens {
    private LinkedList<String> tokens = new LinkedList<>();
    private LinkedList<String> bugzillaBugs = new LinkedList<>(); // potential Bugzilla bugs
    private LinkedList<Integer> bugzillaBugs_id = new LinkedList<>(); // ids for the records.xlsx
    private LinkedList<String> bugzillaReport = new LinkedList<>(); // output from the website
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
    public LinkedList<String> getBugzillaReport() { return bugzillaReport; }
    // adders
    public void addToken(String token) {
        this.tokens.add(token);
    }
    public void addBugzillaBugs(String bugzillaBugs) {
        this.bugzillaBugs.add(bugzillaBugs);
    }
    public void addBugzillaBugs_id(Integer bugzillaBugs_id) { this.bugzillaBugs_id.add(bugzillaBugs_id); }
    public void addBugzillaReport(String bugzillaReport) {
        this.bugzillaReport.add(bugzillaReport);
    }
}
