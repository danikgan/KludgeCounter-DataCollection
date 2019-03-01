package common;

public class TemporaryFiles {
    public enum analysing {
        // part of the first operation
        GIT_DIFF("git-diff.txt"),
        PMD_ALERTS("pmd-alerts.txt"),
        COMMITS_LIST("commits-list.txt"),
        IDENTIFY_PMD("files-list.txt"),
        EXECUTION_STATUS("execution-status.txt"),
        // part of the second operation
        CHECK_INPUT("checking-input.txt"),
        // main output
        OUTPUT_ONE("git-commits.xlsx"),
        OUTPUT_TWO("bugzilla-history.xlsx"),
        OUTPUT_THREE("bugzilla-overview.xlsx");
        // getters
        private String string;
        analysing(String string) {
            this.string = string;
        }
        public String getString() {
            return string;
        }
    }
}
