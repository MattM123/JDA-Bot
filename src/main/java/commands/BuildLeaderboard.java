package commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class BuildLeaderboard extends Paginator.Builder {
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	
	public BuildLeaderboard() {
		this.allowTextInput(false);
		this.setColor(Color.blue);
		this.setColumns(2);
		this.setEventWaiter(new EventWaiter());
		this.setItemsPerPage(10);
		
		
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
			
			String items = "";
			
			while (rs.next()) {
				items += guild.getMemberById(rs.getLong("id")).getUser().getAsTag() + " " + rs.getString("count");
			}
			
			this.addItems(items);
			
		} catch (SQLException e) {
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard. \n**[ERROR]** " + e.getMessage()).queue();
		}
	}
	

	

}
