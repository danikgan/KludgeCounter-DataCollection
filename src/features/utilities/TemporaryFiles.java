package features.utilities;

public class TemporaryFiles {
    public enum analysing {
        GITDIFF("git-diff.txt"),
        PMDALERTS("pmd-alerts.txt"),
        COMMITSLIST("commits-list.txt"),
        IDENTIFYPMD("files-list.txt"),
        EXECUTIONSTATUS("execution-status.txt"),
//        OUTPUTCHECK("records-TEMP.txt"),
        OUTPUT("records.xlsx");

        private String string;
        analysing(String string) {
            this.string = string;
        }
        public String getString() {
            return string;
        }
    }
}
