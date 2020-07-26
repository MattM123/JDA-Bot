package commands;

import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.controllers.admin.ServersController;
import com.stanjg.ptero4j.entities.panel.admin.Server;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerStatusCommand extends ListenerAdapter {
	private static  PteroAdminAPI api = new PteroAdminAPI("https://witherpanel.com/", "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens");
	
	
	@Override
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		 

		 
	//	 if (statusString == "false") {
		//	 embed.addField("Status: ", "Offline", true);
	//	 }
	//	 
	//	 else if (statusString == "true") {
	//		 embed.addField("Status: ", "Online", true);
	//	 }
		Server server = api.getServersController().getServer(12 /* Server ID */); 
		String name = server.getName();
		
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.addField("Server Name: " , name, true);
		if (event.getMessage().getContentRaw().equals("!server status")) {
			
			event.getChannel().sendMessage(embed.build());
		}
		
	}
	
//	public static void main (String[]args) {
//		
//	}
}

