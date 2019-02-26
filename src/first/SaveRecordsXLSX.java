package first;

import first.utilities.DeleteFiles;
import first.utilities.TemporaryFiles;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SaveRecordsXLSX {
    private final int MAX_CELL_CHAR = 1000;
    private String[] columns = { "Project", "Developer",
            "Commit", "Date", "Comment",
            "Alerts", "Alert Count", "Bug ID"};
    private String outputFile = TemporaryFiles.analysing.OUTPUT.getString();
//    private String outputFile_compare = TemporaryFiles.analysing.OUTPUT.getString();
    // needed for all methods
    private LinkedList<Records> records;
    private String gitPath;
    // constructor
    public SaveRecordsXLSX(LinkedList<Records> records, String gitPath){
        this.records = records;
        this.gitPath = gitPath;
    }
    // creating new records
    public void createNewRecords() {
//        outputFile += "-" + randomStringGeneration() + ".xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet( "Records");

        // header font
        Font headerFont = workbook.createFont();
        headerFont.setBold(true); // font bold
        headerFont.setFontHeightInPoints((short) 14); // font size
        headerFont.setColor(IndexedColors.BLACK.getIndex()); // font color

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setWrapText(true); // auto-wrapping for all rows, not just header

        // Create a header row
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with contacts data
        int rowNum = 1;
        try {
            writeToExcel(sheet, rowNum, workbook);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
    public void checkRecordsExist() {
        boolean recordsFound = false;
        Process process = null;
        try {
            String[] command = {"ls"};
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(gitPath));
            process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals(outputFile)){
                    recordsFound = true;
//                    outputFile = line.trim(); // setting the name
                    System.out.println("SAVED TO: \"" + outputFile + "\"");
                    break;
                }
            }
            process.getInputStream().close();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            System.out.println("*** SaveRecordsXLSX: Error checking for existing repositories.");
            e.printStackTrace();

        } finally { if (process != null) { process.destroy(); } }

        try {
            if (recordsFound) {
                // previous records have been found!
                System.out.println("*** Previous records were found..");
                modifyExistingWorkbook();
            } else {
                System.out.println("*** Previous records weren't found. Creating new ones.");
                createNewRecords();
            }
        } catch (InvalidFormatException | IOException e) {
            System.out.println("*** SaveRecordsXLSX: Error creating new records.");
            e.printStackTrace();
        }

    }
    private void modifyExistingWorkbook() throws InvalidFormatException, IOException {
        // Obtain a workbook from the excel file
        Workbook workbook = WorkbookFactory.create(new File(gitPath + "/" + outputFile));
        Sheet sheet = workbook.getSheetAt(0); // Get Sheet at index 0
//        Row row = sheet.getRow(1); // Get Row at index 1
        int rowNum = sheet.getPhysicalNumberOfRows();
        try {
            writeToExcel(sheet, rowNum, workbook);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
    private void writeToExcel(Sheet sheet, int rowNum, Workbook workbook) throws IOException, InvalidFormatException {
        // remove duplicate data
        Set<Records> set = new HashSet<>(records); // sets removed duplicates
        records = new LinkedList<>(set);
        // proceed
        CellStyle otherCellStyle = workbook.createCellStyle();
        otherCellStyle.setWrapText(true); // auto-wrapping for all rows, except for header
        StringBuilder stringBuilder;
        for (Records record : records) {
            for (int i = 0; i <= record.getUniqueAlerts_count().size(); i++) { // equals to the # of commits
                Row row = sheet.createRow(rowNum++);
//                System.out.println("Writing: " + rowNum);

                row.createCell(0).setCellValue(record.getProjectName());
                row.createCell(1).setCellValue(record.getAuthorName().get(i));
                row.createCell(2).setCellValue(record.getCommitNumber().get(i));
                row.createCell(3).setCellValue(record.getCommitDate().get(i));

                // for comments
//                Cell cell = row.createCell(4);
//                stringBuilder = new StringBuilder(record.getCommitComment().get(i));
//                checkMaxChars(stringBuilder, cell, otherCellStyle);
                Cell cell = row.createCell(4);
                cell.setCellStyle(otherCellStyle); // auto-wrapping
                cell.setCellValue(record.getCommitComment().get(i)); // no limit

                // checking for the last entry
                if (i == record.getUniqueAlerts_count().size()) {
                    row.createCell(5).setCellValue("N/A");
                    row.createCell(6).setCellValue(-1);
                } else {
                    // for PMD alerts
//                    cell = row.createCell(5);
                    cell = row.createCell(5);
                    stringBuilder = new StringBuilder(record.getUniqueAlerts().get(i));
                    checkMaxChars(stringBuilder, cell);
                    cell.setCellStyle(otherCellStyle);
                    // alerts count
                    row.createCell(6).setCellValue(record.getUniqueAlerts_count().get(i));
                }
            }
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        System.out.println("Closing...");
        new DeleteFiles(new File(gitPath + "/" + outputFile)); // required to update
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(gitPath + "/" +
                outputFile);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
    // needed for cells with excessive information writing
    private void checkMaxChars(StringBuilder stringBuilder, Cell cell) {
        if (stringBuilder.toString().length() > MAX_CELL_CHAR) {
            stringBuilder.delete(MAX_CELL_CHAR, stringBuilder.toString().length());
            stringBuilder.append("\n...{continued}...");
        }
        cell.setCellValue(stringBuilder.toString());
//        cell.setCellStyle(otherCellStyle);
    }
}
