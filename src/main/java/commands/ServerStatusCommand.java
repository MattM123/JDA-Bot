package commands;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerStatusCommand extends ListenerAdapter {

	private static PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens");
	private static UserServer server = api.getServersController().getServer("ef773a66");
	
	public static String serverName() {
		String name = server.getName();
		return name;
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status");
		embed.addField("Test", "Test", false);
		embed.addField("Server Name: " , serverName(), true);
		
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server")) {
			event.getChannel().sendMessage(embed.build()).queue();
			
		}
		
	}
	
	public static void main (String[]args) {
		System.out.println(serverName());
	}
	
}

