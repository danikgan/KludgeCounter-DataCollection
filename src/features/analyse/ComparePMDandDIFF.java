package features.analyse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
//TODO Implement a line checker, so that the lines read == lines of the file
public class ComparePMDandDIFF {
    // temporary, needed for calculations
//    private LinkedList<Integer> lines_temp = new LinkedList<>();
    private int lowerBound_temp = 0;
    private int upperBound_temp = 0;
    private LinkedList<Integer> linesOfCode_temp = new LinkedList<>();

    // passed from the caller
    private String fullPath;
    private String filePath;

    // returned
    private String uniqueAlerts = " "; // space by default
    private int uniqueAlerts_count = 0; // added to uniqueAlerts_count

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
            String identifier_lines = "@@ ";
            String identifier_added = "+ ";
            String identifier_removed = "- ";
            int counterTODELETE = 0;
            while (line != null) {
                boolean line_changed = false;
                StringBuilder stringBuilder_line = new StringBuilder(line);
//                System.out.println("L: " + line);

                // identifying git-diff through diff --git
                if (stringBuilder_line.length() >= 11) { // avoids out of bound error
                    if (stringBuilder_line.substring(0, 11).equals(identifier)) {
                        stringBuilder_line.delete(0, 11);
                        // '+1' as delete(startIndex, finishIndex-1)
                        // '+1' to remove 'b' in 'b/...'
                        stringBuilder_line.delete(0, stringBuilder_line.indexOf(" ")+2);

                        // save the name of the file and the folders above it within the project repository
                        filePath = stringBuilder_line.toString().trim();
                    }
                }

                // identifying lower and upper bounds through @@
                if (stringBuilder_line.length() >= 3) {
                    if (stringBuilder_line.substring(0,3).equals(identifier_lines)) {
//                        System.out.println("filePath: " + filePath);
                        stringBuilder_line.delete(0,stringBuilder_line.indexOf("+")+1);
//                        System.out.println("Output:" + stringBuilder_line.toString());

                        int index_temp = stringBuilder_line.indexOf(",");
                        if (index_temp>=0) {
                            lowerBound_temp = Integer.valueOf(stringBuilder_line.toString().substring(0, index_temp));
                            upperBound_temp = lowerBound_temp + Integer.valueOf(stringBuilder_line.toString().substring(index_temp+1, stringBuilder_line.indexOf(" ")));

                            if (upperBound_temp>lowerBound_temp && upperBound_temp>0){
                                upperBound_temp -= 1; // deletes one extra (unnecessary) line
                            }

//                            System.out.println("LB: " + lowerBound_temp);
//                            System.out.println("UB: " + upperBound_temp);
                        } else {
                            lowerBound_temp = Integer.valueOf(stringBuilder_line.toString().substring(0, stringBuilder_line.indexOf(" ")));
                            upperBound_temp = lowerBound_temp;
//                            System.out.println("LB=UB: " + lowerBound_temp);
                        }

//                        System.out.println("Finding pmd alerts...");
                        if (upperBound_temp > 0 || lowerBound_temp > 0) {
//                            System.out.println("Getting changes...");
                            line = bufferedReader.readLine(); // go to the next line
//                            stringBuilder_line = new StringBuilder(line);

                            int counter = 0;
                            while (line != null) {
                                counterTODELETE++;
//                                stringBuilder_line = new StringBuilder(line);

                                if (line.length() >= 11) {
                                    // when git diff is found again
                                    if (line.substring(0, 11).equals(identifier)) {
//                                        System.out.println("....Exiting....");
//                                        System.out.println(line);
                                        line_changed = true;
                                        break;
                                    }
                                }

                                if (line.length() >= 3) {
                                    // when @@ is found again
                                    if (line.substring(0, 3).equals(identifier_lines)) {
//                                        System.out.println("....Exiting....");
//                                        System.out.println(counterTODELETE);
//                                        System.out.println(line);
                                        line_changed = true;
                                        break;
                                    }
                                }

                                if (line.length() >= 2) {
                                    if (line.substring(0, 2).equals(identifier_added)) {
                                        // saving the correct number
                                        int number_temp = lowerBound_temp + counter;
//                                        System.out.println("Adding this number: " + number_temp);
                                        linesOfCode_temp.add(number_temp);
//                                        System.out.println("This line added: " + linesOfCode_temp.getLast());
                                    }
                                }

                                if (line.length() >= 2) {
                                    if (!line.substring(0, 2).equals(identifier_removed)) {
                                        counter++;
                                    }
                                } else {
                                    counter++;
                                }


                                try {
                                    line = bufferedReader.readLine(); // go to the next line
                                } catch (Exception e) {
                                    System.out.println("*** ComparePMDandDIFF: Error reading amends in " +  readerFileName_gitDiff + ".");
                                    e.printStackTrace();
                                }
//                                System.out.println((counter + lowerBound_temp) + " " + stringBuilder_line.toString());
//                                System.out.println(line);
                            }
                        }

//                        System.out.println("LINE: " + line);
//                        System.out.println("Entering PMD txt...");
                        findInPMD(readerFileName_pmdAlerts);
                        // resetting
//                        System.out.println("LC: " + linesOfCode_temp);
                        linesOfCode_temp.clear();
                        filePath = "";
                    }
                }

                if (!line_changed) {
                    line = bufferedReader.readLine();
                }
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
//            System.out.println("ID: " + identifier);
            StringBuilder uniqueAlerts_SB = new StringBuilder(""); // more efficient than string concatenation
//            lines_temp.clear(); // making it empty
            while (line != null){
                // to avoid errors while reading a line + to check this is a file, not a rep only
                if (line.length() > identifier.length() && identifier.contains(".")) {
                    if (identifier.equals(line.substring(0, identifier.length()))) {
                        // only add lines which are between the bounds of update
                        int lineOfPMDAlert_temp = findLine(line);
                        if (lineOfPMDAlert_temp>=lowerBound_temp && lineOfPMDAlert_temp<=upperBound_temp){
//                            System.out.println("LB: " + lowerBound_temp + " UB: " + upperBound_temp + " Line: " + lineOfPMDAlert_temp);
                            if (linesOfCode_temp.contains(lineOfPMDAlert_temp)){
//                                System.out.println("Added: " + lineOfPMDAlert_temp + " " + identifier);
                                if (uniqueAlerts_SB.toString().equals("")) {
                                    uniqueAlerts_SB.append(line);
                                } else {
                                    uniqueAlerts_SB.append("\n").append(line);
                                }

                                uniqueAlerts_count++; // added to uniqueAlerts_count
                            }
                        }
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
        } catch (Exception e) {
            System.out.println("*** ComparePMDandDIFF: Error reading " + readerFileName_pmdAlerts + ".");
            e.printStackTrace();
        }

        //TODO make lower && upper bound zero here
        lowerBound_temp = 0;
        upperBound_temp = 0;
    }

    // finding the alert line in PMD txt file
    private int findLine(String line) {
        try {
            StringBuilder line_SB = new StringBuilder(line);

            line_SB.delete(0, line.indexOf(':')+1); // removing before the int
            line_SB.delete(line_SB.indexOf(":"), line_SB.toString().length()+1); // removing after the int

//        System.out.println("Line: " + line_SB.toString());
            return Integer.valueOf(String.valueOf(line_SB));
        } catch (Exception e) {
            System.out.println("*** ComparePMDandDIFF: Error finding the line in: \n" + line + "\n");
//            e.printStackTrace();
            return -1; // as an error
        }
    }
}
