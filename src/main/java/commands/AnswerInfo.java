package commands;

public class AnswerInfo {

	final int id;
	final String question;
	final String answer;
	 
	public AnswerInfo(int aID, String q, String a) {
		id = aID;
		question = q;
		answer = a;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public String getQuestion() {
		return question;
	}
} 
