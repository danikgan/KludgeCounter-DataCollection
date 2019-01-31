package features.utilities;

public class FindRepositoryName {
    String folderName;
    // This finds the name of the downloaded repository, so that there's no need to specify manually
    public FindRepositoryName(String link) {
        int gitLink_length = link.length();
        StringBuilder folderName = new StringBuilder();
        // finding the name
        for (int i = gitLink_length-1; i > 0; i--) {
            char gitLink_char = link.charAt(i);

            if (gitLink_char == '/') { break; }
            else { folderName.insert(0, gitLink_char); }

            // remove the .git at the end
            String temp_gitLink_comparison = folderName.toString();
            if (temp_gitLink_comparison.equals(".git")) {
                folderName.delete(0,4);
            }
        }
        this.folderName = String.valueOf(folderName);
    }
    public String getFolderName() {
        return folderName;
    }
}
