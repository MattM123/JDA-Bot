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
	
	public static Connection connect() { 
		Guild guild = Bot.jda.getGuildById(735990134583066679L);
		TextChannel builderAudit = guild.getTextChannelById(928425780084629515L);
		Connection conn = null;  
		try {  
			String url = "jdbc:sqlite:" + fileName;  

			DriverManager.registerDriver(new org.sqlite.JDBC());
			conn = DriverManager.getConnection(url);
			
			
			builderAudit.sendMessage("Database connection established.").queue();  
             
		} catch (SQLException e) {  
			builderAudit.sendMessage(e.getMessage()).queue();
		} finally {  
			try {  
				if (conn != null) {  
					conn.close();  
				}  
			} catch (SQLException ex) {  
				builderAudit.sendMessage(ex.getMessage()).queue();  
			}   
		} 
		return conn;
	}  
}  
