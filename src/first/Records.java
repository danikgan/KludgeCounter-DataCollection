package first;

import java.util.LinkedList;

public class Records {
    private String projectName;
    private LinkedList<String> authorName = new LinkedList<>();
    private LinkedList<String> commitNumber = new LinkedList<>();
    private LinkedList<String> commitDate = new LinkedList<>();
    private LinkedList<String> commitComment = new LinkedList<>();
    private LinkedList<String> uniqueAlerts = new LinkedList<>();
    private LinkedList<Integer> uniqueAlerts_count = new LinkedList<>();

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LinkedList<String> getAuthorName() {
        return authorName;
    }

    public void setAuthorName(LinkedList<String> authorName) {
        this.authorName = authorName;
    }

    public LinkedList<String> getCommitNumber() {
        return commitNumber;
    }

    public void setCommitNumber(LinkedList<String> commitNumber) {
        this.commitNumber = commitNumber;
    }

    public LinkedList<String> getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(LinkedList<String> commitDate) {
        this.commitDate = commitDate;
    }

    public LinkedList<String> getCommitComment() {
        return commitComment;
    }

    public void setCommitComment(LinkedList<String> commitComment) {
        this.commitComment = commitComment;
    }

    public LinkedList<String> getUniqueAlerts() {
        return uniqueAlerts;
    }

    public void setUniqueAlerts(LinkedList<String> uniqueAlerts) {
        this.uniqueAlerts = uniqueAlerts;
    }

    public LinkedList<Integer> getUniqueAlerts_count() {
        return uniqueAlerts_count;
    }

    public void setUniqueAlerts_count(LinkedList<Integer> uniqueAlerts_count) {
        this.uniqueAlerts_count = uniqueAlerts_count;
    }

    @Override
    public String toString() {
        return "\nRecords{" +
                "projectName='" + projectName + '\'' +
                ", authorName=" + authorName +
                ", commitNumber=" + commitNumber +
                ", commitDate=" + commitDate +
                ", commitComment=" + commitComment +
                ", uniqueAlerts=" + uniqueAlerts +
                ", uniqueAlerts_count=" + uniqueAlerts_count +
                '}';
    }
}
