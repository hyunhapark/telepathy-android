package edu.skku.sosil3.sky.telepathy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

@SuppressWarnings("deprecation")
public class JSONDownloader {
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpResponse response;
	private HttpEntity entity;

	public JSONDownloader() {
		super();
		httpClient = null;
		httpGet = null;
		response = null;
		entity = null;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public JSONObject get(String url) throws ClientProtocolException,
			IOException, JSONException {
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet(url);

		JSONObject json = null;
		response = httpClient.execute(httpGet);
		entity = response.getEntity();

		if (entity != null) {
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);

			json = new JSONObject(result);
			instream.close();
		}

		return json;

	}

	public String start(String url) throws ClientProtocolException,
			IOException, JSONException {
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet(url);

		response = httpClient.execute(httpGet);
		entity = response.getEntity();

		if (entity != null) {
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);
			instream.close();
			return result;
		}
		return null;
	}
}
