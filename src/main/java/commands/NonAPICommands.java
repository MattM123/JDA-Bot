package commands;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Consumer;

import javax.sound.sampled.AudioFormat.Encoding;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

public class NonAPICommands extends ListenerAdapter {
	
	private String pippenPoints = "";
	private String counter = "";
	private File buildCounts = new File(System.getProperty("user.dir") + "/src/main/java/commands/BuildCountData");
	private FileWriter append = null;
	private FileWriter overwrite = null;
	private boolean containsUser = false;
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		Guild guild = event.getGuild();
		
		EmbedBuilder helpMenu = new EmbedBuilder();
		helpMenu.setColor(Color.blue);
		helpMenu.setTitle("Command Information");

		helpMenu.addField("=link <minecraft username>", "Automatically assigns build perms and Builder role if user is on the team", false);
		helpMenu.addField("=applicant <minecraft username>", "Assigns user application build perms on the server", false);
		helpMenu.addField("=getapp -<DiscordID> -<number of app>", "Requires Staff role to view players applications", false);
		helpMenu.addField("=server", "Displays server status and resource usage.", false);
		helpMenu.addField("=map", "Shows all of the states that are currently being built accorss BTE Midwest", false);
		helpMenu.addField("=measure", "Measure tutorial derived from the BTE Support Bot", false);
		helpMenu.addField("=role <nameOfRole>", "Self assigns certain roles, use with no parameters for list of roles", false);
		helpMenu.addField("=/<command>", "Requires Admin role to send console command to Minecraft server", false);
		
		
		EmbedBuilder map = new EmbedBuilder();
		map.setTitle("BTE Midwest Map");
		map.setColor(Color.blue);
		map.setImage("https://cdn.discordapp.com/attachments/735998501053530163/901644652157997076/bte_midwest.png");
		
		//returns list of commands used by bot
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=help")) {
			event.getChannel().sendMessage(helpMenu.build()).queue();
		}
		
		//returns measure gif from BTE bot
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=measure")) {
			EmbedBuilder measure = new EmbedBuilder();
			measure.setColor(Color.blue);
			measure.setTitle("Google Earth Pro Measuring Tutotrial");
			measure.setFooter("Gif totally not stolen from the Build The Earth bot");
			measure.setImage("https://i.gyazo.com/thumb/1200/d58446cec35cc504bb36b749346041a9-gif.gif");
			event.getChannel().sendMessage(measure.build()).queue();	
		}
		
		//returns map image of states
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=map")) {
			event.getChannel().sendMessage(map.build()).queue();
		}
		
		//Pings Discord API
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=ping")) {
			MessageChannel channel = event.getChannel();
		    final long time = System.currentTimeMillis();
		    RestAction<Message> action = channel.sendMessage("Pinging Discord API...");
		    Consumer<Message> callback = (message) ->  {
		    	Message m = message;
		        m.editMessage("Discord API Response Time: " + (System.currentTimeMillis() - time) + "ms").queue();
		      };
		      
		     action.queue(callback);      
		}
		
//------------------------------------------------------------------------------------------------------------------------------------
//detect users who are unfamiliar with how to get started and sends them to the info channel
				
		String[] phrases = {"how do i apply","how to be builder","how do i be a builder","how do i become a builder",
							"how do i get builder", "where do i apply", "how do i join", "how to get builder", "how to apply", "submit my build application", 
							"submit my application", "how can i start building"};								
				
		for (int i = 0; i < phrases.length; i++) {
			if (event.getMessage().getContentRaw().contains(phrases[i]) || event.getMessage().getContentRaw().equalsIgnoreCase(phrases[i])) {
				event.getChannel().sendMessage("Go to <#776963885739409458> for info on how to get started").queue();
			}
		}				
		
		
		
