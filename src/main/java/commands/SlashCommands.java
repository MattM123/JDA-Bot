package commands;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;

import Events.RoleEvents;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;


public class SlashCommands extends ListenerAdapter {

	//API authentication
	private BuildTheEarthAPI BTE = new BuildTheEarthAPI(System.getenv("BTE_API"));
	private PteroClient pteroAPI = PteroBuilder.createClient(System.getenv("PANEL_URL"), System.getenv("PTERO_API"));
	
	//The minecraft server thats represented by a Ptero API instance
	private ClientServer midwestServer = pteroAPI.retrieveServerByIdentifier(System.getenv("SERVER_ID")).execute();
	
	
	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		super.onSlashCommand(event);

		 Guild guild = event.getGuild(); 
		 Role adminRole = guild.getRoleById(735991904352731176L);
		 Role leadAdminRole = guild.getRoleById(774017385283190835L);
		 Role staffRole = event.getJDA().getRoleById(901162820484333610L);
		 ArrayList<Member> staff = (ArrayList<Member>) guild.getMembersWithRoles(adminRole, leadAdminRole);
		 
		 EmbedBuilder measure = new EmbedBuilder();
		 measure.setColor(Color.blue);
	     measure.setTitle("Google Earth Pro Measuring Tutotrial");
		 measure.setImage("https://i.gyazo.com/thumb/1200/d58446cec35cc504bb36b749346041a9-gif.gif");
		 
		 EmbedBuilder map = new EmbedBuilder();
		 map.setTitle("BTE Midwest Map");
		 map.setColor(Color.blue);
		 map.setImage("https://cdn.discordapp.com/attachments/735998501053530163/901644652157997076/bte_midwest.png");
			
		EmbedBuilder midwest = new EmbedBuilder();
		if (midwestServer.retrieveUtilization().execute().getState().toString().equals("RUNNING")) 
			midwest.setColor(Color.green);		
		else if (midwestServer.retrieveUtilization().execute().getState().toString().equals("OFFLINE")) 
			midwest.setColor(Color.red);		
		else 
			midwest.setColor(Color.yellow);
		
		midwest.setTitle(midwestServer.getName());
		midwest.addField("Status", midwestServer.retrieveUtilization().execute().getState().toString(), false);
		midwest.addField("CPU Usage", midwestServer.retrieveUtilization().execute().getCPU() + "%/100%", false);
		midwest.addField("Memory Usage", midwestServer.retrieveUtilization().execute().getMemoryFormatted(DataType.GB) + "/" + Integer.parseInt(midwestServer.getLimits().getMemory()) / 1000 + " GB", false);
		midwest.addField("Server Size", midwestServer.retrieveUtilization().execute().getDiskFormatted(DataType.GB) + "/Unlimited", false);
		
//-------------------------------------------------------------------------------------------------------------			
		//deletes messages in bulk
		if (event.getName().equals("remove")) {
			if (event.getMember().getRoles().contains(adminRole) || event.getMember().getRoles().contains(staffRole)
				|| event.getMember().getRoles().contains(leadAdminRole)) {
			
				int amount = Integer.parseInt(event.getOption("amount").getAsString());
					
				if (Integer.parseInt(event.getOption("amount").getAsString()) > 100) {
					event.reply("The API is only able to retrieve a maximum of 100 messages for deletion").queue();
				}
				event.reply("Removing " + amount + " messages").queue();
				event.getChannel().getHistory().retrievePast(amount).queue(channel -> {
					for (int i = 0; i < amount; i++) {
						channel.get(i).delete().queue();
					}			
				});
			}
			else
				event.reply("Only staff can execute this command").queue();		
		}	
		
//-------------------------------------------------------------------------------------------------------------			 
		//Pings Discord API
		if (event.getName().equals("ping")) {
			MessageChannel channel = event.getChannel();
		    final long time = System.currentTimeMillis();
		    RestAction<Message> action = channel.sendMessage("Calculating API Response Time...");
			   Consumer<Message> callback = (message) ->  {
				   Message m = message;
				   m.editMessage("Discord API Response Time: " + (System.currentTimeMillis() - time) + "ms").queue();
			   };			  
			     action.queue(callback);      
		}
//-------------------------------------------------------------------------------------------------------------	
		//returns map image of states
		if (event.getName().equals("map"))
			event.replyEmbeds(map.build()).queue();
		
//-------------------------------------------------------------------------------------------------------------			 
		//returns measure gif from BTE bot
			
		if (event.getName().equals("measure")) {		
			EmbedBuilder measure1 = new EmbedBuilder();
			measure1.setColor(Color.blue);
			measure1.addField("tpll Outline Tutorial", "https://www.youtube.com/watch?v=KlGOijIkePQ", false);
			event.replyEmbeds(measure.build(), measure1.build()).queue();	
		}
//-------------------------------------------------------------------------------------------------------------	
//send command to server console
		 
