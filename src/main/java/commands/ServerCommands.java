package commands;

import java.awt.Color;
import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerState;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
import com.stanjg.ptero4j.entities.panel.user.UserServer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class ServerCommands extends ListenerAdapter {

	private static String apikey = "NXRD3enHrACazTV2sXDERw7e2pPJYNPmK1YzVYJJ4XzdWens";
	private static String serverID = "8f401af5";
//	private Logger logger = LoggerFactory.getLogger(ServerCommands.class);
	private static PteroUserAPI api = new PteroUserAPI("https://witherpanel.com/", apikey);	
	private static UserServer server = api.getServersController().getServer(serverID);
	



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
		
		//6d83c36acd1bb7301e64749b46ebddc2e3b64a67
		String id = "387330197420113930";
		Long userID[] = {Long.parseLong("387330197420113930"), Long.parseLong("97867804463599616"),
				Long.parseLong("195196317071310848"), Long.parseLong("657036933083561995")};
		
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
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3]) {
				
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 10; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add nebraska-builder");
				event.getChannel().sendMessage("Rank updated to Nebraska Builder for user " + namebuilder).queue();
			}
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
		
		//Iowa builder assign
		if (event.getMessage().getContentRaw().startsWith("!iowa")) {
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3]) {
				
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
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3]) {
				
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
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3]) {
				
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
		
		//illinois builder assign
		if (event.getMessage().getContentRaw().startsWith("!illinois")) {
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3]) {
				
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 10; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add illinois-builder");
				event.getChannel().sendMessage("Rank updated to Illinois Builder for user " + namebuilder).queue();
			}
			else {
				event.getChannel().sendMessage("Invalid permissions.").queue();
			}
		}
		
		//event builder assign
		if (event.getMessage().getContentRaw().startsWith("!event")) {
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3]) {
				
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
		
		//minnesota builder assign
		if (event.getMessage().getContentRaw().startsWith("!minnesota")) {
			if (event.getMessage().getAuthor().getIdLong()== userID[0] || event.getMessage().getAuthor().getIdLong() == userID[1] ||
					event.getMessage().getAuthor().getIdLong() == userID[2] || event.getMessage().getAuthor().getIdLong() == userID[3]) {
				
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 11; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}
				
				server.sendCommand("lp user " + namebuilder + " parent add minnesota-builder");
				event.getChannel().sendMessage("Rank updated to Minnesota Builder for user " + namebuilder).queue();
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
		
		
		
		if (event.getMessage().getContentRaw().startsWith("!link")) {
			
			//Parses minecraft username for later use
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String MCusername = "";
		
			for (int i = 6; i < chararr.length; i++) {
				MCusername += chararr[i];
			}
			event.getChannel().sendMessage(MCusername);
			
				Guild guild = event.getGuild();
				String line;
				BufferedReader in; 
				StringBuilder json = new StringBuilder();
				URL url;
				HttpsURLConnection conn = null;
				JsonArray jarray = null;
				
				try {
					//BTE API Authentication
					url = new URL("https://buildtheearth.net/api/v1/members");
					conn = (HttpsURLConnection) url.openConnection();
					conn.setRequestProperty("Host","buildtheearth.net");
					conn.setRequestProperty("Authorization", "Bearer 6d83c36acd1bb7301e64749b46ebddc2e3b64a67");
					conn.setRequestProperty("Accept", "application/json");
					conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
					conn.setRequestMethod("GET");
					
					//Storing JSON from request into a JSON Array
					
					if (conn.getResponseCode() > 200) {
						event.getChannel().sendMessage("Error Code: " + String.valueOf(conn.getResponseCode())).queue();
					}
					
					
					in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					if ((line = in.readLine()) != null) {
						json.append(line);
					}
					in.close();
					
					//parsing JSON Array
					
					JsonElement ele = JsonParser.parseString(json.toString());
					jarray = ele.getAsJsonObject().getAsJsonArray("members");
					
					
				} catch (MalformedURLException e) {
					String stack = ExceptionUtils.getStackTrace(e);
					event.getChannel().sendMessage(stack.subSequence(0, 1000)).complete();
				} catch (IOException e) {
					String stack = ExceptionUtils.getStackTrace(e);
					event.getChannel().sendMessage(stack.subSequence(0, 1000)).complete();
				} catch (JSONException e) {
					String stack = ExceptionUtils.getStackTrace(e);
					event.getChannel().sendMessage(stack.subSequence(0, 1000)).complete();
				}
				
				//Creation of arraylist with user IDs
				
				ArrayList<Long> ids = new ArrayList<Long>();
				for (int i = 0; i < jarray.size(); i++) {
					ids.add(jarray.get(i).getAsJsonObject().get("discordId").getAsLong());
				}
				
				//If user ID exists in array, give builder role
				
				event.getChannel().sendMessage("breakpoint1").queue();
				
				ArrayList<Role> roles = (ArrayList<Role>) event.getMember().getRoles();
				event.getChannel().sendMessage(roles.get(0).getName()).queue();
				int temp = 0;
				for (int i = 0; i < ids.size(); i++) {
					if (event.getAuthor().getIdLong() == ids.get(i) && !roles.contains(guild.getRoleById(Long.parseLong("735991952931160104")))) {
						guild.addRoleToMember(event.getMember(), guild.getRoleById(Long.parseLong("735991952931160104")));
						event.getChannel().sendMessage("You now have builder Role!").queue();
						temp = 1;
						break;
					}		
					else if (roles.contains(guild.getRoleById(Long.parseLong("735991952931160104")))) {
						event.getChannel().sendMessage("You already have builder role! Assigning server rank.").queue();
						temp = 1;
						break;
					}
				}
				event.getChannel().sendMessage("breakpoint2").queue();
				//if user has state role, assign corresponding minecraft server rank
				
				ArrayList<Member> iowa = (ArrayList<Member>) guild.getMembersWithRoles(guild.getRoleById(Long.parseLong("735995164493086720")));
				ArrayList<Member> kansas = (ArrayList<Member>) guild.getMembersWithRoles(guild.getRoleById(Long.parseLong("735995176165834756")));
				ArrayList<Member> nebraska = (ArrayList<Member>) guild.getMembersWithRoles(guild.getRoleById(Long.parseLong("735995136978321541")));
				ArrayList<Member> illinois = (ArrayList<Member>) guild.getMembersWithRoles(guild.getRoleById(Long.parseLong("735995095773609986")));
				ArrayList<Member> missouri = (ArrayList<Member>) guild.getMembersWithRoles(guild.getRoleById(Long.parseLong("735995115113414656")));
				ArrayList<Member> minnesota = (ArrayList<Member>) guild.getMembersWithRoles(guild.getRoleById(Long.parseLong("735995196738633819")));
	
				if (temp == 1) {
					if (kansas.contains(event.getMember())) {
						server.sendCommand("lp user " + MCusername + " parent add kansas-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Kansas Builder for user " + MCusername).queue();
						}
					else if (iowa.contains(event.getMember())) {
						server.sendCommand("lp user " + MCusername + " parent add iowa-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Iowa Builder for user " + MCusername).queue();
						}
					else if (nebraska.contains(event.getMember())) {
						server.sendCommand("lp user " + MCusername + " parent add nebraska-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Nebraska Builder for user " + MCusername).queue();
						}
					else if (illinois.contains(event.getMember())) {
						server.sendCommand("lp user " + MCusername + " parent add illinois-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Illinois Builder for user " + MCusername).queue();
						}
					else if (missouri.contains(event.getMember())) {
						server.sendCommand("lp user " + MCusername + " parent add missouri-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Missouri Builder for user " + MCusername).queue();
						}
					else if (minnesota.contains(event.getMember())) {
						server.sendCommand("lp user " + MCusername + " parent add minnesota-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Minnesota Builder for user " + MCusername).queue();
						}
					else {
						event.getChannel().sendMessage("Looks like you don't have a state role. Go to #role-menu to select one and run the command again.").queue();
					}
				}
				
				else if (temp == 0) {
					event.getChannel().sendMessage("Looks like you're not on the team or we havn't gotten to your application yet. If this is wrong, then ping mattress#1852").queue();
				}
		}
	
	}	
	
}
			


			




