package commands;

import java.awt.Color;
import java.io.File;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommands extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.blue);
		embed.setTitle("Command Information");
		embed.addField("!server status", "Shows the server status and resource usage.", false);
		embed.addField("!map", "Shows all of the states that are currently being built accorss BTE Midwest", false);
		
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!commands")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!measure")) {
			File file = new File("measurecommand.mp4");
			EmbedBuilder measure = new EmbedBuilder();
			measure.setImage("attachment://measurecommand.ext");
			
			event.getChannel().sendFile(file).embed(embed.build()).queue();

		}
		
		
		
	}
}
