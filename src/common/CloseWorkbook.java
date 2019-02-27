package common;

import first.utilities.DeleteFiles;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CloseWorkbook {
	public CloseWorkbook(String path, Workbook workbook) throws IOException {
		// delete previous file
		new DeleteFiles(new File(path)); // required to update
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}
}
