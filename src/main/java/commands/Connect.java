package commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class Connect {  
	private static String fileName = "builderData.db";
	private static Connection conn = null;
	private static Guild guild = Bot.jda.getGuildById(735990134583066679L);
	private static TextChannel errorlog = guild.getTextChannelById(928432209872977990L);
	
	public static Connection connect() { 
		try {  
			String url = "jdbc:sqlite:" + fileName;  

			DriverManager.registerDriver(new org.sqlite.JDBC());
			conn = DriverManager.getConnection(url);		
             
		} catch (SQLException e) {  
			errorlog.sendMessage(e.getMessage()).queue();
		}
		return conn;
	}
}  
