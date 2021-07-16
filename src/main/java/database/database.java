package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

//connects to sqlite database
public class database {
	public static String output = "";
	private static String dbURL = "jdbc:sqlite:src/main/java/commands/bot.db";
	
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
	        
	        finally
	        {
	          try
	          {
	            if(conn != null)
	              conn.close();
	          }
	          catch(SQLException e)
	          {	  
	            output = e.getMessage();
	          }	       
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
	    
	    public static String getMemberData() {
	    	String select = "SELECT memberCount FROM countmembers";
	    	boolean i = false;
	    	int o = 0;
	    	String label = "";
	    	
	        try {
	        	Class.forName("org.sqlite.JDBC");
	        	Connection conn = DriverManager.getConnection(dbURL);
	            Statement stmt  = conn.createStatement();
	            ResultSet rs    = stmt.executeQuery(select);
	            
	            output = "Driver Connected: " + conn.getMetaData().getDriverName();
	            i = rs.first();
	            label =  rs.getMetaData().getColumnLabel(1);
	           
				// loop through the result set of member counts
				while (rs.next()) {
				  o = rs.getInt("memberCount");
				} 
    	
		            
		        } catch (SQLException | ClassNotFoundException e) {
		            output = e.getMessage();
		        }
	        return i + ":" + o + ":" + label;
	    }
	    
	    
}
