package first.analyse;

import first.utilities.TemporaryFiles;

import java.io.*;

public class ApplyPMD {
    private static String versionPMD = "6.10.0";

    public static void setVersionPMD(String versionPMD) {
        System.out.println("PMD version: " + versionPMD);
        ApplyPMD.versionPMD = versionPMD;
    }

    public ApplyPMD() { }

    public ApplyPMD(String projectName, String gitPath) {
        //String command = "pmd-bin-6.10.0/bin/run.sh pmd -d projectName -R rulesets/java/quickstart.xml";
        Process process = null;
        try {
            String command = "pmd-bin-" +
                    versionPMD +
                    "/bin/run.sh pmd -d " +
                    gitPath + "/" +
                    projectName +
                    " -R rulesets/java/quickstart.xml";
//            System.out.println(command);
            process = (Process) Runtime.getRuntime().exec(command);

            InputStream inputStream = process.getInputStream();
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;

            BufferedWriter writer = new BufferedWriter(new FileWriter(TemporaryFiles.analysing.PMDALERTS.getString()));
            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line); // the output is here
                writer.write(line);
                writer.newLine();
            }

            process.getInputStream().close();
            writer.close();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            System.out.println("*** ApplyPMD: Error applying PMD on: " + projectName);
            e.printStackTrace();
        }
        finally { if (process != null) process.destroy(); }
    }
}
