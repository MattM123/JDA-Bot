package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import commands.NonAPICommands;
import commands.ServerCommands;

public class Bot {	


	public static void main (String[] args) {
		
		String token = "NzMyMjkwMTUzNjg2NDk5MzY4.XwycWQ.AHOlF_0KI7GeFj4-Oyxu2-_3gpM"; //token used to hook into the Discord bot
		try {
			JDA jda = JDABuilder.createDefault(token).build();
			
			jda.addEventListener(new ServerCommands()); //The ServerCommands object contains classes with calls to the other 2 API's
			jda.addEventListener(new NonAPICommands()); //Basic commands with no API authentication
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
