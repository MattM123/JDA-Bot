package com.marcuzzo.JDABot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import java.util.ArrayList;
import java.util.List;
import commands.SlashCommands;

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
				.build().awaitReady();
			
			jda.addEventListener(new SlashCommands());
			Guild guild = jda.getGuildById(735990134583066679L);
			
			if (guild != null) {
				
				jda.updateCommands()
					.addCommands(Commands.slash("console", "Send a console command to the BTE Midwest minecraft server")
	                    .addOption(OptionType.STRING, "command", "The command to send to the server console", true))
				
					.addCommands(Commands.slash("remove", "Deletes messages in bulk")
	                    .addOption(OptionType.INTEGER, "amount", "The number of messages to remove", true))
				
					.addCommands(Commands.slash("apply", "Obtain trial builder permissions on the Minecraft server"))
				
					.addCommands(Commands.slash("ping", "Calculates Discord API response time"))
				
					.addCommands(Commands.slash("server", "View resource usage of the Minecraft server"))
				
					.addCommands(Commands.slash("map", "Displays map of states our team is building"))
				
					.addCommands(Commands.slash("measure", "Gives gif and video tutorial on measuring buildings"))
				
					.addCommands(Commands.slash("getapp", "View an application of a user who has previously applied to the team")						
						.addOption(OptionType.USER, "user", "The user whos applications you want to retrieve", true)
						.addOption(OptionType.INTEGER, "n", "Retrieve the n-th application from the users application history", true))
				
					.addCommands(Commands.slash("getapp", "Attempts to find the closest block pallete to the provided color")
						.addOption(OptionType.STRING, "hex", "Color value represented in hex", false)
						.addOption(OptionType.ATTACHMENT, "image", "The average color value of the image will be extracted", false)).queue();
						
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
