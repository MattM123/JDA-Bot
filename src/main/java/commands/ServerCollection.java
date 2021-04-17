package commands;

import java.util.ArrayList;
import java.util.List;

public class ServerCollection {
	
	final List<Server> servers;
	
	public ServerCollection(List<Server> servs) {
		servers = new ArrayList<Server>();
		servers.addAll(servs);
	}
	
	public List<Server> getServers() {
		return servers;
	}
}
