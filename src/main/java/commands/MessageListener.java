package commands;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!test")) {
			event.getChannel().sendMessage("Tested!").queue();
			
		}
	}
}

