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
		itemsPerPage = 10;
		columns = 2;
		this.setColumns(columns);
		this.allowTextInput(false);
		this.setColor(Color.blue);
		this.setEventWaiter(new EventWaiter());
		this.setItemsPerPage(itemsPerPage);
		this.setTimeout(99, TimeUnit.SECONDS);
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
			
			String[] addThis = null;
			int pointer = 0;
			char[] namespace = "᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼᲼".toCharArray(); //size 35
			char[] countspace = "᲼᲼᲼᲼᲼".toCharArray(); //size 5
			int total = 0; 
			ArrayList<String> items = new ArrayList<String>();

			while (rs.next()) {	
				if (guild.getMemberById(rs.getLong("id")).getUser().getAsTag().length() < namespace.length && guild.getMemberById(rs.getLong("id")) != null) {
					items.add(guild.getMemberById(rs.getLong("id")).getUser().getAsTag());
					items.add(rs.getString("count"));		
					total += rs.getInt("count");
				}
			
				addThis = items.toArray(String[]::new);
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
	
			this.setItems(addThis);
			this.setText("**__Total Buildings Built: " + total + "__**");
			
			
			if (addThis.length > itemsPerPage)
				pages = addThis.length / itemsPerPage;
			else {
				pages = 1;
			}
	
		} catch (SQLException e) {
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard. \n**[ERROR]** " + e.getMessage()).queue();
		}
	}
	
	@Override 
	public Builder addItems(String... items) {
		for (int i = 0; i < items.length; i++) {
			
		}
		return this;
	}
}
