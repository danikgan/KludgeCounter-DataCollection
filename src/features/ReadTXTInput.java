package features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

public class ReadTXTInput {
    private String txtName = "links.txt";
    private Boolean txtExists = false;

    // to send to the main class
    // made static as they are supposed to be a one-instance scenario
    private static LinkedList<Integer> commitsQuantity = new LinkedList<>();
    private static LinkedList<String> links = new LinkedList<>();

    public ReadTXTInput(String txtPath) {
        txtPath += "/"; // add this to read the file of "path/file.txt"
        System.out.println("Path: " + txtPath);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(txtPath + txtName));
            String line = bufferedReader.readLine();

            while (line != null){
                StringBuilder line_SB = new StringBuilder(line);

                String commitQuantity_toConvert = "";
                int linkIndex = 0;
                for (int i = 0; i < line_SB.length(); i++) {

                    if (line_SB.charAt(i) != ' ') {
                        commitQuantity_toConvert += line_SB.charAt(i); // add the char to the string of commit count

                    } else {
                        // getting the quantity of commits per repository
                        commitsQuantity.add(Integer.parseInt(commitQuantity_toConvert));

                        // getting the link for repository
                        linkIndex = i;
                        line_SB.delete(0, linkIndex);
                        links.add(line_SB.toString().trim());

                        break;
                    }
                }

                line = bufferedReader.readLine();
            }

            txtExists = true;
            bufferedReader.close();

        } catch (Exception e) {
            System.out.println("*** ReadTXTInput: Error reading the TXT file.");
            txtExists = false;
        }
    }

    public LinkedList<Integer> getCommitsQuantity() {
        if (!txtExists || commitsQuantity.isEmpty()) {
            return null; // error
        } else {
            return commitsQuantity;
        }
    }

    public LinkedList<String> getLinks() {
        if (!txtExists || links.isEmpty()) {
            return null; // error
        } else {
            return links;
        }
    }

    public Boolean getTxtExists() {
        return txtExists;
    }
}
