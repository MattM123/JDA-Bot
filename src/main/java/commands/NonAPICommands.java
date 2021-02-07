package commands;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NonAPICommands extends ListenerAdapter {
//	private static ArrayList<String> records;
//	private Logger logger = LoggerFactory.getLogger(NonAPICommands.class);

	public static String regionXZ(double x, double z) {
		x = (int) (Math.floor(x / 32));
		z = (int) (Math.floor(z / 32));
		
		return String.valueOf(x) + "." + String.valueOf(z) + ".3dr/mca/2dr"; 
	}
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
				
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.blue);
		embed.setTitle("Command Information");
		embed.addField("***__Midwest Server Commands__***", "" ,false);
		embed.addField("!server", "Shows the server status and resource usage.", false);
		embed.addField("!map", "Shows all of the states that are currently being built accorss BTE Midwest", false);
		embed.addField("!measure", "Measure tutorial derived from the BTE Support Bot", false);
		embed.addField("!regcon <Xcoord> <Zcoord>", "Converts in-game coordinates to region file format", false);
		embed.addField("!link <minecraft username>", "Automatically assigns build perms and Builder role if you are on the team", false);
		embed.addField("!applicant <minecraft username>", "Requires permissions to assign 'ApplicationInProgress' rank", false);
		embed.addField("!event <minecraft username>", "Requires permissions to assign 'Event' rank", false);
		embed.addField("***__Wisconsin Server Commands__***", "" ,false);
		embed.addField("?help", "Meta help command that displays these Wisconsin server commands", false);
		embed.addField("?host_stats", "", false);
		embed.addField("?server_stats 2", "", false);
		embed.addField("?list", "", false);
		embed.addField("?query", "", false);
		embed.addField("?restart 2", "", false);
		embed.addField("?start 2", "", false);
		embed.addField("?stop 2", "", false);

		
		
		
		EmbedBuilder embed1 = new EmbedBuilder();
		embed1.setTitle("BTE Midwest Map");
		embed1.setColor(Color.blue);
		embed1.setImage("https://i.imgur.com/meaamm7.png");
		embed1.setFooter("Made by: MN Admin | Mr Jew");
		
		//returns list of commands used by bot
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!commands")) {
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
		//returns measure gif from BTE bot
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!measure")) {
			EmbedBuilder measure = new EmbedBuilder();
			measure.setColor(Color.blue);
			measure.setTitle("Google Earth Pro Measuring Tutotrial");
			measure.setFooter("Gif totally not stolen from the Build The Earth bot");
			measure.setImage("https://i.gyazo.com/thumb/1200/d58446cec35cc504bb36b749346041a9-gif.gif");
			event.getChannel().sendMessage(measure.build()).queue();	
		}
		
		//returns map image of states
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!map")) {
			event.getChannel().sendMessage(embed1.build()).queue();
		}
		
		//chunk conversion to region file
		if (event.getMessage().getContentRaw().startsWith("!regcon")) {
			String Xbuilder = "";
			String Zbuilder = "";
			char[] chararr = event.getMessage().getContentRaw().toCharArray();
			
			for (int i = 8; i < chararr.length; i++) {
				if (chararr[i] == ' ') {
					for (int j = (i + 1); j < chararr.length; j++) {
						Zbuilder += chararr[j];
					}
					break;
				}
				else {
					Xbuilder += chararr[i];
				}
			}
			event.getChannel().sendMessage("Your file should look something like this: " + regionXZ(Double.parseDouble(Xbuilder), Double.parseDouble(Zbuilder))).queue();

		}
	}
}
