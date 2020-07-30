package commands;

import java.awt.Color;
import java.util.Random;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerState;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerCommands extends ListenerAdapter {

	private static PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens");
	private static UserServer server = api.getServersController().getServer("ef773a66");
	//private long msgid;
	
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

//	public long lastMessageId(long id) {
//		id = msgid;
//		return msgid;
//	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		String id = "387330197420113930";
		long idlong = Long.parseLong(id);
		
		
		String namebuilder = "";
		String rankbuilder = "";
		String password = "password";
		String current = "password";
		

		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status").setColor(Color.blue);
		embed.addField("Server Name: " , serverName(), false);
		embed.addField("Server Status: ", serverStatus(), false);
		embed.addField("CPU Usage: ", cpuUsage(), false);
		embed.addField("Disk Usage: ", diskUsage(), false);
		embed.addField("Memory Ussage: ", memoryUsage(), false);
	
		//set password command		
		if (event.getMessage().getContentRaw().startsWith("!setpassword ", 0)) {
			for (int i = 0; i < event.getMessage().getContentRaw().length(); i++) {
				if (i == ' ') {
					current += i;
				}
			}
			if (current != password) {
				event.getChannel().sendMessage("Incorrect password.").queue();
			}
			else {
				User user = event.getMessage().getAuthor();
				Random rand = new Random();
				user.openPrivateChannel().complete().sendMessage("Your new password is: " + (CharSequence) rand.longs(1000000000, 999999999)).queue();
			}
		}



	
		//server status command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server status")) {
			event.getChannel().sendMessage(embed.build()).queue();	
		}
		
		//server restart command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server restart")) {	
			if (event.getAuthor().getIdLong() == idlong) {
				event.getChannel().sendMessage("Server Restarting...").queue();
				server.restart();
		}	
			else {
				event.getChannel().sendMessage("You do not have permission to alter the server state.").queue();
			}
		}
	
		//server start command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server start")) {
			if (serverStatus() == "ONLINE" && event.getAuthor().getIdLong() == idlong) {
				event.getChannel().sendMessage("Server already running!").queue();
			}
			
			else if (serverStatus() == "OFFLINE" && event.getAuthor().getIdLong() == idlong) {
				server.start();
				event.getChannel().sendMessage("Server Starting...").queue();
			}
			
			else {
				event.getChannel().sendMessage("You do not have permission to alter the server state.").queue();
			}
		}
		
		//server stop command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server stop")) {
			if (serverStatus() == "OFFLINE" && event.getAuthor().getIdLong() == idlong) {
				event.getChannel().sendMessage("Server already stopped!").queue();
			}
			
			else if (serverStatus() == "ONLINE" && event.getAuthor().getIdLong() == idlong) {
				server.stop();
				event.getChannel().sendMessage("Server Stopping...").queue();
			}
			
			else {
				event.getChannel().sendMessage("You do not have permission to alter the server state.").queue();
			}
		}
		
		//console commands
		if (event.getMessage().getContentRaw().startsWith("!console")) { //!console
			if (event.getMessage().getContentRaw().contains("parent") 
					&& event.getMessage().getContentRaw().contains("add") 
					&& event.getMessage().getContentRaw().contains("user")) { //contains parent/add/user
				if (event.getAuthor().getIdLong() == idlong) { //id check
					server.sendCommand(event.getMessage().getContentRaw().substring(8));
					
					char[] arr = event.getMessage().getContentRaw().toCharArray();
					
					for (int i = 17; i < arr.length; i++) {
						if (event.getMessage().getContentRaw().charAt(i) == ' ') {
							break;
						}
						else {
							namebuilder += event.getMessage().getContentRaw().charAt(i);
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
				}//id check
				else {
					event.getChannel().sendMessage("You do not have permission to send console commands.").queue();
					}
			}//contains parent/add/user
			else {
				server.sendCommand(event.getMessage().getContentRaw().substring(8));
				event.getChannel().sendMessage("Console command issued.").queue();
			}
		}//!console
	}
}
			
		

			




