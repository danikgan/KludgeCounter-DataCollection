package Features;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class recordEntries {
    // TODO some function to record the counts to CSV

    // check if csv is empty
    // if not, add the headers: Project Name, Date, Error Count
    // followed by the entries

    private static List<Features.entries> entries = new LinkedList<>();
    private static String[] columns = { "Project Name", "Date", "Error Count"};
    //private static List<Contact> contacts = new ArrayList<Contact>();

    public recordEntries(List<Features.entries> entries, String filePath) throws IOException, InvalidFormatException {
        this.entries = entries;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("OSP Records");

        // header font
        Font headerFont = workbook.createFont();
        headerFont.setBold(true); // font bold
        headerFont.setFontHeightInPoints((short) 14); // font size
        headerFont.setColor(IndexedColors.BLACK.getIndex()); // font color

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with contacts data
        int rowNum = 1;

        for (Features.entries entry : entries) {
            for (int i = 0; i < entry.getDate().size(); i++) {
                //System.out.println(entry.date.get(i) + ": " + entry.count.get(i));
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getProject_name());
                row.createCell(1).setCellValue(entry.getDate().get(i));
                row.createCell(2).setCellValue(entry.getCount().get(i));
            }
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(filePath + "/osp-records.xlsx");
        workbook.write(fileOut);
        fileOut.close();
    }

//    public recordEntries(String filePath) {
//        // first create file object for file placed at location
//        // specified by filepath
//        File file = new File(filePath + "/osp-records.xlsx");
//        try {
//            // create FileWriter object with file as parameter
//            FileWriter outputfile = new FileWriter(file);
//
//            // create CSVWriter object filewriter object as parameter
//            CSVWriter writer = new CSVWriter(outputfile);
//
//            // adding header to csv
//            String[] header = { "Name", "Class", "Marks" };
//            writer.writeNext(header);
//
//            // add data to csv
//            String[] data1 = { "Aman", "10", "620" };
//            writer.writeNext(data1);
//            String[] data2 = { "Suraj", "10", "630" };
//            writer.writeNext(data2);
//
//            // closing writer connection
//            writer.close();
//        }
//
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
