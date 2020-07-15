package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import okhttp3.internal.http2.Http2Connection.Listener;

public class Bot {

	public static void main (String[] args) {

		int port = Integer.parseInt(System.getenv("PORT"));
		String host = "0.0.0.0";
		
		String token = "NzMyMjkwMTUzNjg2NDk5MzY4.Xw0Srg.Y6xwY481QrIGqcbHpv2fF4a5ZxY";
		try {
			JDA jda = JDABuilder.createDefault(token).build();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
