package commands;

import java.util.List;

public class Application {

	final int id;
	final List<AnswerInfo> answers;
	
	public Application(int appID, List<AnswerInfo> ans) {
		id = appID;
		answers = ans;
		
	}
	
	public List<AnswerInfo> getAnswerList() {
		return answers;
	}
	 
   @Override
   public String toString() {
	   return "User [id=" + id + ", name=" + answers + "]";
	    }
}