		if (event.getName().equals("console")) {
			if (staff.contains(event.getMember())) {
				
				midwestServer.sendCommand(event.getOption("command").getAsString()).execute();
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.blue);
				emb.setTitle("Command Executed");
				emb.addField(event.getOption("command").getAsString(), "", false);	
				event.replyEmbeds(emb.build()).queue();
			}
			
			else {
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.blue);
				emb.setTitle("Only administrators can execute this command");
				event.replyEmbeds(emb.build()).queue();
			}
		}
//-------------------------------------------------------------------------------------------------------------		
//Gives applicant builder permissions if they havnt already been rejected
		
		File file = new File("Applicants.txt");
		StringBuilder content = new StringBuilder();
		event.reply(file.getAbsolutePath()).queue();
		
		if (event.getName().equals("apply")) {	
			try {
				Scanner scan = new Scanner(file);
				String line;
				while (scan.hasNextLine()) {
					line = scan.nextLine();
					content.append(line);
					
					if (line.contains(event.getUser().getId())) {
						event.reply("You have already applied to the team!").queue();
					}
				}
				
				if (!content.toString().contains(event.getUser().getId())) {
					guild.addRoleToMember(event.getMember(), guild.getRoleById(923068579992186912L)).queue();
					event.reply("Trial builder permissions assigned to <@" + event.getMember().getId() + ">").queue();	
					
					try {
						FileWriter writer = new FileWriter(file);
						writer.write(event.getMember().getId() + "\n");	
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				
				scan.close();		
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//-----------------------------------------------------------------------------------------------------------------------------
//get server stats
		
		if (event.getName().equals("server"))	
			event.replyEmbeds(midwest.build()).queue();
		
//-------------------------------------------------------------------------------------------------------------------------------------------	
//Retrieves an application of user given a discord ID(or Tag) and an integer representing which application in the list to return
		
		if (event.getName().equals("getapp")) { 
			if (staff.contains(event.getMember())) {
				
				Member user = event.getOption("user").getAsMember();
				int appNum = Integer.parseInt(event.getOption("n").getAsString());
				boolean isNull = false;
				
				//Test run for errors
				BTE.getApplicationHistory(user.getId()); 
				//if theres an exception in retrieving the member list then it stores the stacktrace of that exception in the API objects public string
				if (!BTE.stackTrace.equals("") && !BTE.stackTrace.equals("User has not applied to the team nor have they been merged into it")) 
					event.reply(BTE.stackTrace).queue();
				
				
				//If user not found on team
				else if (BTE.stackTrace.equals("User has not applied to the team nor have they been merged into it") && !isNull) 
					event.reply(BTE.stackTrace).queue();		
				
				
				//If you are trying to get the 0th application
				else if (appNum == 0 && !isNull) 
					event.reply("Cannot retrieve the 0th application on a user").queue();
				
				
				else {
					ApplicationInfo application = BTE.getApplicationHistory(user.getId());
					int appIndex = appNum - 1;
					
					//If user exists on team but no applications exist for them, user was merged into the team
					if ((application.getApplications().isEmpty())) 	
						event.reply("This user was most likely merged into the team\nNo applications found for user").queue();
					
					
					//If you are trying to retrieve more applications then the user has
					else if (appIndex >= application.getApplications().size() && application.getApplications().size() > 0) 
						event.reply("User does not have that many applications, try a lower number.").queue();
					
					
					//If you are trying to retrieve an application that does not exist
					else if (application.getApplications().size() < 1) 
						event.reply("User does not have any applications. They were most likely merged into the team").queue();
					

					//Returns application
					else {
						EmbedBuilder app = new EmbedBuilder();
						EmbedBuilder images = new EmbedBuilder();
						images.setColor(Color.blue);
						app.setColor(Color.BLUE);

						app.setTitle("[" + appNum + "] Application Questions for " + user.getUser().getAsTag());
						images.setTitle("[" + appNum + "] Application Media for " + user.getUser().getAsTag());

						app.addField(application.getApplications().get(appIndex).getAnswerList().get(0).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(0).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(1).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(1).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(3).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(3).getAnswer(), false);
						app.addBlankField(false);
						app.addField(application.getApplications().get(appIndex).getAnswerList().get(4).getQuestion(), application.getApplications().get(appIndex).getAnswerList().get(4).getAnswer(), false);
						app.addBlankField(false);
						app.addField("Previous BTE Builds", application.getApplications().get(appIndex).getUrl(), false);
						app.addBlankField(false);
						app.addField("Reference Sources Used", application.getApplications().get(appIndex).getAnswerList().get(2).getAnswer(), false);

						event.replyEmbeds(app.build()).queue();	
					}
				}
			}
			else 
				event.reply("Only staff can execute this command").queue();
		}
	}
}		
/*==========================================================================================================================================
======================================================LEGACY: replaced with DiscordSRV======================================================
============================================================================================================================================

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

			




