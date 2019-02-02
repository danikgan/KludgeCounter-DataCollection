import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    // Main method
    public static void main(String[] args) {
        System.out.println("Hi there!");

        menu();
    }

    private static void menu() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nSelect one of the operations:" +
                "\n1. Use \"links.txt\" to analyse Git using PMD." +
                "\n2. Use \"records.xlsx\" for Bugzilla." +
                "\nPress \'0\' for exit.");
        try {
            int answer = scan.nextInt();
            switch (answer) {
                case 1:
                    new GitPMDAnalyser();
                    break;
                case 2:
                    System.out.println("This option isn't ready yet.");
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
}
