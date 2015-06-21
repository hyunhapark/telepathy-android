package edu.skku.sosil3.sky.telepathy;



import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class getNewsfeedTask extends AsyncTask<Void, Void, Void>
{
	JSONObject data = null;
	
	Context ctx;
	
	
	public getNewsfeedTask(Context ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	protected Void doInBackground(Void... params) {
		JSONDownloader jd = new JSONDownloader();
		try {
			data = jd.get(Constants.URL_SERVER_HOST+Constants.URI_GET_NEWSFEED);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		
		super.onPostExecute(result);
		//Log.i(null,data.toString());
		/*
		JSONArray arr = null;
		int length=0;
		try {
			arr = data.getJSONArray("list");
			length = arr.length();
		} catch (JSONException je) {
			je.printStackTrace();
		} catch (NullPointerException npe) {
			Toast.makeText(ctx, "Error. check network status and try again later.", Toast.LENGTH_LONG).show();
			return;
		} 
		for(int i=0 ; i<length ; i++){
			JSONObject json = null;
			try {
				json = arr.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Date date = null;
			Calendar cal = null;
			String max = null;
			String min = null;
			String description = null;
			String main = null;
			
			try {
				date = new Date(Long.parseLong(json.getString("dt"))*1000L);
				cal = Calendar.getInstance();
				cal.setTime(date);
				max = json.getJSONObject("temp").getString("max");
				min = json.getJSONObject("temp").getString("min");
				description = json.getJSONArray("weather").getJSONObject(0).getString("description");
				main = json.getJSONArray("weather").getJSONObject(0).getString("main");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			int month = cal.get(Calendar.MONTH)+1;		// 1~12
			int day = cal.get(Calendar.DAY_OF_MONTH);	// 1~31
			int dow = cal.get(Calendar.DAY_OF_WEEK)-1;	// 0~6
			
			StringBuilder sbdate = new StringBuilder();
			sbdate.append(month);
			sbdate.append("/");
			sbdate.append(day);
			String sdate = sbdate.toString();
			
			MainActivity.wl.add(new Weather(Constants.SDAY_KO[dow], sdate, description, main, max, min));
		}
		for(int i=0 ; i<length ; i++){
			MainActivity.lv_data.set(i, MainActivity.wl.get(i).toString());
		}
		MainActivity.aa.notifyDataSetChanged();
		Toast.makeText(ctx, "updated", Toast.LENGTH_SHORT).show();
		*/
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	
}
