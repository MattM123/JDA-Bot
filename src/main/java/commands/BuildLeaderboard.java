package commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class BuildLeaderboard extends Paginator.Builder {
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	public int pages;
	private int itemsPerPage;
	
	public BuildLeaderboard() {
		itemsPerPage = 2;
		this.allowTextInput(false);
		this.setColor(Color.blue);
		this.setEventWaiter(new EventWaiter());
		this.refresh();
		this.setItemsPerPage(itemsPerPage);
		this.setTimeout(99, TimeUnit.SECONDS);
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
			Statement data = Connect.connect().createStatement();
			ResultSet rs = data.executeQuery(getData);
			
			String[] addThis = new String[rs.getFetchSize() / 2];
			int pointer = 0;
			char[] record = "                                        ".toCharArray(); //size 40
			
			while (rs.next()) {
				
				if (guild.getMemberById(rs.getLong("id")).getUser().getAsTag().length() < record.length) {
					for (int i = 0; i < guild.getMemberById(rs.getLong("id")).getUser().getAsTag().length(); i++) {
						record[i] = guild.getMemberById(rs.getLong("id")).getUser().getAsTag().charAt(i);
					}
					for (int i = 0; i < rs.getString("count").length(); i++) {
						record[record.length - i] = rs.getString("count").charAt(i);
					}
					
					addThis[pointer] = record;
					pointer += 1;
				}
			}
			
			this.addItems(addThis);
			
			if (addThis.length > itemsPerPage)
				pages = (int) Math.ceil(addThis.length / itemsPerPage);
			else {
				pages = 1;
			}
	
		} catch (SQLException e) {
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard. \n**[ERROR]** " + e.getMessage()).queue();
		}
	}
}
