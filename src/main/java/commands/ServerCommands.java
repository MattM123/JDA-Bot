package commands;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerState;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class ServerCommands extends ListenerAdapter {

	private static String apikey = "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens";
	private static String serverID = "8f401af5";
	private static PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", apikey);	
	private static UserServer server = api.getServersController().getServer(serverID);
	private CraftyControllerAPI crafty = new CraftyControllerAPI("XMLQUX8L6WZF194VUOTH1C5RM7KJ5J53");
	private BuildTheEarthAPI BTE = new BuildTheEarthAPI("6d83c36acd1bb7301e64749b46ebddc2e3b64a67");

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
		return s.getCpuUsage() + "% / 100%";
	}
	
	public static String diskUsage() {
		NumberFormat format = NumberFormat.getInstance();
		ServerUsage s = server.getServerUsage();
		String GB = (s.getDiskUsage() / 1024 ) + "GB / ";
		String MB = format.format(s.getDiskUsage()) + "MB / ";
		String KB = format.format(s.getDiskUsage() * 1024) + "KB";
	
		return  GB + MB + KB;
	}
	
	public static String memoryUsage() {
		ServerUsage s = server.getServerUsage();
		return s.getMemoryUsage() + "MB / " + server.getLimits().getMemory() + "MB";
	}
	    
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		 Guild guild = event.getGuild(); 

		Long userID[] = {387330197420113930L, 97867804463599616L,
				195196317071310848L, 657036933083561995L, 387330197420113930L};
		
		
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status").setColor(Color.blue);
		embed.addField("Server Name: " , serverName(), false);
		embed.addField("Server Status: ", serverStatus(), false);
		embed.addField("CPU Usage: ", cpuUsage(), false);
		embed.addField("Disk Usage: ", diskUsage(), false);
		embed.addField("Memory Usage: ", memoryUsage(), false);
		
		/*
		//start server
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!start") && event.getAuthor().getId().equals("808088551861518388")) {
			PowerState b = server.getPowerState();
			event.getChannel().sendMessage(b.toString()).queue();
			
			if (b.toString().equals("ON")) {
				event.getChannel().sendMessage("Server is already online.").queue();
			}

			else if (b.toString().equals("OFF")) {
				event.getChannel().sendMessage("Starting server...").queue();
				server.sendCommand("start");
			}
		}
		*/
		//server status
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
		//event builder assign
		if (event.getMessage().getContentRaw().startsWith("!event")) {
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3] ||
					event.getMessage().getAuthor().getIdLong() == userID[4]) {
				
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 7; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add event-builder");
				event.getChannel().sendMessage("Rank updated to Event Builder for user " + namebuilder).queue();
			}
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
		
		//Applicant builder assign
		if (event.getMessage().getContentRaw().startsWith("!applicant")) {
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3]) {
				
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
		
		//crafty test
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!test")) {
			JsonElement ele = JsonParser.parseString(crafty.getServerStats().get(2).toString());
			event.getChannel().sendMessage(ele.getAsJsonArray().get(0).toString()).queue();
			
		
			
	
		}

		//give build perms based on presence on build team
		if (event.getMessage().getContentRaw().startsWith("!link")) {		
			
			//Parses minecraft username for later use
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String MCusername = "";
		
			for (int i = 6; i < chararr.length; i++) {
				MCusername += chararr[i];
			}
			
			
				//Getting username from application for input validation	
				String usernameApplied = BTE.getUsernameAppliedWith(event.getAuthor().getId());
				
				if (usernameApplied.contains("Error Code: ") || usernameApplied.contains("MalformedURLException") 
						|| usernameApplied.contains("IOException") || usernameApplied.contains("JSONException")) {
					
					event.getChannel().sendMessage("There was an error with retrieveing the users' application data.").queue();
					event.getChannel().sendMessage(usernameApplied).queue();
				}
			
				
				//If user ID exists in member list and builder role is not already assigned, give builder role
				
					List<Role> roles = event.getMember().getRoles();
				
					int temp = 0;
					for (int i = 0; i < BTE.getMemberList().size(); i++) {	
						if (roles.contains(guild.getRoleById(735991952931160104L)) && (MCusername.equalsIgnoreCase(usernameApplied))) {
							event.getChannel().sendMessage("You already have builder role! Assigning server rank.").queue();
							temp = 1;
							break;
						}
									
						else if (event.getAuthor().getIdLong() == BTE.getMemberList().get(i) && !roles.contains(guild.getRoleById(Long.parseLong("735991952931160104"))) && (MCusername.equalsIgnoreCase(usernameApplied))) {
							guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
							event.getChannel().sendMessage("You now have Builder role!").queue();
							temp = 1;
							break;
						}
						else if (!(MCusername.equalsIgnoreCase(usernameApplied))) {
							event.getChannel().sendMessage("The username you applied with and the one you used to run this command do not match.").queue();
							break;
						}
					}
	
					//if user has state role, assign corresponding minecraft server rank else have user get state role and run command again.
		
					if (temp == 1) {
						if (roles.contains(guild.getRoleById(735995176165834756L))) {
							server.sendCommand("lp user " + MCusername + " parent add kansas-builder");
							event.getChannel().sendMessage("Minecraft server rank updated to Kansas Builder for user " + MCusername).queue();
	
						}
						else if (roles.contains(guild.getRoleById(735995164493086720L))) {
							server.sendCommand("lp user " + MCusername + " parent add iowa-builder");
							event.getChannel().sendMessage("Minecraft server rank updated to Iowa Builder for user " + MCusername).queue();
	
						}
						else if (roles.contains(guild.getRoleById(735995136978321541L))) {
							server.sendCommand("lp user " + MCusername + " parent add nebraska-builder");
							event.getChannel().sendMessage("Minecraft server rank updated to Nebraska Builder for user " + MCusername).queue();
	
						}
						else if (roles.contains(guild.getRoleById(735995095773609986L))) {
							server.sendCommand("lp user " + MCusername + " parent add illinois-builder");
	
						}
						else if (roles.contains(guild.getRoleById(735995115113414656L))) {
							server.sendCommand("lp user " + MCusername + " parent add missouri-builder");
							event.getChannel().sendMessage("Minecraft server rank updated to Missouri Builder for user " + MCusername).queue();
	
						}
						else if (roles.contains(guild.getRoleById(735995196738633819L))) {
							server.sendCommand("lp user " + MCusername + " parent add minnesota-builder");
							event.getChannel().sendMessage("Minecraft server rank updated to Minnesota Builder for user " + MCusername).queue();
						}
						else if (roles.contains(guild.getRoleById(808415301799641119L))) {
							server.sendCommand("lp user " + MCusername + " parent add oklahoma-builder");
							event.getChannel().sendMessage("Minecraft server rank updated to Oklahoma Builder for user " + MCusername).queue();
						}
						
						else {
							event.getChannel().sendMessage("Looks like you don't have a state role. Go to #role-menu to select one and run the command again to get server build perms.").queue();
						}
					}
					
					//if user is not on the team at all, print this
					
					else if (temp == 0) {
						event.getChannel().sendMessage("Looks like you're not on the team or your username was invalid. If this is wrong, then ping mattress#1852").queue();
					}
		}
		
	}	
}	
	
			


			




