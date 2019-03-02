package first.preprocessing;

import first.analyse.ApplyPMD;
import first.analyse.UseTerminal;
import common.DeleteFiles;
import common.TemporaryFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static first.analyse.ApplyPMD.setVersionPMD;

public class IdentifyPMD {
    private boolean pmdFound = false;
    // auto-detecting PMD version within the repository
    public IdentifyPMD() {
        String[] command = {"ls"};
        String fileReader = TemporaryFiles.analysing.IDENTIFY_PMD.getString();
        new UseTerminal(command, "", fileReader);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileReader));
            String line = bufferedReader.readLine();
            String identifier = "pmd-bin-";
            while (line != null) {
                if (line.length()>identifier.length()) {
                    if (line.substring(0,identifier.length()).equals(identifier)) {
                        if (line.substring(line.length()-4).equals(".zip")) {
                            System.out.println("This ZIP is redundant: " + line);
                        } else {
                            new ApplyPMD(); // setting the correct version of PMD to the PMD compiler
                            setVersionPMD(line.substring(identifier.length()));
                            pmdFound = true;
                        }
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
    public boolean isPmdFound() {
        if (!pmdFound) {
            System.out.println("*** Error in finding PMD. Please, ensure the latest version of PMD is within the repository.");
        }
        return pmdFound;
    }
}
