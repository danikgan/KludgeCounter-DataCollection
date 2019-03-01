package common;

import first.preprocessing.IdentifyOS;

import java.util.Scanner;

public class AskInputPath {
    String inputPath;
    public AskInputPath(String asking) {
        Scanner scan = new Scanner(System.in);
        // detecting the operating system
        IdentifyOS identifyOS = new IdentifyOS();
        inputPath = identifyOS.getGitPath();
        // asking the user for the direction
        System.out.println(asking);
        System.out.print(inputPath);
//        inputPath += scan.nextLine(); // adding to the string produced by the identifyOS
        String userInput = scan.nextLine(); // adding to the string produced by the identifyOS
        // checking there is no extra "/" in the user answer
        while (userInput.charAt(userInput.length()-1) == '/') {
            userInput = userInput.substring(0, userInput.length()-1);
        }
        inputPath += userInput;
    }
    //
    public String getInputPath() {
        return inputPath;
    }
}
