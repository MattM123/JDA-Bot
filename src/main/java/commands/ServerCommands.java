package commands;

import java.awt.Color;

import org.apache.commons.lang.RandomStringUtils;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerState;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerCommands extends ListenerAdapter {

	private static String apikey = "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens";
	private static String serverID = "";
	private static PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", apikey);
	//NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens
	private static UserServer server = api.getServersController().getServer(serverID);
	//ef773a66
	

	private StringBuilder stored = new StringBuilder(15);

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
		//387330197420113930
		long idlong = Long.parseLong(id);		
		
		String generatedString = RandomStringUtils.random(15, true, true);
		String namebuilder = "";
		String rankbuilder = "";
		String passwordkeyed = "";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status").setColor(Color.blue);
		embed.addField("Server Name: " , serverName(), false);
		embed.addField("Server Status: ", serverStatus(), false);
		embed.addField("CPU Usage: ", cpuUsage(), false);
		embed.addField("Disk Usage: ", diskUsage(), false);
		embed.addField("Memory Ussage: ", memoryUsage(), false);
	
		//set command perms
		if (event.getMessage().getContentRaw().startsWith("!setperms")) {
			String perm = "";
			String pass = "";
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			for (int i = 0; i < event.getMessage().getContentRaw().length(); i++) {
				if (chararr[9] == ' ') {
					perm += event.getMessage().getContentRaw().substring(10, 28);
				}
				else {
					event.getChannel().sendMessage("Invalid syntax.").queue();
					break;
				}
				
				if (chararr[29] == ' ') {
					pass += event.getMessage().getContentRaw().substring(30, 45);
				}
				
				else {
					event.getChannel().sendMessage("Invalid syntax.").queue();
					break;
				}			
			}
			
			
			if (pass.equals(this.stored.toString())) {
				id.replace(".*", perm);
				event.getChannel().sendMessage("Permissions set.").queue();
			}
			
			else {
				event.getChannel().sendMessage("Wrong password.").queue();
			}
		}
		//set api key
		if (event.getMessage().getContentRaw().startsWith("!apikey") && idlong == event.getMessage().getAuthor().getIdLong()) {
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			for (int i = 0; i < event.getMessage().getContentRaw().length(); i++) {
				if (chararr[i] == ' ') {
					apikey += event.getMessage().getContentRaw().substring(7);
				}
			}
			event.getChannel().sendMessage("API key set.").queue();
		}
		//change api key
		if (event.getMessage().getContentRaw().startsWith("!changekey") && idlong == event.getMessage().getAuthor().getIdLong()) {
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String apistore = "";
			for (int i = 0; i < event.getMessage().getContentRaw().length(); i++) {
				if (chararr[i] == ' ') {
					apistore += event.getMessage().getContentRaw().substring(11);
				}
			}
			apikey.replace(".*", apistore);
			event.getChannel().sendMessage("API key changed.").queue();
		}
		
		//set server ID
		if (event.getMessage().getContentRaw().startsWith("!serverid") && idlong == event.getMessage().getAuthor().getIdLong()) {
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			for (int i = 0; i < event.getMessage().getContentRaw().length(); i++) {
				if (chararr[i] == ' ') {
					serverID += event.getMessage().getContentRaw().substring(10);
				}
			}
			event.getChannel().sendMessage("Server ID set.").queue();
		}
		
		//change server ID
		if (event.getMessage().getContentRaw().startsWith("!changeid") && idlong == event.getMessage().getAuthor().getIdLong()) {
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String idstore = "";
			for (int i = 0; i < event.getMessage().getContentRaw().length(); i++) {
				if (chararr[i] == ' ') {
					idstore += event.getMessage().getContentRaw().substring(10);
				}
			}
			apikey.replace(".*", idstore);
			event.getChannel().sendMessage("Server ID changed.").queue();
		}
		
		
		//password generator
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!passwordgen")) {
			if (this.stored.toString().isEmpty() == true) {
				this.stored.replace(0, 14, generatedString);
				User user = event.getMessage().getAuthor();
				user.openPrivateChannel().complete()
		    		.sendMessage("Your new password is: " + this.stored + ". You can change your password in the future with !setpassword <currentpassword>.").queue();
			}
			else {
				event.getChannel().sendMessage("An initial password has already been set.").queue();
			}
		}
		
		//set password command		
		if (event.getMessage().getContentRaw().startsWith("!setpassword")) {	
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			
			for (int i = 0; i < chararr.length; i++) {
				if (chararr[i] == ' ') {
					passwordkeyed += event.getMessage().getContentRaw().substring(13);
				}
			}
			
			if (!(passwordkeyed.equals(this.stored.toString()))) {
			event.getChannel().sendMessage("Incorrect password.").queue();
		}
			
			if (passwordkeyed.equals(this.stored.toString()) && !(passwordkeyed.isEmpty())) {
				this.stored.replace(0, 14, generatedString);
	
				event.getChannel().sendMessage("Check your DMs!").queue();
				
				User user1 = event.getMessage().getAuthor();
			    user1.openPrivateChannel().complete()
			    	.sendMessage("Your new password is: " + this.stored).queue();  
			}
			
				if (this.stored.toString().isEmpty()) {
				event.getChannel().sendMessage("Please use !passwordgen to generate an initial password.").queue();
			}
		}
		
	
		//server status command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server status")) {
			if (apikey.isEmpty() || serverID.isEmpty()) {
				event.getChannel().sendMessage("Set the api key and server ID with !apikey <apikey> and !serverid <serverID> first!").queue();
			}
			event.getChannel().sendMessage(embed.build()).queue();	
		}
		
		//server restart command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server restart")) {	
			if (event.getAuthor().getId() == id) {
				event.getChannel().sendMessage("Server Restarting...").queue();
				server.restart();
		}	
			else {
				event.getChannel().sendMessage("You do not have permission to alter the server state.").queue();
			}
		}
	
		//server start command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server start")) {
			if (serverStatus() == "ONLINE" && event.getAuthor().getId() == id) {
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
			if (serverStatus() == "OFFLINE" && event.getAuthor().getId() == id) {
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
			


			




