package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

//connects to sqlite database
public class botDatabase {
	private static String output = "";
	
	    public static String createNewDatabase(String fileName) {

	        String url = "jdbc:sqlite:" + fileName;

	        try (Connection conn = DriverManager.getConnection(url)) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                output = "The driver name is " + meta.getDriverName() + "\n"
	                + "A new database has been created.";
	            }

	        } catch (SQLException e) {
	            output = e.getMessage().substring(0, 1000);
	        }
	        return output;
	    }
}
