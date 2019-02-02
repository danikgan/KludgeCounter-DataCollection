package second.preprocessing;

import first.analyse.ApplyPMD;
import first.analyse.UseTerminal;
import first.utilities.DeleteFiles;
import first.utilities.TemporaryFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static first.analyse.ApplyPMD.setVersionPMD;

public class CheckInputExists {
    // result of the search
    private boolean result = false;
    // checking input file exists at the specified path
    public CheckInputExists(String inputPath) {
        String[] command = {"ls"};
        String fileReader = TemporaryFiles.analysing.CHECKINPUT.getString();
        new UseTerminal(command, inputPath, fileReader); // output file will be produced of name fileReader
        try {
            // using this file for comparison
            String identifier = TemporaryFiles.analysing.OUTPUT.getString();
            // find it in the txt document created by terminal output
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileReader));
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.length()==identifier.length()) {
                    if (line.substring(0,identifier.length()).equals(identifier)) {
                        // found!
                        System.out.println("The input file has been found.");
                        result = true;
                    }
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("*** CheckInputExists: Error finding the input file.");
            e.printStackTrace();
        }
        new DeleteFiles(new File(fileReader)); // delete the temporary file
    }
    // return the result of findings
    public boolean isResult() {
        return result;
    }
}
