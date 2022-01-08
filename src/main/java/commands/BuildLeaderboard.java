package commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import com.jagrosh.jdautilities.menu.Paginator.Builder;
import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class BuildLeaderboard extends Paginator.Builder {
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	
	public BuildLeaderboard() {
		this.allowTextInput(false);
		this.setColor(Color.blue);
		this.setColumns(2);
		this.setEventWaiter(new EventWaiter());
		this.setItemsPerPage(10);
		this.setTimeout(5, TimeUnit.SECONDS);
		//this.setFinalAction(message -> message.getChannel().sendMessage("Timeout").queue());
		this.refresh();
		
	}
	
	public BuildLeaderboard(User access) {
		this.allowTextInput(true);
		this.setUsers(access);
		
	}
	
	public void refresh() {
		try {
			String getData = "SELECT * FROM buildcounts ORDER BY count DESC";
			Statement data = Connect.connect().createStatement();
			ResultSet rs = data.executeQuery(getData);
			
			String [] items = new String[10000];
			
			while (rs.next()) {
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null) {
						items[i] = guild.getMemberById(rs.getLong("id")).getUser().getAsTag();
						items[i + 1] = rs.getString("count");
						break;
					}
				}
			}
			
			this.clearItems();
			this.addItems(items);
			
			
			
		} catch (SQLException e) {
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard. \n**[ERROR]** " + e.getMessage()).queue();
		}
	}
}
