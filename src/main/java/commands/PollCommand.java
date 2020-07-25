package commands;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PollCommand extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		String g = event.getMessage().getContentRaw().substring(12);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(g);
		embed.setColor(Color.magenta);
		embed.addField("Yes: ", "<:Minnesota:736378489137135748>", true);
		embed.addField("No: ", "<:Missouri:736378456333615206>", true);

		
		if (event.getMessage().getContentRaw().startsWith("!simplepoll") && event.getMessage().getContentRaw().endsWith("?")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
	}

}
