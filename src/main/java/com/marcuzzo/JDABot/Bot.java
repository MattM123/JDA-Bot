package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
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
	
	//private CommandClientBuilder cmds = new CommandClientBuilder();
	static List<CommandData> cmds = new ArrayList<CommandData>();
	
	public static void main (String[] args) {		
		try {
			jda = JDABuilder.createDefault(token)
				.setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
				.build();
			
			Guild guild = jda.getGuildById(735990134583066679L);
			jda.addEventListener(new APICommands()); //The ServerCommands object contains classes with calls to the other 2 API's
			jda.addEventListener(new NonAPICommands()); //Basic commands with no API authentication
			
			if (guild != null) {
		
				guild.upsertCommand(new CommandData("console", "Send a console command to the BTE Midwest minecraft server")
	                    .addOption(OptionType.STRING, "console command", "The command to send to the server console")).queue();
				
				guild.upsertCommand(new CommandData("apply", "Obtain trial builder permissions on the Minecraft server")).queue();
				
				guild.upsertCommand(new CommandData("server", "View resource usage of the Minecraft server"));
				
				guild.upsertCommand(new CommandData("getapp", "View an application of a user who has previously applied to the team")
						.addOption(OptionType.USER, "user", "The user whos applications you want to retrieve")
						.addOption(OptionType.INTEGER, "n", "Retrieve the n-th application from the users application history"));
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
