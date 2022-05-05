package commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.codec.net.QCodec;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;

public class NonAPICommands extends ListenerAdapter {

	private String counter = "";
	private int page = 1;
	private BuildLeaderboard bl = new BuildLeaderboard();
	public static Guild pubGuild;
	public int timeout = 6;
	public boolean hasPoll = false;
	public long pollMessage = 0;
	private String[] options;
	private EmbedBuilder poll;
	private List<User> users;
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		Guild guild = event.getGuild();
		
		EmbedBuilder helpMenu = new EmbedBuilder();
		helpMenu.setColor(Color.blue);
		helpMenu.setTitle("Command Information");

		helpMenu.addField("=link <minecraft username>", "Automatically assigns build perms and Builder role if user is on the team", false);
		helpMenu.addField("=applicant <minecraft username>", "Assigns user application build perms on the server", false);
		helpMenu.addField("=getapp -<DiscordID OR DiscordTag> -<number of app>", "Requires Staff role to view players applications", false);
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
			event.getChannel().sendMessageEmbeds(helpMenu.build()).queue();
		}
		
		//returns measure gif from BTE bot
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=measure")) {
			EmbedBuilder measure = new EmbedBuilder();
			measure.setColor(Color.blue);
			measure.setTitle("Google Earth Pro Measuring Tutotrial");
			measure.setImage("https://i.gyazo.com/thumb/1200/d58446cec35cc504bb36b749346041a9-gif.gif");
			
			EmbedBuilder measure1 = new EmbedBuilder();
			measure1.setColor(Color.blue);
			measure1.setTitle("/tpll Outline Tutorial");
			measure1.setImage("https://i.imgur.com/G5c6bJl.gif");
			event.getChannel().sendMessageEmbeds(measure.build(), measure1.build()).queue();	
		}
		
		//returns map image of states
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=map")) {
			event.getChannel().sendMessageEmbeds(map.build()).queue();
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
//command for self-assigning roles
		//LEGACY: replaced with yagpdb
/*		
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
					&& !rolebuilder.equalsIgnoreCase("minnesota") && !rolebuilder.equalsIgnoreCase("illinois") && !rolebuilder.equalsIgnoreCase("wisconsin")
					&& !rolebuilder.equalsIgnoreCase("event"))) {
				
				EmbedBuilder roleMenu = new EmbedBuilder();
				
				roleMenu.setColor(Color.blue);
				roleMenu.addField("Command Usage", "=role nameOfRole", false);
				roleMenu.addField("*__State Roles__*", "Nebraska\nIowa\nKansas\nMinnesota\nMichigan\nWisconsin\nMissouri\nIllinois\nOklahoma", false);
				roleMenu.addField("*__Other Roles__*", "Event - This role is notified of build events", false);
				roleMenu.setFooter("You can only have one state role at a time!");
				
				event.getChannel().sendMessageEmbeds(roleMenu.build()).queue();
				
			}
			
			//if role name is not empty
			else {
				
				//adds or removes event role
				if (rolebuilder.equalsIgnoreCase("event")) {
					if (!event.getMember().getRoles().contains(guild.getRoleById(781973005223854120L)))
						guild.addRoleToMember(event.getMember().getIdLong(), guild.getRoleById(781973005223854120L)).queue();
					else
						guild.removeRoleFromMember(event.getMember().getIdLong(), guild.getRoleById(781973005223854120L)).queue();
				}
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
					}
				}
			}
		}
		*/	
		//BuildCount Tracker
		TextChannel buildSubmissionChannel = guild.getTextChannelById(926285692542283846L);
		TextChannel trackerChannel = guild.getTextChannelById(926460270782586921L);
		
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
		
