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
	
		//The amount of messages to be considered channel spam
		int messageAmount = 3;		
		
		//How many messages should be analysed to determine spam
		int cacheSize = messageAmount;
	
		if (event.isFromGuild() && event.getChannelType().isMessage() && !event.getMessage().isEphemeral() && !event.getAuthor().isBot()) {
				//Comapares cached messages and authors with new messages. 				
				if (messageCache.size() == cacheSize) {
					int counter = 0;
					User spammer = null;
					
					//Iterates through cache and determines if channel spam is happenin
					for (int i = 0; i < messageCache.size(); i++) {
						if (event.getMessage().getContentRaw().equals(messageCache.get(i).getMessage().getContentRaw()) 
								&& event.getAuthor().equals(messageCache.get(i).getUser()) 
								&& !event.getChannel().equals(messageCache.get(i).getChannel())) {
							counter++;
							spammer = event.getAuthor();
						}					
					} 
					
					//The criteria for determining channel spam are:
					//If at least messageAmount messages have the same content and author but different channels
					//And if the time difference between the last cached message and the first cached message is less than interval
					
					if (counter >= messageAmount && messageCache.get(0).getTime() - messageCache.get(messageCache.size() - 1).getTime() < interval) {
						double time = (messageCache.get(0).getTime() - messageCache.get(messageCache.size() - 1).getTime()) / 1000;
						EmbedBuilder emb = new EmbedBuilder();
						emb.setColor(Color.red);
						emb.setTitle(spammer.getAsTag() + " is suspected of channel spamming and has been muted");
						emb.addField(messageAmount + " messages containing the same content were sent by this user in " + time + " seconds", 
							"`" + messageCache.get(0).getMessage().getContentRaw() + "` in " + messageCache.get(0).getChannel().getAsMention() + ": 0.0\n"
							+ "`" + messageCache.get(1).getMessage().getContentRaw() + "` in " + messageCache.get(1).getChannel().getAsMention() + ": " + (messageCache.get(0).getTime() - messageCache.get(messageCache.size() - 2).getTime()) / 1000 + "s\n"
							+ "`" + messageCache.get(2).getMessage().getContentRaw() + "` in " + messageCache.get(2).getChannel().getAsMention() + ": " + (messageCache.get(0).getTime() - messageCache.get(messageCache.size() - 1).getTime()) / 1000 + "s", false);
						
					//	guild.getMember(spammer).timeoutFor(10, TimeUnit.MINUTES).queue();
						guild.getTextChannelById(786328890280247327L).sendMessageEmbeds(emb.build()).queue();
					}
					
					//keeps cache updated with most recent messages 
					messageCache.removeLast();
					messageCache.addFirst(new Tuple(event.getMessage(), event.getAuthor(), event.getChannel(), System.currentTimeMillis()));
				}
				else {
					messageCache.addFirst(new Tuple(event.getMessage(), event.getAuthor(), event.getChannel(), System.currentTimeMillis()));
				}

				guild.getTextChannelById(786328890280247327L).sendMessage("Cache: " + messageCache.toString()).queue();
		}
	}
}
