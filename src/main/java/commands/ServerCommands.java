package commands;

import java.awt.Color;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.stanjg.ptero4j.PteroUserAPI;
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
	    
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		 Guild guild = event.getGuild(); 

		Long userID[] = {387330197420113930L, 97867804463599616L,
				195196317071310848L, 657036933083561995L, 387330197420113930L};
		

		
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
		
		//Server stats from crafty
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server")) {
			
			JsonElement allServers = JsonParser.parseString(crafty.getServerStats());
			JsonArray servers = allServers.getAsJsonArray();
					
			//Wisconsin status
			JsonElement ele = JsonParser.parseString(servers.get(3).toString());
			String status = "";
			String memory = ele.getAsJsonObject().get("memory_usage").toString().substring(1, ele.getAsJsonObject().get("memory_usage").toString().length() - 1);
			String players = "";
			
			if (crafty.getServerStats().contains("MalformedURLException") || crafty.getServerStats().contains("IOException") || crafty.getServerStats().contains("JSONException")
					|| crafty.getServerStats().contains("NoSuchAlgorithmException") || crafty.getServerStats().contains("KeyManagementException")) {
				
				event.getChannel().sendMessage(crafty.getServerStats().subSequence(0, 1000)).queue();
			}
			else {
				
				if (ele.getAsJsonObject().get("server_running").toString().equals("false")) {
					status = "OFFLINE";
				}
				else {
					status = "ONLINE";
				}
				
				if (!(ele.getAsJsonObject().get("players").toString().equals("\"[]\""))) {
					players = ele.getAsJsonObject().get("players").toString().substring(3, ele.getAsJsonObject().get("players").toString().length() - 3);
				}
				else {
					players = "There are currently no players online";
				}
				
				//Midwest status
				JsonElement ele1 = JsonParser.parseString(servers.get(1).toString());
				String status1 = "";
				String memory1 = ele1.getAsJsonObject().get("memory_usage").toString().substring(1, ele1.getAsJsonObject().get("memory_usage").toString().length() - 1);
				String players1 = "";
				
				if (ele.getAsJsonObject().get("server_running").toString().equals("false")) {
					status1 = "OFFLINE";
				}
				else {
					status1 = "ONLINE";
				}
				
				if (!(ele1.getAsJsonObject().get("players").toString().equals("\"[]\""))) {
					players1 = ele1.getAsJsonObject().get("players").toString().substring(3, ele1.getAsJsonObject().get("players").toString().length() - 3);
				}
				else {
					players1 = "There are currently no players online";
				}
				
				EmbedBuilder stats = new EmbedBuilder();
				stats.setTitle("Midwest Build Server Status");
				stats.setColor(Color.BLUE);
				stats.addField("Server Status", status1, false);
				stats.addField("CPU Usage", ele1.getAsJsonObject().get("cpu_usage") + "%", false);
				stats.addField("Memory Usage", memory1, false);
				stats.addField("Players Online", players1, false);
				stats.addField("ID", ele1.getAsJsonObject().get("id").toString(), false);
				
				
				EmbedBuilder stats2 = new EmbedBuilder();
			
				stats2.setTitle("Wisconsin Build Server Status");
				stats2.setColor(Color.BLUE);
				stats2.addField("Server Status", status, false);
				stats2.addField("CPU Usage", ele.getAsJsonObject().get("cpu_usage") + "%", false);
				stats2.addField("Memory Usage", memory, false);
				stats2.addField("Players Online", players, false);
				stats2.addField("ID", ele.getAsJsonObject().get("id").toString(), false);
				
				event.getChannel().sendMessage(stats2.build()).queue();
				event.getChannel().sendMessage(stats.build()).queue();
			}
		}
		//crafy test
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!test")) {
			if (!crafty.sendCommand("ping").contains("Command sent to console:")) {
				event.getChannel().sendMessage(crafty.sendCommand("lp user NovemberRain123 parent add administrator"));
			}
			else {
				event.getChannel().sendMessage("An error occured while sending the console command");
				event.getChannel().sendMessage(crafty.sendCommand("lp user NovemberRain123 parent add administrator"));
			}
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
				
				if ((usernameApplied.contains("Error Code: ") || usernameApplied.contains("MalformedURLException") 
						|| usernameApplied.contains("IOException") || usernameApplied.contains("JSONException")) && usernameApplied.length() > 30) {
					
					event.getChannel().sendMessage("There was an error with retrieveing the users' application data.").queue();
					event.getChannel().sendMessage(usernameApplied).queue();
				}
				
					List<Role> roles = event.getMember().getRoles();
					int temp = 0;
					
					//retreieves the member list test
					BTE.getMemberList(); 
					//if theres an exception in retrieving the member list then it stores the stacktrace of that exception in the API objects public string
					if (!BTE.stackTrace.equals("")) {
						event.getChannel().sendMessage(BTE.stackTrace).queue();
					}
					
					else {
						//If user ID exists in member list and builder role is not already assigned, give builder role
						
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
		
		if (event.getMessage().getContentRaw().startsWith("!getapp")) {
			
			char[] charArr = event.getMessage().getContentRaw().toCharArray();
			String user = "";
			String appNum = "";
			for (int i = 8; i < 25; i++) {
					user += charArr[i];
				}			
				appNum += charArr[28];
				
				event.getChannel().sendMessage("user: " + user).queue();
				event.getChannel().sendMessage("appNum: " + appNum);
		}
		
			
	}
		
}	

	
			


			




