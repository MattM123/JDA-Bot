package commands;

import java.util.List;

public class Application {

	final int id;
	final List<AnswerInfo> answers;
	final String user;
	final String mediaUrl;
	
	public Application(int appID, List<AnswerInfo> ans, String tag, String url) {
		id = appID;
		answers = ans;
		user = tag;
		mediaUrl = url;
	}
	
	public List<AnswerInfo> getAnswerList() {
		return answers;
	}
	 
	public String getUser() {
		return user;
	}
	
	public String getUrl() {
		return mediaUrl;
	}
   @Override
   public String toString() {
	   return "User [id=" + id + ", name=" + answers + "]";
	    }
}
