package first.utilities;

public class TemporaryFiles {
    public enum analysing {
        // part of the first operation
        GITDIFF("git-diff.txt"),
        PMDALERTS("pmd-alerts.txt"),
        COMMITSLIST("commits-list.txt"),
        IDENTIFYPMD("files-list.txt"),
        EXECUTIONSTATUS("execution-status.txt"),
        // do not delete this
        OUTPUT("records.xlsx"),
        // part of the second operation
        CHECKINPUT("checking-input.txt");

        private String string;
        analysing(String string) {
            this.string = string;
        }
        public String getString() {
            return string;
        }
    }
}
