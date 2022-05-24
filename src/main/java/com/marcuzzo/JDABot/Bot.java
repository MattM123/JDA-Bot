package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.List;

import commands.APICommands;
import commands.NonAPICommands;

public class Bot {	
	private static String token = System.getenv("BOT_TOKEN"); //token used to hook into the Discord bot (bot token)	
	public static JDA jda;
	private static List<CommandData> cmds = new ArrayList<CommandData>();	
	
	public static void main (String[] args) {		
		try {
			jda = JDABuilder.createDefault(token)
				.setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
				.build();
			
			jda.addEventListener(new APICommands()); //The ServerCommands object contains classes with calls to the other 2 API's
			jda.addEventListener(new NonAPICommands()); //Basic commands with no API authentication
			
			cmds.add(new CommandData("user", "Shows information about a specific Discord User.")
                    .addOption(OptionType.USER, "user", "The user you want to get the information from."));
			
			jda.getGuildById(735990134583066679L).updateCommands().addCommands(cmds).queue();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
