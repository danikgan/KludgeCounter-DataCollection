package first.analyse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

public class ReadTXTCommits {
    private LinkedList<String> commitNumber = new LinkedList<>();
    private LinkedList<String> authorName = new LinkedList<>();
    private LinkedList<String> commitDate = new LinkedList<>();
    private LinkedList<String> commitComment = new LinkedList<>();

    private boolean txtExists = false;

    public ReadTXTCommits(String readerFileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(readerFileName));
            String line = bufferedReader.readLine();

            String fullComment = ""; // to records longer than 1 line comments
            boolean commentContinued = false;
            while (line != null){
                StringBuilder stringBuilder = new StringBuilder(line);

                if (stringBuilder.length() >= 4) { // avoids out of bound error
                    if (stringBuilder.substring(0,4).equals("    ")) {
                        if (commentContinued) {
                            fullComment += "\n" + stringBuilder.substring(4, stringBuilder.length()).trim();
                        } else {
                            fullComment = stringBuilder.substring(4, stringBuilder.length()).trim();
                        }

                        commentContinued = true;
                    } else if (stringBuilder.substring(0,5).equals("Date:")) {
                        commitDate.add(stringBuilder.substring(6, stringBuilder.length()).trim());
//                        System.out.println("Date: " + commitDate.getLast());
                    } else if (stringBuilder.substring(0,6).equals("commit")) {
                        if (commentContinued) {
                            commitComment.add(fullComment);
//                            System.out.println("Comment: \n" + commitComment.getLast());
//                            System.out.println("");
                        }

                        commitNumber.add(stringBuilder.substring(7, stringBuilder.length()).trim());
//                        System.out.println("Commit: " + commitNumber.getLast());

                        commentContinued = false;
                    } else if (stringBuilder.substring(0,7).equals("Author:")){
                        authorName.add(stringBuilder.substring(8, stringBuilder.length()).trim());
//                        System.out.println("Author: " + authorName.getLast());
                    }
                }

                line = bufferedReader.readLine();

                if (line == null && commentContinued) {
                    commitComment.add(fullComment);
//                    System.out.println("Comment: \n" + commitComment.getLast());
//                    System.out.println("");
                }
            }

            txtExists = true;
            bufferedReader.close();

        } catch (Exception e) {
            System.out.println("*** ReadTXTCommits: Error reading the " + readerFileName + " file.");
            txtExists = false;
        }

        if (txtExists && commitComment.size() == commitDate.size()
                && commitNumber.size() == authorName.size()
                && commitNumber.size() == commitDate.size()) {
            //System.out.println("all good!");
        } else {
            txtExists = false;
        }
    }

    public boolean isTxtExists() {
        return txtExists;
    }

    public LinkedList<String> getCommitNumber() {
        return commitNumber;
    }

    public LinkedList<String> getAuthorName() {
        return authorName;
    }

    public LinkedList<String> getCommitDate() {
        return commitDate;
    }

    public LinkedList<String> getCommitComment() {
        return commitComment;
    }
}
