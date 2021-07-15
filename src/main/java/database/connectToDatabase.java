package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

//connects to sqlite database
public class connectToDatabase {
	private static String output = "";
	
	    public static String Connect(String fileName) {

	        try {  
	            Class.forName("org.sqlite.JDBC");
	            String dbURL = "jdbc:sqlite:" + fileName;
	            
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
}
