package commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
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
		this.setTimeout(99999999, TimeUnit.DAYS);
		this.setFinalAction(message -> guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Leaderboard timed out.").queue());
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
			
			ArrayList<String> items = new ArrayList<String>();
			
			while (rs.next()) {
				items.add(guild.getMemberById(rs.getLong("id")).getUser().getAsTag());
				items.add(rs.getString("count"));
			}
				
			String[] addThis = new String[items.size()];
			for (int i = 0; i < items.size(); i++) {
				addThis[i] = items.get(i);
			}
			
			this.clearItems();
			this.addItems(addThis);
			
			
			
		} catch (SQLException e) {
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard. \n**[ERROR]** " + e.getMessage()).queue();
		}
	}
}
