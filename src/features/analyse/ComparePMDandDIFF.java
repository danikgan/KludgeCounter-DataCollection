package features.analyse;

import java.io.BufferedReader;
import java.io.FileReader;

public class ComparePMDandDIFF {
    private String fullPath;
    private String filePath;

//    private LinkedList<String> uniqueAlerts = new LinkedList<>();
    private String uniqueAlerts = " "; // space by default
    private int uniqueAlerts_count = 0; // added to uniqueAlerts_count

//    public LinkedList<String> getUniqueAlerts() {
//        return uniqueAlerts;
//    }


    public String getUniqueAlerts() {
        return uniqueAlerts;
    }

    public int getUniqueAlerts_count() {
        return uniqueAlerts_count;
    }

    public ComparePMDandDIFF(String fullPath, String readerFileName_gitDiff, String readerFileName_pmdAlerts) {
        this.fullPath = fullPath;

        try { // diff --git a/.../file b/.../file (.java or .xml, or else)
            BufferedReader bufferedReader = new BufferedReader(new FileReader(readerFileName_gitDiff));
            String line = bufferedReader.readLine();

            String identifier = "diff --git ";
            while (line != null) {
                StringBuilder stringBuilder_line = new StringBuilder(line);

                if (stringBuilder_line.length() >= 11) { // avoids out of bound error
                    if (stringBuilder_line.substring(0, 11).equals(identifier)) {
                        stringBuilder_line.delete(0, 11);
//                        System.out.println("Source: " + stringBuilder_line.toString().trim());
//                        System.out.println("Index of: " + stringBuilder_line.indexOf(" "));

                        // '+1' as delete(startIndex, finishIndex-1)
                        // '+1' to remove 'b' in 'b/...'
                        stringBuilder_line.delete(0, stringBuilder_line.indexOf(" ")+2);
//                        System.out.println("Path is: " + fullPath + stringBuilder_line.toString().trim());
                        filePath = stringBuilder_line.toString().trim();

                        if (!filePath.equals("")) {
//                            System.out.println("Finding errors in PMD...: ");
                            findInPMD(readerFileName_pmdAlerts);
                        } else {
                            System.out.println("*** ComparePMDandDIFF: filePath is empty.");
                        }
                    }
                }

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("*** ComparePMDandDIFF: Error reading " + readerFileName_gitDiff + ".");
            e.printStackTrace();
        }
    }

    // Whenever an error is found in gitDiff, it is then check within the PMD txt
    // If found, added to the list: quantity and strings of error
    private void findInPMD(String readerFileName_pmdAlerts) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(readerFileName_pmdAlerts));
            String line = bufferedReader.readLine();

            String identifier = fullPath + filePath;
            StringBuilder uniqueAlerts_SB = new StringBuilder(""); // more efficient than string concatenation
            while (line != null){
                if (line.length() >= identifier.length()) { // to avoid errors while reading a line
                     if (identifier.equals(line.substring(0, identifier.length()))) {
//                         System.out.println("Counter++");
//                         uniqueAlerts.add(line); // adding the string value of the PMD alert
                         if (uniqueAlerts_SB.toString().equals("")) {
                             uniqueAlerts_SB.append(line);
                         } else {
                             uniqueAlerts_SB.append("\n").append(line);
                         }

                         uniqueAlerts_count++; // added to uniqueAlerts_count
                    }
                }

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            if (uniqueAlerts.equals(" ") && !uniqueAlerts_SB.toString().equals("")) { // saving to the String
                uniqueAlerts = uniqueAlerts_SB.toString();
            } else if (!uniqueAlerts_SB.toString().equals("")) { // checking if concatenation is not empty
                uniqueAlerts += "\n" + uniqueAlerts_SB.toString();
            }
//            System.out.println("Alerts 2: " + uniqueAlerts);
        } catch (Exception e) {
            System.out.println("*** ComparePMDandDIFF: Error reading " + readerFileName_pmdAlerts + ".");
            e.printStackTrace();
        }
    }
}
