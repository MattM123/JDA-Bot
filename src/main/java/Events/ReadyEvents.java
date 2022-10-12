package Events;

import java.util.ArrayList;
import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyEvents extends ListenerAdapter {
	
	class Tuple { 
	    private Message x;
	    private User y;
	    
	    public Tuple(Message x, User y) {
	    	this.x = x;
	    	this.y = y;
	    }
	    
	    public Message getMessage() {
	    	return x;
	    }
	    
	    public User getUser() {
	    	return y;
	    }
	} 
	
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	ArrayList<Tuple> messages = new ArrayList<Tuple>();
	
	//Channel spam detection
	public void onReadyEvent(MessageReceivedEvent event) {
		
		//The time interval the messages need to be sent within for it to be considered channel spam
		int interval = 10000;
		//The amount of messages to be considered channel spam
		int messageAmount = 3;		
		//Clears cache before next execution
		messages.clear();
		
		long start = System.currentTimeMillis();
		long end = start + interval;
		
		while (System.currentTimeMillis() != end) {
				//Comapares cached messages and authors with new messages. Clears cache if tuples differ.
				if (messages.isEmpty() || !messages.contains(new Tuple(event.getMessage(), event.getAuthor()))) {
					messages.clear();
					messages.add(new Tuple(event.getMessage(), event.getAuthor()));
				}
				//If message and author match, caches 
				else if (messages.contains(new Tuple(event.getMessage(), event.getAuthor()))) {				
					messages.add(new Tuple(event.getMessage(), event.getAuthor()));
				}
				
				//If cache is full, user has sent 3 of the same messages in 3 different channels
				if (messages.size() == messageAmount) {
					
				}
				guild.getTextChannelById(786328890280247327L).sendMessage("Cache: " + messages.toString()).queue();
		}
	}
}
