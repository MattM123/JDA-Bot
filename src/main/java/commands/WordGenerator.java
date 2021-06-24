package commands;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordGenerator {

	private Response response;
	private String stackTrace = "";
	
	public WordGenerator() {
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
			.url("https://wordsapiv1.p.rapidapi.com/words/?random=true")
			.get()
			.addHeader("x-rapidapi-key", "c48982ebb1msh2d7538258cff917p115863jsn5851ee1dfaf2")
			.addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
			.build();

		try {
			response = client.newCall(request).execute();
		} catch (IOException e) {
			stackTrace = "Bad Request";
		}
	}
	
	public String getWord() {
		return response.body().toString();
	}
}
