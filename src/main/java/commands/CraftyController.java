package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONException;

public class CraftyController {
	
	private static String apikey;
	
	public CraftyController(String api) {
		api = apikey;
	}
	
	//HOST_STATS = '/api/v1/host_stats'
	public String getHostStats() {
		String line;
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;

		try {
			//Disabling verify SSL
			SSLContext context = SSLContext.getInstance("TLSv1.2");
			TrustManager[] trustManager = new TrustManager[] {
			    (TrustManager) new X509TrustManager() {
			       public X509Certificate[] getAcceptedIssuers() {
			           return new X509Certificate[0];
			       }
			       public void checkClientTrusted(X509Certificate[] certificate, String str) {}
			       public void checkServerTrusted(X509Certificate[] certificate, String str) {}
			    }
			};
			context.init(null, trustManager, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

			url = new URL("https://panel.richterent.com/api/v1/host_stats,XMLQUX8L6WZF194VUOTH1C5RM7KJ5J53");
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
			conn.setRequestMethod("GET");
			
			//Storing JSON from request into string. Prints error code and error stream if encountered.
			
			if (conn.getResponseCode() > 200) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = in.readLine()) != null) {
					json.append(line);
				}
				in.close();
				return "Error Code: " + String.valueOf(conn.getResponseCode()) + "\n" + json.toString();
			}		
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			if ((line = in.readLine()) != null) {
				json.append(line);
			}
			in.close();
			
			
				
			
		} catch (MalformedURLException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack;
		} catch (IOException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack;
		} catch (JSONException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack;
		} catch (NoSuchAlgorithmException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack;
		} catch (KeyManagementException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			return stack;
		}
		
		return json.toString();
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
