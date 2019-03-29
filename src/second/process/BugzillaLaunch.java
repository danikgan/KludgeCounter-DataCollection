package second.process;

import second.process.data.Tokens;

import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.io.PrintWriter;

import static java.lang.Thread.sleep;

public class BugzillaLaunch {
	private String responseErrorCode = "";
	// uses Bugzilla BugsHistory collected and saved in Tokens to download REST from online
	// e.g. https://bz.apache.org/bugzilla/rest.cgi/bug/62952/history
	public BugzillaLaunch(LinkedList<Tokens> listTokens) {
		int count = 0;
		for (Tokens tokens:listTokens) {
			for (String bug:tokens.getBugzillaBugs()) {
				try {
					useBugzilla(bug, tokens, count);
				} catch (Exception e) {
					System.out.println("*** BugzillaLaunch: " +
//					System.out.println("*** " + e.getClass() + ": " +
							"Error finding " + bug + " on Bugzilla.");
					e.printStackTrace();
				}
				count++;
			}
		}
	}
	// accessing each bug through REST and Bugzilla website
	private void useBugzilla(String bug, Tokens tokens, Integer temp_count) throws Exception {
		// creating the url address
		String url_string_overview = "https://bz.apache.org/bugzilla/rest.cgi/bug/" + bug;
		String url_string_history = "https://bz.apache.org/bugzilla/rest.cgi/bug/" + bug + "/history";
		System.out.println(temp_count + ". URL: " + url_string_overview);
		while(true){
			try {
				accessingURL(url_string_overview, tokens, true);
				accessingURL(url_string_history, tokens, false);
				break;
			} catch (Exception e) {
//				StringWriter errors = new StringWriter();
//				e.printStackTrace(new PrintWriter(errors));
//				System.out.println(errors);
//				if (errors.toString().substring(0,tooManyRequest.length())
//						.equals(tooManyRequest)) {
//				e.printStackTrace();
				if (responseErrorCode.equals("429"))	 {
					System.out.println("Too many requests reached. Retrying... " +
							"[Takes 2 minutes]");
					sleep(125000); // 2 minutes and 5 seconds (just in case)
				} else {
					System.out.println("*** The bug " + bug + " was not found on Bugzilla.");
					break;
				}
			}
		}
	}
	private void accessingURL(String url_string, Tokens tokens, Boolean history_or_overview)
			throws IOException {
		// setting the connection
		URL url = new URL(url_string);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		// setting the type of connection
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setRequestProperty("Accept", "application/json");
		// in case of errors
		if (httpURLConnection.getResponseCode() != 200) {
			responseErrorCode = Integer.toString(httpURLConnection.getResponseCode());
			throw new RuntimeException("*** BugzillaLaunch: HTTP Error code : "
					+ httpURLConnection.getResponseCode());
		}
		// get output
		InputStreamReader in = new InputStreamReader(httpURLConnection.getInputStream());
		BufferedReader br = new BufferedReader(in);
		String output;
		while ((output = br.readLine()) != null) {
			if (history_or_overview) {
				tokens.addBugzillaReport_overview(output); // saving overview
			} else {
				tokens.addBugzillaReport_history(output); // saving history
			}
		}
		// terminate the connection
		httpURLConnection.disconnect();
	}
}
