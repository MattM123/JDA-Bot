package commands;

import java.awt.Color;
import java.util.function.Consumer;

import database.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

public class NonAPICommands extends ListenerAdapter {
	

	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		Guild guild = event.getGuild(); 
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.blue);
		embed.setTitle("Command Information");
		embed.addField("***__Midwest Server Commands__***", "" ,false);
		embed.addField("=server", "Displays server status and resource usage.", false);
		embed.addField("=map", "Shows all of the states that are currently being built accorss BTE Midwest", false);
		embed.addField("=measure", "Measure tutorial derived from the BTE Support Bot", false);
		embed.addField("=link <minecraft username>", "Automatically assigns build perms and Builder role if you are on the team", false);
		embed.addField("=applicant <minecraft username>", "Requires permissions to assign 'ApplicationInProgress' rank", false);
		embed.addField("=event <minecraft username>", "Requires permissions to assign 'Event' rank", false);
		embed.addField("=getapp -<DiscordID> -<number of app>", "Requires permissions to view players applications", false);

		
		
		
		EmbedBuilder embed1 = new EmbedBuilder();
		embed1.setTitle("BTE Midwest Map");
		embed1.setColor(Color.blue);
		embed1.setImage("https://cdn.discordapp.com/attachments/735998501053530163/808887801432768552/path3233-86.png");
		
		//returns list of commands used by bot
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=help")) {
			event.getChannel().sendMessage(embed.build()).queue();
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
			event.getChannel().sendMessage(embed1.build()).queue();
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
		
		//Opt in/out of announcement pings by self assigning a @Notify role.
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=notify")) {
			Role notify = event.getGuild().getRoleById(783330424805261342L);
			
			if (event.getGuild().getMemberById(event.getAuthor().getIdLong()).getRoles().contains(notify)) {
				event.getGuild().removeRoleFromMember(event.getGuild().getMemberById(event.getAuthor().getId()), notify).queue();
				
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.blue);
				emb.setTitle(event.getGuild().getRoleById(783330424805261342L).getName() + " role has been removed from @" + event.getAuthor().getName());
				
				event.getChannel().sendMessage(emb.build()).queue();
			}
			else {
				event.getGuild().addRoleToMember(event.getGuild().getMemberById(event.getAuthor().getId()), notify).queue();
				
				EmbedBuilder emb = new EmbedBuilder();
				emb.setColor(Color.blue);
				emb.setTitle(event.getGuild().getRoleById(783330424805261342L).getName() + " role has been given to @" + event.getAuthor().getName());
				
				event.getChannel().sendMessage(emb.build()).queue();
			}
		}

		//Retrieves member count data from database
		if (event.getMessage().getContentRaw().equalsIgnoreCase("=members")) {
			Data.getMemberData();
			
			for (int i = 0; i < Data.getMemberData().size(); i++) {
				event.getChannel().sendMessage(String.valueOf(Data.getMemberData().get(i))).queue();
			}
			
			event.getChannel().sendMessage(Data.output).queue();	        
		}
		
	}
}
