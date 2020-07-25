package com.marcuzzo.JDABot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.util.TwitchUtils;
import com.github.twitch4j.helix.TwitchHelix;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

	/*
	private static String clientID = "gywb3p2x3s9x3rc6eqze6ws2swm2i0";
	private static String oauth = "glv77l9rkdbi95h02djbglosp21xx9";
	
	
	public final static String getJSON(String channelName) {
		String line;
		StringBuilder total = new StringBuilder();
		URL url;
		HttpURLConnection connection;

		try {
			url = new URL(("https://api.twitch.tv/helix/streams?user_login=" + channelName)); //"https://id.twitch.tv/oauth2/authorize" + 
										//"?client_id=" + clientID +
										//"&redirect_uri=https://twitchapps.com/tokengen/" + 
										//"&response_type=code")); 
			
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Client-ID", clientID);
			connection.setRequestProperty("Authorization", oauth);
			
			InputStream is = connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while ((line = in.readLine()) != null) {
				total.append(line);
			}
			in.close();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		  catch (IOException e) {
			e.printStackTrace();
		}
		
		return total.toString();
	}
	*/
	
	
	
	public static void main (String[] args) {

		String token = "NzMyMjkwMTUzNjg2NDk5MzY4.XxinCA.00eQw8ZSdMq1chDG1v_KpQKTPKU";	
		try {
			JDA jda = JDABuilder.createDefault(token).build();
			jda.addEventListener(new MessageListener());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//getJSON("Clonetrooper_332");
		
		//oauth: glv77l9rkdbi95h02djbglosp21xx9
		
	}
}
