package br.com.uarini.ticket.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

public final class HttpHandler {
	private List<String> cookies;
	private HttpURLConnection conn;

	public final static String USER_AGENT = "Mozilla/5.0";

	private static HttpHandler handler;

	private HttpHandler() {
	}

	public static HttpHandler getInstance() {
		if (handler == null) {
			handler = new HttpHandler();
		}
		return handler;
	}
	
	public static void releaseInstance() {
		handler = null;
	}


	public String getPageContent(String url) throws Exception {

		final URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		} else {
			conn.setRequestProperty("Cookie", "");
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// Get the response cookies
		Map<String, List<String>> cabecalho = conn.getHeaderFields();
		Set<String> keys = cabecalho.keySet();
		for ( String key : keys ) {
			for ( String value : cabecalho.get(key) ) {
				Log.d(null, " \t" + value);
			}
		}
		setCookies(conn.getHeaderFields().get("Set-Cookie"));

		return response.toString();

	}

	public List<String> getCookies() {
		return cookies;
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}

}
