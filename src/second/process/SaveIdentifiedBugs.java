package second.process;

import common.CloseWorkbook;
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
	// used in the analysis to keep track of the lines
	int rowNum = 0;
	private int initialRow = 1; // start of the doc
	private int commentNumber = 0; // start of the line for the project in the doc
	// constructor
	public SaveIdentifiedBugs(String project, LinkedList<Tokens> listTokens, String inputPath) throws IOException {
		// Obtain a workbook from the excel file
		Workbook workbook = WorkbookFactory.create(new File(inputPath + "/"
				+ TemporaryFiles.analysing.OUTPUT.getString()));
		Sheet sheet = workbook.getSheetAt(0); // Get Sheet at index 0
		rowNum = sheet.getPhysicalNumberOfRows(); // getting the max number of rows
		// proceed
		CellStyle otherCellStyle = workbook.createCellStyle();
		otherCellStyle.setWrapText(true); // auto-wrapping for all rows, except for header
		for (Tokens token:listTokens) {
			for (int i = 0; i < token.getBugzillaBugs().size(); i++) {
				try {
					writeToXLSX(project,
							token.getBugzillaBugs().get(i), token.getBugzillaBugs_id().get(i),
							sheet, otherCellStyle);
				} catch (IOException e) {
					System.out.println("*** SaveIdentifiedBugs: Error in recording to "
							+ TemporaryFiles.analysing.OUTPUT.getString() + ".");
					e.printStackTrace();
				}
			}
		}
		// Resize 7th column to fit the content size
		sheet.autoSizeColumn(7);
		// closing the workbook
		String path = inputPath + "/" + TemporaryFiles.analysing.OUTPUT.getString();
		new CloseWorkbook(path, workbook);
	}
	// writing bugs to records.xlsx
	private void writeToXLSX(String project,
							 String bug, Integer bugId,
							 Sheet sheet, CellStyle otherCellStyle) throws IOException {
		while (initialRow < rowNum-1) {
			Row row = sheet.getRow(initialRow);
//			System.out.println("Line: " + initialRow);
			if (String.valueOf(row.getCell(0)).equals(project)) {
				if (commentNumber == bugId) {
					// save the bugs
					if (!String.valueOf(row.getCell(7)).equals("")) {
						bug = row.getCell(7) + "\n" + bug;
					}
					Cell cell = row.createCell(7);
					cell.setCellStyle(otherCellStyle); // setting auto-wrapping
					cell.setCellValue(bug);
					break; // break before "commentNumber++" and "initialRow" so that multiple values can be per one line
				}
				commentNumber++;
			}
			initialRow++;
		}
	}
}
