package commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.marcuzzo.JDABot.Bot;
import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;


public class APICommands extends ListenerAdapter {

	//API authentication
	private BuildTheEarthAPI BTE = new BuildTheEarthAPI(System.getenv("BTE_API"));
	private PteroClient pteroAPI = PteroBuilder.createClient(System.getenv("PANEL_URL"), System.getenv("PTERO_API"));
	
	//The minecraft server thats represented by a Ptero API instance
	private ClientServer midwestServer = pteroAPI.retrieveServerByIdentifier(System.getenv("SERVER_ID")).execute();
			
	//User role list
	private List<Role> roles;
	
	//Guild used for onReady Events
	private Guild guild;

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		 Guild guild = event.getGuild(); 
		 Role staffRole = guild.getRoleById(901162820484333610L);                                             
		 ArrayList<Member> staff = (ArrayList<Member>) guild.getMembersWithRoles(staffRole);

//-------------------------------------------------------------------------------------------------------------	
//send command to server console
		 
		if (event.getMessage().getContentRaw().startsWith("=/")) {
			if (staff.contains(event.getMessage().getMember())) {
				String cmdBuilder = "";
				for (int i = 2; i < event.getMessage().getContentRaw().length(); i++) {
					cmdBuilder += event.getMessage().getContentRaw().charAt(i);
				}
				
				midwestServer.sendCommand(cmdBuilder).execute();
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.blue);
				emb.setTitle("Command Executed");
				emb.addField(cmdBuilder, "", false);	
				event.getChannel().sendMessageEmbeds(emb.build()).queue();
				}
			
			else {
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.blue);
				emb.setTitle("Only staff can execute this command");
				event.getChannel().sendMessageEmbeds(emb.build()).queue();
			}
		}
//-------------------------------------------------------------------------------------------------------------		
//Gives applicant builder permissions
		
		if (event.getMessage().getContentRaw().startsWith("=applicant")) {	
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String namebuilder = "";
		
			for (int i = 11; i < chararr.length; i++) {
				namebuilder += chararr[i];
			}		
			
			//if they actually type 'mcusername' instead of their mc username lol. Or if they include <>
			if (namebuilder.equals("mcusername") || namebuilder.equals("<mcusername>") || (namebuilder.contains(">") && namebuilder.contains("<"))) {
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.BLUE);
				emb.setTitle("Replace `<mcusername>` with your acutal in-game username excluding the `<>`");
				event.getChannel().sendMessageEmbeds(emb.build()).queue();
				return;
			}
			
			midwestServer.sendCommand("lp user " + namebuilder + " parent add applicant").execute();
			
			//assigns applicant role after getting applicant perms
			if (!guild.getMemberById(event.getAuthor().getIdLong()).getRoles().contains(guild.getRoleById(923068579992186912L)))
				guild.addRoleToMember(event.getAuthor().getIdLong(), guild.getRoleById(923068579992186912L)).queue();
	
			EmbedBuilder emb = new EmbedBuilder();
			emb.setColor(Color.BLUE);
			emb.setTitle("Applicant build permissions assigned to " + namebuilder);
			emb.setFooter("If you did not recieve permissions, you will need to logon to the server and rerun this bot command");
			event.getChannel().sendMessageEmbeds(emb.build()).queue();
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
	
			event.getChannel().sendMessageEmbeds(midwest.build()).queue();
		}
//-----------------------------------------------------------------------------------------------------------------------------
//LEGACY: give build perms based on presence on build team
		
