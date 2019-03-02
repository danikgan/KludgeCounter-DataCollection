package first.analyse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

public class ComparePMDandDIFF {
	// temporary, needed for calculations
	private int lowerBound_temp = 0;
	private int upperBound_temp = 0;
	private LinkedList<Integer> linesOfCode_temp = new LinkedList<>();
	// passed from the caller
	private String fullPath;
	private String filePath;
	// returned
	private String uniqueAlerts = " "; // space by default
	private int uniqueAlerts_count = 0; // added to uniqueAlerts_count
	private int numberOfFilesModified = 0;
	private int numberOfLinesModified = 0;
	// constructor
	public ComparePMDandDIFF(String fullPath, String readerFileName_gitDiff, String readerFileName_pmdAlerts) {
		this.fullPath = fullPath;
		try { // diff --git a/.../file b/.../file (.java or .xml, or else)
			BufferedReader bufferedReader = new BufferedReader(new FileReader(readerFileName_gitDiff));
			String line = bufferedReader.readLine();
			// main identifiers used to read lines
			String identifier_diff = "diff --git ";
			String identifier_atat = "@@ ";
			// within "@@"
			String identifier_added = "+";
			String identifier_removed = "-";
			while (line != null) {
				// replaces all tabs with spaces
				if (line.contains("\t")) {
					line = line.replaceAll("\t", " ");
				}
//				System.out.println("L: " + line);
				boolean line_changed = false; // needed not to jump ahead
				StringBuilder stringBuilder_line = new StringBuilder(line);
				// identifying git-diff through diff --git
				if (stringBuilder_line.length() >= 11) { // avoids out of bound error
					if (stringBuilder_line.substring(0, 11).equals(identifier_diff)) {
						stringBuilder_line.delete(0, 11);
						// '+1' as delete(startIndex, finishIndex-1)
						// '+1' to remove 'b' in 'b/...'
						stringBuilder_line.delete(0, stringBuilder_line.indexOf(" ")+2);
						// save the name of the file and the folders above it within the project repository
						filePath = stringBuilder_line.toString().trim();
						// save how many files were changed
						numberOfFilesModified++;
					}
				}
				// identifying lower and upper bounds through @@
				if (stringBuilder_line.length() >= 3) {
					if (stringBuilder_line.substring(0,3).equals(identifier_atat)) {
						// setting the bounds
						stringBuilder_line.delete(0,stringBuilder_line.indexOf("+")+1);
						int index_temp = stringBuilder_line.indexOf(",");
						if (index_temp >= 0) {
							lowerBound_temp = Integer.valueOf(stringBuilder_line.toString()
									.substring(0, index_temp));
							// upper bound is either equal to lower, or is greater than it
							upperBound_temp = lowerBound_temp
									+ Integer.valueOf(stringBuilder_line.toString()
									.substring(index_temp+1, stringBuilder_line.indexOf(" ")));
							// remove the extra (unnecessary) line
							if (upperBound_temp > lowerBound_temp
									&& upperBound_temp > 0) {
								upperBound_temp -= 1;
							}
						} else {
							lowerBound_temp = Integer.valueOf(stringBuilder_line.toString()
									.substring(0, stringBuilder_line.indexOf(" ")));
							upperBound_temp = lowerBound_temp;
						}
						// within @@
						if (upperBound_temp > 0 && index_temp >= 0) { // index must be > 0, which means there are new lines
//							System.out.println("L: " + lowerBound_temp + " U: " + upperBound_temp);
							// continue to the PMD getting the unique lines
							line = bufferedReader.readLine(); // go to the next line
							int counter = 0; // needed to understand if we are within the upper bound
							while (line != null) {
								// replaces all tabs with spaces
								if (line.contains("\t")) {
									line = line.replaceAll("\t", " ");
								}
//								System.out.println("WL: " + line);
								// when git diff is found again
								if (line.length() >= 11) {
									if (line.substring(0, 11).equals(identifier_diff)) {
										line_changed = true;
										break; // exits the loop
									}
								}
								// when @@ is found again
								if (line.length() >= 3) {
									if (line.substring(0, 3).equals(identifier_atat)) {
										line_changed = true;
										break; // exits the loop
									}
								}
								// when + is found
								if (line.length() >= 1) {
									if (line.substring(0, 1).equals(identifier_added)) {
										// saving the correct number
										int number_temp = lowerBound_temp + counter;
										linesOfCode_temp.add(number_temp);
										// save how many lines were changed
										numberOfLinesModified++;
//										System.out.println("ADDED: " + linesOfCode_temp.getLast());
									}
								}
								// when - is found
								if (line.length() >= 1) {
									if (!line.substring(0, 1).equals(identifier_removed)) {
										counter++;
									}
								} else {
									counter++;
								}
								// continue the loop until break or end of the document
								try {
									line = bufferedReader.readLine(); // go to the next line
								} catch (Exception e) {
									System.out.println("*** ComparePMDandDIFF: Error reading amends in "
											+  readerFileName_gitDiff + ".");
									e.printStackTrace();
								}
							}
						}
						// further analysis of PMD txt
						findInPMD(readerFileName_pmdAlerts);
						// resetting
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
			StringBuilder uniqueAlerts_SB = new StringBuilder(""); // more efficient than string concatenation
//            lines_temp.clear(); // making it empty
			while (line != null){
				// to avoid errors while reading a line + to check this is a file, not a rep only
				if (line.length() > identifier.length() && identifier.contains(".")) {
					if (identifier.equals(line.substring(0, identifier.length()))) {
						// only add lines which are between the bounds of update
						int lineOfPMDAlert_temp = findLine(line);
						if (lineOfPMDAlert_temp >= lowerBound_temp
								&& lineOfPMDAlert_temp <= upperBound_temp){
							if (linesOfCode_temp.contains(lineOfPMDAlert_temp)){
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
		// resetting
		lowerBound_temp = 0;
		upperBound_temp = 0;
	}
	// finding the alert line in PMD txt file
	private int findLine(String line) {
		try {
			StringBuilder line_SB = new StringBuilder(line);
			line_SB.delete(0, line.indexOf(':')+1); // removing before the int
			line_SB.delete(line_SB.indexOf(":"), line_SB.toString().length()+1); // removing after the int
			return Integer.valueOf(String.valueOf(line_SB));
		} catch (Exception e) {
			System.out.println("*** ComparePMDandDIFF: Error finding the line in: \n" + line + "\n");
			return -1; // as an error
		}
	}
	public String getUniqueAlerts() {
		return uniqueAlerts;
	}
	public int getUniqueAlerts_count() {
		return uniqueAlerts_count;
	}
	public int getNumberOfLinesModified() {
		return numberOfLinesModified;
	}
	public int getNumberOfFilesModified() {
		return numberOfFilesModified;
	}
}
