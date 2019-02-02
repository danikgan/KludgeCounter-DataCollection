package first.utilities;

import java.io.File;

public class DeleteFiles {
    public DeleteFiles(File directory) { // delete directory and all the files within
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File file : contents) {
                new DeleteFiles(file);
            }
        }

        directory.delete();
    }
}
