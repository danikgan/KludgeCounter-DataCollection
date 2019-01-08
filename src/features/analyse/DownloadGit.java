package features.analyse;

import java.io.File;
import java.io.*;
//import org.apache.commons.io.FileUtils;

/** Downloads git repositories.
 *
 * 1. Downloads the git repository at the link.
 * 2. Checks the download is successful.
 */

public class DownloadGit {
    private String projectName = "";

    private String link = "";

    public String DownloadGit(String link, String gitPath) { // download following the git link
        System.out.println("Downloading: " + link);
        this.link = link;

        Process process = null;
        try {
            String[] command = {"git", "clone", link};
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(gitPath));
            process = processBuilder.start();

//            consoleOutput(process);

            // close all buffers
//            process.getInputStream().close();
//            process.getOutputStream().close();
//            process.getErrorStream().close();
            process.waitFor();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            System.out.println("*** DownloadGit: Error downloading from this git repository: " + link +".");

            return null;
        } finally { if (process != null) { process.destroy(); } }

        // checking the repository exists
        projectName = String.valueOf(findRepositoryName());
        File f = new File(gitPath + "/" + projectName);
        if (f.exists() && f.isDirectory()) {
            return projectName;
        } else {
            System.out.println("*** DownloadGit: The git link does not exist and was not downloaded.\n" +
                    "Please, check this link: " + link + ".");
            return null;
        }
    }

    private String findRepositoryName() { // This finds the name of the downloaded repository, so that there's no need to specify manually
        int gitLink_length = link.length();
        StringBuilder folderName = new StringBuilder();

        for (int i = gitLink_length-1; i > 0; i--) {
            char gitLink_char = link.charAt(i);

            if (gitLink_char == '/') { break; }
            else { folderName.insert(0, gitLink_char); }

            // remove the .git at the end
            String temp_gitLink_comparison = folderName.toString();
            if (temp_gitLink_comparison.equals(".git")) {
                folderName.delete(0,4);
                //System.out.println("Deleted: " + findRepositoryName + ".");
            }
        }

        //System.out.println("Folder name: " + String.valueOf(findRepositoryName));
        return String.valueOf(folderName);
    }
}
