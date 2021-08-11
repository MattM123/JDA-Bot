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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class BuildTheEarthAPI {
	private String apikey;
	private ArrayList<Long> userIDs = new ArrayList<Long>();
	public String stackTrace = "";
	
	public BuildTheEarthAPI(String key) {
		apikey = key;
	}
	
	//Given a discord ID, this method returns the username the user applied to team with
	public String getUsernameAppliedWith(String userID) {	
		String line;
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;
		ArrayList<AnswerInfo> answers = null;
		String usernameAppliedWith = null;
		
		//API Authentication
		try {
			url = new URL("https://buildtheearth.net/api/v1/applications/" + userID);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("Host","buildtheearth.net");
			conn.setRequestProperty("Authorization", "Bearer " + apikey);
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
			
			//JSON Deserialization of a users applciation to store the questions and answers					
			Gson gson = new Gson();
			
			//ApplicationInfo class contains a few classes that the JSON is deserialized into
			ApplicationInfo applicationArray = gson.fromJson(json.toString(), ApplicationInfo.class);  
			 
			//retrieving username value from application answers	
			answers = (ArrayList<AnswerInfo>) applicationArray.getApplications().get(0).getAnswerList();
			usernameAppliedWith = answers.get(1).getAnswer();		
			
		} catch (MalformedURLException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack.subSequence(0, 1000).toString();
		} catch (IOException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack.subSequence(0, 1000).toString();
		} catch (JSONException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack.subSequence(0, 1000).toString();
		} catch (IndexOutOfBoundsException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack.subSequence(0, 1000).toString() + answers.get(2).getAnswer();
		}
		return usernameAppliedWith;
	}
	
	//returns a list of all users discord IDs that are on the team
	public ArrayList<Long> getMemberList() {
		stackTrace = "";
		String line;
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;
		JsonArray jarray = null;
		
		//API authentication
		try {
			url = new URL("https://buildtheearth.net/api/v1/members");
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("Host","buildtheearth.net");
			conn.setRequestProperty("Authorization", "Bearer " + apikey);
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
			}		
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			if ((line = in.readLine()) != null) {
				json.append(line);
			}
			in.close();
			
			JsonElement ele = JsonParser.parseString(json.toString());
			jarray = ele.getAsJsonObject().getAsJsonArray("members");
			
		} catch (MalformedURLException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack.subSequence(0, 1000).toString();
		} catch (IOException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack.subSequence(0, 1000).toString();
		} catch (JSONException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack.subSequence(0, 1000).toString();
		}
		
		for (int i = 0; i < jarray.size(); i++) {
			userIDs.add(jarray.get(i).getAsJsonObject().get("discordId").getAsLong());
		}
		
		return userIDs;
	}
	
	public ApplicationInfo getApplicationHistory(String userID) {	
		stackTrace = "";
		String line;
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;
		ApplicationInfo applicationArray = null;
		
		//API Authentication
		try {
			url = new URL("https://buildtheearth.net/api/v1/applications/" + userID);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("Host","buildtheearth.net");
			conn.setRequestProperty("Authorization", "Bearer " + apikey);
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
				stackTrace = "Error Code: " + String.valueOf(conn.getResponseCode()) + " " + json.toString();
			}		
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			if ((line = in.readLine()) != null) {
				json.append(line);
			}
			in.close();
			
			//JSON Deserialization of a users applciation to store the questions and answers
			
			
			Gson gson = new Gson();
			//ApplicationInfo class contains a few classes that the JSON is deserialized into
			applicationArray = gson.fromJson(json.toString(), ApplicationInfo.class); 		
				
		} catch (MalformedURLException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack.subSequence(0, 1000).toString();
		} catch (IOException e) {
			stackTrace = "User has not applied to the team nor have they been merged into it";
		} catch (JSONException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack.subSequence(0, 1000).toString();
		}
		
		return applicationArray;
	}
}
