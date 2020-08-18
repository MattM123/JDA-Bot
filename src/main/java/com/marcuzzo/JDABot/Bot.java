package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import commands.HelpCommand;
import commands.MapCommand;
import commands.ServerCommands;

public class Bot {	

	public static void main (String[] args) {
		
		String token = "NzMyMjkwMTUzNjg2NDk5MzY4.XxinCA.00eQw8ZSdMq1chDG1v_KpQKTPKU";	
		try {
			JDA jda = JDABuilder.createDefault(token).build();
			
			jda.addEventListener(new MapCommand());
			jda.addEventListener(new ServerCommands());
			jda.addEventListener(new HelpCommand());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
