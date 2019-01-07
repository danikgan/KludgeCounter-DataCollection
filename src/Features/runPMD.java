package Features;

import java.io.*;

public class runPMD {
    private String pmdVersion = "6.10.0";

//    private void pmdFunctions (String path) {
//        // read PMD version
//        int count = runPMD(path);
//        if (count == -1) { System.out.println("*** The directory specified is incorrect.\n" +
//                "*** Please, check your input, and try again."); }
//        else if (count == -10) { saveError(); }
//        //else if (count == -11) updatePMD();
//        else { System.out.println("Count is = " + count + ".\n"); }
//    }

    public int runPMD(String path) {
        Process p = null;

        StringBuilder command = new StringBuilder("pmd-bin-" + pmdVersion +
                "/bin/run.sh pmd -d " +
                path +
                " -R rulesets/java/quickstart.xml");

        int count = 0;
        try {
            p = (Process) Runtime.getRuntime().exec(String.valueOf(command));
            p.getOutputStream().close(); // close stdin of child

            InputStream processStdOutput = p.getInputStream();
            Reader r = new InputStreamReader(processStdOutput);
            BufferedReader br = new BufferedReader(r);
//            String line;

//            BufferedWriter writer = new BufferedWriter(new FileWriter("temp-file.txt"));
//            while ((line = br.readLine()) != null) {
            while ((br.readLine()) != null) {
                //System.out.println(line); // the output is here
//                writer.write(line);
//                writer.newLine();
                count++;
            }

//            writer.close();
            p.waitFor();
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        finally { if (p != null) p.destroy(); }

        /** Save this for later in case we need to identify errors
         * Also, the code above to write to txt the output
         */
//        int count = 0;
//        boolean areEqual = true;
//        try {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader("temp-file.txt"));
//            BufferedReader bufferedReader_error = new BufferedReader(new FileReader("src/Features/Errors/error.txt"));
//
//            String line = bufferedReader.readLine();
//            String line_error = bufferedReader_error.readLine();
//
//            while (line != null){
//                // make a comparison with the error file to check for errors
//                if (areEqual) { areEqual = line.equals(line_error); }
//
//                count++;
//                line = bufferedReader.readLine();
//                line_error = bufferedReader_error.readLine();
//            }
//
//            bufferedReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        if (areEqual) return -1;
//        else return count;
        System.out.print("- count is " + count + "\n");
        return count;
    }

//
//    private void saveError() {
//        System.out.println("Saved.");
//    }
//    private void updatePMD() {
//        System.out.println("Updated.");
//    }
}
