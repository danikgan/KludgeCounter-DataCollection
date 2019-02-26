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
    private LinkedList<String> who = new LinkedList<>();
    private LinkedList<String> when = new LinkedList<>();
    private Integer id;
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
    // higher level
    public LinkedList<String> getWho() {
        return who;
    }
    public LinkedList<String> getWhen() {
        return when;
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
    // higher level
    public void addWho(String who) {
        this.who.add(who);
    }
    public void addWhen(String when) {
        this.when.add(when);
    }
    // single values
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getProject() {
        return project;
    }
    public void setProject(String project) {
        this.project = project;
    }
}