//------------------------------------------------------------------------------------------------------------------------------------
//manually increments database record by 1
		TextChannel audit = guild.getTextChannelById(929158963499515954L);
		TextChannel stacktrace = guild.getTextChannelById(928822585779707965L);
		if (event.getMessage().getContentRaw().startsWith("=add ") && guild.getMemberById(event.getAuthor().getId()).getRoles().contains(guild.getRoleById(901162820484333610L))) {
			boolean isPresent = false;
			Connect.connect();
			
			String id = "";
			for (int i = 5; i < event.getMessage().getContentRaw().length(); i++) {
				id += event.getMessage().getContentRaw().charAt(i);
			}
			
			//get ID and counts form database				
			try {
				String getIds = "SELECT id, count FROM buildcounts;";
				Statement stmt  = Connect.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery(getIds);
						
				//If id exists in table, increment build count of id
				while (rs.next()) {			
					if (rs.getLong("id") == Long.parseLong(id)) {
						String getCount = "SELECT count FROM buildcounts WHERE id = " + id + ";";
						Statement stmt1  = Connect.getConnection().createStatement();
						ResultSet rs1 = stmt1.executeQuery(getCount);
											
						rs1.next();
						String incrementCount = "UPDATE buildcounts SET count = " + (rs1.getInt("count") + 1) + " WHERE id = " + id + ";";
						Statement stmt2  = Connect.getConnection().createStatement();
						stmt2.executeUpdate(incrementCount);
						isPresent = true;	
						audit.sendMessage("**[DATA]** Manually incremented record for " + guild.getMemberById(id).getUser().getAsTag() + " (" + (rs1.getInt("count") + 1) + ").").queue();
						break;
					}
				}					
				
				//if id does not exist in table, add record for id with count of 1
				if (!isPresent) {
					audit.sendMessage("**[ERROR]** Record for " + guild.getMemberById(id).getUser().getAsTag() + " could not be manually incremented since it does not exist.").queue();
				}
			} catch (SQLException e) {
				audit.sendMessage("**[ERROR]** Could not manually increment record. \n[ERROR] " + e.getMessage() + ".").queue();	
			}
			
			if (Connect.getConnection() != null) {  
				try {
					Connect.getConnection().close();
				} catch (SQLException e) {
					audit.sendMessage("**[ERROR]** " + e.getMessage()).queue();
					if (ExceptionUtils.getStackTrace(e).length() >= 1900)
						stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1900)).queue();
					else {
						stacktrace.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
					}
				} 							
			}
		}
		
//------------------------------------------------------------------------------------------------------------------------------------
//manually decrements database record by 1
		if (event.getMessage().getContentRaw().startsWith("=remove ") && guild.getMemberById(event.getAuthor().getId()).getRoles().contains(guild.getRoleById(901162820484333610L))) {			
			boolean isPresent = false;
			Connect.connect();
			
			String id = "";
			for (int i = 8; i < event.getMessage().getContentRaw().length(); i++) {
				id += event.getMessage().getContentRaw().charAt(i);
			}
			
			//get ID and counts form database				
			try {
				String getIds = "SELECT id, count FROM buildcounts;";
				Statement stmt  = Connect.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery(getIds);
						
				//If id exists in table, increment build count of id
				while (rs.next()) {			
					if (rs.getLong("id") == Long.parseLong(id)) {
						if (rs.getInt("count") > 0) {
							String getCount = "SELECT count FROM buildcounts WHERE id = " + id + ";";
							Statement stmt1  = Connect.getConnection().createStatement();
							ResultSet rs1 = stmt1.executeQuery(getCount);
												
							rs1.next();
							String incrementCount = "UPDATE buildcounts SET count = " + (rs1.getInt("count") - 1) + " WHERE id = " + id + ";";
							Statement stmt2  = Connect.getConnection().createStatement();
							stmt2.executeUpdate(incrementCount);
							isPresent = true;	
							audit.sendMessage("**[DATA]** Manually decremented record for " + guild.getMemberById(id).getUser().getAsTag() + " (" + (rs1.getInt("count") - 1) + ").").queue();
							break;
						}
						else {
							isPresent = true;	
							audit.sendMessage("**[ERROR]** Could not manually decrement a record that is already 0.").queue();
						}
					}
				}					
				
				//if id does not exist in table, add record for id with count of 1
				if (!isPresent) {
					audit.sendMessage("**[ERROR]** Record for " + guild.getMemberById(id).getUser().getAsTag() + " could not be manually decremented since it does not exist.").queue();
				}
			} catch (SQLException e) {
				audit.sendMessage("**[ERROR]** Could not manually decrement record. \n[ERROR] " + e.getMessage() + ".").queue();	
			}
			
			if (Connect.getConnection() != null) {  
				try {
					Connect.getConnection().close();
				} catch (SQLException e) {
					audit.sendMessage("**[ERROR]** " + e.getMessage()).queue();
					if (ExceptionUtils.getStackTrace(e).length() >= 1900)
						stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1900)).queue();
					else {
						stacktrace.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
					}
				} 							
			}
		}
