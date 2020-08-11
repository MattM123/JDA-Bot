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
		embed.addField("!setpassword <currentpassword>", "Requires permissions to regenerate the password.", false);
		embed.addField("!server start", "SRequires permissions to start the server.", false);
		embed.addField("!server stop", "Requires permissions to stop the server.", false);
		embed.addField("!Server restart", "Requires permissions to restart the server.", false);
		embed.addField("!console <command>", "Requires permissions to send server console commands", false);
		embed.addField("!server status", "Shows the server status", false);
		embed.addField("!map", "Shows the current map of Midwest states that are being built", false);

		if (event.getMessage().getContentRaw().equals("!bothelp")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
	}
}
