package Events;

import java.awt.Color;
import java.time.Duration;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyMessageEventListener extends ListenerAdapter {
	
	class Tuple { 
	    private Message x;
	    private User y;
	    private MessageChannel z;
	    private long time;
	    
	    public Tuple(Message x, User y, MessageChannel z, long time) {
	    	this.x = x;
	    	this.y = y;
	    	this.z = z;
	    	this.time = time;
	    }
	    
	    public Message getMessage() {
	    	return x;
	    }
	    
	    public User getUser() {
	    	return y;
	    }
	    
	    public MessageChannel getChannel() {
	    	return z;
	    }
	    
	    public long getTime() {
	    	return time;
	    }
	    @Override
	    public String toString() {
	    	return x.getContentRaw() + "," + y.getAsTag() + "," + z.getName() + "," + time;
	    	
	    }
	} 
	
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	private LinkedList<Tuple> messageCache = new LinkedList<Tuple>();
	
	//Channel spam detection
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {	

		//The time interval in milliseconds the messages need to be sent within for it to be considered channel spam
		int interval = 10000;
	
		//The amount of messages to be cached and analyzed. In execution, this will consider messageAmount + 1 messages when determining potential channel spam
		//since the event is only triggered when a message is recieved
		int messageAmount = 2;		
		
		/*
		 * E.x messageAmount = 2 
		 * will consider + 1 messages than whats in its cache. In this case, 3 messages instead of 2. 
		 * 
		 * 			1							      2		    			3
		 * |	             |	   	          |					  | |			   	    |	 
		 * |Current message  |    Compared    |messageCache.get(0)| |messageCache.get(1)|
		 * |recieved by event|	  against     |	               	  | |			        |
		 * |	             |
		 */
		
		int cacheSize = messageAmount;
		
		//Counts similar messages
		int counter = 0;
		User spammer = null;
		
	
		if (event.isFromGuild() && event.getChannelType().isMessage() && !event.getMessage().isEphemeral() && !event.getAuthor().isBot()) {
									
			//Comapares cached messages and authors with new messages. 	
			if (messageCache.size() == cacheSize) {	
				
				//Iterates through cache and determines if channel spam is happening						
					for (int i = 0; i < messageCache.size(); i++) {
						if (event.getMessage().getContentRaw().equals(messageCache.get(i).getMessage().getContentRaw()) 
								&& event.getAuthor().equals(messageCache.get(i).getUser()) 
								&& !event.getChannel().equals(messageCache.get(i).getChannel())) {
							
							counter++;
							guild.getTextChannelById(786328890280247327L).sendMessage("c:" + counter).queue();
							spammer = event.getAuthor();
						}
					}
				
				//No spam detected carry on updating cache
				if (counter < messageAmount) {						
					messageCache.removeLast();
					messageCache.addFirst(new Tuple(event.getMessage(), event.getAuthor(), event.getChannel(), System.currentTimeMillis()));
				}
			}
			
			else {
				//keeps cache updated with most recent messages
				messageCache.addFirst(new Tuple(event.getMessage(), event.getAuthor(), event.getChannel(), System.currentTimeMillis()));
			}
			
			//The criteria for determining channel spam are:
			//If at least messageAmount messages have the same content and author but different channels
			//And if the time difference between the last cached message and the first cached message is less than interval				
			if (counter >= messageAmount && (messageCache.get(0).getTime() - (event.getMessage().getTimeCreated().toEpochSecond() * 1000)) < interval) {
				
				//The time between the first message in cache being recieved and the current message thats being processed
				double timeTotal = (messageCache.get(0).getTime() - event.getMessage().getTimeCreated().toEpochSecond())  / 1000.0;
				
				//The time between the first message in chace being recieved and the second message in cache being recieved
				double t1 = messageCache.get(0).getTime() -  messageCache.get(1).getTime() / 1000.0;
				
				//The time between the second message in cache being recieved and the current message being processed
				double t2 = ( messageCache.get(1).getTime() - event.getMessage().getTimeCreated().toEpochSecond()) / 1000.0;
				//		double t2 = (messageCache.get(0).getTime() - messageCache.get(messageCache.size() - 2).getTime()) / 1000.0; 
						
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.red);
				emb.setTitle(spammer.getAsTag() + " is suspected of channel spamming and has been muted");
				if (messageCache.get(0).getMessage().getContentRaw().length() < 1000) {
					emb.addField(messageAmount + " messages containing the same content were sent by this user in " + timeTotal + " seconds", 
						"`" + messageCache.get(0).getMessage().getContentRaw() + "` in " + messageCache.get(0).getChannel().getAsMention() + ": 0.000s\n"
						+ "`" + messageCache.get(1).getMessage().getContentRaw()+ "` in " + messageCache.get(1).getChannel().getAsMention() + ": " + t1 + "s\n"
						+ "`" + event.getMessage().getContentRaw() + "` in " + event.getMessage().getChannel().getAsMention() + ": " + t2 + "s", false);
				}
				else {
					emb.addField(messageAmount + " messages containing the same content were sent by this user in " + timeTotal + " seconds", 
							"`" + messageCache.get(0).getMessage().getContentRaw().substring(0, 100) + "...` in " + messageCache.get(0).getChannel().getAsMention() + ": 0.000s\n"
							+ "`" + messageCache.get(1).getMessage().getContentRaw().substring(0, 100) + "...` in " + messageCache.get(1).getChannel().getAsMention() + ": " + t1 + "s\n"
							+ "`" + event.getMessage().getContentRaw() + "` in " + event.getMessage().getChannel().getAsMention() + ": " + t2 + "s", false);
				}
			//	guild.getMember(spammer).timeoutFor(10, TimeUnit.MINUTES).queue();
				messageCache.get(0).getMessage().delete().queue();
				messageCache.get(1).getMessage().delete().queue();
				event.getMessage().delete().queue();
				
				//keeps cache updated with most recent messages after spam detected
				messageCache.removeLast();
				messageCache.addFirst(new Tuple(event.getMessage(), event.getAuthor(), event.getChannel(), System.currentTimeMillis()));
				
				guild.getTextChannelById(786328890280247327L).sendMessageEmbeds(emb.build()).queue();
			
			}
			guild.getTextChannelById(786328890280247327L).sendMessage("Current: " + event.getMessage().toString() + "Cached: " + messageCache.toString()).queue();
			guild.getTextChannelById(786328890280247327L).sendMessage(("" +(messageCache.get(0).getTime() - (event.getMessage().getTimeCreated().toEpochSecond() * 1000)) / 1000.0)).queue();
			guild.getTextChannelById(786328890280247327L).sendMessage("" + counter).queue();
		}
	}
}
