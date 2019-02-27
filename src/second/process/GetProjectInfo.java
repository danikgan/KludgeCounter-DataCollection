package second.process;

import common.CloseWorkbook;
import first.utilities.TemporaryFiles;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import second.process.data.ProjectsData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class GetProjectInfo {
    // save references to InputRetrieval here
    private LinkedList<ProjectsData> projectsData = new LinkedList<>();
    // getting project information, such as comments and commits
    public GetProjectInfo(String inputPath, String projectName) throws IOException  {
        // if 1 project name, then 1 extractFromXLSX
        projectsData.add(extractFromXLSX(inputPath, projectName));
    }
    public GetProjectInfo(String inputPath, LinkedList<String> projectNames) throws IOException {
        // if "all", then for each project
        // saving all projects
        for (String projectName:projectNames){
            projectsData.add(extractFromXLSX(inputPath, projectName));
        }
    }
    /** Gets information from XLSX
     * read each line of the records
     * when the project names match, add that project's info: comments and commits
     * add that info to an instance of InputRetrieval class
     * add them within this class per project
     */
    private ProjectsData extractFromXLSX(String inputPath, String projectName) throws IOException {
        // file to open
        String fileName = TemporaryFiles.analysing.OUTPUT.getString();
        // Obtain a workbook from the excel file
        Workbook workbook = WorkbookFactory.create(new File(inputPath + "/" + fileName));
        Sheet sheet = workbook.getSheetAt(0); // Get Sheet at index 0
        // create project to return
        ProjectsData projectsData = new ProjectsData(projectName);
        // seek through the whole file
        int rowNum = sheet.getPhysicalNumberOfRows(); // total number of entries
        Row row;
        // going through each row
        for (int i = 1; i < rowNum; i++) {
            row = sheet.getRow(i);
            if (projectName.equals(String.valueOf(row.getCell(0)))) {
                projectsData.addCommitNumber(String.valueOf(row.getCell(2)));
                projectsData.addComment(String.valueOf(row.getCell(4)));
                // make the 7th cell, which is about Bug ID, equal to null to avoid data duplication
                row.createCell(7).setCellValue(""); // deleting any previous data to avoid duplication
            }
        }
        // Close the workbook
        String path = inputPath + "/" + TemporaryFiles.analysing.OUTPUT.getString();
        new CloseWorkbook(path, workbook);
        // return the project discovered
        return projectsData;
    }
    // getter of the information collected
    public LinkedList<ProjectsData> getProjectsData() {
        return projectsData;
    }
}