//------------------------------------------------------------------------------------------------------------------------------------
//merge backlog into database 
		TextChannel backlog = guild.getTextChannelById(928431170620887080L);
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=merge") && guild.getMemberById(event.getAuthor().getId()).getRoles().contains(guild.getRoleById(901162820484333610L))) {
			Connect.connect();
			
			//For all messages containing an ID in backlog, increments the corresponding database record by 1
			backlog.getHistory().retrievePast(100).queue(messages -> {
				if (messages.size() <= 0) {
					audit.sendMessage("**[BACKLOG]** Could not merge blacklog since there are no messages to merge.").queue();	
				}
				else {
					boolean isPresent = false;
					for (int i = 0; i < messages.size(); i++) {
						if (messages.get(i).getContentRaw().length() == 18) {
							//get ID and counts form database				
							try {
								String getIds = "SELECT id, count FROM buildcounts;";
								Statement stmt  = Connect.getConnection().createStatement();
								ResultSet rs = stmt.executeQuery(getIds);
											
								//If id exists in table, increment build count of id
								while (rs.next()) {			
									if (rs.getLong("id") == Long.parseLong(messages.get(i).getContentRaw())) {
										String getCount = "SELECT count FROM buildcounts WHERE id = " + Long.parseLong(messages.get(i).getContentRaw()) + ";";
										Statement stmt1  = Connect.getConnection().createStatement();
										ResultSet rs1 = stmt1.executeQuery(getCount);
															
										rs1.next();
										String incrementCount = "UPDATE buildcounts SET count = " + (rs1.getInt("count") + 1) + " WHERE id = " + Long.parseLong(messages.get(i).getContentRaw()) + ";";
										Statement stmt2  = Connect.getConnection().createStatement();
										stmt2.executeUpdate(incrementCount);
										isPresent = true;
										break;									
									}	
									messages.get(i).delete().queue();
								}	
				
								//if id does not exist in table, add record for id with count of 1
								if (!isPresent) {
									String addUser = "INSERT INTO buildcounts VALUES (" + Long.parseLong(messages.get(i).getContentRaw()) + ", 1);"; 
									Statement stmt2  = Connect.getConnection().createStatement();
									stmt2.executeUpdate(addUser);
									audit.sendMessage("**[DATA]** New record added for " + messages.get(i).getAuthor().getAsTag() + " with an ID of " + Long.parseLong(messages.get(i).getContentRaw()) + " (1).").queue();
								}
							} catch (SQLException e) {	
								audit.sendMessage("**[ERROR]** Backlog cannot be merged right now").queue();
								if (ExceptionUtils.getStackTrace(e).length() >= 1900)
									stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1900)).queue();
								else {
									stacktrace.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
								}
								break;
							} catch (NumberFormatException e) {
								audit.sendMessage("**[ERROR]** Backlog message could not be merged because it is not a vaild user ID.").queue();
							}
							   
							if (Connect.getConnection() != null) {  
								try {
									Connect.getConnection().close();
								} catch (SQLException e) {
									audit.sendMessage("**[ERROR]** " + e.getMessage()).queue();
									if (ExceptionUtils.getStackTrace(e).length() >= 1900)
										stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1900)).queue();
									else {
										stacktrace.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
									}
								}  
							} 
						}
					}
					audit.sendMessage("**[BACKLOG]** Merge successful.").queue();	
					
					if (Connect.getConnection() != null) {  
						try {
							Connect.getConnection().close();
						} catch (SQLException e) {
							audit.sendMessage("**[ERROR]** " + e.getMessage()).queue();
							if (ExceptionUtils.getStackTrace(e).length() >= 1900)
								stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1900)).queue();
							else {
								stacktrace.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
							}
						} 							
					}
				}
			});
		}
		