/*		if (event.getMessage().getContentRaw().startsWith("=link")) {			
			
			//Parses minecraft username for later use
			String[] args = event.getMessage().getContentRaw().split(" ");
			String MCusername = args[1];
			
			
			//if they actually type 'mcusername' instead of their mc username lol
			if (MCusername.equals("mcusername") || MCusername.equals("<mcusername>") || (MCusername.contains(">") && MCusername.contains("<"))) {
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.BLUE);
				emb.setTitle("Replace `<mcusername>` with your acutal in-game username exluding the <>");
				event.getChannel().sendMessageEmbeds(emb.build()).queue();
			}
			
				//Getting username from builder application 
				//String usernameApplied = BTE.getUsernameAppliedWith("309114198812655617"); //test case for specific user
				String usernameApplied = BTE.getUsernameAppliedWith(event.getAuthor().getId());
				
				if ((usernameApplied.contains("Error Code: ") || usernameApplied.contains("MalformedURLException") || usernameApplied.contains("IOException") 
						|| usernameApplied.contains("JSONException") || usernameApplied.contains("Error Code:"))) {
					
					//if info cannot be obtained due to server error
					if (usernameApplied.contains("Server returned HTTP response code: 5")) {					
						EmbedBuilder emb = new EmbedBuilder();
						emb.setColor(Color.BLUE);  
						emb.setTitle("Buildtheearth.net is currently experiencing an outage and is unable to retrieve your information. Sorry for any inconvineience.");
						event.getChannel().sendMessageEmbeds(emb.build()).queue();
					}
					//if info cannot be obtained due to my shitty ass code
					else {						
						EmbedBuilder emb = new EmbedBuilder();
						emb.setColor(Color.BLUE);  
						emb.setTitle("There was an error retrieveing the users application data. The URL or API may have changed.");
						emb.addField("", usernameApplied.toString().substring(0, 1000), false);
						event.getChannel().sendMessageEmbeds(emb.build()).queue();
					}
				}
				else {
					//roles = guild.getMemberById("309114198812655617").getRoles(); //test case for specific user
					
					roles = event.getMember().getRoles();
					boolean isBuilder = false;
					
					//pain
					JsonElement builderElement = JsonParser.parseString("{" + "\"" + "discordId\":" + "\"" + event.getAuthor().getIdLong() + "\"" 
							+ ",\"" + "discordTag\":" + "\"" + event.getAuthor().getAsTag() + "\"" + ",\"" + "role\":\"builder" + "\"" + "}");
						
						//retrieves the member list test
						BTE.getMemberList(); 
						//if there's an exception in retrieving the member list then it stores the stacktrace of that exception in the API objects public string
						if (!BTE.stackTrace.isEmpty()) {
							EmbedBuilder emb = new EmbedBuilder();
							emb.setColor(Color.BLUE);
							emb.setTitle("There was an exception when retrieving the member list");
							emb.addField("Exception", BTE.stackTrace, false);
							event.getChannel().sendMessageEmbeds(emb.build()).queue();
						}
						
						//if user has been merged into the team, i.e has not submitted an application but is on the team						  
						else if (usernameApplied.contains("IndexOutOfBoundsException")
							&& (BTE.getMemberList().contains(builderElement))) {
	
							//test case for specific user
							//guild.addRoleToMember(guild.getMemberById(309114198812655617L), guild.getRoleById(735991952931160104L)).queue();
							
							guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
							EmbedBuilder emb = new EmbedBuilder();
							emb.setColor(Color.BLUE);
							emb.setTitle("User has been merged into the team");
							event.getChannel().sendMessageEmbeds(emb.build()).queue();
							
							isBuilder = true;
						}
						
						else {
							//No errors
							if (BTE.stackTrace.isEmpty()) {
	
								//if user already has builder role						
								if (roles.contains(guild.getRoleById(735991952931160104L)) && (MCusername.equalsIgnoreCase(usernameApplied))) {		
									isBuilder = true;				
								}
									
								
								if (BTE.getMemberList().contains(builderElement) && !roles.contains(guild.getRoleById(735991952931160104L)) 
									&& (MCusername.equalsIgnoreCase(usernameApplied))) {
									
									//test case for specific user
									//guild.addRoleToMember(guild.getMemberById(309114198812655617L), guild.getRoleById(735991952931160104L)).queue();
									
									guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
					
									EmbedBuilder emb = new EmbedBuilder();
									emb.setColor(Color.BLUE);
									emb.setTitle("You now have Builder role!");
									event.getChannel().sendMessageEmbeds(emb.build()).queue();
									
									isBuilder = true;
								}	
							}
						}						
					
						//if user has state role, assign corresponding minecraft server rank else have user get state role and run command again.
						if (isBuilder) {
							
							//removes applicant role since user is builder
							if (guild.getMemberById(event.getAuthor().getIdLong()).getRoles().contains(guild.getRoleById(923068579992186912L)))
								guild.removeRoleFromMember(guild.getMember(event.getAuthor()), guild.getRoleById(923068579992186912L)).queue();							
							
							if (roles.contains(guild.getRoleById(735995176165834756L))) {
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add kansas-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Kansas Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995164493086720L))) {								
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add iowa-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Iowa Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995136978321541L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add nebraska-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Nebraska Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995095773609986L))) {
								
								midwestServer.sendCommand("lp user " + MCusername + " parent add illinois-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Illinois Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(735995115113414656L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add missouri-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Missouri Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
		
							}
							else if (roles.contains(guild.getRoleById(735995196738633819L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add minnesota-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Minnesota Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(808415301799641119L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add oklahoma-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Oklahoma Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(798079627360337970L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add wisconsin-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Wisconsin Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
							}
							else if (roles.contains(guild.getRoleById(900746635427053678L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add michigan-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Michigan Builder for " + MCusername);
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
							}
							
							else {
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Could not assign build permissions. Please choose a state with `=role` and run the command again.");
								event.getChannel().sendMessageEmbeds(emb.build()).queue();
							}
						}
						
						//if user is not on the team at all or invalid username, print this						
						else {
							EmbedBuilder emb = new EmbedBuilder();
							emb.setColor(Color.BLUE);
							emb.setTitle("You're not on the team or your username was invalid");
							event.getChannel().sendMessageEmbeds(emb.build()).queue();  
						}							
											
				}
			}
*/		
//-------------------------------------------------------------------------------------------------------------------------------------------	
//Retrieves an application of user given a discord ID(or Tag) and an integer representing which application in the list to return
		
		if (event.getMessage().getContentRaw().startsWith("=getapp")) { 
			if (staff.contains(event.getMessage().getMember())) {
				String[] args = event.getMessage().getContentRaw().split(" ");
				
				String user = args[1];
				String appNum = args[args.length - 1];
				
				//checks for tag. If can be parsed to long, then its an ID, else it will be assumed a tag if the exception is caught
				try {
					Long.parseLong(user);
				}
				catch (NumberFormatException e) {
					//parses tag and gets user ID
					String tag = event.getMessage().getContentRaw().substring(8, event.getMessage().getContentRaw().lastIndexOf('#') + 5);
					try {
						user = guild.getMemberByTag(tag).getId();				
					}
					catch (IllegalArgumentException f) {
						EmbedBuilder emb = new EmbedBuilder();
						emb.setColor(Color.blue);
						emb.addField("Invalid User", "Tag must be a valid user and must be in the discord server", false);
						event.getChannel().sendMessageEmbeds(emb.build()).queue();			
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
					event.getChannel().sendMessageEmbeds(notOnTeam.build()).queue();		
				}
				
				//If you are trying to get the 0th application
				else if (Integer.parseInt(appNum) == 0) {
					EmbedBuilder emb = new EmbedBuilder();
					emb.setColor(Color.blue);
					emb.addField("Application does not exist", "Cannot retrieve the 0th application on a user", false);
					event.getChannel().sendMessageEmbeds(emb.build()).queue();
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
						event.getChannel().sendMessageEmbeds(noinfo.build()).queue();
					}
					
					//If you are trying to retrieve more applications then the user has
					else if (appIndex >= application.getApplications().size() && application.getApplications().size() > 0) {
						EmbedBuilder noApp = new EmbedBuilder();
						noApp.setColor(Color.BLUE);
						noApp.setTitle("User does not have that many applications, try a lower number.");
						
						event.getChannel().sendMessageEmbeds(noApp.build()).queue();
					}
					
					//If you are trying to retrieve an application that does not exist
					else if (application.getApplications().size() < 1) {
						EmbedBuilder noApp = new EmbedBuilder();
						noApp.setColor(Color.BLUE);
						noApp.setTitle("User does not have any applications. They were most likely merged into the team");
						
						event.getChannel().sendMessageEmbeds(noApp.build()).queue();
					}

					//Returns application
					else {
						EmbedBuilder app = new EmbedBuilder();
						EmbedBuilder images = new EmbedBuilder();
						images.setColor(Color.blue);
						app.setColor(Color.BLUE);
						
						Guild pubGuild = Bot.jda.getGuildById(735990134583066679L);
						
						app.setTitle("[" + appNum + "] Application Questions for " + pubGuild.getMemberById(user).getUser().getName());
						images.setTitle("[" + appNum + "] Application Media for " +  pubGuild.getMemberById(user).getUser().getName());

						app.addField(application.getApplications().get(appIndex).getAnswerList().get(0).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(0).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(1).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(1).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(3).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(3).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(4).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(4).getAnswer(), false);
						
						event.getChannel().sendMessageEmbeds(app.build()).queue();
						event.getChannel().sendMessage("**__Previous BTE Builds__**\n" + application.getApplications().get(appIndex).getUrl()).queue();
						event.getChannel().sendMessage("**__Reference Sources Used__**\n" + application.getApplications().get(appIndex).getAnswerList().get(2).getAnswer()).queue();

					}
				}
			}
			else {
				EmbedBuilder noperm = new EmbedBuilder();
				noperm.setColor(Color.BLUE);
				noperm.setTitle("Only staff can execute this command");
				
				event.getChannel().sendMessageEmbeds(noperm.build()).queue();
			}
		}
	}

	
	@Override
	public void onReady(ReadyEvent event) {
		Timer appTimer = new Timer();
		Timer permTimer = new Timer();
		guild = Bot.jda.getGuildById(735990134583066679L);
		Role builder = guild.getRoleById(735991952931160104L);
		
//-------------------------------------------------------------------------------------------------------------------------------------------	
//Notifies staff members of new applications since BTE bot stopped doing it
		appTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				TextChannel staff = event.getJDA().getGuildById(735990134583066679L).getTextChannelById(951957461869420565L);									
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.blue);
				
				//if a message already exists in the channel, edits the current one to include the updated pending applications if there are any
				try {
					if (staff.hasLatestMessage()) {
						RestAction<Message> action = staff.retrieveMessageById(staff.getLatestMessageId());
						Message message = action.complete();
						
						if (BTE.getPendingApplications().getApplications().size() > 0) {						    							    	
						   	for (int i = 0; i < BTE.getPendingApplications().getApplications().size(); i++) {
						   		emb.addField(BTE.getPendingApplications().getApplications().get(i).user.getUserTag() + " has applied to the team.\n" ,
					   				"View their application here: https://buildtheearth.net/buildteams/36/applications/" 
				    				+ BTE.getPendingApplications().getApplications().get(i).id + "\n\n", false);			
					    	}
					    	message.editMessageEmbeds(emb.build()).queue();
					    }
					    //deletes message if all apps have already been reviewed
					    else {
					    	message.delete().queue();
					    }
					}
					//If there is no message history, a message is created and sent instead of edited if there is at least one pending application
				} catch (ErrorResponseException e) {
					if (BTE.getPendingApplications().getApplications().size() > 0) {    	
						   for (int i = 0; i < BTE.getPendingApplications().getApplications().size(); i++) {
					    	emb.addField(BTE.getPendingApplications().getApplications().get(i).user.getUserTag() + " has applied to the team.\n" ,
					   				"View their application here: https://buildtheearth.net/buildteams/36/applications/" 
					    			+ BTE.getPendingApplications().getApplications().get(i).id + "\n\n", false);	
						   }
						   	staff.sendMessageEmbeds(emb.build()).queue();	    
					}
				}		
			}
		}, 1000, 10000);
		
//-------------------------------------------------------------------------------------------------------------------------------------------	
//Compares team ID list with discord users and assigns build perms if necessary
		permTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				//For each guild member that is on website team, if they do not have builder role, assign builder role 
				for (int i = 0; i < BTE.getMemberList().size(); i++) {
					Member guildMember = guild.getMemberById(BTE.getMemberList().get(i).getAsJsonObject().get("discordId").getAsLong());
					
				
					if (!guildMember.equals(null) && !guildMember.getRoles().contains(builder) ) {
						guild.addRoleToMember(guildMember.getIdLong(), builder).queue();
					}		
				}
			}		
		}, 1000, 10000);
	}	

}	

	
			


			




