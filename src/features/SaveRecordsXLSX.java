package features;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class SaveRecordsXLSX {
    private static final int MAX_CELL_CHAR = 1000;
    private static String[] columns = { "Project", "Developer",
            "Commit", "Date", "Comment",
            "Alerts", "Alert Count"};

    public SaveRecordsXLSX(LinkedList<Records> records, String gitPath) throws IOException, InvalidFormatException  {
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

        CellStyle otherCellStyle = workbook.createCellStyle();
        otherCellStyle.setWrapText(true); // auto-wrapping for all rows, except for header

        for (Records record : records) {
            for (int i = 0; i < record.getCommitNumber().size(); i++) { // equals to the # of commits
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(record.getProjectName());
                row.createCell(1).setCellValue(record.getAuthorName().get(i));
                row.createCell(2).setCellValue(record.getCommitNumber().get(i));
                row.createCell(3).setCellValue(record.getCommitDate().get(i));

                // for comments
                Cell cell = row.createCell(4);
                StringBuilder stringBuilder = new StringBuilder(record.getCommitComment().get(i));
                if (stringBuilder.toString().length() > MAX_CELL_CHAR) {
                    stringBuilder.delete(MAX_CELL_CHAR, stringBuilder.toString().length());
                    stringBuilder.append("\n...{continued}...");
                }
                cell.setCellValue(stringBuilder.toString());
                cell.setCellStyle(otherCellStyle);

                // for PMD alerts
                cell = row.createCell(5);
                stringBuilder = new StringBuilder(record.getUniqueAlerts().get(i));
                if (stringBuilder.toString().length() > MAX_CELL_CHAR) {
                    stringBuilder.delete(MAX_CELL_CHAR, stringBuilder.toString().length());
                    stringBuilder.append("\n...{continued}...");
                }
                cell.setCellValue(stringBuilder.toString());
                cell.setCellStyle(otherCellStyle);

                row.createCell(6).setCellValue(record.getUniqueAlerts_count().get(i));
            }
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(gitPath + "/records.xlsx");
        workbook.write(fileOut);
        fileOut.close();
    }
}
