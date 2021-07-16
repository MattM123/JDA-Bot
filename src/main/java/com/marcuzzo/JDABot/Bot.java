package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import commands.APICommands;
import commands.NonAPICommands;
import database.databaseManipulator;

public class Bot {	
	private static String token = "NzMyMjkwMTUzNjg2NDk5MzY4.XwycWQ.DV6g4DyHxlI01yxROtKMggZFW_Q"; //token used to hook into the Discord bot (bot token)
	private static JDA jda;

	public static void main (String[] args) {
        
		
		try {
			jda = JDABuilder.createDefault(token)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
				.build();
			
			jda.addEventListener(new APICommands()); //The ServerCommands object contains classes with calls to the other 2 API's
			jda.addEventListener(new NonAPICommands()); //Basic commands with no API authentication
			
			new java.util.Timer().schedule( 
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			            	String memberCountSQL = "INSERT INTO members (memberCount)\n"
													+ "VALUES(" + String.valueOf(jda.getGuildById(735990134583066679L).getMemberCount()) + ")";
			            	
			            	databaseManipulator.sendSQLStatement(memberCountSQL);
			            }
			        }, 
			        10000
			);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
