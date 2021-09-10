package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.Random;

import commands.APICommands;
import commands.NonAPICommands;

public class Bot {	
	private static String token = System.getenv("BOT_TOKEN"); //token used to hook into the Discord bot (bot token)	
	public static JDA jda;

	
	public static void main (String[] args) {
        
		
		try {
			jda = JDABuilder.createDefault(token)
				.setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
				.build();
			
			jda.addEventListener(new APICommands()); //The ServerCommands object contains classes with calls to the other 2 API's
			jda.addEventListener(new NonAPICommands()); //Basic commands with no API authentication
			
			//Arraylist containing arrays of activities
			ArrayList<String[]> activities = new ArrayList<String[]>();
			String[] watch = {"the abyss", "you", "Rick and Morty", "the singularity come"};
			String[] play = {"buildtheearth.net", "Minecraft", "the bongos", "Generation Zero", "Roko's Basilisk"};
			String[] listen = {"lofi", "the screams", "dial-up"};
			activities.add(watch);
			activities.add(play);
			activities.add(listen);
			
			//random array
			Random rand1 = new Random();
			
			//random index inside array
			Random rand = new Random();
		
			while (true) {
				int getarray = rand1.nextInt(activities.size() - 0 + 1) + 0;
				int activity = rand.nextInt(activities.get(getarray).length - 0 + 1) + 0;
				
				if (activities.get(getarray) == watch) {
					jda.getPresence().setActivity(Activity.watching(watch[activity]));
				}
				else if (activities.get(getarray) == play) {
					jda.getPresence().setActivity(Activity.playing(play[activity]));
				}
				else if (activities.get(getarray) == listen) {
					jda.getPresence().setActivity(Activity.listening(watch[activity]));
				}
				jda.wait(60000);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
