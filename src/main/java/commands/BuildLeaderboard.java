package commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.EmbedPaginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BuildLeaderboard {

	public int pages;
	public int total = 0;
	
	public BuildLeaderboard() {

	}
	
	public BuildLeaderboard(User access) {

		
	}
	
	public MessageEmbed[] refresh() {	
		MessageEmbed[] embeds = null;
		ResultSet rs = null;
		ArrayList<String> items = null;
		
		//connects to database and pulls data
		try {
			String getData = "SELECT * FROM buildcounts ORDER BY count DESC;";
			Statement data = Connect.connect().createStatement();
			rs = data.executeQuery(getData);		
		
			items = new ArrayList<String>();			

			while (rs.next()) {	
				Guild guild = NonAPICommands.pubGuild;
				try {
						items.add(guild.getMemberById(rs.getString("id")).getUser().getAsTag() + "\n");	
				} catch (NullPointerException e) {
					items.add("Missing User\n");
				}				
								
				items.add(rs.getString("count") + "\n");										
				total += rs.getInt("count");			
			}
		} catch (SQLException e) {
			Guild guild = NonAPICommands.pubGuild;
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard: " + e.getMessage()).queue();
		}
		
		//closes connection
		Guild guild = NonAPICommands.pubGuild;
		TextChannel stacktrace = guild.getTextChannelById(928822585779707965L);
		TextChannel audit = guild.getTextChannelById(929158963499515954L);
		if (Connect.connect() != null) {  
			try {
				Connect.connect().close();
			} catch (SQLException e) {
				audit.sendMessage("**[ERROR]** " + e.getMessage()).queue();
				if (ExceptionUtils.getStackTrace(e).length() >= 1900)
					stacktrace.sendMessage(ExceptionUtils.getStackTrace(e).substring(0, 1900)).queue();
				else {
					stacktrace.sendMessage(ExceptionUtils.getStackTrace(e)).queue();
				}
			} 							
		}
	
		if (items.size() > 20) {
			embeds = new MessageEmbed[items.size() / 20];
		}
		else {
			embeds = new MessageEmbed[1];
			String names = "";
			String counts = "";
			for (int i = 0; i < items.size() / 2; i += 2) {
				names += items.get(i);
			}
			for (int i = 0; i < items.size() / 2; i += 2) {
				counts += items.get(i);
			}
			
			EmbedBuilder emb = new EmbedBuilder();
			emb.setColor(Color.blue);
			emb.addField("__Builder__", names, true);
			emb.addField("__Count__", counts, true);
		}
		/*
		itemEmbeds = new ArrayList<MessageEmbed>();
		if (rs != null && items != null && itemEmbeds != null && total != 0) {
			//Creating embeds that will be paginated
			int page = 0;
				for (int i = 0; i < items.size(); i += 10) {
					EmbedBuilder emb = new EmbedBuilder();
					page += 1;
					try {
						emb.setColor(Color.blue); 
						emb.setTitle("Page " + page + "/" + Math.round((items.size() + 5.0) / 10));
					
						emb.addField("__User__", items.get(i) + "\n" + items.get(i + 2) + "\n" + items.get(i + 4) + "\n"
							+ items.get(i + 6) + "\n" + items.get(i + 8), true);
				
						emb.addField("__Build Count__", items.get(i + 1) + "\n" + items.get(i + 3) + "\n" + items.get(i + 5) + "\n"
							+ items.get(i + 7) + "\n" + items.get(i + 9), true);

				} catch (IndexOutOfBoundsException e) {
					try {
						emb.addField("__User__", items.get(i) + "\n" + items.get(i + 2) + "\n" + items.get(i + 4) + "\n"
							+ items.get(i + 6), true);
					
						emb.addField("__Build Count__", items.get(i + 1) + "\n" + items.get(i + 3) + "\n" + items.get(i + 5) + "\n"
							+ items.get(i + 7), true);
						
					} catch (IndexOutOfBoundsException f){
						try {
							emb.addField("__User__", items.get(i) + "\n" + items.get(i + 2) + "\n" + items.get(i + 4), true);					
							emb.addField("__Build Count__", items.get(i + 1) + "\n" + items.get(i + 3) + "\n" + items.get(i + 5), true);
							
						} catch (IndexOutOfBoundsException g) {
							try {
								emb.addField("__User__", items.get(i) + "\n" + items.get(i + 2), true);					
								emb.addField("__Build Count__", items.get(i + 1) + "\n" + items.get(i + 3), true);
							
							} catch (IndexOutOfBoundsException h) {
								emb.addField("__User__", items.get(i), true);					
								emb.addField("__Build Count__", items.get(i + 1), true);
								
								itemEmbeds.add(emb.build());
								break;
							}
							itemEmbeds.add(emb.build());
							break;
						}
						itemEmbeds.add(emb.build());
						break;
					}
					
					itemEmbeds.add(emb.build());
					break;
				}
					
				itemEmbeds.add(emb.build());
				
			}
			*/
			return embeds;
			//pages = itemEmbeds.size();
			//this.setItems(itemEmbeds);
			//this.setText("**__Total Buildings: " + total + "__**");
			
			
		//}
	}
}
