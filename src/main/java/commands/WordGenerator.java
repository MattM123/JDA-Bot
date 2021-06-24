package commands;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordGenerator {

	private Response response;
	public String stackTrace = "";
	
	//Constructor that authenticates to word generator API
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
		StringBuilder resString = new StringBuilder();
		byte[] byteArray;
		try {
			byteArray = response.body().bytes();
			
			for(int i = 0; i < byteArray.length; i++) {
				resString.append(byteArray[i]);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resString.toString();

	}
}
