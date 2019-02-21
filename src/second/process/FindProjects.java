package second.process;

import first.utilities.TemporaryFiles;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/** Reads all projects present in records.xlsx
 * Assumes no prior knowledge.
 */
public class FindProjects {
    LinkedList<String> projectNames = new LinkedList<>();
    public FindProjects(String inputPath) {
        // read xlsx file
        try {
            readXLSX(inputPath);
        } catch (Exception e) {
            System.out.println("*** FindProjects: Error reading " + TemporaryFiles.analysing.OUTPUT.getString() +
                    " file's entries.");
            e.printStackTrace();
        }
    }
    private void readXLSX(String inputPath) throws IOException {
        // file to open
        String fileName = TemporaryFiles.analysing.OUTPUT.getString();
        // Obtain a workbook from the excel file
        Workbook workbook = WorkbookFactory.create(new File(inputPath + "/" + fileName));
        Sheet sheet = workbook.getSheetAt(0); // Get Sheet at index 0
        // seek through the whole file
        int rowNum = sheet.getPhysicalNumberOfRows(); // total number of entries
//        System.out.println("RN: " + rowNum); // testing
        Row row;
        for (int i = 1; i < rowNum; i++) {
            row = sheet.getRow(i);
            projectNames.add(String.valueOf(row.getCell(0)));
//            System.out.println("Project added: " + projectNames.getLast()); // testing
        }
        // remove duplicate data
        Set<String> set = new HashSet<>(projectNames); // sets removed duplicates
        projectNames = new LinkedList<>(set);
        // Close the workbook
        workbook.close();
    }
    // return search results
    public LinkedList<String> getProjectNames() {
        return projectNames;
    }
}
