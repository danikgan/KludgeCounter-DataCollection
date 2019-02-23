package second.process;

import second.process.data.BugzillaRestOutput;
import second.process.data.Tokens;

import java.util.LinkedList;

/** This class handles the output received from REST
 *
 * Done by tokenising the input.
 */
public class RetrieveBugzillaData {
    // the main returned information
    private LinkedList<BugzillaRestOutput> bugzillaRestOutputs = new LinkedList<>();
    // used in processing
    private BugzillaRestOutput bugzillaRestOutput;
    int changesID = 0;
    // constructor
    public RetrieveBugzillaData(LinkedList<Tokens> listTokens, String project) {
        // commence the parsing!
        for (Tokens tokens:listTokens) {
            for (String bugzillaReport:tokens.getBugzillaReport()) {
                System.out.println("ID: " + tokens.getBugzillaBugs());
                // initialise new for each report
                bugzillaRestOutput = new BugzillaRestOutput();
                // save project name
                bugzillaRestOutput.setProject(project);
                // retrieve data for this instance
                StringBuilder stringBuilder = new StringBuilder(bugzillaReport);
                changesID = 0;
                retrieveData(stringBuilder);
                // saving the analysed entry
                bugzillaRestOutputs.add(bugzillaRestOutput);
            }
        }
    }
    // parsing
    private void retrieveData(StringBuilder stringBuilder) {
        // used for while loops and if-else
        int indexOfHistory = stringBuilder.indexOf("history");
        int indexOfID = stringBuilder.indexOf("id");
        // needed for termination of "]"
        char temp_char = stringBuilder.toString().charAt(stringBuilder.indexOf("\"")+4);
        int temp_char_int = stringBuilder.toString().indexOf("\"");
        // starts the analysis
        if (indexOfHistory < indexOfID) { // history is closer
            stringBuilder.delete(0, stringBuilder.indexOf("history"));
            // getting the history
            while (indexOfHistory < indexOfID) {
                stringBuilder = new StringBuilder(
                        keepTrackOfChangesID(stringBuilder, false));
                // update the values
                indexOfHistory = stringBuilder.indexOf("changes"); // because history was removed from the StringBuilder
                indexOfID = stringBuilder.indexOf("id");
                if (indexOfHistory == -1) { indexOfHistory = 2147483647; }
            }
            stringBuilder = new StringBuilder(getID(stringBuilder)); // getting the id
        } else if (indexOfHistory > indexOfID) { // id is closer
            stringBuilder = new StringBuilder(getID(stringBuilder)); // getting the id
            stringBuilder.delete(0, stringBuilder.indexOf("history"));
            // getting the history
            while (temp_char != ']') { // || stringBuilder.toString().equals("\":[]}]}")
//                System.out.println("L2 " + temp_char);
                if (stringBuilder.toString().length() > temp_char_int+5) {
//                    // checking for: value, ..."alias"=".."] - after id
                    if (stringBuilder.toString().substring(temp_char_int+1, temp_char_int+6).equals("alias")) {
//                        System.out.println("ALIAS1");
                        break; // against alias
                    }
//                    // checking for: value", ..."alias"=".."] - after history
                    if (stringBuilder.toString().length() > temp_char_int+10) {
                        if (stringBuilder.toString().substring(temp_char_int+5, temp_char_int+10).equals("alias")) {
//                            System.out.println("ALIAS2");
                            break; // against alias
                        }
                    }
                    // if break did not happen, continue
                    stringBuilder = new StringBuilder(keepTrackOfChangesID(stringBuilder, true));
                }
                // update the values
                temp_char = stringBuilder.toString().charAt(stringBuilder.indexOf("\"")+4);
                temp_char_int = stringBuilder.toString().indexOf("\"");
            }
        }
    }
    // getting the id
    private StringBuilder getID(StringBuilder stringBuilder) {
        // getting the id
        stringBuilder.delete(0, stringBuilder.indexOf("id"));
        stringBuilder.delete(0, stringBuilder.indexOf(":")+1); // removing till the value
        bugzillaRestOutput.setId(stringBuilder.toString().substring(0, 5)); // getting 5-digit id value
//        System.out.println("ID: " + bugzillaRestOutput.getId());
        return stringBuilder;
    }
    // keeping track of changes ID
    private StringBuilder keepTrackOfChangesID(StringBuilder stringBuilder, boolean loop) {
        if (stringBuilder.indexOf("[")+1 == stringBuilder.indexOf("]")) {
            // history is empty
//            System.out.println("History is empty.");
            if (loop) {
                // without this, string builder is ":[]}]}
                // thus, we remove till []}]}
                // this give index of -1 for checking on "
                // -1 +4 will equal to ]
                stringBuilder.delete(0, stringBuilder.indexOf("\"")+2); // to exit the loop 2
//                System.out.println("SB: " + stringBuilder);
            } else {
                stringBuilder.delete(0, stringBuilder.indexOf("\"")); // to exit the loop 1
            }
        } else {
            changesID++; // next ID
            stringBuilder = new StringBuilder(insideOfHistory(stringBuilder));
            stringBuilder = new StringBuilder(insideOfHistory(stringBuilder));
            stringBuilder = new StringBuilder(insideOfHistory(stringBuilder));
        }
        return stringBuilder;
    }
    // checking with the "history" block
    private StringBuilder insideOfHistory(StringBuilder stringBuilder) {
        // all three types of insides
        int indexOfWho = stringBuilder.indexOf("who");
        int indexOfWhen = stringBuilder.indexOf("when");
        int indexOfChanges = stringBuilder.indexOf("changes");
        // check none values are "-1"
        if (indexOfWho == -1) { indexOfWho = 2147483647; }
        if (indexOfWhen == -1) { indexOfWhen = 2147483647; }
        if (indexOfChanges == -1) { indexOfChanges = 2147483647; }
        // continue the analysis
//        System.out.println("O: " + indexOfWho + " E: " + indexOfWhen + " C: " + indexOfChanges);
        if (indexOfChanges < indexOfWhen && indexOfChanges < indexOfWho) { // history the closest
            stringBuilder.delete(0, stringBuilder.indexOf("changes"));
            stringBuilder = new StringBuilder(insideOfChanges(stringBuilder));
        } else if (indexOfWhen < indexOfChanges && indexOfWhen < indexOfWho) { // when the closest
            stringBuilder.delete(0, stringBuilder.indexOf("when"));
            stringBuilder = new StringBuilder(insideOfWhen(stringBuilder));
        } else if (indexOfWho < indexOfWhen && indexOfWho < indexOfChanges) { // who the closest
            stringBuilder.delete(0, stringBuilder.indexOf("who"));
            stringBuilder = new StringBuilder(insideOfWho(stringBuilder));
        } else {
            System.out.println("*** RetrieveBugzillaData: " +
                    "No \"who\", \"when\" or \"changes\" " +
                    "was found in the report.");
            System.out.println("SB: " + stringBuilder.toString());
        }
        return stringBuilder;
    }
    // inside of "who" block
    private StringBuilder insideOfWho(StringBuilder stringBuilder) {
        if (stringBuilder.toString().substring(0, stringBuilder.indexOf("\"")).equals("who")) {
            // removing till the next value
            stringBuilder.delete(0, stringBuilder.indexOf("\"")+1);
            stringBuilder.delete(0, stringBuilder.indexOf("\"")+1);
            // saving the value
            bugzillaRestOutput.setWho(stringBuilder.toString().substring(0, stringBuilder.indexOf("\"")));
//            System.out.println("WHO: " + bugzillaRestOutput.getWho());
        } else {
            System.out.println("*** RetrieveBugzillaData: Warning, something went wrong with \"who\" block. ");
        }
        return stringBuilder;
    }
    // inside of "when" block
    private StringBuilder insideOfWhen(StringBuilder stringBuilder) {
        if (stringBuilder.toString().substring(0, stringBuilder.indexOf("\"")).equals("when")) {
            // removing till the next value
            stringBuilder.delete(0, stringBuilder.indexOf("\"")+1);
            stringBuilder.delete(0, stringBuilder.indexOf("\"")+1);
            // saving the value
            bugzillaRestOutput.setWhen(stringBuilder.toString().substring(0, stringBuilder.indexOf("\"")));
//            System.out.println("WHEN: " + bugzillaRestOutput.getWhen());
        } else {
            System.out.println("*** RetrieveBugzillaData: Warning, something went wrong with \"when\" block. ");
        }
        return stringBuilder;
    }
    // inside of the "changes" block
    private StringBuilder insideOfChanges(StringBuilder stringBuilder) {
        // checking we are inside the current changes block
        char temp_char = stringBuilder.toString().charAt(stringBuilder.indexOf("\"")+2);
        if (temp_char != ']') {
//            System.out.println("\nP: " + stringBuilder.indexOf("who") + " A: " + stringBuilder.indexOf("added"));
            // save the ID of changes
            bugzillaRestOutput.addChanges(changesID);
//            System.out.println("C: " + bugzillaRestOutput.getChanges().getLast());
            // repeat for "added", "field_name" or "removed", which are unordered
            stringBuilder = new StringBuilder(currentItem(stringBuilder));
            stringBuilder = new StringBuilder(currentItem(stringBuilder));
            stringBuilder = new StringBuilder(currentItem(stringBuilder));
            // repeat until all of one changes are done
            return insideOfChanges(stringBuilder);
        } else {
            return stringBuilder;
        }
    }
    // either of the three insides of "changes", which are unordered
    private StringBuilder currentItem(StringBuilder stringBuilder) {
        stringBuilder.delete(0, stringBuilder.indexOf("\"")+1);
        stringBuilder.delete(0, stringBuilder.indexOf("\"")+1);
        // used in the processing
        String string = stringBuilder.toString().substring(0, stringBuilder.indexOf("\""));
        int answer = 0;
        // checking which type
        switch (string) {
            case "added":       answer = 1;
                                break;
            case "field_name":  answer = 2;
                                break;
            case "removed":     answer = 3;
                                break;
            default:            System.out.println("*** RetrieveBugzillaData: " +
                                        "No \"added\", \"field_name\" or \"removed\" " +
                                        "was found in changes.");
        }
        // continuing with removing these: _"_
        stringBuilder.delete(0, stringBuilder.indexOf("\"")+1);
        stringBuilder.delete(0, stringBuilder.indexOf("\"")+1);
        // setting values to correct types
        if (answer == 1) {
            bugzillaRestOutput.addAdded(stringBuilder.toString().substring(0, stringBuilder.indexOf("\"")));
//            System.out.println("A: " + bugzillaRestOutput.getAdded().getLast());
        } else if (answer == 2) {
            bugzillaRestOutput.addField_name(stringBuilder.toString().substring(0, stringBuilder.indexOf("\"")));
//            System.out.println("F: " + bugzillaRestOutput.getField_name().getLast());
        } else if (answer == 3) {
            bugzillaRestOutput.addRemoved(stringBuilder.toString().substring(0, stringBuilder.indexOf("\"")));
//            System.out.println("R: " + bugzillaRestOutput.getRemoved().getLast());
        }
        return stringBuilder;
    }
    // the main return value
    public LinkedList<BugzillaRestOutput> getBugzillaRestOutputs() {
        return bugzillaRestOutputs;
    }
}
