import first.preprocessing.IdentifyOS;
import first.utilities.TemporaryFiles;
import second.preprocessing.CheckInputExists;
import second.process.FindProjects;

import java.util.LinkedList;
import java.util.Scanner;

class FindBugs {
    /** This class is doing (pseudocode):
     *
     * read the records file to get projects from it
     * ask the user which project to analyse for bugs or allow to analyse all
     * get comments of the project
     *      tokenise each comment
     *      for each token, which suits the model (defined)
     *          use GET to download JSON file from Bugzilla
     *          read attributes from the JSON
     *          save
     * save all info collected (record to separate XLSX or the same?)
     */
    FindBugs() {
        String inputPath = askInput(); // getting the path to the input file
        boolean inputFound = new CheckInputExists(inputPath).isResult();
        if (inputFound) {
            // proceed
            System.out.println("Finding existing projects...");
            LinkedList<String> projects = new FindProjects(inputPath).getProjectNames();
            if (projects.size() > 0) {
                askWhichProjectToAnalyse(projects);
            } else {
                System.out.println("*** No projects were found in the inout file.");
            }
        } else {
            System.out.println("*** Input file was not found. Check the path.");
        }
    }

    private String askInput() {    // asking for the path of input
        Scanner scan = new Scanner(System.in);
        // detecting the operating system
        IdentifyOS identifyOS = new IdentifyOS();
        String inputPath = identifyOS.getGitPath();
        // getting the path to the text input file
        System.out.println("\nSpecify the path to the input file, which was the output of the previous operation named as" + TemporaryFiles.analysing.OUTPUT.getString() + ":");
        System.out.printf(inputPath);
        inputPath += scan.nextLine(); // adding to the string produced by the identifyOS
        // returning to the main
        return inputPath;
    }
    private void askWhichProjectToAnalyse(LinkedList<String> projects) {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nIn which project would you want to find bugs in?" +
                "\nAlternatively, state \"all\" to find bugs in all the projects listed.");
        // printing available projects to the user
        for (String project:projects) {
            if (project.equals(projects.getLast())) {
                System.out.print("\"" + project + "\".\n");
            } else {
                System.out.print("\"" + project + "\"" + ", ");
            }
        }
        String answer = scan.nextLine();
        if (projects.contains(answer)){
            System.out.println("Proceeding to finding bugs in " + answer +"...");
        } else if (answer.equals("all") || answer.equals("All") || answer.equals("ALL")) {
            System.out.println("Proceeding to finding bugs in all projects...");
        } else {
            System.out.println("*** The entered project is not listed. Try again.");
            askWhichProjectToAnalyse(projects);
        }
    }
}
