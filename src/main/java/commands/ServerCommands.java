package commands;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerState;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
		
		//countdown
		if (event.getMessage().getContentRaw().startsWith("!countdown")) {
			String timebuilder = "";
		//	SimpleDateFormat format = new SimpleDateFormat("HH:MM:SS");
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			
			for (int i = 11; i < chararr.length; i++) {
				timebuilder += chararr[i];
			}
			
			int hour = Integer.parseInt(timebuilder.substring(0, 1));
			int minute = Integer.parseInt(timebuilder.substring(3, 4));
			int second = Integer.parseInt(timebuilder.substring(6, 7));
			
			for (int i = 0; i < 172800; i++ ) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				event.getChannel().sendMessage(String.valueOf(hour--) + ":" + String.valueOf(minute--) + ":" + String.valueOf(second--)).queue();
		//		hour--;
		//		minute--;
		//		second--;
			
				if ( hour > -1) {
					if (minute == -1) {
						minute = 59;
					}				
					if (second == -1) {
						second = 59;
					}			
				}
				else
				{
					break;
				}
			}
			event.getChannel().sendMessage("Countdown complete!").queue();
		}
	}
	
}
			


			




