package commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {  
	private static String fileName = "builderData.db";
	
	public static String connect() { 
		String status = "";
		Connection conn = null;  
		try {  
			String url = "jdbc:sqlite:" + fileName;  
			Class.forName("org.sqlite.JDBC"); 
			conn = DriverManager.getConnection(url);  
             
			status = "Database connection established.";  
             
		} catch (SQLException e) {  
			status = e.getMessage(); 
		} catch (ClassNotFoundException e) {
			status = "Class Not Found: " + e.getMessage();
		} finally {  
			try {  
				if (conn != null) {  
					conn.close();  
				}  
			} catch (SQLException ex) {  
				status = ex.getMessage();  
			}   
		} 
		return status;
	}  
}  
