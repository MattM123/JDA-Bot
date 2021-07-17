package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import commands.APICommands;
import commands.NonAPICommands;

public class Bot {	
	private static String token = "NzMyMjkwMTUzNjg2NDk5MzY4.XwycWQ.sro0wc0HUIAO1dNvbh7LgMtK3_k"; //token used to hook into the Discord bot (bot token)
	public static JDA jda;

	public static void main (String[] args) {
        
		
		try {
			jda = JDABuilder.createDefault(token)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
				.build();
			
			jda.addEventListener(new APICommands()); //The ServerCommands object contains classes with calls to the other 2 API's
			jda.addEventListener(new NonAPICommands()); //Basic commands with no API authentication
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