//------------------------------------------------------------------------------------------------------------------------------------
//creates poll
		if (event.getMessage().getContentRaw().startsWith("=poll")) {
			String content = event.getMessage().getContentRaw();
			String opts = "";
			String title = "";
			poll = new EmbedBuilder();
			
			if (content.contains("-opts")) {
				String[] args = {content.substring(6, content.indexOf("-opts ")), content.substring(content.indexOf("-opts ") + 6)};
				title = args[0];
				opts = args[1];
				
				
				if (!title.isEmpty() && !opts.isEmpty()) {
					hasPoll = true;
					options = args[1].split(",");

					if (poll != null) {
						poll.setTitle(title);
						poll.setColor(Color.blue);
					
						
						for (int i = 0; i < options.length; i++) {
							poll.addField(options[i], "Score: 0.0", false);
						}
						//stores message id of poll for use in calculating scores
						event.getChannel().sendMessageEmbeds(poll.build()).queue((message) -> {
							pollMessage = message.getIdLong();
						});
					}
					else {
						event.getChannel().sendMessage("Cannot create poll, please try again.").queue();
					}
				}
				else {
					event.getChannel().sendMessage("Title and or poll options are missing.").queue();
				}
			}
			else {
				event.getChannel().sendMessage("You must specify poll options with the `-opts opt1,opt2,etc` argument").queue();
			}			
		}
		
	}

	@Override
	public void onReady(ReadyEvent e) {
//------------------------------------------------------------------------------------------------------------------------------------
//Updates Leaderboard
		pubGuild = Bot.jda.getGuildById(735990134583066679L);
		TextChannel leaderboard = pubGuild.getTextChannelById(929171594125914152L);
		
		//turns page every 8 seconds
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {		
				if (page == bl.pages) {
					page = 0;
				}	
				
				leaderboard.retrieveMessageById(leaderboard.getLatestMessageId()).queue(message -> {		
					try { 	
						page += 1;
						bl.build().paginate(message, page);
							
					} catch (IllegalArgumentException e) {
						bl.refresh();
					}								
				});
			}
		}, 8000, 8000);
		
		//Refresh data every minute
		Timer timer1 = new Timer();
		timer1.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				bl.refresh();
			}
		}, 60000, 60000);
	}
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {	
		Guild guild = event.getGuild();
		
		//BuildTracker 2.0
		TextChannel audit = guild.getTextChannelById(929158963499515954L);
		TextChannel backlog = guild.getTextChannelById(928431170620887080L);
		TextChannel builderSubmissions = guild.getTextChannelById(928365525355098112L);
		TextChannel stacktrace = guild.getTextChannelById(928822585779707965L);
		
		//If reaction was ✅ ,  was used in submission channel, and used by a staff member
		if (event.getReaction().getChannel().equals(builderSubmissions)) {	
			if (event.getReactionEmote().getEmoji().equals("✅") && event.getMember().getRoles().contains(guild.getRoleById(901162820484333610L))) {
					builderSubmissions.retrieveMessageById(event.getMessageIdLong()).queue((message) -> {
						boolean isPresent = false;
						Connect.connect();
						
						//get ID and counts from database				
						try {
							String getIds = "SELECT id, count FROM buildcounts;";
							Statement stmt  = Connect.getConnection().createStatement();
							ResultSet rs = stmt.executeQuery(getIds);
									
							//If id exists in table, increment build count of id
							while (rs.next()) {			
								if (rs.getLong("id") == message.getAuthor().getIdLong()) {
									String getCount = "SELECT count FROM buildcounts WHERE id = " + message.getAuthor().getIdLong() + ";";
									Statement stmt1  = Connect.getConnection().createStatement();
									ResultSet rs1 = stmt1.executeQuery(getCount);
													
									rs1.next();
									String incrementCount = "UPDATE buildcounts SET count = " + (rs1.getInt("count") + 1) + " WHERE id = " + message.getAuthor().getIdLong() + ";";
									Statement stmt2  = Connect.getConnection().createStatement();
									stmt2.executeUpdate(incrementCount);
									isPresent = true;
									break;
								}
							}					
							
							//if id does not exist in table, add record for id with count of 1
							if (!isPresent) {
								String addUser = "INSERT INTO buildcounts VALUES (" + message.getAuthor().getId() + ", 1);"; 
								Statement stmt2  = Connect.getConnection().createStatement();
								stmt2.executeUpdate(addUser);
								audit.sendMessage("**[DATA]** New record added for " + message.getAuthor().getAsTag() + " with an ID of " + message.getAuthor().getId() + " (1).").queue();
							}
						} catch (SQLException e) {
							backlog.sendMessage(message.getAuthor().getId()).queue();	
							audit.sendMessage("**[ERROR]** " + e.getMessage()).queue();
							if (ExceptionUtils.getStackTrace(e).length() >= 1900)
								stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1900)).queue();
							else {
								stacktrace.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
							}
						} 
						   
						if (Connect.getConnection() != null) {  
							try {
								Connect.getConnection().close();
							} catch (SQLException e) {
								audit.sendMessage("**[ERROR]** " + e.getMessage()).queue();
								if (ExceptionUtils.getStackTrace(e).length() >= 1900)
									stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1900)).queue();
								else {
									stacktrace.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
								}
							} 							
						}  
					});					
				}
					
			
		}
		//If reaction matches a poll option, option score is incremented based on role
		if (hasPoll && pollMessage != 0 && event.getMessageIdLong() == pollMessage) {
			users = new ArrayList<>();
			event.getChannel().retrieveMessageById(pollMessage).queue((message) -> {
				
				//poulates user array and looks for duplicate users
				for (MessageReaction reaction : message.getReactions()){
				    try {
						users.addAll(reaction.retrieveUsers().submit().get());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//checks how many times user in in list
				int counter = 0;
				for (int e = 0; e < users.size(); e++) {
					if (users.get(e).equals(event.getUser())) 
						counter++;		
				}
				
					for (int i = 0; i < users.size(); i++) {														
						event.getChannel().sendMessage(users.toString()).queue();
						
					
						//if user is in list once or more, increment score
						if (counter == 1) {
							for (int r = 0; r < options.length; r++) {
								if (options[r].contains(event.getReactionEmote().getName())) {
									double currentScore = Double.parseDouble(poll.getFields().get(r).getValue().substring(7));
				
									if (event.getMember().getRoles().contains(guild.getRoleById(735991952931160104L)) || event.getMember().getRoles().contains(guild.getRoleById(901920567664443392L))
										|| event.getMember().getRoles().contains(guild.getRoleById(958109276512084020L)) || event.getMember().getRoles().contains(guild.getRoleById(958109526551306350L))) {
										poll.getFields().set(r, new Field(options[r], "Score: " + String.valueOf(currentScore += 1.0), false));
										
										//edits embed to update score
										event.getChannel().editMessageEmbedsById(pollMessage, poll.build()).queue();
									}
									else {
										poll.getFields().set(r, new Field(options[r], "Score: " + String.valueOf(currentScore += 0.5), false));
										
										//edits embed to update score
										event.getChannel().editMessageEmbedsById(pollMessage, poll.build()).queue();
									}
								}
							}
						}
						//if user is not in list on reaction add decrement score
						
						else {	
							/*
							for (int w = 0; w < options.length; w++) {
								if (options[w].contains(event.getReactionEmote().getName())) {
									double currentScore = Double.parseDouble(poll.getFields().get(i).getValue().substring(7));

									if (event.getMember().getRoles().contains(guild.getRoleById(735991952931160104L)) || event.getMember().getRoles().contains(guild.getRoleById(901920567664443392L))
										|| event.getMember().getRoles().contains(guild.getRoleById(958109276512084020L)) || event.getMember().getRoles().contains(guild.getRoleById(958109526551306350L))) {
										poll.getFields().set(w, new Field(options[w], "Score: " + String.valueOf(currentScore -= 1.0), false));
										
										//edits embed to update score
										event.getChannel().editMessageEmbedsById(pollMessage, poll.build()).queue();
									}
									else {
										poll.getFields().set(w, new Field(options[w], "Score: " + String.valueOf(currentScore -= 0.5), false));
										
										//edits embed to update score
										event.getChannel().editMessageEmbedsById(pollMessage, poll.build()).queue();
									}
								}
								
							}
							*/
							event.getChannel().sendMessage(users.toString()).queue();
						}
					}			
				});
			}
		}		
	

	
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {	
		Guild guild = event.getGuild();	
		int counter = 0;
		for (int e = 0; e < users.size(); e++) {
			if (users.get(e).equals(event.getUser())) 
				counter++;		
		}
		
		//if user is not in list after removing emoji, decrement score on emote remove
		if (hasPoll && pollMessage != 0 && event.getMessageIdLong() == pollMessage && counter <= 2) {
			for (int w = 0; w < options.length; w++) {
				if (options[w].contains(event.getReactionEmote().getName())) {
					double currentScore = Double.parseDouble(poll.getFields().get(w).getValue().substring(7));

					if (event.getMember().getRoles().contains(guild.getRoleById(735991952931160104L)) || event.getMember().getRoles().contains(guild.getRoleById(901920567664443392L))
						|| event.getMember().getRoles().contains(guild.getRoleById(958109276512084020L)) || event.getMember().getRoles().contains(guild.getRoleById(958109526551306350L))) {
						poll.getFields().set(w, new Field(options[w], "Score: " + String.valueOf(currentScore -= 1.0), false));
						
						//edits embed to update score
						event.getChannel().editMessageEmbedsById(pollMessage, poll.build()).queue();
					}
					else {
						poll.getFields().set(w, new Field(options[w], "Score: " + String.valueOf(currentScore -= 0.5), false));
						
						//edits embed to update score
						event.getChannel().editMessageEmbedsById(pollMessage, poll.build()).queue();
					}
				}
			}
		}
		
	}
	
}

