package commands;

import java.awt.Color;
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
		embed.addField("!measure", "Measure tutorial derived from the BTE Support Bot", false);
		embed.addField("-----------------------------------------------------------------------------", "" , false);
		embed.addField("!iowa <minecraft username>", "Requires permissions to assign 'Iowa Builder' rank", false);
		embed.addField("!nebraska <minecraft username>", "Requires permissions to assign 'Nebraska Builder' rank", false);
		embed.addField("!applicant <minecraft username>", "Requires permissions to assign 'ApplicationInProgress' rank", false);
		embed.addField("!start", "Requires permissions to start the server", false);
		embed.addField("!stop", "Requires permissions to stop the server", false);
		embed.addField("!restart", "Requires permissions to restart the server", false);
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!commands")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!measure")) {
			EmbedBuilder measure = new EmbedBuilder();
			measure.setColor(Color.blue);
			measure.setImage("https://i.gyazo.com/thumb/1200/d58446cec35cc504bb36b749346041a9-gif.gif");
			event.getChannel().sendMessage(measure.build()).queue();

			 
			
		}		
	}
}
