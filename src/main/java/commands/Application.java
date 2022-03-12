package commands;

import java.util.List;

public class Application {

	final int id;
	final List<AnswerInfo> answers;
	final String mediaUrl;
	final Applicant user;
	
	public Application(int appID, List<AnswerInfo> ans, String url, Applicant u) {
		id = appID;
		answers = ans;
		mediaUrl = url;
		user = u;
	}
	
	public List<AnswerInfo> getAnswerList() {
		return answers;
	}

	public class Applicant {
		final String discordTag;
		final long discordId;
		public Applicant(String t, long id) {
			discordTag = t;
			discordId = id;
		}
		public String getUserTag() {
			return discordTag;
		}
		public long getUserID() {
			return discordId;
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
