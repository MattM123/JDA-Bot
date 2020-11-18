package commands;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NonAPICommands extends ListenerAdapter {

	public static String regionXZ(double x, double z) {
		x = (int) (Math.floor(x / 32));
		z = (int) (Math.floor(z / 32));
		DecimalFormat f = new DecimalFormat("#.#");
		
		return "r." + f.format(x) + "." + f.format(z) + ".mca"; 
	}
	
	public String checkUser() {
		URL url;
		String str = "";
		try {
			url = new URL("https://buildtheearth.net/buildteams/36/users/csv");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			
			str = connection.getContentType();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.blue);
		embed.setTitle("Command Information");
		embed.addField("!server status", "Shows the server status and resource usage.", false);
		embed.addField("!map", "Shows all of the states that are currently being built accorss BTE Midwest", false);
		embed.addField("!measure", "Measure tutorial derived from the BTE Support Bot", false);
		embed.addField("!regcon <Xcoord> <Zcoord>", "Converts in-game coordinates to region file format", false);
		embed.addField("---------Permissions Required---------", "" , false);
		embed.addField("!iowa <minecraft username>", "Requires permissions to assign 'Iowa Builder' rank", false);
		embed.addField("!nebraska <minecraft username>", "Requires permissions to assign 'Nebraska Builder' rank", false);
		embed.addField("!kansas <minecraft username>", "Requires permissions to assign 'Kansas Builder' rank", false);
		embed.addField("!missouri <minecraft username>", "Requires permissions to assign 'Missouri Builder' rank", false);
		embed.addField("!illinois <minecraft username>", "Requires permissions to assign 'Illinois Builder' rank", false);
		embed.addField("!applicant <minecraft username>", "Requires permissions to assign 'ApplicationInProgress' rank", false);
		embed.addField("!start", "Requires permissions to start the server", false);
		embed.addField("!stop", "Requires permissions to stop the server", false);
		embed.addField("!restart", "Requires permissions to restart the server", false);
		
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
			measure.setImage("https://i.gyazo.com/thumb/1200/d58446cec35cc504bb36b749346041a9-gif.gif");
			event.getChannel().sendMessage(measure.build()).queue();	
		}
		
		//returns map image of states
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!map")) {
			event.getChannel().sendMessage(embed.build()).queue();
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
			event.getChannel().sendMessage(regionXZ(Double.parseDouble(Xbuilder), Double.parseDouble(Zbuilder))).queue();
		}
		
		//checks is user is part of team and assigns Midwest Builder role if they are
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!app status")) {
			Guild guild = event.getGuild(); //gets guild
			event.getChannel().sendMessage(checkUser()).queue();
			
		//	String users[]; //array to store name list

			
			// .. then download the file
		//	try {   //gets CSV data and stores in users array
		//		URL website = new URL("https://buildtheearth.net/buildteams/36/users/csv");
		//		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		//		FileOutputStream fos = new FileOutputStream("CSV.txt");
		//		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				
			
				
			/*			
				InputStream input = new URL("https://buildtheearth.net/buildteams/36/users/csv").openStream();
				Scanner scan = new Scanner(input);
				String line = scan.nextLine();
				PrintWriter write = new PrintWriter("CSV.txt");
				//Reader reader = new InputStreamReader(input, "UTF-8");
				while (scan.nextLine() != null) {
					write.append(line);
				}
				event.getChannel().sendMessage(line).queue();
				*/
			//	users = (String[]) csv.getRecords().toArray();
			//	csv.close();
				
		//		event.getChannel().sendMessage(users[5]).queue();
		//		event.getChannel().sendMessage("test").queue();
		/*		
			int k = 0;
			for (int i = 0; i < users.length; i++) {  //assignes role if user is in list
				if (users[i].contains(event.getAuthor().getName())) {
					event.getChannel().sendMessage("You've been accepted! Go to #role-menu to select your state.").queue();
					guild.addRoleToMember(event.getAuthor().getIdLong(), guild.getRoleById("735991952931160104"));
					k = 1;
					break;
				}
				
			}
			
			if (k == 0) { //if user is not in list, k = 0
				event.getChannel().sendMessage("Looks like your application hasn't been looked yet at or you weren't accepted :(").queue();
			}
			*/
				
	//		} catch (MalformedURLException e) {
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
		}
	}
}
