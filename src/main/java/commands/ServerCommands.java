package commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
				195196317071310848L, 657036933083561995L};
		
		
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Nebraska/Iowa Build Server Status").setColor(Color.blue);
		embed.addField("Server Name: " , serverName(), false);
		embed.addField("Server Status: ", serverStatus(), false);
		embed.addField("CPU Usage: ", cpuUsage(), false);
		embed.addField("Disk Usage: ", diskUsage(), false);
		embed.addField("Memory Usage: ", memoryUsage(), false);
		
		
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
		
		//server status
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!server")) {
			event.getChannel().sendMessage(embed.build()).queue();
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

		//give build perms based on presence on build team
		if (event.getMessage().getContentRaw().startsWith("!link")) {
			
			//Parses minecraft username for later use
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String MCusername = "";
		
			for (int i = 6; i < chararr.length; i++) {
				MCusername += chararr[i];
			}
			
			//Authentication to application to retrieve the username they applied with
			
			String line;
			BufferedReader in; 
			StringBuilder json = new StringBuilder();
			URL url;
			HttpsURLConnection conn = null;
			ArrayList<AnswerInfo> answers = null;
			String usernameAppliedWith = null;
			
			try {
				url = new URL("https://buildtheearth.net/api/v1/applications/" + event.getAuthor().getId());
				conn = (HttpsURLConnection) url.openConnection();
				conn.setRequestProperty("Host","buildtheearth.net");
				conn.setRequestProperty("Authorization", "Bearer 6d83c36acd1bb7301e64749b46ebddc2e3b64a67");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
				conn.setRequestMethod("GET");
				
				//Storing JSON from request into string. Prints error code and error stream if encountered.
				
				if (conn.getResponseCode() > 200) {
					event.getChannel().sendMessage("Error Code: " + String.valueOf(conn.getResponseCode())).queue();
					in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					while ((line = in.readLine()) != null) {
						json.append(line);
					}
					in.close();
					event.getChannel().sendMessage(json.toString()).queue();
				}
				
				
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				if ((line = in.readLine()) != null) {
					json.append(line);
				}
				in.close();
				
				//JSON Deserialization
				
				Gson gson = new Gson();
				ApplicationInfo applicationArray = gson.fromJson(json.toString(), ApplicationInfo.class);  
				 
				//retrieving username from application answers
				
				answers = (ArrayList<AnswerInfo>) applicationArray.getApplications().get(0).getAnswerList();
				usernameAppliedWith = answers.get(4).getAnswer();
				

				
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
			
			//Authenticating to members endpoint to check if user is on team
			
				String line2;
				BufferedReader in2; 
				StringBuilder json2 = new StringBuilder();
				URL url2;
				HttpsURLConnection conn2 = null;
				JsonArray jarray = null;
				
				//BTE API Authentication, member and applications endpoint
			
				try {
					url2 = new URL("https://buildtheearth.net/api/v1/members");
					conn2 = (HttpsURLConnection) url2.openConnection();
					conn2.setRequestProperty("Host","buildtheearth.net");
					conn2.setRequestProperty("Authorization", "Bearer 6d83c36acd1bb7301e64749b46ebddc2e3b64a67");
					conn2.setRequestProperty("Accept", "application/json");
					conn2.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
					conn2.setRequestMethod("GET");
					
					//Storing JSON from request into a JSON Array. Prints error code and error stream if encountered.
					
					if (conn2.getResponseCode() > 200) {
						event.getChannel().sendMessage("Error Code: " + String.valueOf(conn2.getResponseCode())).queue();
						in2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
						while ((line2 = in2.readLine()) != null) {
							json2.append(line2);
						}
						in2.close();
						event.getChannel().sendMessage(json2.toString()).queue();
					}
					
					
					in2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
					if ((line2 = in2.readLine()) != null) {
						json2.append(line2);
					}
					in2.close();
					
					//parsing JSON Element to JSON Array
					
					JsonElement ele = JsonParser.parseString(json2.toString());
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
				
				//Creation of arraylist with user IDs from API request
				
				ArrayList<Long> ids = new ArrayList<Long>();
				for (int i = 0; i < jarray.size(); i++) {
					ids.add(jarray.get(i).getAsJsonObject().get("discordId").getAsLong());
				}
					
				//If user ID exists in array and builder role is not already assigned, give builder role
			
				List<Role> roles = event.getMember().getRoles();
			
				int temp = 0;
				for (int i = 0; i < ids.size(); i++) {	
					if (roles.contains(guild.getRoleById(735991952931160104L)) && (MCusername.equalsIgnoreCase(usernameAppliedWith))) {
						event.getChannel().sendMessage("You already have builder role! Assigning server rank.").queue();
						temp = 1;
						break;
					}
								
					else if (event.getAuthor().getIdLong() == ids.get(i) && !roles.contains(guild.getRoleById(Long.parseLong("735991952931160104"))) && (MCusername.equalsIgnoreCase(usernameAppliedWith))) {
						guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
						event.getChannel().sendMessage("You now have Builder role!").queue();
						temp = 1;
						break;
					}
					else if (!(MCusername.equalsIgnoreCase(usernameAppliedWith))) {
						event.getChannel().sendMessage("The username you applied with and the one you used to run this command do not match.").queue();
						event.getChannel().sendMessage("Command run with:" + MCusername + " | Applied with: " + usernameAppliedWith).queue();
						break;
					}
				}

				//if user has state role, assign corresponding minecraft server rank else have user get state role and run command again.
	
				if (temp == 1) {
					if (roles.contains(guild.getRoleById(735995176165834756L))) {
						server.sendCommand("lp user " + MCusername + " parent add kansas-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Kansas Builder for user " + MCusername).queue();
					//	if (event.getChannel().equals(event.getGuild().getGuildChannelById(802232830129995847L))) {
					//		if (event.getMessage().getContentRaw().contains("A user for") && (event.getMessage().getContentRaw().contains("could not be found"))) {
					//			event.getChannel().sendMessage("Rank failed to assign, user UUID could not be found in server cache").queue();
					//		}
					//	}
						}
					else if (roles.contains(guild.getRoleById(735995164493086720L))) {
						server.sendCommand("lp user " + MCusername + " parent add iowa-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Iowa Builder for user " + MCusername).queue();
					//	if (event.getChannel().equals(event.getGuild().getGuildChannelById(802232830129995847L))) {
					//		if (event.getMessage().getContentRaw().contains("A user for") && (event.getMessage().getContentRaw().contains("could not be found"))) {
					//			event.getChannel().sendMessage("Rank failed to assign, user UUID could not be found in server cache").queue();
					//		}
					//	}
						}
					else if (roles.contains(guild.getRoleById(735995136978321541L))) {
						server.sendCommand("lp user " + MCusername + " parent add nebraska-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Nebraska Builder for user " + MCusername).queue();
					//	if (event.getChannel().equals(event.getGuild().getGuildChannelById(802232830129995847L))) {
					//		if (event.getMessage().getContentRaw().contains("A user for") && (event.getMessage().getContentRaw().contains("could not be found"))) {
					//			event.getChannel().sendMessage("Rank failed to assign, user UUID could not be found in server cache").queue();
					//		}
					//	}
						}
					else if (roles.contains(guild.getRoleById(735995095773609986L))) {
						server.sendCommand("lp user " + MCusername + " parent add illinois-builder");
					//	event.getChannel().sendMessage("Minecraft server rank updated to Illinois Builder for user " + MCusername).queue();
					//	if (event.getChannel().equals(event.getGuild().getGuildChannelById(802232830129995847L))) {
					//		if (event.getMessage().getContentRaw().contains("A user for") && (event.getMessage().getContentRaw().contains("could not be found"))) {
					//			event.getChannel().sendMessage("Rank failed to assign, user UUID could not be found in server cache").queue();
					//		}
					//	}
						}
					else if (roles.contains(guild.getRoleById(735995115113414656L))) {
						server.sendCommand("lp user " + MCusername + " parent add missouri-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Missouri Builder for user " + MCusername).queue();
					//	if (event.getChannel().equals(event.getGuild().getGuildChannelById(802232830129995847L))) {
					//		if (event.getMessage().getContentRaw().contains("A user for") && (event.getMessage().getContentRaw().contains("could not be found"))) {
					//			event.getChannel().sendMessage("Rank failed to assign, user UUID could not be found in server cache").queue();
					//		}
					//	}
						}
					else if (roles.contains(guild.getRoleById(735995196738633819L))) {
						server.sendCommand("lp user " + MCusername + " parent add minnesota-builder");
						event.getChannel().sendMessage("Minecraft server rank updated to Minnesota Builder for user " + MCusername).queue();
					//	if (event.getChannel().equals(event.getGuild().getGuildChannelById(802232830129995847L))) {
					//		if (event.getMessage().getContentRaw().contains("A user for") && (event.getMessage().getContentRaw().contains("could not be found"))) {
					//			event.getChannel().sendMessage("Rank failed to assign, user UUID could not be found in server cache").queue();
					//		}
					//	}
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
	
		
		//reads server console and sends server message when corrupted areas have been encountered
		
		//if (event.getChannel().equals(event.getGuild().getGuildChannelById(802232830129995847L))) {
	//		if (event.getMessage().getContentRaw().contains("Cube is corrupted!") && (event.getMessage().getContentRaw().contains("Cube will be regenerated")
	//			&& (event.getMessage().getContentRaw().contains("ERROR")))); {
	//			server.sendCommand("say A corrupted area has been loaded in but it is being regenerated.");
		//	}
		//	if (event.getMessage().getContentRaw().contains("Could not load cube in") && (event.getMessage().getContentRaw().contains("ERROR"))) {
		//		server.sendCommand("say A corrupted area has failed to regenerate.");
		//	}
			
		//}
		
	}	
	
}
			


			




