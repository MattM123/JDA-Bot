package commands;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.inject.spi.Message;
import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.PteroAction;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class APICommands extends ListenerAdapter {

	private BuildTheEarthAPI BTE = new BuildTheEarthAPI("6d83c36acd1bb7301e64749b46ebddc2e3b64a67");
	private String apikey = "ocOQoS7GAfsHJVQVEUL4QroU3N43c7gxQJLUb4kmtumkkAbq";
	private String midwestServerID = "7c5eec7c";
	private PteroClient api = PteroBuilder.createClient("https://panel.richterent.com/", apikey);
	private ClientServer midwestServer = (ClientServer) api.retrieveServerByIdentifier(midwestServerID);
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		 Guild guild = event.getGuild(); 
		 Role staffRole = guild.getRoleById(735991904352731176L);
		 ArrayList<Member> staff = (ArrayList<Member>) guild.getMembersWithRoles(staffRole);
		
		//event builder assign
		if (event.getMessage().getContentRaw().startsWith("=event")) {
			if (staff.contains(event.getMessage().getMember())) {
				
				char[] chararr = event.getMessage().getContentRaw().toCharArray();
				String namebuilder = "";
			
				for (int i = 7; i < chararr.length; i++) {
					namebuilder += chararr[i];
				}			
				midwestServer.sendCommand("lp user " + namebuilder + " parent add event-builder");
				
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.BLUE);
				emb.setTitle("Rank updated to Event Builder for user " + namebuilder);
				event.getChannel().sendMessage(emb.build()).queue();
			}
			else {
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.BLUE);
				emb.setTitle("Invalid Permissions");
				event.getChannel().sendMessage(emb.build()).queue();
			}
		}
//-------------------------------------------------------------------------------------------------------------		
		//Applicant builder assign
		if (event.getMessage().getContentRaw().startsWith("=applicant")) {	
				
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String namebuilder = "";
		
			for (int i = 11; i < chararr.length; i++) {
				namebuilder += chararr[i];
			}		
			
			midwestServer.sendCommand("lp user " + namebuilder + " parent add applicants");
	
			EmbedBuilder emb = new EmbedBuilder();
			emb.setColor(Color.BLUE);
			emb.setTitle("Rank updated to Application In Progress for user " + namebuilder);
			event.getChannel().sendMessage(emb.build()).queue();
		}

//-----------------------------------------------------------------------------------------------------------------------------
		//test API
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=server")) {
			EmbedBuilder midwest = new EmbedBuilder();
			if (midwestServer.retrieveUtilization().execute().getState().toString().equals("RUNNING")) {
				midwest.setColor(Color.green);
			}
			else if (midwestServer.retrieveUtilization().execute().getState().toString().equals("OFFLINE")) {
				midwest.setColor(Color.red);
			}
			else {
				midwest.setColor(Color.yellow);
			}
			midwest.setTitle(midwestServer.getName());
			midwest.addField("Status", midwestServer.retrieveUtilization().execute().getState().toString(), false);
			midwest.addField("CPU Usage", midwestServer.retrieveUtilization().execute().getCPU() + "/" + midwestServer.getLimits().getCPU(), false);
			midwest.addField("Memory Usage", midwestServer.retrieveUtilization().execute().getMemoryFormatted(DataType.GB) + "/" + midwestServer.getLimits().getMemory(), false);
			midwest.addField("Server Size", midwestServer.retrieveUtilization().execute().getDiskFormatted(DataType.GB) + "/" + midwestServer.getLimits().getDisk(), false);
	
			event.getChannel().sendMessage(midwest.build()).queue();
		}
