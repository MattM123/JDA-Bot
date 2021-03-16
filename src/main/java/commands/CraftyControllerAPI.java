package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CraftyControllerAPI {
	
	private static String apikey;
	public String stackTrace = "";
	
	public CraftyControllerAPI(String api) {
		apikey = api;
	}
	
	public void fixUntrustCertificate() throws KeyManagementException, NoSuchAlgorithmException{
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // set the  allTrusting verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
}
	
	//returns the list of servers and their stats
	public String getServerStats() {
		String line;
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;
		JsonArray jarray = null;

		try {
			fixUntrustCertificate();
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
				return "Error Code: " + String.valueOf(conn.getResponseCode()) + "\n" + json.toString();
			}		
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = in.readLine()) != null) {
				json.append(line);
			}
			in.close();	
			
			JsonElement ele = JsonParser.parseString(json.toString());
			jarray = ele.getAsJsonObject().getAsJsonArray("data");
			
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
		
		return jarray.toString();
	}
	
	
	public String sendCommand(String command) {
		String line;
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;
		

		try {
			fixUntrustCertificate();
			url = new URL("https://panel.richterent.com/api/v1/server/send_command?token=" + apikey + "&id=6");
			//conn = (HttpsURLConnection) url.openConnection();
			
			
			
			//HttpMethodParams com = (HttpMethodParams) params;
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://panel.richterent.com/api/v1/server/send_command?token=" + apikey + "&id=6");
			
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("&command=", command));
		//	HttpClient client = new HttpClient();
		//	HttpMethod post = new PostMethod("https://panel.richterent.com/api/v1/server/send_command?token=" + apikey + "&id=6");
			
			//post.setParams((HttpParams) com);
			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.192 Safari/537.36");
			post.setHeader("Accept", "text/html");
			post.setHeader("Host", "panel.richterent.com");
			post.setHeader("Content-Type", "multipart/form-data");
			post.setEntity(new UrlEncodedFormEntity(params));
			client.execute(post);
			client.close();
			
			//post.execute(new HttpState(), post);
			//conn = (HttpsURLConnection) url.openConnection();
			
			/*	    
		
			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.192 Safari/537.36");
			post.setHeader("Accept", "text/html");
			post.setHeader("Host", "panel.richterent.com");
			post.setHeader("Content-Type", "multipart/form-data");
			post.setEntity(new UrlEncodedFormEntity(data));
			
		*/	
		
			
			
		//	conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.192 Safari/537.36");
		//	conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
		//	conn.setRequestProperty("Accept", "text/html");
		//	conn.setRequestProperty("Host", "panel.richterent.com");
		//	conn.setRequestProperty("command", command);
		//	conn.setRequestMethod("POST"); // PUT is another valid option
	
			//Storing JSON from request into string. Prints error code and error stream if encountered.
			
			/*
			if (post. > 200) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = in.readLine()) != null) {
					json.append(line);
				}
				in.close();
				return "Error Code: " + String.valueOf(conn.getResponseCode() + "\n" + json.toString() + "\n" + code);
			}		
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = in.readLine()) != null) {
				json.append(line);
			}
			in.close();	
		
		*/	
		} catch (MalformedURLException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (IOException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (JSONException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (NoSuchAlgorithmException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (KeyManagementException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		}
		return "Command sent to console: " + command;
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
