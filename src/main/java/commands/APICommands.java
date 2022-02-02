package commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
	private PteroClient pteroAPI = PteroBuilder.createClient(System.getenv("PANEL_URL"), System.getenv("PTERO_API"));
	
	//The minecraft server thats represented by a Ptero API instance
	private ClientServer midwestServer = pteroAPI.retrieveServerByIdentifier("766e4abc").execute();
			
	//User role list
	private List<Role> roles;
	
	//Timer to tell bot when to check pending applications
	Timer timer = new Timer();

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
//give build perms based on presence on build team
		
		if (event.getMessage().getContentRaw().startsWith("=link")) {			
			
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
	
					
					EmbedBuilder emb = new EmbedBuilder();
					emb.setColor(Color.BLUE);  
					emb.setTitle("There was an error retrieveing the users application data.");
					emb.addField("", usernameApplied.toString().substring(0, 1000), false);
					event.getChannel().sendMessageEmbeds(emb.build()).queue();
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
		
//-------------------------------------------------------------------------------------------------------------------------------------------	
//Retrieves an application of user given a discord ID and an integer representing which application in the list to return
		
		if (event.getMessage().getContentRaw().startsWith("=getapp")) { 
			if (staff.contains(event.getMessage().getMember())) {
				String[] args = event.getMessage().getContentRaw().split(" ");
				
				String user = args[1];
				String appNum = args[2];
				
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
						
						event.getChannel().sendMessageEmbeds(app.build()).queue();
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
//-------------------------------------------------------------------------------------------------------------------------------------------	
//Notifies staff members of new applications since BTE bot stopped doing it
	
/*            //BROKEN, cannot differentiate between bot message author and human message author
	private boolean isBot = true;
	
	@Override
	public void onReady(ReadyEvent event) {
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				TextChannel staff = event.getJDA().getGuildById(735990134583066679L).getTextChannelById(735992503408263229L);									
				int applications = BTE.getPendingApplications().getApplications().size() - 1;
				
			    if (BTE.getPendingApplications().getApplications().size() > 0) {
			    	EmbedBuilder emb = new EmbedBuilder();
			    	
			    	staff.getHistory().retrievePast(1)
			    		 .map(historyMessages -> historyMessages.get(0))
					     .queue(historyMessage -> {
					    	 isBot = historyMessage.getAuthor().isBot();
					     });
							    
			    	emb.setTitle("There is " + applications  + " new application(s) to review");
			    	emb.setColor(Color.blue);
			    	
			    	if (!isBot && applications > 0)
			    		staff.sendMessage(emb.build()).queue();
			    }
			}
		}, 1000, 600000);
	}
*/	
	
}	

	
			


			




