package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//connects to sqlite database
public class databaseManipulator {
	public static String output = "";
	private static String dbURL = "jdbc:sqlite:bot.db";
	
	 public static Connection connect() {
	        // SQLite connection string
	
	        Connection conn = null;
	        try {
	        	Class.forName("org.sqlite.JDBC"); 
	            conn = DriverManager.getConnection(dbURL);
	            output = "Driver Connected: " + conn.getMetaData().getDriverName();
	        } catch (SQLException | ClassNotFoundException e) {
	            output = e.getMessage();
	        }
	        return conn;
	    }
	    
	    public static void sendSQLStatement(String sql) {
	    	try (Connection conn = connect();
	    			Statement stmt = conn.createStatement()){
	    		
	    		stmt.execute(sql);
	    		
	    	} catch (SQLException e) {
				output = e.getMessage();
			}
	    }
}
