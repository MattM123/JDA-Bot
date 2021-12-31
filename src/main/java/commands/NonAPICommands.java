package commands;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

public class NonAPICommands extends ListenerAdapter {
	
	private int pippenPoints = 0;
	private String counter = "";
	private int incrementMe;
	
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
		
		//Pippen Tracker
		TextChannel pippenSubmissionChannel = guild.getTextChannelById(926285739627532309L);
		TextChannel pippenTrackerChannel = guild.getTextChannelById(926290849011228753L);

		if (event.getMessage().getChannel().equals(pippenSubmissionChannel) && ((event.getMessage().getContentRaw().contains("PippenFTS#3088") && event.getAuthor().getIdLong() == 514185975744823302L))) {
			pippenPoints++;
			
			if (!pippenTrackerChannel.hasLatestMessage()) {
				pippenTrackerChannel.sendMessage("PippenTracker 1.0").queue();
			}
						
			pippenTrackerChannel.retrieveMessageById(pippenTrackerChannel.getLatestMessageIdLong()).queue((message) -> {
					pippenTrackerChannel.editMessageById(pippenTrackerChannel.getLatestMessageIdLong(),"**__You need more then " + pippenPoints + " completed buildings to beat Pippen!__**").queue();
			});
		}
		
		//BuildCount Tracker
		TextChannel buildSubmissionChannel = guild.getTextChannelById(926285692542283846L);
		TextChannel trackerChannel = guild.getTextChannelById(926460270782586921L);

		if (event.getMessage().getChannel().equals(buildSubmissionChannel) && ((event.getMessage().getContentRaw().contains(event.getAuthor().getAsTag())))) {
			pippenPoints++;
			
			if (!trackerChannel.hasLatestMessage()) {
				trackerChannel.sendMessage("BuildTracker 1.0").queue();
			}
			
			trackerChannel.retrieveMessageById(trackerChannel.getLatestMessageIdLong()).queue((message) -> {
					
				//If a users build count is already in the message   
				if (message.getContentRaw().contains(event.getAuthor().getAsTag() + " : ")) {
	
					//Retrieving the users current build count, storing it, and incrementing it
					
					for (int i = message.getContentRaw().indexOf(event.getAuthor().getAsTag() + " : "); i < message.getContentRaw().length(); i++) {
						if (message.getContentRaw().charAt(i) == ':') {
							for (int j = i; j < message.getContentRaw().length(); j++) {
								try {
									int s = Integer.parseInt(message.getContentRaw().substring(j, j));
									counter += String.valueOf(s);
								}
								catch (NumberFormatException e) {
									break;
								}
							}
						}
					}
				}
				else {
					String f = message.getContentRaw();
					message.editMessage(f += event.getAuthor().getAsTag() + " : 1\n").queue();
				}
			});
			
				try {
					incrementMe = Integer.parseInt(counter);
					incrementMe++;
				}
				catch (NumberFormatException e) {
					
				}

				//Editing the count to reflect the incremented value
				trackerChannel.retrieveMessageById(trackerChannel.getLatestMessageIdLong()).queue((message) -> {
					for (int i = message.getContentRaw().indexOf(event.getAuthor().getAsTag() + " : "); i < message.getContentRaw().length(); i++) {
						if (message.getContentRaw().charAt(i) == ':') {
							message.getContentRaw().replace(message.getContentRaw().substring(i + 2, counter.length()), String.valueOf(incrementMe));
						}
					}
					
	
				});
				counter = "";
				
			}
	}
/*	
	@Override  
	public void onReady(ReadyEvent event){
		Timer timer = new Timer();
		TextChannel pippenSubmissionChannel = event.getJDA().getGuildById(735990134583066679L).getTextChannelById(926285739627532309L);
		TextChannel pippenTrackerChannel = event.getJDA().getGuildById(735990134583066679L).getTextChannelById(926290849011228753L);
		long latestMessageLong = pippenSubmissionChannel.getLatestMessageIdLong();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				pippenSubmissionChannel.retrieveMessageById(latestMessageLong).queue((message) -> {
					 	message.editMessage("You need more then " + pippenPoints + " completed buildings to beat Pippen!" ).queue();
					    message.editMessage("You need more then " + pippenPoints + " completed buildings to beat Pippen!" ).queue();		    					    
				});
			}
		}, 1000, 1000);
	}
	*/
}
