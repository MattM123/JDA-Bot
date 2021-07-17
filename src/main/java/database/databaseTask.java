package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class databaseTask implements Runnable {
	private String dbURL = "jdbc:sqlite:src/main/java/database/bot.db";
	private String sql;

    long untilNextInvocation = 10000;

    private final ScheduledExecutorService service;

    public databaseTask(ScheduledExecutorService service, String sql) {
    	this.sql = sql;
        this.service = service;

    }

    @Override
    public void run() {
    	
        try {
        	Class.forName("org.sqlite.JDBC");
        	Connection conn = DriverManager.getConnection(dbURL);
            Statement stmt  = conn.createStatement();
            stmt.execute(sql);
            
        } catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			
		} finally {
            service.schedule(new databaseTask(service, sql), untilNextInvocation, TimeUnit.MILLISECONDS);
        }

    }

}
