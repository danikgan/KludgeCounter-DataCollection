package Features;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class useGitRepository {
    // these are updated anyway
    private int currentYear = 2018;
    private int currentMonth = 11;

    // these are specified by the user
    private String gitPath;
    private String gitLink;
    private int date_month;
    private int date_year;

    // derived automatically
    private String folderName = null;

    public useGitRepository(String gitPath, String gitLink, int date_year, int date_month) {
        this.gitPath = gitPath;
        this.gitLink = gitLink.trim(); // add trim to avoid issues with spaces
        this.date_month = date_month;
        this.date_year = date_year;

        currentDate();
    }

    public int downloadMasterRepository() {

        Process process = null;
        try {
            String[] command = {"git", "clone", gitLink};
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(gitPath));
            process = processBuilder.start();

            //consoleOutput(process);

            process.getOutputStream().close(); // close stdin of child
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            System.out.println("*** Error downloading from the specified git link: " + gitLink +".\n");

            return -1;
        }
        finally { if (process != null) { process.destroy(); } }

        // checking the repository exists
        folderName = String.valueOf(repositoryName());
        File f = new File(gitPath + "/" + folderName);
//        System.out.println("FN: " + folderName);
        if (f.exists() && f.isDirectory()) {
            return 0;
        } else {
            System.out.println("*** The git link does not exist and was not downloaded.\n" +
                    "Please, check this link: " + gitLink + ".");
            return -1;
        }
    }

    public entries backtraceGitRepository() {
        /** Save your analysis
         *
         * Initiate a temporary entry here. Then, add it to the overall list in the main.
         * The properties of the entry can be edited in the so-called class.
         */
        entries entry = new entries();


        /** Run the backtracing.
         *
         * Record the time and count (possibly, errors) at each iteration.
         */
        System.out.println("Project name: " + repositoryName());
        String fullPath = gitPath + "/" + folderName;
        //System.out.println("full: " + fullPath + " name: " + folderName);

        entry.setProject_name(folderName); // saves the name of the project

        for (int i = date_year; i <= currentYear; i++) {
            int j = 1;
            if (date_year == i){
                j = date_month;
            }


            while (j <= 12) {
                String date = updateGitBranch(i,j, fullPath);
                int count = new runPMD().runPMD(fullPath);

                // save the info collected
                entry.setDate(date);
                entry.setCount(count);

                if (i == currentYear && j == currentMonth) {
                    System.out.println("Finishing...");
                    break;
                }

                j++;
            }
        }

        return entry;
    }

    private String updateGitBranch(int temp_year, int temp_month, String fullPath) {

        //System.out.println("full: " + fullPath);
        String command = "git checkout `git rev-list -n 1 --before=\"";
        command += Integer.toString(temp_year);
        command += "-";
        command += Integer.toString(temp_month);
        command += "-01 12:00\" master`";
        //System.out.println("Full command: " + command);
        //System.out.println("Full path: " + fullPath);

        Process process = null;
        try {
            String[] execute = {"bash", "-c", command};

            ProcessBuilder processBuilder = new ProcessBuilder(execute);
            processBuilder.directory(new File(fullPath));
            process = processBuilder.start();

            //consoleOutput(process);

            process.getOutputStream().close(); // close stdin of child
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            System.out.println("*** UpdateGitBranch is causing an error.");
        }
        finally { if (process != null) { process.destroy(); } }

        // Run PMD on the update repository
        System.out.print("@" + temp_month + "/" + temp_year + ": ");
        return temp_month + "/" + temp_year;
    }

    // This finds the name of the downloaded repository, so that there's no need to specify manually
    private String repositoryName () {
        int gitLink_length = gitLink.length();
        StringBuilder folderName = new StringBuilder();

        for (int i = gitLink_length-1; i > 0; i--) {
            char gitLink_char = gitLink.charAt(i);

            if (gitLink_char == '/') { break; }
            else { folderName.insert(0, gitLink_char); }

            // remove the .git at the end
            String temp_gitLink_comparison = folderName.toString();
            if (temp_gitLink_comparison.equals(".git")) {
                folderName.delete(0,4);
                //System.out.println("Deleted: " + folderName + ".");
            }
        }

        //System.out.println("Folder name: " + String.valueOf(folderName));
        return String.valueOf(folderName);
    }

    private void consoleOutput(Process process) throws IOException {
        InputStream processStdOutput = process.getInputStream();
        Reader r = new InputStreamReader(processStdOutput);
        BufferedReader br = new BufferedReader(r);
        String line; while ((line = br.readLine()) != null) {
            System.out.println("\t" + line);
        }
    }

    private void currentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM");
        LocalDateTime now = LocalDateTime.now();
        String temp_date = dtf.format(now);

        String temp_year = String.valueOf(temp_date.charAt(0)) + String.valueOf(temp_date.charAt(1)) + String.valueOf(temp_date.charAt(2)) + String.valueOf(temp_date.charAt(3));
        currentYear = Integer.parseInt(temp_year); // System.out.println("Year: " + currentYear);
        String temp_month = String.valueOf(temp_date.charAt(5)) + String.valueOf(temp_date.charAt(6));
        currentMonth = Integer.parseInt(temp_month); // System.out.println("Month: " + currentMonth);
    }
}
