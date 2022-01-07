package commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class Connect {  
	private static Connection conn = null;
	private static Guild guild = Bot.jda.getGuildById(735990134583066679L);
	private static TextChannel errorlog = guild.getTextChannelById(928432209872977990L);
	
	public static Connection connect() { 
		try {  
			//jdbc:postgresql://localhost/test?user=fred&password=secret
			String unformatted = System.getenv("DATABASE_URL");
			
			String host = unformatted.substring(unformatted.indexOf('@') + 1, unformatted.indexOf(".com:") + 4);		
			String port = unformatted.substring(unformatted.indexOf(".com:") + 5, unformatted.indexOf(".com:") + 9);
			String database = unformatted.substring(unformatted.indexOf(port) + 5, unformatted.indexOf(port) + 19);
			String user = unformatted.substring(unformatted.indexOf("://") + 4, unformatted.indexOf("//" + 18));
			String formatted = "jdbc:postgresql://" + host + "/" + database + "?user=";
			
			errorlog.sendMessage(user).queue();
			DriverManager.registerDriver(new org.postgresql.Driver());
			//conn = 
             
		} catch (SQLException e) {  
			errorlog.sendMessage(e.getMessage()).queue();
		}
		return conn;
	}
}  
