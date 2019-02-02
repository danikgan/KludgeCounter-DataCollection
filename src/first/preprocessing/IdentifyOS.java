package first.preprocessing;

import java.util.Scanner;

public class IdentifyOS {
    String gitPath = "";
    public IdentifyOS() {
        String operatingSystem_autoIdentifier = System.getProperty("os.name");
        switch (operatingSystem_autoIdentifier.substring(0,3)) {
            case "Mac": gitPath = "/Users/" + System.getProperty("user.name") + "/"; break;
//            case "Lin": gitPath += "/home/" + System.getProperty("user.name") + "/"; break;
            default: askOS();
        }
    }
    // asking about operating system
    private void askOS() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Are you on MacOS (M), Linux (L) or Windows (W)?" +
                "\n(E) for else:" +
                "\t(Hint: your operating system is " + System.getProperty("os.name") + ".)");
        char operatingSystem = scan.nextLine().charAt(0);
        switch (operatingSystem) {
            case 'M': case 'm': gitPath = "/Users/" + System.getProperty("user.name") + "/"; break;
            case 'L': case 'l': gitPath = "/home/" + System.getProperty("user.name") + "/"; break;
            case 'W': case 'w': gitPath = "/"; break;
            default: gitPath = "/";
        }
    }
    public String getGitPath() {
        return gitPath;
    }
}
