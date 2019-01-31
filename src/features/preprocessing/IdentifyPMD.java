package features.preprocessing;

import features.analyse.ApplyPMD;
import features.analyse.UseTerminal;
import features.utilities.DeleteFiles;
import features.utilities.TemporaryFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static features.analyse.ApplyPMD.setVersionPMD;

public class IdentifyPMD {
    // auto-detecting PMD version within the repository
    public IdentifyPMD() {
        String[] command = {"ls"};
        String fileReader = TemporaryFiles.analysing.IDENTIFYPMD.getString();
        new UseTerminal(command, "", fileReader);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileReader));
            String line = bufferedReader.readLine();
            String identifier = "pmd-bin-";
            while (line != null) {
                if (line.length()>identifier.length()) {
                    if (line.substring(0,identifier.length()).equals(identifier)) {
                        new ApplyPMD(); // setting the correct version of PMD to the PMD compiler
                        setVersionPMD(line.substring(identifier.length()));
                    }
                }

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("*** Main: Error finding PMD in the repository.");
            e.printStackTrace();
        }

        new DeleteFiles(new File(fileReader));
    }
}
