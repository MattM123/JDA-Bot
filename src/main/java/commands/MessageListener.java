package commands;
import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("BTE Midwest Map");
		embed.setColor(Color.magenta);
		embed.setImage("https://imgur.com/gallery/dTQ4r5e");
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!map")){
		}
		event.getChannel().sendMessage(embed.build());
	}
}

