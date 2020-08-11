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
		embed.addField("!passwordgen" , "Generates an initial password for first time use with the bot", false);
		embed.addField("!setpassword <currentpassword>", "Requires permissions to regenerate the password", false);
		embed.addField("!server status", "Shows the server status", false);
		embed.addField("!server start", "Starts the server. Requires permissions", false);
		embed.addField("!server stop", "Stops the server. Requires permissions", false);
		embed.addField("!Server restart", "Restarts the server. Requires permissions", false);

		if (event.getMessage().getContentRaw().equals("!help")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
	}
}
