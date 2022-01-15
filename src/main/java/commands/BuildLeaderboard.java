package commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.EmbedPaginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BuildLeaderboard extends EmbedPaginator.Builder {

	public int pages;
	
	public BuildLeaderboard() {
		this.allowTextInput(false);
		this.setEventWaiter(new EventWaiter());
		this.setFinalAction(message -> refresh());
		this.wrapPageEnds(true);
	}
	
	public BuildLeaderboard(User access) {
		this.allowTextInput(true);
		this.setUsers(access);
		
	}
	
	public void refresh() {		
		ResultSet rs = null;
		ArrayList<String> items = null;
		ArrayList<MessageEmbed> itemEmbeds = new ArrayList<MessageEmbed>();
		int total = 0;
		
		//connects to database and pulls data
		try {
			String getData = "SELECT * FROM buildcounts ORDER BY count DESC;";
			Statement data = Connect.connect().createStatement();
			rs = data.executeQuery(getData);		
		
			items = new ArrayList<String>();
			total = 0; 
			

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
		
		if (rs != null && items != null && itemEmbeds != null && total != 0) {
			//Creating embeds that will be paginated
			int page = 1;
			
			if (items.size() > 20) {
				String names = "";
				String counts = "";
				for (int i = 0; i < items.size(); i++) {
					if (i % 2 == 0 && names.split("\n").length < 10) {
						names += items.get(i);
					}
					if (i % 2 != 0 && counts.split("\n").length < 10) {
						counts += items.get(i);
					}
					
					//adds every 20 items to embed
					if (names.split("\n").length == 10 && counts.split("\n").length == 10) {
						EmbedBuilder emb = new EmbedBuilder();
						emb.setTitle("Page " + page + "/" + Math.round((items.size() + 5.0) / 10));
						emb.setColor(Color.blue);
						emb.addField("__Builder__", names, true);
						emb.addField("__Build Count__", counts, true);
						itemEmbeds.add(emb.build());
						page += 1;
						names = "";
						counts = "";
					}
					
					//Adds leftovers to an embed
					if (i == items.size() - 1 && items.size() % 20 != 0) {
						EmbedBuilder emb = new EmbedBuilder();
						emb.setTitle("Page " + page + "/" + Math.round((items.size() + 5.0) / 10));
						emb.setColor(Color.blue);
						emb.addField("__Builder__", names, true);
						emb.addField("__Build Count__", counts, true);
						itemEmbeds.add(emb.build());
						page += 1;
						names = "";
						counts = "";
					}
				}
			}
			else {	
				String names = "";
				String counts = "";
				for (int i = 0; i < items.size(); i++) {
					if (i % 2 == 0) {
						names += items.get(i);
					}
					else {
						counts += items.get(i);
					}
				}
					EmbedBuilder emb = new EmbedBuilder();
					emb.setTitle("Page 1/1");
					emb.setColor(Color.blue);
					emb.addField("__Builder__", names, true);
					emb.addField("__Build Count__", counts, true);
					itemEmbeds.add(emb.build());
					page += 1;

			}
			
			pages = itemEmbeds.size();
			this.setItems(itemEmbeds);
			this.setText("**__Total Buildings: " + total + "__**");
		}
	}
}
