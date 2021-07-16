package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//connects to sqlite database
public class databaseManipulator {
	private static String output = "";
	private static String dbURL = "jdbc:sqlite:bot.db";
	
	    public static String connect() {

	        try {  
	            Class.forName("org.sqlite.JDBC");
	      
	            
	            // create a connection to the database  
	            Connection conn = DriverManager.getConnection(dbURL); 
	            
	            if (conn != null) {  
	                DatabaseMetaData meta = conn.getMetaData();  
	                output = "The driver name is " + meta.getDriverName() + "\n" 
	                + "A new database has been created.";  
	            }  
	              
	            else {
	            	output = "Null connection";
	            }

	        } catch (SQLException e) {
	            output = e.getMessage().substring(0, 1000);
	        } catch (ClassNotFoundException e) {
				output = e.getMessage().substring(0, 1000);
        	}
	        return output;
	    }
	    
	    public static void sendSQLStatement(String sql) {
	    	try (Connection conn = DriverManager.getConnection(dbURL);
	    			Statement stmt = conn.createStatement()){
	    		
	    		stmt.execute(sql);
	    		
	    	} catch (SQLException e) {
				output = e.getMessage();
			}
	    }
}
