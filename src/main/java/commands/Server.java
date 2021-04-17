package commands;

import java.util.ArrayList;
import java.util.List;

public class Server {
	
	final int id;
	final boolean running;
	final double CPUusage;
	final String memUsage;
	final List<Player> players;
	
	public Server(int sid, boolean stat, double cpu, String mem, ArrayList<Player> play ) {
		id = sid;
		running = stat;
		CPUusage = cpu;
		memUsage = mem;
		players = play;
	}
	
	public int getID() {
		return id;
	}
	
	public List<Player> getPlayerList() {
		return players;
	}
	
	public String getStatus() {
		if (running) {
			return "ONLINE";
		}
		else {
			return "OFFLINE";
		}
	}
	
	public double getCpuUsage() {
		return CPUusage;
	}
	
	public String getMemUsage() {
		return memUsage;
	}
	
	
	   @Override
	   public String toString() {
		   return "User [id=" + id + ", status=" + running + ", cpu_usage=" + CPUusage + ", memory_usage=" + memUsage + ", name=" + players + "]";
		    }
}
