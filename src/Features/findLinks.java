package Features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class findLinks {
    private List<String> links = new LinkedList<>();

    public List<String> findLinks(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "/links.txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                links.add(line);
            }

            bufferedReader.close();
            return links;

        } catch (Exception e){
            System.err.format("*** Exception occurred trying to read.");
            e.printStackTrace();
            return null;
        }
    }
}