//------------------------------------------------------------------------------------------------------------------------------------
//command for self-assigning roles
		
		Role[] stateRoles = {
			guild.getRoleById(735995136978321541L), //nebraska
			guild.getRoleById(798079627360337970L), //wisconsin
			guild.getRoleById(735995164493086720L), //iowa
			guild.getRoleById(735995176165834756L), //kasnas
			guild.getRoleById(900746635427053678L), //michigan
			guild.getRoleById(735995115113414656L), //missouri
			guild.getRoleById(808415301799641119L), //oklahoma
			guild.getRoleById(735995095773609986L), //illinois
			guild.getRoleById(735995196738633819L) //minnesota
		};
				
		if (event.getMessage().getContentRaw().startsWith("=role")) {
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String rolebuilder = "";
		
			for (int i = 6; i < chararr.length; i++) {
				rolebuilder += chararr[i];
			}
			rolebuilder = rolebuilder.toLowerCase();
						
			//if user provides no role name to obtain
			if (rolebuilder.isEmpty() || (!rolebuilder.equalsIgnoreCase("nebraska") && !rolebuilder.equalsIgnoreCase("iowa") && !rolebuilder.equalsIgnoreCase("kansas")
					&& !rolebuilder.equalsIgnoreCase("oklahoma") && !rolebuilder.equalsIgnoreCase("missouri") && !rolebuilder.equalsIgnoreCase("michigan") 
					&& !rolebuilder.equalsIgnoreCase("minnesota") && !rolebuilder.equalsIgnoreCase("illinois") && !rolebuilder.equalsIgnoreCase("wisconsin"))) {
				
				EmbedBuilder roleMenu = new EmbedBuilder();
				
				roleMenu.setColor(Color.blue);
				roleMenu.addField("Command Usage", "=role nameOfRole", false);
				roleMenu.addField("*__State Roles__*", "Nebraska\nIowa\nKansas\nMinnesota\nMichigan\nWisconsin\nMissouri\nIllinois\nOklahoma", false);
				roleMenu.addField("*__Other Roles__*", "Event - This role is notified of build events", false);
				roleMenu.setFooter("You can only have one state role at a time!");
				
				event.getChannel().sendMessage(roleMenu.build()).queue();
				
			}
			
			//if role name is not empty
			else {
				//if user is obtaining state role when they already have one removes current state roles and assigns new one they chose				
				
				for (int i = 0; i < stateRoles.length; i++) {
					if (event.getMember().getRoles() != null && event.getMember().getRoles().contains(stateRoles[i])) {
						guild.removeRoleFromMember(event.getMember().getIdLong(), stateRoles[i]).queue();
						break;
					}
				}
				
				switch (rolebuilder.toLowerCase()) {
				case "nebraska": 
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[0]).queue();
					break;
				case "wisconsin":
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[1]).queue();
					break;
				case "iowa":
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[2]).queue();
					break;
				case "kansas":
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[3]).queue();
					break;
				case "michigan":
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[4]).queue();
					break;
				case "missouri":
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[5]).queue();
					break;
				case "oklahoma":
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[6]).queue();
					break;
				case "illinois":
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[7]).queue();
					break;
				case "minnesota":
					guild.addRoleToMember(event.getMember().getIdLong(), stateRoles[8]).queue();
					break;
				case "event":
					guild.addRoleToMember(event.getMember().getIdLong(), guild.getRoleById(781973005223854120L)).queue();
					break;
				}	
			}
		}
		
		TextChannel buildSubmissionChannel = guild.getTextChannelById(926285692542283846L);
		TextChannel trackerChannel = guild.getTextChannelById(926460270782586921L);
		
		
		//BuildCount Tracker
		if (event.getMessage().getChannel().equals(buildSubmissionChannel) && ((event.getMessage().getContentRaw().contains(event.getAuthor().getAsTag())))) {
			
			trackerChannel.retrieveMessageById(trackerChannel.getLatestMessageIdLong()).queue((message) -> {
				String content = message.getContentRaw();
				String authorTag = event.getAuthor().getAsTag();
					
				if (content.length() < 1980) {
					//If a users build count is already in the message   
					if (content.contains(authorTag.substring(0, authorTag.length() - 5) + " : ")) {
						//Retrieving the users current build count, storing it, and incrementing it			
						for (int i = content.indexOf(authorTag.substring(0, authorTag.length() - 5) + " : "); i < content.length(); i++) {
							if (content.charAt(i) == ':') {
								boolean breakMeDaddy = false;
								
								for (int j = i + 2; j < content.length(); j++) {
									try {
										int s = Integer.parseInt(content.substring(j, j + 1));							
										counter += String.valueOf(s);
									}
									catch (NumberFormatException e) {
										breakMeDaddy = true;
										break;
									}
								}
								if (breakMeDaddy) {
									break;
								}
							}
						}
					
					int buildCount = Integer.parseInt(counter);
					//Incrementing build count and editing the count						
					String[] messageLines = content.split("\n");
					for (int i = 0; i < messageLines.length; i++) {
						if (messageLines[i].contains(authorTag.substring(0, authorTag.length() - 5))) {
							message.editMessage(content.replace(messageLines[i], authorTag.substring(0, authorTag.length() - 5) + " : " + (buildCount + 1))).queue();
							break;				
						}
						}
					}	
				}

				//if no count exists for user
				if (!content.contains(authorTag.substring(0, authorTag.length() - 5) + " : ")) {	
					message.editMessage(content += "\n" + authorTag.substring(0, authorTag.length() - 5) + " : 1").queue();
			
				}				
			});
			counter = "";
		}
	}
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		Guild guild = event.getGuild();
	
		//BuildTracker 2.0
		TextChannel builderSubmissions = guild.getTextChannelById(928365525355098112L);
		TextChannel stacktrace = guild.getTextChannelById(928822585779707965L);
		TextChannel backlog = guild.getTextChannelById(928431170620887080L);
		TextChannel errorlog = guild.getTextChannelById(928432209872977990L);
		containsUser = false;
		

		//If reaction was ✅ and was used in submission channel
		if (event.getReaction().getChannel().equals(builderSubmissions)) {	
			if (event.getReactionEmote().getEmoji().equals("✅")) {
					builderSubmissions.retrieveMessageById(event.getMessageIdLong()).queue((message) -> {
						//if database connection is successful	
						//String table = "CREATE TABLE " + "\"" + "buildcounts" + "\"" + "(" + "\"" + "id" + "\"" +  "INTEGER, " + "\"" + "counts" + "\"" + " INTEGER, " + "PRIMARY KEY(\"id\"))";
						String getIds = "SELECT id FROM buildcounts";
						   try {
							//   event.getChannel().sendMessage("break").queue();
							///   Statement stmt1  = Connect.connect().createStatement();
							//   int rs1 = stmt1.executeUpdate(write);
							   
							//   event.getChannel().sendMessage("Write: " + rs1).queue();
							   Statement stmt  = Connect.connect().createStatement();
							   ResultSet rs = stmt.executeQuery(getIds);
								
							   //Searching user IDs
							   while (rs.next()) {
								   event.getChannel().sendMessage(String.valueOf("Id: " + rs.getLong("id"))).queue();
								   //event.getChannel().sendMessage(String.valueOf("Count: " + rs.getInt("counts"))).queue();
							
							   }
						   } catch (SQLException e) {
							   errorlog.sendMessage(e.getMessage()).queue();
							   stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1500)).queue();
							   backlog.sendMessage(message.getAuthor().getId()).queue();
							   
							} 
						   
							if (Connect.connect() != null) {  
								try {
									Connect.connect().close();
								} catch (SQLException e) {
									errorlog.sendMessage(e.getMessage()).queue();
									stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1500)).queue();
								}  
							}  
					});
					/*
						for (int i = 0; i < content.size(); i++) {						
							String[] line = content.get(i).split(":");
							if (line[0].equals(message.getAuthor().getId())) {	
								event.getChannel().sendMessage("break1").queue();
								containsUser = true;
								int count = Integer.parseInt(line[1]);
								line[1] = String.valueOf(count += 1);
							}
							
								String replace = "";
								for(String str: content) {
									  replace += (str + "\n");
								}
								try {
									overwrite.write(replace);
									overwrite.close();
								} catch (IOException e) {
									backlog.sendMessage(message.getAuthor().getId()).queue();
						
									if (ExceptionUtils.getStackTrace(e).length() > 1500)
										errorlog.sendMessage(ExceptionUtils.getStackTrace(e).subSequence(0, 1500)).queue();
									else
										errorlog.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
								}
								List<String> cd = null;
								try {
									cd = Files.readAllLines(Paths.get(buildCounts.getPath()));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								for (int j = 0; j < cd.size(); j++) {
									event.getChannel().sendMessage(cd.get(i)).queue();
								}
							}
						
						
						event.getChannel().sendMessage(String.valueOf(containsUser)).queue();
						if (!containsUser) {
							try {
								event.getChannel().sendMessage("break2").queue();								
								append.write(message.getAuthor().getId() + ":1\n");			
								append.close();
								event.getChannel().sendMessage("break3").queue();
								List<String> c = Files.readAllLines(Paths.get(buildCounts.getPath()));
								for (int i = 0; i < c.size(); i++) {
									event.getChannel().sendMessage(c.get(i)).queue();
								}
								event.getChannel().sendMessage("break4").queue();
							} catch (IOException e) {
								backlog.sendMessage(message.getAuthor().getId()).queue();
					
								if (ExceptionUtils.getStackTrace(e).length() > 1500)
									errorlog.sendMessage(ExceptionUtils.getStackTrace(e).subSequence(0, 1500)).queue();
								else
									errorlog.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
							}
							*/							
						}
					
			
		}
	}
}