//-----------------------------------------------------------------------------------------------------------------------------

		//give build perms based on presence on build team
		if (event.getMessage().getContentRaw().startsWith("=link")) {		
			
			//Parses minecraft username for later use
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String MCusername = "";
		
			for (int i = 6; i < chararr.length; i++) {
				MCusername += chararr[i];
			}
			
			event.getChannel().sendMessage(MCusername).queue();
				//Getting username from application for input validation	
				String usernameApplied = BTE.getUsernameAppliedWith(event.getAuthor().getId());
				
				if ((usernameApplied.contains("Error Code: ") || usernameApplied.contains("MalformedURLException") 
						|| usernameApplied.contains("IOException") || usernameApplied.contains("JSONException") || usernameApplied.contains("Error Code:"))) {
					
					EmbedBuilder emb = new EmbedBuilder();
					emb.setColor(Color.BLUE);
					emb.setTitle("There was an error with retrieveing the users' application data.");
					emb.addField("", usernameApplied.toString().substring(0, 1000), false);
					event.getChannel().sendMessage(emb.build()).queue();
				}
				event.getChannel().sendMessage("1").queue();
				
					List<Role> roles = event.getMember().getRoles();
					int temp = 0;
					
					//retreieves the member list test
					BTE.getMemberList(); 
					//if theres an exception in retrieving the member list then it stores the stacktrace of that exception in the API objects public string
					if (!BTE.stackTrace.equals("")) {
						event.getChannel().sendMessage(BTE.stackTrace).queue();
					}
					
					else {
						event.getChannel().sendMessage("2").queue();
						//If user ID exists in member list and builder role is not already assigned, give builder role
						
						for (int i = 0; i < BTE.getMemberList().size(); i++) {	
							if (roles.contains(guild.getRoleById(735991952931160104L)) && (MCusername.equalsIgnoreCase(usernameApplied))) {
								
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("You already have builder role! Assigning server rank.");
								event.getChannel().sendMessage(emb.build()).queue();
								
								temp = 1;
								break;
							}
										
							else if (event.getAuthor().getIdLong() == BTE.getMemberList().get(i) && !roles.contains(guild.getRoleById(Long.parseLong("735991952931160104"))) && (MCusername.equalsIgnoreCase(usernameApplied))) {
								guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
				
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("You now have Builder role!");
								event.getChannel().sendMessage(emb.build()).queue();
								
								temp = 1;
								break;
							}
							else if (!(MCusername.equalsIgnoreCase(usernameApplied))) {
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("The username you applied with and the one you used to run this command do not match.");
								event.getChannel().sendMessage(emb.build()).queue();
								break;
							}
						}
		
						//if user has state role, assign corresponding minecraft server rank else have user get state role and run command again.
			
						if (temp == 1) {
							if (roles.contains(guild.getRoleById(735995176165834756L))) {
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add kansas-builder");
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Kansas Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995164493086720L))) {								
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add iowa-builder");
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Iowa Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995136978321541L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add nebraska-builder");
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Nebraska Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995095773609986L))) {
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add illinois-builder");
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Illinois Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(735995115113414656L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add missouri-builder");
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Missouri Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995196738633819L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add minnesota-builder");
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Minnesota Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(808415301799641119L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add oklahoma-builder");
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Oklahoma Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
							}
							
							else {
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Go to #role-menu to select one and run the command again to get server build perms.");
								event.getChannel().sendMessage(emb.build()).queue();
							}
						}
						
						//if user is not on the team at all, print this
						
						else if (temp == 0) {
							EmbedBuilder emb = new EmbedBuilder();
							emb.setColor(Color.BLUE);
							emb.setTitle("Looks like you're not on the team or your username was invalid. If this is wrong, then ping mattress#1852");
							event.getChannel().sendMessage(emb.build()).queue();
						}
					}
		}
//-------------------------------------------------------------------------------------------------------------------------------------------	
		//Retrieves an application of user given a discord ID and an integer representing which application in the list to return
		if (event.getMessage().getContentRaw().startsWith("=getapp")) { 
			if (staff.contains(event.getMessage().getMember())) {
				String message = event.getMessage().getContentRaw();
				
				char[] charArr = event.getMessage().getContentRaw().toCharArray();
				String user = "";
				String appNum = "";
				
				for (int i = 9; i <= charArr.length; i++) {
					if (i == 9) {
						user += message.substring(i, message.lastIndexOf(" "));	
						appNum += message.substring(i + 20);
					}
				}
				
				//Test run for errors
				BTE.getApplicationHistory(user); 
				//if theres an exception in retrieving the member list then it stores the stacktrace of that exception in the API objects public string
				if (!BTE.stackTrace.equals("") && !BTE.stackTrace.equals("User has not applied to the team nor have they been merged into it")) {
					event.getChannel().sendMessage(BTE.stackTrace).queue();
				}
				
				else if (BTE.stackTrace.equals("User has not applied to the team nor have they been merged into it")) {
					EmbedBuilder notOnTeam = new EmbedBuilder();
					notOnTeam.setColor(Color.BLUE);
					notOnTeam.setTitle("No data on user");
					notOnTeam.addField(BTE.stackTrace, "This error resulted from a FileNotFoundException, meaning the Discord ID does not exist on the team.", false);
					event.getChannel().sendMessage(notOnTeam.build()).queue();
					
				
				}
				
				//else everything will run as it should
				else {
					ApplicationInfo application = BTE.getApplicationHistory(user);
					int appIndex = Integer.parseInt(appNum) - 1;
					
					if ((application.getApplications().isEmpty())) {
						
						EmbedBuilder noinfo = new EmbedBuilder();
						noinfo.setColor(Color.BLUE);
						noinfo.setTitle("No data on user");
						noinfo.addField("This user was most likely merged into the team", "", false);
						event.getChannel().sendMessage(noinfo.build()).queue();
					}
					
					else if (appIndex >= application.getApplications().size()) {
						EmbedBuilder noApp = new EmbedBuilder();
						noApp.setColor(Color.BLUE);
						noApp.setTitle("User does not have that many applications, try a lower number.");
						
						event.getChannel().sendMessage(noApp.build()).queue();
					}
					else {
						EmbedBuilder app = new EmbedBuilder();
						app.setColor(Color.BLUE);
						app.setTitle("Application " + appNum + " for user ID " + user);
						
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(0).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(0).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(1).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(1).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(2).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(2).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(3).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(3).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(4).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(4).getAnswer(), false);
						
						
						event.getChannel().sendMessage(app.build()).queue();
					}
				}
			}
			else {
				EmbedBuilder noperm = new EmbedBuilder();
				noperm.setColor(Color.BLUE);
				noperm.setTitle("You must be a staff member to use this command");
				
				event.getChannel().sendMessage(noperm.build()).queue();
			}
		}
	
	}
		
}	

	
			


			




