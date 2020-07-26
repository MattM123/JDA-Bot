package commands;

import java.awt.Color;

import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.entities.panel.admin.Server;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerStatusCommand extends ListenerAdapter {
	private  PteroAdminAPI api = new PteroAdminAPI("https://panel.pterodactyl.io/", "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens");
	private Boolean g;
	
	@Override
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
	
		
		 Server server = api.getServersController().getServer(12 /* Server ID */); 
		 Boolean truthBool = server.isSuspended();
		 String truthString = truthBool.toString();
		 
		 EmbedBuilder embed = new EmbedBuilder();
		 
		 if (truthString == "false") {
			 embed.addField("Status: ", "Offline", true);
		 }
		 
		 else if (truthString == "true") {
			 embed.addField("Status: ", "Online", true);
		 }
		 
		
		if (event.getMessage().getContentRaw().startsWith("!server status")) {
			
			event.getChannel().sendMessage(embed.build());
		}
		
	}

}
