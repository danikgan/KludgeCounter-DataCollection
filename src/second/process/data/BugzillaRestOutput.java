package second.process.data;

import scala.Int;

import java.util.LinkedList;

public class BugzillaRestOutput {
    // data to store
    private String project;
    // additional
    private LinkedList<String> added = new LinkedList<>();
    private LinkedList<String> field_name = new LinkedList<>();
    private LinkedList<String> removed = new LinkedList<>();
    private LinkedList<Integer> changes = new LinkedList<>();
    private String who;
    private String when;
    private String id;
    public BugzillaRestOutput() { }
    // getters
    public LinkedList<String> getAdded() {
        return added;
    }
    public LinkedList<String> getField_name() {
        return field_name;
    }
    public LinkedList<String> getRemoved() {
        return removed;
    }
    public LinkedList<Integer> getChanges() {
        return changes;
    }
    // adders
    public void addAdded(String added) {
        this.added.add(added);
    }
    public void addField_name(String field_name) {
        this.field_name.add(field_name);
    }
    public void addRemoved(String removed) {
        this.removed.add(removed);
    }
    public void addChanges(Integer changes) {
        this.changes.add(changes);
    }
    // single values
    public String getWho() {
        return who;
    }
    public void setWho(String who) {
        this.who = who;
    }
    public String getWhen() {
        return when;
    }
    public void setWhen(String when) {
        this.when = when;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProject() {
        return project;
    }
    public void setProject(String project) {
        this.project = project;
    }
}
