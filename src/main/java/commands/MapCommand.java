package commands;
import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MapCommand extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("BTE Midwest Map");
		embed.setColor(Color.blue);
		embed.setImage("https://i.imgur.com/meaamm7.png");
		embed.addField("Made by: ", "MN Admin | Mr Jew", true);
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!map")){
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
	}
}

