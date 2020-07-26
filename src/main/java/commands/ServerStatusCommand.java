package commands;

import java.awt.Color;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerState;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
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
	
	public static String serverStatus() {
		PowerState b = server.getPowerState();
		String status = "";
		
		if (b.toString() == "ON") {
			status = "ONLINE";
		}
		
		else if (b.toString() == "OFF") {
			status = "OFFLINE";
		}
		return status;
	}
	
	
	public static String cpuUsage() {
		ServerUsage s = server.getServerUsage();
		return s.getCpuUsage() + "/" + server.getLimits().getCpu() + "%";
	}
	
	public static String diskUsage() {
		ServerUsage s = server.getServerUsage();
		return s.getDiskUsage() + "/" + server.getLimits().getDisk() + "MB";
	}
	
	public static String memoryUsage() {
		ServerUsage s = server.getServerUsage();
		return s.getMemoryUsage() + "/" + server.getLimits().getMemory() + "MB";
	
	}

	
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status").setColor(Color.blue);
		embed.addField("Server Name: " , serverName(), false);
		embed.addField("Server Status: ", serverStatus(), false);
		embed.addField("CPU Usage: ", cpuUsage(), false);
		embed.addField("Disk Usage: ", diskUsage(), false);
		embed.addField("Memory Ussage: ", memoryUsage(), false);
		
		
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server status")) {
			event.getChannel().sendMessage(embed.build()).queue();	
		}
		
		else if (event.getMessage().getContentRaw().equalsIgnoreCase("!server restart") && event.getMessage().getAuthor().getName() == "mattm") {
			server.restart();
			event.getChannel().sendMessage("Server Restarting...").queue();
		}
		
		else if (event.getMessage().getContentRaw().equalsIgnoreCase("!server start") && event.getMessage().getAuthor().getName() == "mattm") {
			if (serverStatus() == "ONLINE") {
				event.getChannel().sendMessage("Server already running!").queue();
			}
			else {
				server.start();
				event.getChannel().sendMessage("Server Starting...").queue();
			}
		}
		
		else if (event.getMessage().getContentRaw().equalsIgnoreCase("!server stop")) {
			server.stop();
			event.getChannel().sendMessage("Server Stopping...").queue();
		}
		
	}
	public static void main (String[]args) {
		System.out.println(cpuUsage());
	}
}

