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
import com.jagrosh.jdautilities.menu.Paginator;
import com.jagrosh.jdautilities.menu.Paginator.Builder;
import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BuildLeaderboard extends Paginator.Builder {
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	public int pages;
	private int itemsPerPage;
	private int columns;
	
	public BuildLeaderboard() {
		itemsPerPage = 500;
		columns = 2;
		this.setColumns(columns);
		this.allowTextInput(false);
		this.setColor(Color.blue);
		this.setEventWaiter(new EventWaiter());
		this.setItemsPerPage(itemsPerPage);
		this.setTimeout(5, TimeUnit.SECONDS);
		this.setFinalAction(message -> refresh());
		this.wrapPageEnds(false);
	
		
		
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
			String[] addThis = new String[rs.getRow()];
			String[] names = new String[rs.getRow()];
			String[] counts = new String[rs.getRow()];

		//	int pointer = 0;
		//	char[] namespace = "᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼".toCharArray(); //size 35
		//	char[] countspace = "᲼᲼᲼᲼᲼".toCharArray(); //size 5
			int total = 0; 
			rs.beforeFirst();

			while (rs.next()) {	
				if (guild.getMemberById(rs.getLong("id")) != null) {
					if (rs.getRow() < names.length) {
					//	names[rs.getRow() - 1] = guild.getMemberById(rs.getString("id")).getUser().getAsTag();
					//	counts[rs.getRow() - 1] = rs.getString("count");	
				//		items.add(guild.getMemberById(rs.getString("id")).getUser().getAsTag());
				//		items.add(rs.getString("count"));
					}
					//for (int i = 0; i < addThis.length; i++) {
					////	if (addThis[i] == null)
					//		addThis[i] = rs.getString("count");
				//	}
					
					total += rs.getInt("count");
				/*	
					String nameString = "";
					String countString = "";
					for (int i = 0; i < countspace.length; i++) {
						countString += countspace[i];
					}
					for (int i = 0; i < namespace.length; i++) {
						nameString += namespace[i];
					}
					
					addThis[pointer] = nameString + countString;
					pointer += 1;
					
					//Reseting char spaces for next record
					for (int i = 0; i < countspace.length; i++) {
						countspace[i] = '᲼';
					}
					for (int i = 0; i < namespace.length; i++) {
						namespace[i] = '᲼';
					}
				*/
				}
			}
			this.clearItems();
			for (int i = 0; i < names.length - 1; i++) {
				this.addItems(names[i]);
			}
			for (int i = 0; i < counts.length - 1; i++) {
				this.addItems(counts[i]);
			} 
			//TextChannel leaderboard = Bot.jda.getGuildById(735990134583066679L).getTextChannelById(929171594125914152L);
			//leaderboard.sendMessage(Arrays.toString(items.toArray(String[]::new))).queue();
		//	this.setItems(items.toArray(String[]::new));
			this.setText("**__Total Buildings: " + total + "__**");
			
			
			if ((names.length + counts.length) > itemsPerPage)
				pages = (names.length + counts.length) / itemsPerPage;
			else {
				pages = 1;
			}
	
		} catch (SQLException e) {
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard. \n**[ERROR]** " + e.getMessage()).queue();
		}
	}
}
