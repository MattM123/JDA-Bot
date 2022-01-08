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
	
	public static Connection connect() { 
		try {  
			String unformatted = System.getenv("DATABASE_URL");
			
			String host = unformatted.substring(unformatted.indexOf('@') + 1, unformatted.indexOf(".com:") + 4);		
			String port = unformatted.substring(unformatted.indexOf(".com:") + 5, unformatted.indexOf(".com:") + 9);
			String database = unformatted.substring(unformatted.indexOf(port) + 5, unformatted.indexOf(port) + 19);
			String user = unformatted.substring(11, 25);
			String password = unformatted.substring(26, 90);
			String formatted = "jdbc:postgresql://" + host + "/" + database + "?user=" + user + "&password=" + password;
			
			DriverManager.registerDriver(new org.postgresql.Driver());
			conn = DriverManager.getConnection(formatted);  
             
		} catch (SQLException e) {  
			e.printStackTrace();
		}
		return conn;
	}
}  
