package Events;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;

public class RoleEvents extends ListenerAdapter {

	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		Role improve = guild.getRoleById(1006335736695500801L);
		File file = new File("/src/main/java/Resources//RejectedUsers.txt");
		
		//If user is assigned a specific role, they are given a minimum 2 week period to improve their building and reapply
		if (event.getRoles().contains(improve)) {
			try {
				FileWriter writer = new FileWriter(file);
				writer.write(event.getMember().getId() + ":" + System.currentTimeMillis() + 1209600000 + "\n");	
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	
		
		
		
/*==========================================================================================================================================
======================================================LEGACY: replaced with ticket bot/SRV==================================================
============================================================================================================================================
		
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
						
						if (BTE.getPendingApplications() != null && BTE.getPendingApplications().getApplications().size() > 0) {						    							    	
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
					try {
						if (BTE.getPendingApplications().getApplications().size() > 0) {    	
							   for (int i = 0; i < BTE.getPendingApplications().getApplications().size(); i++) {
						    	emb.addField(BTE.getPendingApplications().getApplications().get(i).user.getUserTag() + " has applied to the team.\n" ,
						   				"View their application here: https://buildtheearth.net/buildteams/36/applications/" 
						    			+ BTE.getPendingApplications().getApplications().get(i).id + "\n\n", false);	
							   }
							   	staff.sendMessageEmbeds(emb.build()).queue();	    
						}
					} catch (NullPointerException r) {
						r.printStackTrace();
					}
					//There is a message history but the message was not sent by the bot send new message instead of trying to edit
				} catch (IllegalStateException e) {
					if (BTE.getPendingApplications().getApplications().size() > 0) {    	
						   for (int i = 0; i < BTE.getPendingApplications().getApplications().size(); i++) {
					    	emb.addField(BTE.getPendingApplications().getApplications().get(i).user.getUserTag() + " has applied to the team.\n" ,
					   				"View their application here: https://buildtheearth.net/buildteams/36/applications/" 
					    			+ BTE.getPendingApplications().getApplications().get(i).id + "\n\n", false);	
						   }
						   	staff.sendMessageEmbeds(emb.build()).queue();	    
					}
					
				} catch (NullPointerException e) {
					System.out.println("Somthing was null ig idk");
				}
			}
		}, 1000, 10000);
		
//-------------------------------------------------------------------------------------------------------------------------------------------	
//Compares team ID list with discord users and assigns build perms if necessary
		TextChannel test = guild.getTextChannelById(786328890280247327L);
		permTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {

				//For each guild member that is on website team, if they do not have builder role, assign builder role 	
				try {
						for (int i = 0; i < BTE.getMemberList().size(); i++) {				
							try {
								long memberId = guild.getMemberById(BTE.getMemberList().get(i).getAsJsonObject().get("discordId").getAsLong()).getIdLong();
								if (!guild.getMemberById(memberId).equals(null) && !guild.getMemberById(memberId).getRoles().contains(builder)) {
									guild.addRoleToMember(memberId, builder).queue();
								}	
							} catch (NullPointerException e) {
								//If discord ID does not exist in guild, skips user since role cannot be assigned
								continue;
							}
						}
				} catch (MalformedURLException e) {
					String stack = ExceptionUtils.getStackTrace(e);
					test.sendMessage("Malformed URL while retrieving member list <@387330197420113930>").queue();
					test.sendMessage(stack.subSequence(0, 1990)).queue();
				} catch (IOException e) {
					String stack = ExceptionUtils.getStackTrace(e);
					if (stack.contains("Server returned HTTP response code: 5"))
						test.sendMessage("Server side IO Exception while retrieving member list\n" + e.getMessage());
				}
			}		
		}, 1000, 300000);
		*/
	}	
	
}	

