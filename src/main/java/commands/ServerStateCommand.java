package commands;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerStateCommand extends ListenerAdapter {
	private static PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens");
	private static UserServer server = api.getServersController().getServer("ef773a66");
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server restart") && event.getMessage().getAuthor().getName() == "mattm") {
			server.restart();
			event.getChannel().sendMessage("Server Restarting...").queue();
		}
	}
}
