package second.process;

import second.process.data.Tokens;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BugzillaChecker {
    public BugzillaChecker(LinkedList<Tokens> listTokens) {
        // saving bug ids for records.xlsx
        int bug_id = 0;
        for (Tokens tokens:listTokens) {
            for (String token:tokens.getTokens()) {
                // if the token matched the regex pattern, then add to potential Bugzilla BugsHistory
                if (checkAgainstRegex(token)) {
                    tokens.addBugzillaBugs(token);
                    tokens.addBugzillaBugs_id(bug_id);
                }
            }
            bug_id++;
        }
    }

    private boolean checkAgainstRegex(String token) {
        String pattern = "\\b\\d\\d\\d\\d\\d\\b"; // 5 digit token required
        Pattern r = Pattern.compile(pattern);
        // matching against input
        Matcher matcher = r.matcher(token);
//        System.out.println("Yes or no: " + matcher.lookingAt());
        return matcher.lookingAt();
    }
}
