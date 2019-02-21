package second.process;

import second.process.data.Tokens;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class BugzillaLaunch {
    // uses Bugzilla bugs collected and saved in Tokens to download REST from online
    // e.g. https://bz.apache.org/bugzilla/rest.cgi/bug/62952/history
    public BugzillaLaunch(LinkedList<Tokens> listTokens) {
        for (Tokens tokens:listTokens) {
            for (String bug:tokens.getBugzillaBugs()) {
                useBugzilla(bug, tokens);
            }
        }
    }
    // accessing each bug through REST and Bugzilla website
    private void useBugzilla(String bug, Tokens tokens) {
        try {
            // creating the url address
            String url_string = "https://bz.apache.org/bugzilla/rest.cgi/bug/" + bug + "/history";
            System.out.println("URL: " + url_string);
            // setting the connection
            URL url = new URL(url_string);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // setting the type of connection
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            // in case of errors
            if (httpURLConnection.getResponseCode() != 200) {
                throw new RuntimeException("*** BugzillaLaunch: HTTP Error code : "
                        + httpURLConnection.getResponseCode());
            }
            // get output
            InputStreamReader in = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
//                System.out.println(output);
                tokens.addBugzillaReport(output);
            }
            // terminate the connection
            httpURLConnection.disconnect();
        } catch (Exception e) {
            System.out.println("*** The bug " + bug + " was not found on Bugzilla.");
        }
    }
}
