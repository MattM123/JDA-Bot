package commands;

import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.EmbedPaginator;
import com.jagrosh.jdautilities.menu.Paginator;
import com.jagrosh.jdautilities.menu.Paginator.Builder;
import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BuildLeaderboard extends EmbedPaginator.Builder {
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	public int pages;
	private int itemsPerPage;
	
	public BuildLeaderboard() {
		itemsPerPage = 4;
	//	this.setColumns(columns);
		this.allowTextInput(false);
	//	this.setColor(Color.blue);
		this.setEventWaiter(new EventWaiter());
	//	this.setItemsPerPage(itemsPerPage);
		this.setTimeout(5, TimeUnit.SECONDS);
		this.setFinalAction(message -> refresh());
		this.wrapPageEnds(true);
	
		
		
	}
	
	public BuildLeaderboard(User access) {
		this.allowTextInput(true);
		this.setUsers(access);
		
	}
	
	public void refresh() {		
		try {

			String getData = "SELECT * FROM buildcounts ORDER BY count DESC;";
			Statement data = Connect.connect().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = data.executeQuery(getData);
			
			rs.last();
			int numOfRows = rs.getRow() + 1;
			String[] names = new String[rs.getRow() + 1];
			String[] counts = new String[rs.getRow() + 1];
			ArrayList<String> items = new ArrayList<String>();
			ArrayList<MessageEmbed> itemEmbeds = new ArrayList<MessageEmbed>();

			int total = 0; 
			rs.beforeFirst();

			while (rs.next()) {	
				if (guild.getMemberById(rs.getLong("id")) != null) {
					if (rs.getRow() < numOfRows) {
						if (guild.getMemberById(rs.getString("id")).getUser().getAsTag().length() > 15)
							items.add(guild.getMemberById(rs.getString("id")).getUser().getAsTag().substring(0, 10) + "...");
						else
							items.add(guild.getMemberById(rs.getString("id")).getUser().getAsTag());
							
						items.add(rs.getString("count"));
					}					
					total += rs.getInt("count");
				}
			}
	
			//Creating embeds that will be paginated
				for (int i = 0; i < items.size(); i += 4) {
					EmbedBuilder emb = new EmbedBuilder();						
					try {
						emb.setColor(Color.blue);
						
						emb.addField(items.get(i), "", true);
						emb.addField(items.get(i + 1), "", true);
						
						emb.addField("" + '\u200b', "" + '\u200b', true);

						emb.addField(items.get(i + 2), "", true);
						emb.addField(items.get(i + 3), "", true);
						
						emb.addField("" + '\u200b', "" + '\u200b', true);
							
					//	emb.addField(items.get(i + 4), "", true);
					//	emb.addField(items.get(i + 5), "", true);
						
					//	emb.addField("" + '\u200b', "" + '\u200b', true);
							
					//	emb.addField(items.get(i + 6), "", true);
					//	emb.addField(items.get(i + 7), "", true);
						
					//	emb.addField("" + '\u200b', "" + '\u200b', true);
							
					//	emb.addField(items.get(i + 8), "", true);
					//	emb.addField(items.get(i + 9), "", true);

				} catch (IndexOutOfBoundsException e) {
					itemEmbeds.add(emb.build());
					break;
				}
					
				itemEmbeds.add(emb.build());
			}
			
			this.setItems(itemEmbeds);
			this.setText("**__Total Buildings: " + total + "__**");
			
			TextChannel leaderboard = Bot.jda.getGuildById(735990134583066679L).getTextChannelById(929171594125914152L);
			
			
			if (numOfRows > itemsPerPage)
				pages = numOfRows / itemsPerPage;
			else {
				pages = 1;
			}
	
		} catch (SQLException e) {
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard. \n**[ERROR]** " + e.getMessage()).queue();
		}
	}
}
