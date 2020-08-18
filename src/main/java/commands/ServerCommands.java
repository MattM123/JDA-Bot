package commands;

import java.awt.Color;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerState;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerCommands extends ListenerAdapter {

	private static String apikey = "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens";
	private static String serverID = "ef773a66";
	private static PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", apikey);
	private static UserServer server = api.getServersController().getServer(serverID);
	//private StringBuilder stored = new StringBuilder(15);

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
	
//	public String passwordGen() {	
//		String generatedString = RandomStringUtils.random(15, true, true);
//		this.stored += generatedString;
		
//		return generatedString;
//	}
	
	
//	public String passwordStore() {
//		return this.stored.substring(1, 15);
//	}
	

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		String id = "387330197420113930";
		long idlong = Long.parseLong(id.toString());	
		
//		String generatedString = RandomStringUtils.random(15, true, true);
		String namebuilder = "";
		String rankbuilder = "";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status").setColor(Color.blue);
		embed.addField("Server Name: " , serverName(), false);
		embed.addField("Server Status: ", serverStatus(), false);
		embed.addField("CPU Usage: ", cpuUsage(), false);
		embed.addField("Disk Usage: ", diskUsage(), false);
		embed.addField("Memory Ussage: ", memoryUsage(), false);
		
	
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
		
		
		//console commands
		if (event.getMessage().getContentRaw().startsWith("!console")) { //!console
			if (event.getAuthor().getIdLong() == idlong) { //id check
				if (event.getMessage().getContentRaw().contains("parent") 
					&& event.getMessage().getContentRaw().contains("lp")
					&& event.getMessage().getContentRaw().contains("add") 
					&& event.getMessage().getContentRaw().contains("user")) { //contains lp/parent/add/user
		
						server.sendCommand(event.getMessage().getContentRaw().substring(25));
						char[] arr = event.getMessage().getContentRaw().toCharArray();
					
						for (int i = 17; i < arr.length; i++) {
							if (event.getMessage().getContentRaw().charAt(i - 2) == 'r'
								&& event.getMessage().getContentRaw().charAt(i - 3) == 'e'
								&& event.getMessage().getContentRaw().charAt(i - 4) == 's'
								&& event.getMessage().getContentRaw().charAt(i - 5) == 'u') {
								namebuilder = event.getMessage().getContentRaw().substring(event.getMessage().getContentRaw().charAt(i), ' ');
							}
						}
					
						for (int i = 0; i < arr.length; i++) {
							if (event.getMessage().getContentRaw().charAt(i) == 'a'
								&& event.getMessage().getContentRaw().charAt(i + 1) == 'd'
								&& event.getMessage().getContentRaw().charAt(i + 2) == 'd') {
								rankbuilder = event.getMessage().getContentRaw().substring(i + 4);
							}
						}
				
					event.getChannel().sendMessage("Server rank updated to " + rankbuilder + " for " + namebuilder).queue();
				}//contains lp/parent/add/user
				else {
					server.sendCommand(event.getMessage().getContentRaw().substring(25));
					event.getChannel().sendMessage("Console command issued.").queue();
					}
			}//id check
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}//!console
		
	}
	
}
			


			




