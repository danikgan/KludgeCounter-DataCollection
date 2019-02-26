package second.process;

import second.process.data.ProjectsData;
import second.process.data.Tokens;

import java.util.Arrays;
import java.util.LinkedList;

public class CommentTokenisation {
    // to use
    private String[] separators = {" ", "-", ":", ".", ",", ";", ":", "*", "&", "#", "@",
            "\'", "\"", "\\", "/", "]", "[", "}", "{", "(", ")",
            "\n", "!", "?", "+", "<", ">", "=", "%", "|"};
    // to store
    private LinkedList<Tokens> listTokens = new LinkedList<>();
    // constructor: saves tokens of a comment to a list
    public CommentTokenisation(ProjectsData projectData) {
        for (String comment:projectData.getComments()) {
            // for each comment, tokenise it
            StringBuilder toTokenise = new StringBuilder(comment);
            Tokens tokens = new Tokens();
//            System.out.println("Line: " + toTokenise.toString());
            listTokens.add(getTokens(toTokenise, tokens));
        }
    }
    // used to tokenise each comment
    private Tokens getTokens(StringBuilder toTokenise, Tokens tokens) {
        if (Arrays.stream(separators).parallel().anyMatch(toTokenise.toString()::contains)){
            int index = 0;
            boolean indexChanged = false; // to track that the value of 'index' is set to be or not to set to '0'
//            System.out.println("CL: " + toTokenise.toString());
            for (String separator:separators) {
                int tempIndex = toTokenise.indexOf(separator);
//                System.out.println("I: " + index + " S: " + separator + " TI: " + tempIndex);
                if (index > tempIndex && tempIndex >= 0) { // for index > temp
                    // getting the lowest index to get token by token from left to right
                    index = tempIndex;
                    indexChanged = true;
                } else if (!indexChanged && tempIndex >= 0) { // for the first discovered index
                    index = tempIndex;
                    indexChanged = true;
                }
            }
//            System.out.println("FI: " + index);
            // if the substring is not empty
            if (!toTokenise.toString().substring(0, index).equals("")) {
                tokens.addToken(toTokenise.toString().substring(0, index));
            }
            toTokenise.delete(0, index+1); /// +1 to delete the separator
//            System.out.println("Token: " + tokens.getTokens().getLast());
            // repeat until all is tokenised
            return getTokens(toTokenise, tokens);
        } else {
            // if the substring is not empty
            if (!toTokenise.toString().equals("")) {
                tokens.addToken(toTokenise.toString());
            }
//            System.out.println("Token: " + tokens.getTokens().getLast());
            return tokens;
        }
    }
    // getters
    public LinkedList<Tokens> getListTokens() {
        return listTokens;
    }
}
