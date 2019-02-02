import first.utilities.DeleteFiles;
import first.utilities.TemporaryFiles;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    // Main method
    public static void main(String[] args) {
        System.out.println("*** Script v2.3.3");
        menu();
    }
    // asking user how to proceed
    private static void menu() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nSelect one of the operations:" +
                "\n1. Use \"links.txt\" to analyse Git using PMD." +
                "\n2. Use \"records.xlsx\" for Bugzilla." +
                "\n3. Delete all temporary files. (USED FOR TESTING)" +
                "\nPress \'0\' for exit.");
        try {
            int answer = scan.nextInt();
            switch (answer) {
                case 1:
                    new GitPMDAnalyser();
                    break;
                case 2:
                    System.out.println("This option isn't ready yet.");
//                    new FindBugs();
                    menu();
                    break;
                case 3:
                    System.out.println("Deleting temporary files...");
                    deleteTemporaryFiles();
                    menu();
                    break;
                case 0:
                    System.out.println("\nBye-bye!");
                    break;
                default:
                    System.out.println("Wrong input, try again.");
                    menu();
            }
        } catch (InputMismatchException e) {
            System.out.println("Wrong input, try again. Must be of integer type.");
            menu();
        }
    }
    // delete temporary files within the script repository, used mostly for testing
    private static void deleteTemporaryFiles() {
        // part of first operation
        String file = TemporaryFiles.analysing.GITDIFF.getString();
        new DeleteFiles(new File(file));
        file = TemporaryFiles.analysing.PMDALERTS.getString();
        new DeleteFiles(new File(file));
        file = TemporaryFiles.analysing.COMMITSLIST.getString();
        new DeleteFiles(new File(file));
        file = TemporaryFiles.analysing.IDENTIFYPMD.getString();
        new DeleteFiles(new File(file));
        file = TemporaryFiles.analysing.EXECUTIONSTATUS.getString();
        new DeleteFiles(new File(file));
        // part of the second operation
        file = TemporaryFiles.analysing.CHECKINPUT.getString();
        new DeleteFiles(new File(file));
        System.out.println("Deleted.");
    }
}
