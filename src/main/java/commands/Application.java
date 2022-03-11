package commands;

import java.util.List;

public class Application {

	final int id;
	final List<AnswerInfo> answers;
	final Applicant user;
	final String mediaUrl;
	
	public Application(int appID, List<AnswerInfo> ans, String url, String u) {
		id = appID;
		answers = ans;
		mediaUrl = url;
		user = new Applicant(u);
	}
	
	public List<AnswerInfo> getAnswerList() {
		return answers;
	}
	 
	public class Applicant {
		private String tag;
		public Applicant(String t) {
			tag = t;
		}
		public String getUserTag() {
			return tag;
		}
	}
	public String getUrl() {
		return mediaUrl;
	}
   @Override
   public String toString() {
	   return "User [id=" + id + ", name=" + answers + "]";
	    }
}
