package Features;

import java.util.LinkedList;
import java.util.List;

public class entries {
    private String project_name;
    private List<String> date = new LinkedList<>();
    private List<Integer> count = new LinkedList<>();
    //private List<String> errors = new LinkedList<>();


    public entries() {}

//    public entries(String project_name, List<String> date, List<Integer> count) {
//        this.project_name = project_name;
//        this.date = date;
//        this.count = count;
//
//        //prepend
//        //or
//        //append
//    }


    public String getProject_name() {
        return project_name;
    }

    public List<String> getDate() {
        return date;
    }

    public List<Integer> getCount() {
        return count;
    }

    public void showData() {
        for (int i = 0; i < date.size(); i++) {
            System.out.println(date.get(i) + ": " + count.get(i));
        }
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setDate(String date) {
        this.date.add(date);
    }

    public void setCount(Integer count) {
        this.count.add(count);
    }
}
