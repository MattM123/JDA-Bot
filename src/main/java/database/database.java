package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.lang.exception.ExceptionUtils;

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
	    
	    public static ArrayList<Integer> getMemberData() {
	    	String select = "SELECT memberCount FROM countmembers";   
	    	ArrayList<Integer> count = new ArrayList<Integer>();
	    	
	        try {
	        	Class.forName("org.sqlite.JDBC");
	        	Connection conn = DriverManager.getConnection(dbURL);
	            Statement stmt  = conn.createStatement();
	            ResultSet rs    = stmt.executeQuery(select);
	            
	           
				// loop through the result set of member counts
//rs.getArray(columnLabel);
				while (rs.next()) {
					count.add(rs.getInt("memberCount"));
				} 
    	
		            
		        } catch (SQLException | ClassNotFoundException e) {
		        	String stack = ExceptionUtils.getStackTrace(e);
					output = stack.subSequence(0, 1000).toString();
		           
		        }
	        return count;
	    }
	    
	    
}
