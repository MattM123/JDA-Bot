package commands;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
		Reader charReader = null;
		Writer charWriter = null;
		CharBuffer charBuff = null;
		ResponseBody body = response.body();
		
		try {
			charBuff = CharBuffer.allocate(body.bytes().length);
			charWriter = new FileWriter("/JDABot/src/main/java/commands/word.txt");
			charReader = response.body().charStream();
			charReader.transferTo(charWriter);
			charWriter.write(charBuff.array());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.body().close();

		for (int i = 0; i < charBuff.capacity(); i++) {
			resString.append(charBuff.array()[i]);
		}
		
		response.close();
		return resString.toString();

	}
}
