package commands;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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

			EmbedBuilder measure = new EmbedBuilder();
			InputStream file = null;
			try {
				file = new URL("https://i.gyazo.com/d58446cec35cc504bb36b749346041a9.mp4").openStream();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			measure.setImage("attachment://measurecommand.mp4"); // we specify this in sendFile as "cat.png"
			    
			event.getChannel().sendFile(file, "measurecommand.mp4").embed(measure.build()).queue();

		}		
	}
}
