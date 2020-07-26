package commands;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerStatusCommand extends ListenerAdapter {
	

	private static  PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens");
	private static String name;


	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		UserServer server = api.getServersController().getServer("ef773a66");
		
		//UserServersController controller = api.getServersController();
		//UserServer server = controller.getServer("aaaa88");
		name = server.getName();
		
	//	EmbedBuilder embed = new EmbedBuilder();
		//embed.setTitle("Nebraska/Iowa Build Server Status");
	//	embed.addField("Server Name: " , name, true);
		
		if (event.getMessage().getContentRaw().equals("!server")) {
			
			if (name == null) {
				event.getChannel().sendMessage("I'm having trouble communicating with the panel!");
			}
			else {
				//	event.getChannel().sendMessage(embed.build());
				event.getChannel().sendMessage(name);
			}
		}
		
	}
}

