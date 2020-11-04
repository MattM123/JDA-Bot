package commands;

import java.awt.Color;
import java.util.Map;

import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerState;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
import com.stanjg.ptero4j.entities.panel.admin.Server;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerCommands extends ListenerAdapter {

	private static String apikey = "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens";
	private static String serverID = "8f401af5";
	
	private static PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", apikey);	
	private static UserServer server = api.getServersController().getServer(serverID);
	
	private static PteroAdminAPI adminAPI = new PteroAdminAPI("https://witherpanel.com/", apikey);
	private static Server admin = (Server) adminAPI.getServersController().getServers(serverID);

	public static String getLog() {
		
		Map<String, String> test = admin.getContainer().getEnvironmentVariables();
		int str = test.size();
		//String[] arr = (String[]) test.values().toArray();
	//	for (int i = 0; i < test.values().size(); i++) {
	//		str += arr[i];
	//	}

		return "String Map Size: " + String.valueOf(str);
	}

	public static String serverName() {
		return server.getName();
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
		return s.getCpuUsage() + "/" + "100%";
	}
	
	public static String diskUsage() {
		ServerUsage s = server.getServerUsage();
		return s.getDiskUsage() + "MB/unlimited";
	}
	
	public static String memoryUsage() {
		ServerUsage s = server.getServerUsage();
		return s.getMemoryUsage() + "MB/" + server.getLimits().getMemory() + "MB";
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		String id = "387330197420113930";
		long idlong = Long.parseLong(id.toString());	
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status").setColor(Color.blue);
		embed.addField("Server Name: " , serverName(), false);
		embed.addField("Server Status: ", serverStatus(), false);
		embed.addField("CPU Usage: ", cpuUsage(), false);
		embed.addField("Disk Usage: ", diskUsage(), false);
		embed.addField("Memory Usage: ", memoryUsage(), false);
		
	
		//server status command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server status")) {
				event.getChannel().sendMessage(embed.build()).queue();
		}
		
		//server restart command
		if (event.getMessage().getContentRaw().startsWith("!restart")) {
			if (event.getAuthor().getIdLong() == idlong) {	
				server.restart();
				event.getChannel().sendMessage("Server Restarting...").queue();
			}	
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
			
		
	
		//server start command
		if (event.getMessage().getContentRaw().startsWith("!start")) {
			if (event.getAuthor().getIdLong() == idlong) {
				if (serverStatus().equals("ONLINE")) { 
					event.getChannel().sendMessage("Server already running!").queue();
				}
				else {
					server.start();
					event.getChannel().sendMessage("Server Starting...").queue();
				}
			}
			
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
		
		
		//server stop command
		if (event.getMessage().getContentRaw().startsWith("!stop")) {
			if (event.getAuthor().getIdLong() == idlong) {
				if (serverStatus().equals("OFFLINE")) {
					event.getChannel().sendMessage("Server already stopped!").queue();
				}
				else {
					server.stop();
					event.getChannel().sendMessage("Server Stopping...").queue();
				}
			}
				
			else {
				event.getChannel().sendMessage("Invalid permsissions.").queue();
			}
		}
		
		//Nebraska builder assign
		if (event.getMessage().getContentRaw().startsWith("!nebraska")) {
			if (event.getMessage().getAuthor().getIdLong() == idlong) {
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 10; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add builder");
				event.getChannel().sendMessage("Rank updated to Nebraska Builder for user " + namebuilder).queue();
			}
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
		
		//Iowa builder assign
		if (event.getMessage().getContentRaw().startsWith("!iowa")) {
			if (event.getMessage().getAuthor().getIdLong() == idlong) {
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 6; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add iowa-builder");
				event.getChannel().sendMessage("Rank updated to Iowa Builder for user " + namebuilder).queue();
			}
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
		
		//Kansas builder assign
		if (event.getMessage().getContentRaw().startsWith("!kansas")) {
			if (event.getMessage().getAuthor().getIdLong() == idlong) {
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 8; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add kansas-builder");
				event.getChannel().sendMessage("Rank updated to Kansas Builder for user " + namebuilder).queue();
			}
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}

		//missouri builder assign
		if (event.getMessage().getContentRaw().startsWith("!missouri")) {
			if (event.getMessage().getAuthor().getIdLong() == idlong) {
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 10; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add missouri-builder");
				event.getChannel().sendMessage("Rank updated to Missouri Builder for user " + namebuilder).queue();
			}
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
		//Applicant builder assign
		if (event.getMessage().getContentRaw().startsWith("!applicant")) {
			if (event.getMessage().getAuthor().getIdLong() == idlong) {
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 11; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add applicants");
				event.getChannel().sendMessage("Rank updated to Application In Progress for user " + namebuilder).queue();
			}
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!test")) {
			event.getChannel().sendMessage(getLog()).queue();
		}
		
	}	
}
			


			




