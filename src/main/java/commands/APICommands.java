package commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class APICommands extends ListenerAdapter {

	//API authentication
	private BuildTheEarthAPI BTE = new BuildTheEarthAPI(System.getenv("BTE_API"));
	private PteroClient api = PteroBuilder.createClient(System.getenv("PANEL_URL"), System.getenv("PTERO_API"));
	
	//The minecraft server
	private ClientServer midwestServer = api.retrieveServers().execute().get(0);
	
	//User role list
	private List<Role> roles;
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		 Guild guild = event.getGuild(); 
		 Role staffRole = guild.getRoleById(735991904352731176L); 
		 ArrayList<Member> staff = (ArrayList<Member>) guild.getMembersWithRoles(staffRole, guild.getRoleById(736002669130547211L));
	
//-------------------------------------------------------------------------------------------------------------	
//send command to server console
		 
		if (event.getMessage().getContentRaw().startsWith("=/") && staff.contains(event.getMessage().getMember())) {
			String cmdBuilder = "";
			for (int i = 2; i < event.getMessage().getContentRaw().length(); i++) {
				cmdBuilder += event.getMessage().getContentRaw().charAt(i);
			}
			
			midwestServer.sendCommand(cmdBuilder).execute();
			EmbedBuilder emb = new EmbedBuilder();
			emb.setColor(Color.blue);
			emb.setTitle("Command Executed");
			emb.addField(cmdBuilder, "", false);
			
			event.getChannel().sendMessage(emb.build()).queue();
		}
//-------------------------------------------------------------------------------------------------------------		
//Gives applicant builder permissions
		
		if (event.getMessage().getContentRaw().startsWith("=applicant")) {	
				
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String namebuilder = "";
		
			for (int i = 11; i < chararr.length; i++) {
				namebuilder += chararr[i];
			}		
			
			midwestServer.sendCommand("lp user " + namebuilder + " parent add applicants").execute();
	
			EmbedBuilder emb = new EmbedBuilder();
			emb.setColor(Color.BLUE);
			emb.setTitle("Rank updated to Application In Progress for user " + namebuilder);
			event.getChannel().sendMessage(emb.build()).queue();
		}

//-----------------------------------------------------------------------------------------------------------------------------
//get server stats
		
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
			midwest.addField("CPU Usage", midwestServer.retrieveUtilization().execute().getCPU() + "%/100%", false);
			midwest.addField("Memory Usage", midwestServer.retrieveUtilization().execute().getMemoryFormatted(DataType.GB) + "/" + Integer.parseInt(midwestServer.getLimits().getMemory()) / 1000 + " GB", false);
			midwest.addField("Server Size", midwestServer.retrieveUtilization().execute().getDiskFormatted(DataType.GB) + "/Unlimited", false);
	
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
				//Getting username from builder application 
				String usernameApplied = BTE.getUsernameAppliedWith(event.getAuthor().getId());
				
				if ((usernameApplied.contains("Error Code: ") || usernameApplied.contains("MalformedURLException") || usernameApplied.contains("IOException") 
						|| usernameApplied.contains("JSONException") || usernameApplied.contains("Error Code:") || usernameApplied.contains("IndexOutOfBounds"))) {
	
					
					EmbedBuilder emb = new EmbedBuilder();
					emb.setColor(Color.BLUE);  
					emb.setTitle("There was an error retrieveing the users application data.");
					emb.addField("", usernameApplied.toString().substring(0, 1000), false);
					event.getChannel().sendMessage(emb.build()).queue();
				}
				else {
					roles = event.getMember().getRoles();
					boolean isBuilder = false;
					
					//retrieves the member list test
					BTE.getMemberList(); 
					//if there's an exception in retrieving the member list then it stores the stacktrace of that exception in the API objects public string
					if (!BTE.stackTrace.equals("")) {
						EmbedBuilder emb = new EmbedBuilder();
						emb.setColor(Color.BLUE);
						emb.setTitle("There was an exception when retrieving the member list");
						emb.addField("Exception", BTE.stackTrace, false);
						event.getChannel().sendMessage(emb.build()).queue();
					}
					
					else { 
						//If user ID exists in member list and builder role is not already assigned, give builder role
						for (int i = 0; i < BTE.getMemberList().size(); i++) {	
							
							//if user has builder role and username is valid, assign builder role
							if (roles.contains(guild.getRoleById(735991952931160104L)) && (MCusername.equalsIgnoreCase(usernameApplied))) {
								
								isBuilder = true;
								break;
							}

							//if user already has builder role
							else if (event.getMember().getIdLong() == BTE.getMemberList().get(i) && !roles.contains(guild.getRoleById(Long.parseLong("735991952931160104"))) 
								&& (MCusername.equalsIgnoreCase(usernameApplied))) {
								guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
				
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("You now have Builder role!");
								event.getChannel().sendMessage(emb.build()).queue();
								
								isBuilder = true;
								break;
							}
							
							//if user has been merged into the team, i.e do not have a username they applied with but on the team
							if (BTE.getApplicationHistory(event.getMember().getId()).applications.size() <= 0 && BTE.getMemberList().contains(event.getMember().getIdLong())) {
								guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("User has been merged into the team");
								
								isBuilder = true;
						}
					
		
						//if user has state role, assign corresponding minecraft server rank else have user get state role and run command again.
						if (isBuilder) {
							if (roles.contains(guild.getRoleById(735995176165834756L))) {
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add kansas-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Kansas Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995164493086720L))) {								
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add iowa-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Iowa Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995136978321541L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add nebraska-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Nebraska Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995095773609986L))) {
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add illinois-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Illinois Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(735995115113414656L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add missouri-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Missouri Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995196738633819L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add minnesota-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Minnesota Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(808415301799641119L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add oklahoma-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Oklahoma Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(798079627360337970L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add wisconsin-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Wisconsin Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
							}
							
							else {
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Select a role from #" + guild.getGuildChannelById(736010885738266674L).getName() + " and run the command again.");
								event.getChannel().sendMessage(emb.build()).queue();
							}
						}
						
						//if user is not on the team at all or invalid username, print this						
						else {
							EmbedBuilder emb = new EmbedBuilder();
							emb.setColor(Color.BLUE);
							emb.setTitle("You're not on the team or your username was invalid");
							event.getChannel().sendMessage(emb.build()).queue();
						}							
					}						
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
				
				//If user not found on team
				else if (BTE.stackTrace.equals("User has not applied to the team nor have they been merged into it")) {
					EmbedBuilder notOnTeam = new EmbedBuilder();
					notOnTeam.setColor(Color.BLUE);
					notOnTeam.setTitle("No data on user");
					notOnTeam.addField(BTE.stackTrace, "User account does not exist on the team", false);
					event.getChannel().sendMessage(notOnTeam.build()).queue();
					
				
				}
				
				else {
					ApplicationInfo application = BTE.getApplicationHistory(user);
					int appIndex = Integer.parseInt(appNum) - 1;
					
					//If user exists on team but no applications exist for them, user was merged into the team
					if ((application.getApplications().isEmpty())) {		
						EmbedBuilder noinfo = new EmbedBuilder();
						noinfo.setColor(Color.BLUE);
						noinfo.setTitle("No applications found for user");
						noinfo.addField("This user was most likely merged into the team", "", false);
						event.getChannel().sendMessage(noinfo.build()).queue();
					}
					
					//If you are trying to retrieve an application that does not exist
					else if (appIndex >= application.getApplications().size()) {
						EmbedBuilder noApp = new EmbedBuilder();
						noApp.setColor(Color.BLUE);
						noApp.setTitle("User does not have that many applications, try a lower number.");
						
						event.getChannel().sendMessage(noApp.build()).queue();
					}
					
					//Returns application
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
		
//------------------------------------------------------------------------------------------------------------------------------------
//sends users to getting started channel
		
		ArrayList<Pattern> phrases = new ArrayList<Pattern>();
		phrases.add(Pattern.compile("how do i apply", Pattern.CASE_INSENSITIVE));
		phrases.add(Pattern.compile("how to be builder|how do i be a builder|how do i become a builder", Pattern.CASE_INSENSITIVE));
		phrases.add(Pattern.compile("how do i get builder role|how do i get builder", Pattern.CASE_INSENSITIVE));
		
		
		for (int i = 0; i < phrases.size(); i++) {
			if (phrases.get(i).matcher(event.getMessage().getContentRaw()).matches()) {
				event.getChannel().sendMessage("Go to <#new-players-please-read> for info on how to get started").queue();
			}
		}

	}
}	

	
			


			




