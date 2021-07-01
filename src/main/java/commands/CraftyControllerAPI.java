package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
	public String getServerList() {
		String line;
		String out = "test";
		BufferedReader in; 
		StringBuilder json = new StringBuilder();
		URL url;
		HttpsURLConnection conn = null;

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
		} catch (NoSuchAlgorithmException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		} catch (KeyManagementException e) {
			String stack = ExceptionUtils.getStackTrace(e);
			stackTrace = stack;
		}
		
		return out;
	}
	
	public HttpResponse postWithFormData(String url, List<BasicNameValuePair> params) throws IOException {
		
        // building http client
        HttpClient httpClient = new HttpClient();
        HttpPost post = new HttpPost(url);

        // adding the form data
	    post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.192 Safari/537.36");
	    post.setHeader("Accept", "text/html");
	    post.setHeader("Host", "panel.richterent.com");
	    post.setHeader("Content-Type", "multipart/form-data");
        post.setEntity(new UrlEncodedFormEntity(params));
        
        return ((org.apache.http.client.HttpClient) httpClient).execute(post);
     
    }
	
	public String sendCommand(String command) {
		String url;
		Response response = null;
		String responseString = "";

		try {
			fixUntrustCertificate();
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
					responseString = response.body().string();
					fixUntrustCertificate();
					
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
		return "Respnonse: " + responseString;
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
