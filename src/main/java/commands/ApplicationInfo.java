package commands;

import java.util.ArrayList;
import java.util.List;

public class ApplicationInfo {
	
	final List<Application> applications;
	
	public ApplicationInfo(List<Application> apps) {
		applications = new ArrayList<Application>();
		applications.addAll(apps);
	}
	
	public List<Application> getApplications() {
		return applications;
	}
}
