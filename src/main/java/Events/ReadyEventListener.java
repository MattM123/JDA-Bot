package Events;

import java.util.ArrayList;
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
	    
	    public Tuple(Message x, User y, TextChannel z, long time) {
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
	private ArrayList<Tuple> messages = new ArrayList<Tuple>();
	
	//Channel spam detection
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {	

		//The time interval in milliseconds the messages need to be sent within for it to be considered channel spam
		int interval = 10000;
	
		//The amount of messages to be considered channel spam
		int messageAmount = 3;		
	
		if (event.isFromGuild() && event.getChannelType().isMessage()) {
				//Comapares cached messages and authors with new messages. 				
				if (!messages.isEmpty()) {
					for (int i = 0; i < messages.size(); i++) {
						//Cache stores tuples with similar content and author but differing channels
						//Messages need to be sent within the time interval to be tracked
						if (event.getMessage().equals(messages.get(i).getMessage()) 
								&& event.getAuthor().equals(messages.get(i).getUser()) 
								&& !event.getChannel().equals(messages.get(i).getChannel())) {
							messages.add(new Tuple(event.getMessage(), event.getAuthor(), (TextChannel) event.getChannel(), System.currentTimeMillis()));
						}
						//If cache is not full and a message with different content and author is recieved, 
						//clears cache and stores the different message thats been recieved.
						else {
							messages.clear();
							messages.add(new Tuple(event.getMessage(), event.getAuthor(), (TextChannel) event.getChannel(), System.currentTimeMillis()));
						}
					}
				}
				//If cache is empty on message recieved, caches the message regardless of content, author, or channel to be used 
				//for the next loop iteration 
				else {
					messages.add(new Tuple(event.getMessage(), event.getAuthor(), (TextChannel) event.getChannel(), System.currentTimeMillis()));
				}
					
				//If cache is full and user has sent 3 of the same messages in 3 different channels within the time interval
				//it is considered channel spam
				if (messages.size() == messageAmount && messages.get(0).getTime() - messages.get(messages.size()).getTime() < interval) {				
					guild.getTextChannelById(786328890280247327L).sendMessage("Channel Spammed:\n" + messages.toString()).queue();
				}
				guild.getTextChannelById(786328890280247327L).sendMessage("Cache: " + messages.toString()).queue();
		}
	}
}
