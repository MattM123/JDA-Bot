package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//connects to sqlite database
public class database {
	public static String output = "";
	private static String dbURL = "jdbc:sqlite:/src/main/java/commands/bot.db";
	
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
	    
	    public static String getMemberData() {
	    	String select = "SELECT memberCount FROM countmembers";
	    	boolean i = false;
	    	int o = 0;
	    	
	        try {
	        	Connection conn = database.connect();
	            Statement stmt  = conn.createStatement();
	            ResultSet rs    = stmt.executeQuery(select);

	            i = rs.first();
	            
	            
				// loop through the result set of member counts
				while (rs.next()) {
				  o = rs.getInt("memberCount");
				} 
    	
		            
		        } catch (Exception e) {
		            output = e.getMessage();
		        }
	        return i + ":" + o;
	    }
	    
	    
}
