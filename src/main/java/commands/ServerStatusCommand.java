package commands;

import java.util.List;

import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.controllers.admin.ServersController;
import com.stanjg.ptero4j.controllers.admin.UsersController;
import com.stanjg.ptero4j.controllers.user.UserServersController;
import com.stanjg.ptero4j.entities.panel.admin.Server;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerStatusCommand extends ListenerAdapter {
	

	private static  PteroAdminAPI api = new PteroAdminAPI("https://witherpanel.com/", "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens");
	private static String name;


	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		Server server = (Server) api.getServersController().getServers("ef773a66");
		name = server.getName();

		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status");
		embed.addField("Server Name: " , name, true);
		
		if (event.getMessage().getContentRaw().equals("!server")) {
			
			event.getChannel().sendMessage(embed.build());
		}
		
	}
	
	 public static void main(String[] args) {
		 System.out.println(name);
	 }

}

