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
	public SaveIdentifiedBugs(String project, LinkedList<Tokens> listTokens, String inputPath) {
		for (Tokens token:listTokens) {
			for (int i = 0; i < token.getBugzillaBugs().size(); i++) {
				try {
					System.out.println("B: " + token.getBugzillaBugs().get(i) + " ID: " + token.getBugzillaBugs_id().get(i));
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

	private void writeToXLSX(String inputPath, String project,
							 String bug, Integer bugId) throws IOException {
		// Obtain a workbook from the excel file
		Workbook workbook = WorkbookFactory.create(new File(inputPath + "/"
				+ TemporaryFiles.analysing.OUTPUT.getString()));
		Sheet sheet = workbook.getSheetAt(0); // Get Sheet at index 0
		int rowNum = sheet.getPhysicalNumberOfRows(); // getting the max number of rows
		// create the additional header
		Row headerRow = sheet.getRow(0);
		Cell headerCell = headerRow.createCell(7);
		headerCell.setCellValue("Bug ID");
		// header cell style
		Cell headerCell_style = headerRow.createCell(0);
		headerCell.setCellStyle(headerCell_style.getCellStyle()); // copying the style from cell index 0
		// proceed
		CellStyle otherCellStyle = workbook.createCellStyle();
		otherCellStyle.setWrapText(true); // auto-wrapping for all rows, except for header
		int initialRow = 1;
		int commentNumber = 0;
		for (int i = 0; i < rowNum; i++) {
			Row row = sheet.getRow(initialRow++);
			if (String.valueOf(row.getCell(0)).equals(project)) {
				if (commentNumber == bugId) {
					// save the bugs
					System.out.println("Fail");
					row.createCell(7).setCellValue(bug);
				}
				commentNumber++;
			}
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < 7; i++) {
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
}
