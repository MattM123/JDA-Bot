package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONException;

import com.google.gson.Gson;

public class BuildTheEarthAPI {
	private String apikey;
	//private String user;
	
	public BuildTheEarthAPI(String key) {
		key = apikey;
	}
	
	//Given a discord ID, this method returns the username the user applied with
	public String getUsernameAppliedWith(String userID) {
	//	user = userID;
		
		String line;
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;
		ArrayList<AnswerInfo> answers = null;
		String usernameAppliedWith = null;
		
		try {
			url = new URL("https://buildtheearth.net/api/v1/applications/" + userID);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("Host","buildtheearth.net");
			conn.setRequestProperty("Authorization", "Bearer 6d83c36acd1bb7301e64749b46ebddc2e3b64a67");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
			conn.setRequestMethod("GET");
			
			//Storing JSON from request into string. Prints error code and error stream if encountered.
			
			if (conn.getResponseCode() > 200) { 			
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = in.readLine()) != null) {
					json.append(line);
				}
				in.close();
				return "Error Code: " + String.valueOf(conn.getResponseCode()) + " " + json.toString();
			}		
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			if ((line = in.readLine()) != null) {
				json.append(line);
			}
			in.close();
			
			//JSON Deserialization
			
			
			Gson gson = new Gson();
			ApplicationInfo applicationArray = gson.fromJson(json.toString(), ApplicationInfo.class);  
			 
			//retrieving username from application answers
			
			answers = (ArrayList<AnswerInfo>) applicationArray.getApplications().get(0).getAnswerList();
			usernameAppliedWith = answers.get(4).getAnswer();		
			
		} catch (MalformedURLException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack.subSequence(0, 1000).toString();
		} catch (IOException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack.subSequence(0, 1000).toString();
		} catch (JSONException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack.subSequence(0, 1000).toString();
		}
		
		return usernameAppliedWith;
	}
	//6d83c36acd1bb7301e64749b46ebddc2e3b64a67
}
