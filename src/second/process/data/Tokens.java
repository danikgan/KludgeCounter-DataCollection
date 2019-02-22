package second.process.data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Tokens {
    private LinkedList<String> tokens = new LinkedList<>();
    private LinkedList<String> bugzillaBugs = new LinkedList<>(); // potential Bugzilla bugs
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
    public LinkedList<String> getBugzillaReport() {
        // remove duplicate data
        Set<String> set = new HashSet<>(bugzillaReport);
        bugzillaReport = new LinkedList<>(set);
        return bugzillaReport;
    }
    // adders
    public void addToken(String token) {
        this.tokens.add(token);
    }
    public void addBugzillaBugs(String bugzillaBugs) {
        this.bugzillaBugs.add(bugzillaBugs);
    }
    public void addBugzillaReport(String bugzillaReport) {
        this.bugzillaReport.add(bugzillaReport);
    }
}
