package second.process.data;

import java.util.LinkedList;

/** Collect information per project */
public class ProjectsData {
    // information collected from the input file
    private String project;
    private LinkedList<String> comments = new LinkedList<>();
    private LinkedList<String> commitNumbers = new LinkedList<>();
    // information got from the Bugzilla website
//    private String bugID;
    // etc...
    // constructor
    public ProjectsData(String project) {
        this.project = project;
    }
    // adders: might be need instead of the setters
    public void addCommitNumber(String commitNumber) {
        this.commitNumbers.add(commitNumber);
    }
    public void addComment(String comment) {
        this.comments.add(comment);
    }
    // getters
    public String getProject() {
        return project;
    }
    public LinkedList<String> getComments() {
        return comments;
    }
    public LinkedList<String> getCommitNumbers() {
        return commitNumbers;
    }
}
