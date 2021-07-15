package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

//connects to sqlite database
public class connectToDatabase {
	private static String output = "";
	
	    public static String Connect(String fileName) {

	       // String url = "jdbc:sqlite:/" + fileName;

	        Connection conn = null;  
	        try {  
	            Class.forName("org.sqlite.JDBC");
	            String url = "jdbc:sqlite:" + fileName;  
	            // create a connection to the database  
	            conn = DriverManager.getConnection(url);  
	              
	            System.out.println("Connection to SQLite has been established.");  

	        } catch (SQLException e) {
	            output = e.getMessage().substring(0, 1000);
	        } catch (ClassNotFoundException e) {
				output = e.getMessage().substring(0, 1000);
			}
	        return output;
	    }
}
