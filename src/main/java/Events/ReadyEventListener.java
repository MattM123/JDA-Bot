package Events;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyEventListener extends ListenerAdapter {
	
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
	private LinkedList<Tuple> messages = new LinkedList<Tuple>();
	
	//Channel spam detection
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {	

		//The time interval in milliseconds the messages need to be sent within for it to be considered channel spam
		int interval = 10000;
	
		//The amount of messages to be considered channel spam
		int messageAmount = 3;		
		
		//How many messages should be analysed to determine spam
		int cacheSize = 5;
	
		if (event.isFromGuild() && event.getChannelType().isMessage() && !event.getMessage().isEphemeral() && !event.getAuthor().isBot()) {
				//Comapares cached messages and authors with new messages. 				
				if (messages.size() == cacheSize) {
					int counter = 0;
					User spammer = null;
					
					//Iterates through cache and determines if channel spam is happening
					for (int i = 0; i < messages.size(); i++) {
						if (event.getMessage().equals(messages.get(i).getMessage()) 
								&& event.getAuthor().equals(messages.get(i).getUser()) 
								&& !event.getChannel().equals(messages.get(i).getChannel())) {
							counter++;
							spammer = event.getAuthor();
						}					
					}
					
					//The criteria for determining channel spam are:
					//If at least messageAmount messages have the same content and author but different channels
					//And if the time difference between the last cached message and the first cached message is less than interval
					if (counter >= messageAmount && messages.get(messages.size() - 1).getTime() - messages.get(0).getTime() < interval) {
						guild.getTextChannelById(786328890280247327L).sendMessage("Channel Spammed by :\n" + spammer.getAsTag() + " in " 
								+ (messages.get(messages.size() - 1).getTime() - messages.get(0).getTime()) + "ms").queue();
					}
					
					//keeps cache updated with most recent messages 
					messages.removeLast();
					messages.addFirst(new Tuple(event.getMessage(), event.getAuthor(), event.getChannel(), System.currentTimeMillis()));
				}
				else {
					messages.addFirst(new Tuple(event.getMessage(), event.getAuthor(), event.getChannel(), System.currentTimeMillis()));
				}

					
				//If cache is full and user has sent 3 of the same messages in 3 different channels within the time interval
				//it is considered channel spam
			//	if (messages.size() == cacheSize && messages.get(0).getTime() - messages.get(messages.size() - 1).getTime() < interval) {				
			//		guild.getTextChannelById(786328890280247327L).sendMessage("Channel Spammed:\n" + messages.toString()).queue();
				//}
				guild.getTextChannelById(786328890280247327L).sendMessage("Cache: " + messages.toString()).queue();
		}
	}
}
