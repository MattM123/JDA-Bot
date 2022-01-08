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
import net.dv8tion.jda.api.entities.User;

public class BuildLeaderboard extends Paginator.Builder {
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	
	public BuildLeaderboard() {
		this.allowTextInput(false);
		this.setColor(Color.blue);
		this.setColumns(2);
		this.setEventWaiter(new EventWaiter());
		this.setItemsPerPage(20);
		this.setTimeout(99999, TimeUnit.DAYS);
		this.setFinalAction(message -> guild.getTextChannelById(786328890280247327L).sendMessage("**[ERROR]** Leaderboard timed out.").queue());
		this.refresh();
		
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
			
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> counts = new ArrayList<String>();
			
			while (rs.next()) {
				names.add(guild.getMemberById(rs.getLong("id")).getUser().getAsTag());
				counts.add(rs.getString("count"));
			}
				
			String[] addThis = new String[names.size() + counts.size()];
			for (int i = 0; i < names.size(); i++) {
				addThis[i] = names.get(i);
			}
			for (int i = names.size(); i < counts.size(); i++) {
				addThis[i] = counts.get(i);
			}
			
			guild.getTextChannelById(786328890280247327L).sendMessage(Arrays.toString(addThis)).queue();
			this.clearItems();
			this.addItems(addThis);
			
			
			
		} catch (SQLException e) {
			guild.getTextChannelById(929158963499515954L).sendMessage("**[ERROR]** Unable to update leaderboard. \n**[ERROR]** " + e.getMessage()).queue();
		}
	}
}
