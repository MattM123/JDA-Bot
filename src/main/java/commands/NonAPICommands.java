package commands;

import java.awt.Color;
import java.util.function.Consumer;

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

		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.blue);
		embed.setTitle("Command Information");

		embed.addField("=link <minecraft username>", "Automatically assigns build perms and Builder role if user is on the team", false);
		embed.addField("=applicant <minecraft username>", "Assigns user application build perms on the server", false);
		embed.addField("=getapp -<DiscordID> -<number of app>", "Requires Staff role to view players applications", false);
		embed.addField("=server", "Displays server status and resource usage.", false);
		embed.addField("=map", "Shows all of the states that are currently being built accorss BTE Midwest", false);
		embed.addField("=measure", "Measure tutorial derived from the BTE Support Bot", false);
		embed.addField("=role <nameOfRole>", "Self assigns certain roles, use with no parameters for list of roles", false);
		embed.addField("=/<command>", "Requires Admin role to send console command to Minecraft server", false);
		
		
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
		
//------------------------------------------------------------------------------------------------------------------------------------
//detect users who are unfamiliar with how to get started and sends them to the info channel
				
		String[] phrases = {"how do i apply","how to be builder","how do i be a builder","how do i become a builder","how do i get builder role",
							"how do i get builder", "where do i apply", "how do i join", "how to get builder", "how to apply", "submit my build application", 
							"submit my application"};								
				
		for (int i = 0; i < phrases.length; i++) {
			if (event.getMessage().getContentRaw().contains(phrases[i]) || event.getMessage().getContentRaw().equalsIgnoreCase(phrases[i])) {
				event.getChannel().sendMessage("Go to <#776963885739409458> for info on how to get started").queue();
			}
		}				
//------------------------------------------------------------------------------------------------------------------------------------
//command for self-assigning roles
		
		if (event.getMessage().getContentRaw().equals("=role")) {
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			String rolebuilder = "";
		
			for (int i = 6; i < chararr.length; i++) {
				rolebuilder += chararr[i];
			}
			
			if (rolebuilder.isEmpty()) {
				EmbedBuilder roleMenu = new EmbedBuilder();
				
				roleMenu.setColor(Color.blue);
				roleMenu.addField("Command Usage", "=role nameOfRole", false);
				roleMenu.addField("*__State Roles__*", "Nebraska\nIowa\nKansas\nMinnesota\nMichigan\nWisconsin\nMissouri\nIllinois\nOklahoma", false);
				roleMenu.addField("*__Other Roles__*", "Event - This role is notified of build events", false);
				roleMenu.setFooter("You can only have one state role at a time!");
				
				event.getChannel().sendMessage(roleMenu.build()).queue();
				
			}
		}
	

	
	}
	
	
	
	
	
	
}
