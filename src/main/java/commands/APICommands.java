package commands;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class APICommands extends ListenerAdapter {

	//API authentication
	private BuildTheEarthAPI BTE = new BuildTheEarthAPI(System.getenv("BTE_API"));
	private PteroClient pteroAPI = PteroBuilder.createClient(System.getenv("PANEL_URL"), System.getenv("PTERO_API"));
	
	//The minecraft server thats represented by a Ptero API instance
	private ClientServer midwestServer;
			
	//User role list
	private List<Role> roles;
	
	//Timer to tell bot when to check pending applications
	Timer timer = new Timer();
	
	
	public void getMidwestServer() {	
		for (int i = 0; i < pteroAPI.retrieveServers().execute().size(); i++) {
			if (pteroAPI.retrieveServers().execute().get(i).getIdentifier().equals("766e4abc"))
				midwestServer = pteroAPI.retrieveServers().execute().get(i);		
		}
	}
	
//-------------------------------------------------------------------------------------------------------------	
//onReady =link command testing
	@Override
	public void onReady(ReadyEvent event) {
	 
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				BTE.getMemberList();				
				if (BTE.stackTrace.isEmpty()) {
					for (int i = 0; i < BTE.getMemberList().size(); i++) {
						if (event.getJDA().getGuildById(735990134583066679L).getMemberById(BTE.getMemberList().get(i)) != null) {
							
						}
					}
				}//ill finish this later
				

			//for each member, if they dont have perms and are not on blacklist, get username applied with and assign state rank based on discord role
			
			//if no builder role
				//assign builder role and perms
			//if has builder role then has perms
				
			//add blacklist, and methods to add and remove users from it. The users in the blacklist would be ignored by this onReady event
				
			}
		}, 0, 10000);
	}
		
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		getMidwestServer();
		
		 Guild guild = event.getGuild(); 
		 Role staffRole = guild.getRoleById(901162820484333610L);                                             
		 ArrayList<Member> staff = (ArrayList<Member>) guild.getMembersWithRoles(staffRole);
		 
		 
		 if (event.getMessage().getContentRaw().equals("=test")) {
			 Document doc = null;
			 try {
				doc = Jsoup.connect("https://buildtheearth.net/buildteams/36/applications").get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 event.getChannel().sendMessage(doc.getElementsByTag("body").get(0).getElementById("app-mount").getElementsByClass("app-1q1i1E").get(0).toString()).queue();
			 
			 
		 }
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
				event.getChannel().sendMessage(emb.build()).queue();
				}
			
			else {
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.blue);
				emb.setTitle("Only staff can execute this command");
				event.getChannel().sendMessage(emb.build()).queue();
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
				event.getChannel().sendMessage(emb.build()).queue();
				return;
			}
			
			midwestServer.sendCommand("lp user " + namebuilder + " parent add applicants").execute();
	
			EmbedBuilder emb = new EmbedBuilder();
			emb.setColor(Color.BLUE);
			emb.setTitle("Applicant build permissions assigned to " + namebuilder);
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
			
			//if they actually type 'mcusername' instead of their mc username lol
			if (MCusername.equals("mcusername") || MCusername.equals("<mcusername>") || (MCusername.contains(">") && MCusername.contains("<"))) {
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.BLUE);
				emb.setTitle("Replace `<mcusername>` with your acutal in-game username exluding the <>");
				event.getChannel().sendMessage(emb.build()).queue();
			}
			
				//Getting username from builder application 
				//String usernameApplied = BTE.getUsernameAppliedWith("501116787501301760"); //test case for specific user
				String usernameApplied = BTE.getUsernameAppliedWith(event.getAuthor().getId());
				
				if ((usernameApplied.contains("Error Code: ") || usernameApplied.contains("MalformedURLException") || usernameApplied.contains("IOException") 
						|| usernameApplied.contains("JSONException") || usernameApplied.contains("Error Code:"))) {
	
					
					EmbedBuilder emb = new EmbedBuilder();
					emb.setColor(Color.BLUE);  
					emb.setTitle("There was an error retrieveing the users application data.");
					emb.addField("", usernameApplied.toString().substring(0, 1000), false);
					event.getChannel().sendMessage(emb.build()).queue();
				}
				else {
					//roles = guild.getMemberById("501116787501301760").getRoles(); //test case for specific user
					roles = event.getMember().getRoles();
					boolean isBuilder = false;
					
					//retrieves the member list test
					BTE.getMemberList(); 
					//if there's an exception in retrieving the member list then it stores the stacktrace of that exception in the API objects public string
					if (!BTE.stackTrace.isEmpty()) {
						EmbedBuilder emb = new EmbedBuilder();
						emb.setColor(Color.BLUE);
						emb.setTitle("There was an exception when retrieving the member list");
						emb.addField("Exception", BTE.stackTrace, false);
						event.getChannel().sendMessage(emb.build()).queue();
					}
					
					//if user has been merged into the team, i.e has not submitted an application but is on the team
					else if (usernameApplied.contains("IndexOutOfBoundsException")
						&& BTE.getMemberList().contains(event.getMember().getIdLong())) {
						
						guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
						EmbedBuilder emb = new EmbedBuilder();
						emb.setColor(Color.BLUE);
						emb.setTitle("User has been merged into the team");
						event.getChannel().sendMessage(emb.build()).queue();
						
						isBuilder = true;
					}
					
					else {
						//No errors
						if (BTE.stackTrace.isEmpty()) {

							//if user already has builder role						
							if (roles.contains(guild.getRoleById(735991952931160104L)) && (MCusername.equalsIgnoreCase(usernameApplied))) {						
								isBuilder = true;					
							}

							//if user does not have builder role and username is valid, assign builder role
					//		if (501116787501301760L == BTE.getMemberList().get(i) && !roles.contains(guild.getRoleById(Long.parseLong("735991952931160104"))) 
					//				&& (MCusername.equalsIgnoreCase(usernameApplied))) { //test case for specific user
								
							
							if (BTE.getMemberList().contains(event.getMember().getIdLong()) && !roles.contains(guild.getRoleById(735991952931160104L)) 
								&& (MCusername.equalsIgnoreCase(usernameApplied))) {
								guild.addRoleToMember(event.getMember(), guild.getRoleById(735991952931160104L)).queue();
				
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("You now have Builder role!");
								event.getChannel().sendMessage(emb.build()).queue();
								
								isBuilder = true;					
							}	
						}
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
							else if (roles.contains(guild.getRoleById(900746635427053678L))) {

								midwestServer.sendCommand("lp user " + MCusername + " parent add michigan-builder").execute();
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Minecraft server rank updated to Michigan Builder for user " + MCusername);
								event.getChannel().sendMessage(emb.build()).queue();
							}
							
							else {
								EmbedBuilder emb = new EmbedBuilder();
								emb.setColor(Color.BLUE);
								emb.setTitle("Could not assign build permissions. Please choose a state with `=role` and run the command again.");
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
					
					//If you are trying to retrieve more applications then the user has
					else if (appIndex >= application.getApplications().size() && application.getApplications().size() > 0) {
						EmbedBuilder noApp = new EmbedBuilder();
						noApp.setColor(Color.BLUE);
						noApp.setTitle("User does not have that many applications, try a lower number.");
						
						event.getChannel().sendMessage(noApp.build()).queue();
					}
					
					//If you are trying to retrieve an application that does not exist
					else if (application.getApplications().size() < 1) {
						EmbedBuilder noApp = new EmbedBuilder();
						noApp.setColor(Color.BLUE);
						noApp.setTitle("User does not have any applications. They were most likely merged into the team");
						
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
				noperm.setTitle("Only staff can execute this command");
				
				event.getChannel().sendMessage(noperm.build()).queue();
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

	
			


			




