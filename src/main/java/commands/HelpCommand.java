package commands;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Command List").setColor(Color.blue);
		embed.addField("!passwordgen" , "Generates an initial password for first time use with the bot.", false);
		embed.addField("!setpassword <currentpassword>", "Regenerates and resets the password.", false);
		embed.addField("!start <password>", "Starts the server.", false);
		embed.addField("!stop <password>", "Stops the server.", false);
		embed.addField("!restart <password>", "Restarts the server.", false);
		embed.addField("!console <password> <command>", "Sends server console commands", false);
		embed.addField("!server status", "Shows the server status of the Iowa/Nebraska build server", false);
		embed.addField("!map", "Shows the current map of Midwest states that are being built", false);

		if (event.getMessage().getContentRaw().equals("!commands")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
	}
}
