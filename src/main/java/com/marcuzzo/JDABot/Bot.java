package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import commands.APICommands;
import commands.NonAPICommands;
import database.databaseTask;

public class Bot {	
	private static String token = "NzMyMjkwMTUzNjg2NDk5MzY4.XwycWQ.sro0wc0HUIAO1dNvbh7LgMtK3_k"; //token used to hook into the Discord bot (bot token)	
	public static JDA jda;
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
	private static String membersInsert = "INSERT INTO countmembers (memberCount) \n VALUES(" + jda.getGuildById(735990134583066679L).getMemberCount() + ");";
	
	public static void main (String[] args) {
        
		
		try {
			jda = JDABuilder.createDefault(token)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
				.build();
			
			jda.addEventListener(new APICommands()); //The ServerCommands object contains classes with calls to the other 2 API's
			jda.addEventListener(new NonAPICommands()); //Basic commands with no API authentication
			new databaseTask(service, membersInsert).run();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
