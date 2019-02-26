package second.process;

import first.Records;
import first.utilities.DeleteFiles;
import first.utilities.TemporaryFiles;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import second.process.data.Tokens;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class SaveIdentifiedBugs {
	private boolean firstEdit = true;
	public SaveIdentifiedBugs(String project, LinkedList<Tokens> listTokens, String inputPath) {
		for (Tokens token:listTokens) {
			for (int i = 0; i < token.getBugzillaBugs().size(); i++) {
				try {
					writeToXLSX(inputPath, project,
							token.getBugzillaBugs().get(i), token.getBugzillaBugs_id().get(i));
				} catch (IOException e) {
					System.out.println("*** SaveIdentifiedBugs: Error in recording to "
							+ TemporaryFiles.analysing.OUTPUT.getString() + ".");
					e.printStackTrace();
				}
			}
		}
	}
	// writing bugs to records.xlsx
	private void writeToXLSX(String inputPath, String project,
							 String bug, Integer bugId) throws IOException {
		// Obtain a workbook from the excel file
		Workbook workbook = WorkbookFactory.create(new File(inputPath + "/"
				+ TemporaryFiles.analysing.OUTPUT.getString()));
		Sheet sheet = workbook.getSheetAt(0); // Get Sheet at index 0
		int rowNum = sheet.getPhysicalNumberOfRows(); // getting the max number of rows
		// proceed
		CellStyle otherCellStyle = workbook.createCellStyle();
		otherCellStyle.setWrapText(true); // auto-wrapping for all rows, except for header
		int initialRow = 1;
		if (firstEdit) { // checking whether the file has been modified
			resetColumn(rowNum, sheet, project);
			firstEdit = false;
		}
		int commentNumber = 0;
		for (int i = 0; i < rowNum-1; i++) {
			Row row = sheet.getRow(initialRow++);
			if (String.valueOf(row.getCell(0)).equals(project)) {
				if (commentNumber == bugId) {
					// save the bugs
					if (!String.valueOf(row.getCell(7)).equals("")) {
						bug = row.getCell(7) + "\n" + bug;
//						System.out.println("B: " + bug);
					}
					Cell cell = row.createCell(7);
					cell.setCellStyle(otherCellStyle); // setting auto-wrapping
					cell.setCellValue(bug);
					break;
				}
				commentNumber++;
			}
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < 8; i++) {
			sheet.autoSizeColumn(i);
		}
		new DeleteFiles(new File(inputPath + "/"
				+ TemporaryFiles.analysing.OUTPUT.getString())); // required to update
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(inputPath + "/"
				+ TemporaryFiles.analysing.OUTPUT.getString());
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}
	// deleting any previous data to avoid duplication
	private void resetColumn(Integer rowNum, Sheet sheet, String project) {
		// set all index 7 to 0
		int initialRow = 1;
		for (int i = 0; i < rowNum-1; i++) {
			Row row = sheet.getRow(initialRow++);
			if (String.valueOf(row.getCell(0)).equals(project)) {
				row.createCell(7).setCellValue("");
			}
		}
	}
}
