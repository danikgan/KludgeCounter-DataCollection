import Features.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // TODO a file, which contains pmdVersion, and it updated by Tools

    static List<Features.entries> entries = new LinkedList<>();
    static String gitPath = "/Users/" + System.getProperty("user.name") + "/";
    static List<String> links = new LinkedList<>();

    public static void main(String[] args) {
        //Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome!\n");

        int result = useLinks();
//        for (Features.entries entry:entries) {
//            System.out.println("\nProject name: " + entry.getProject_name());
//            entry.showData();
//        }
        if (result == -1) {
            System.out.println("*** No links were found.\n" +
                    "*** Check the path specified.");
        } else {
            System.out.println("\nRecording...");
            recordGitOperations();
        }

        System.out.println("\nDone.");
    }

    private static int useLinks() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Set the path to which Git repository(ies) will be downloaded, and where the file \'links.txt\' is contained.\n" +
                "Specify the path after: " + gitPath);
        gitPath += scanner.nextLine();
//        gitPath += "Desktop"; // part of debugging

        links = new findLinks().findLinks(gitPath);
        if (links == null) {
            return -1;
        } else {
            System.out.println("To which YEAR to backtrace? (2014 or 2018)");
        int date_year = scanner.nextInt();
            System.out.println("To which MONTH to backtrace? (4 or 10)");
        int date_month = scanner.nextInt();
//            int date_year = 2018;
//            int date_month = 8;

            for (String link:links) {
                gitOperations(date_year, date_month, link);
            }

            return 0;
        }
    }

    private static void gitOperations(int date_year, int date_month, String gitLink) {

//        // This asks user for the link of Git repository
//        System.out.println("Which git repository would you like to download?\n" +
//                "Enter the link now:");
////        String gitLink = scanner.nextLine();
//        String gitLink = "https://git-wip-us.apache.org/repos/asf/ant-antlibs-dotnet.git"; // part of debugging

        // Backtrace to the specified year

        useGitRepository gitRepository = new useGitRepository(gitPath, gitLink, date_year, date_month);
        System.out.println("\nDownloading...");
        int result = gitRepository.downloadMasterRepository(); // does not add the entry as it is not analysed yet
        if (result != -1) {
            System.out.println("Backtracing...\n");
            entries.add(gitRepository.backtraceGitRepository());
        } else {
            System.out.println("*** Exiting the current process without backtracing...");
        }
        //gitRepository.repositoryName();
        //gitRepository.updateGitBranch(2018, 01, gitPath + "/ant");
    }

    private static void recordGitOperations() {
        try {
            new recordEntries(entries, gitPath);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
