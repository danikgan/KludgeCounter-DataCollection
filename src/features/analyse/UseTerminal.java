package features.analyse;

import java.io.*;
import java.util.Arrays;

public class UseTerminal {
    public UseTerminal(String[] command, String gitPath, String fileName) {
        Process process = null;
        try {
            //String[] command = {"git", "checkout", checkoutNumber};
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(gitPath));
            process = processBuilder.start();

//            System.out.println("Checking out: " + checkoutNumber);
            if (!fileName.equals("")) {
                consoleOutput(process, fileName);
            }

            // close all buffers
//            process.getInputStream().close();
//            process.getOutputStream().close();
//            process.getErrorStream().close();
            process.waitFor();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            System.out.println("*** UseTerminal: Error executing this command: " + Arrays.toString(command) +".\n");
        } finally { if (process != null) { process.destroy(); } }
    }

    private void consoleOutput(Process process, String fileName) throws IOException {
        InputStream processStdOutput = process.getInputStream();
        Reader r = new InputStreamReader(processStdOutput);
        BufferedReader br = new BufferedReader(r);

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//        System.out.println("Here's the console output:");
        String line; while ((line = br.readLine()) != null) {
//            System.out.println("\t" + line);
            writer.write(line);
            writer.newLine();
        }

        process.getInputStream().close();
        writer.close();
    }
}
