package commands;

import java.awt.Color;
import java.io.File;
import java.time.OffsetDateTime;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Command List").setColor(Color.blue);
		embed.addField("!passwordgen" , "Generates an initial password for first time use with the bot.", false);
		embed.addField("!setpassword <currentpassword>", "Regenerates and resets the password.", false);
		embed.addField("!start <password>", "Starts the server.", false);
		embed.addField("!stop <password>", "Stops the server.", false);
		embed.addField("!restart <password>", "Restarts the server.", false);
		embed.addField("!console <password> <command>", "Sends server console commands", false);
		embed.addField("!server status", "Shows the server status of the Iowa/Nebraska build server", false);
		embed.addField("!map", "Shows the current map of Midwest states that are being built", false);
		embed.addField("!measure", "Shows a short tutorial on how to measure in Google Earth Pro, similar to the BTE Support Bot command", false);
		embed.addField("!tp", "Shows a short tutorial on how to teleport using Google Maps, similar to the BTE Support Bot command", false);

//		EmbedBuilder measure = new EmbedBuilder();
//		measure.setThumbnail("https://thumbs.gfycat.com/AdolescentWindingCentipede-mobile.mp4");
		
		//help command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!commands")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
		//BTE Support measure command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!measure")) {	
			
			MessageEmbed measure = new MessageEmbed("https://i.gyazo.com/d58446cec35cc504bb36b749346041a9.mp4", "Measure Tutorial",
					null, EmbedType.VIDEO, null, 10, null, new MessageEmbed.Provider("Gyazo", "www.gyazo.com"), null,
					new MessageEmbed.VideoInfo("https://i.gyazo.com/d58446cec35cc504bb36b749346041a9.mp4", 720, 390), null, null, null);
			
			event.getChannel().sendMessage(measure).queue();
		}
		
		//BTE Support tp command
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!tp")) {
			event.getChannel().sendFile(new File("https://thumbs.gfycat.com/AdolescentWindingCentipede-mobile.mp4")).queue();
		}
	}
}
