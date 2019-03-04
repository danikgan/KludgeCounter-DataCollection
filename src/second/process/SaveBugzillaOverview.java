package second.process;

import common.CloseWorkbook;
import common.TemporaryFiles;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import second.process.data.BugzillaOverview;

import java.io.IOException;
import java.util.LinkedList;

public class SaveBugzillaOverview {
	private String[] columns = { "Project", "Bugzilla ID", "Product", "Assigned To", "CC",
			"Classification", "Component", "Creator", "Creation Time", "Is Open",
			"Last Change Time", "Priority", "Resolution", "Severity", "Status",
			"Summary", "Version"};
	// constructor
	public SaveBugzillaOverview(LinkedList<BugzillaOverview> bugzillaOverviews, String inputPath) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet( "Bugs Overview");
		// header font
		Font headerFont = workbook.createFont();
		headerFont.setBold(true); // font bold
		headerFont.setFontHeightInPoints((short) 14); // font size
		headerFont.setColor(IndexedColors.BLACK.getIndex()); // font color
		// header cell style
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
		for (BugzillaOverview bugzillaOverview : bugzillaOverviews) {
			Row row = sheet.createRow(rowNum++);
			// writing
			row.createCell(0).setCellValue(bugzillaOverview.getProject());
			row.createCell(1).setCellValue(bugzillaOverview.getId());
			row.createCell(2).setCellValue(bugzillaOverview.getProduct());
			row.createCell(3).setCellValue(bugzillaOverview.getAssigned_to());

			Cell cell = row.createCell(4);
			cell.setCellStyle(otherCellStyle);
			cell.setCellValue(bugzillaOverview.getCc());

			row.createCell(5).setCellValue(bugzillaOverview.getClassification());
			row.createCell(6).setCellValue(bugzillaOverview.getComponent());
			row.createCell(7).setCellValue(bugzillaOverview.getCreator());
			row.createCell(8).setCellValue(bugzillaOverview.getCreation_time());
			row.createCell(9).setCellValue(bugzillaOverview.getIs_open());
			row.createCell(10).setCellValue(bugzillaOverview.getLast_change_time());
			row.createCell(11).setCellValue(bugzillaOverview.getPriority());
			row.createCell(12).setCellValue(bugzillaOverview.getResolution());
			row.createCell(13).setCellValue(bugzillaOverview.getSeverity());
			row.createCell(14).setCellValue(bugzillaOverview.getStatus());

			cell = row.createCell(15);
			cell.setCellStyle(otherCellStyle);
			cell.setCellValue(bugzillaOverview.getSummary());

			row.createCell(16).setCellValue(bugzillaOverview.getVersion());
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}
		System.out.println("Closing...");
		try {
			new CloseWorkbook(inputPath + "/" +
					TemporaryFiles.analysing.OUTPUT_THREE.getString(), workbook);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
