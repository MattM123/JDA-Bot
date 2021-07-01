package commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONException;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CraftyControllerAPI {
	
	private static String apikey;
	public String stackTrace = "";
	public String certTrace = "No errors";
	
	public CraftyControllerAPI(String api) {
		apikey = api;			
	}

	private void trustCert() {	
		final String CA_FILE = "JDABot/src/main/java/commands/serverCert.cer";
	
		try {
		FileInputStream fis = new FileInputStream(CA_FILE);
		X509Certificate ca = (X509Certificate) CertificateFactory.getInstance("X.509")
		                        .generateCertificate(new BufferedInputStream(fis));

		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry(Integer.toString(1), ca);

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);

		SSLContext context = SSLContext.getInstance("TLS");
		
		context.init(null, tmf.getTrustManagers(), null);
		} catch (KeyManagementException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			certTrace = stack;
		} catch (NoSuchAlgorithmException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			certTrace = stack;
		} catch (KeyStoreException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			certTrace = stack;
		} catch (FileNotFoundException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			certTrace = stack;
		} catch (CertificateException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			certTrace = stack;
		} catch (IOException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			certTrace = stack;
		}
		
	}
			   
	//returns the list of servers and their stats
	public String getServerList() {
		String line;
		String out = "test";
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;
		
		

		try {
			trustCert();
			url = new URL("https://panel.richterent.com/api/v1/server_stats?token=" + apikey);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.192 Safari/537.36");
			conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
			conn.setRequestProperty("Accept", "text/html");
			conn.setRequestProperty("Host", "panel.richterent.com");
			conn.setRequestMethod("GET");
			
			
			
			//Storing JSON from request into string. Prints error code and error stream if encountered.
			
			if (conn.getResponseCode() > 200) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = in.readLine()) != null) {
					json.append(line);
				}
				in.close();
				stackTrace = "Error Code: " + String.valueOf(conn.getResponseCode()) + "\n" + json.toString();
			}		
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = in.readLine()) != null) {
				json.append(line);
			}
			in.close();	
			
			out = json.toString();  		
			
			
		} catch (MalformedURLException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (IOException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (JSONException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		}
		
		return out;
	}
	
	public String sendCommand(String command) {
		String url;
		Response response = null;
		String responseString = "";

		try {
			trustCert();
			url = "https://panel.richterent.com/api/v1/server/send_command?token=" + apikey + "&id=2";
	
			OkHttpClient client = new OkHttpClient().newBuilder()
					  .build();
					
					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("command", command)
					  .build();
					Request request = new Request.Builder()
					  .url(url)
					  .method("POST", body)
					  .build();
					response = client.newCall(request).execute();
					responseString += response.body().string();
					
					
		} catch (MalformedURLException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (IOException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (JSONException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		}
		return responseString;
	}
	/*
	HOST_STATS = '/api/v1/host_stats'
    SERVER_STATS = '/api/v1/server_stats' 
    ADD_USER = '/api/v1/crafty/add_user' 
    DEL_USER = '/api/v1/crafty/del_user' 
    GET_LOGS = '/api/v1/crafty/get_logs'
    SEARCH_LOGS = '/api/v1/crafty/search_logs'

    SEND_CMD = '/api/v1/server/send_command'
    GET_LOGS = '/api/v1/server/get_logs'
    SEARCH_LOGS = '/api/v1/server/search_logs'
    FORCE_BACKUP = '/api/v1/server/force_backup'
    START = '/api/v1/server/start'
    STOP = '/api/v1/server/stop'
    RESTART = '/api/v1/server/restart'
    LIST = '/api/v1/list_servers'
    
    Https://panel.richterent.com/api/v1/endpoint,api key,data to send
    XMLQUX8L6WZF194VUOTH1C5RM7KJ5J53
	 */
}
