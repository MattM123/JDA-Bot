package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

	public static void main (String[] args) {

		int port = Integer.parseInt(System.getenv("PORT"));
	//	String host = "0.0.0.0";
		
		String token = "NzMyMjkwMTUzNjg2NDk5MzY4.XxinCA.00eQw8ZSdMq1chDG1v_KpQKTPKU";
		try {
			JDA jda = JDABuilder.createDefault(token).build();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
