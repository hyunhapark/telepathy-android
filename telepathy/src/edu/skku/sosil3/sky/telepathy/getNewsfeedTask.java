package edu.skku.sosil3.sky.telepathy;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class GetNewsfeedTask extends AsyncTask<Void, Void, Void>
{
	JSONObject data = null;
	String url = null;
	Context ctx;
	TabActivity ta;
	
	
	
	public GetNewsfeedTask(Context ctx, String url, int lim, int page, boolean clear) {
		super();
		this.ctx = ctx;
		this.ta = (TabActivity) ctx;
		if (clear){
			this.url = url+lim*page+"/0";
			((TabActivity)ctx).array1.clear();
		}else{
			this.url = url+lim+"/"+page;
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		JSONDownloader jd = new JSONDownloader();
		try {
			data = jd.get(url);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			((TabActivity)ctx).nf_page--;
		} catch (IOException e) {
			((TabActivity)ctx).nf_page--;
		} catch (JSONException e) {
			e.printStackTrace();
			((TabActivity)ctx).nf_page--;
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		
		super.onPostExecute(result);
		//Log.i(null,data.toString());
		
		int cnt=0;
		JSONArray arr;
		
		try {
			cnt = data.getInt("cnt");
			arr = data.getJSONArray("arr");
		} catch (JSONException e) {
			Toast.makeText(ctx, "Error. check network status and try again later.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			((TabActivity)ctx).nf_page--;
			return;
		}catch (NullPointerException npe) {
			Toast.makeText(ctx, "Error. check network status and try again later.", Toast.LENGTH_LONG).show();
			((TabActivity)ctx).nf_page--;
			return;
		} 
		for (int i=0; i<cnt; i++){
			JSONObject json = null;
			try {
				json = arr.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			PostItem pi = null;
			ArrayList<CommentItem> cmt = new ArrayList<CommentItem>();
			JSONObject jcmts;
			try {
				jcmts = json.getJSONObject("p_comments");
				int ccnt = jcmts.getInt("cnt");
				JSONArray cmts = jcmts.getJSONArray("arr");
				for (int j=0;j<ccnt;j++){
					JSONObject jcmt = cmts.getJSONObject(j);
					Date d = new Date(Long.parseLong(jcmt.getString("c_date")));
					
					CommentItem ci = new CommentItem(jcmt.getString("c_user"),
							d.getMonth()+"/"+d.getDate()+" "+d.getHours()+":"+d.getMinutes(), 
							jcmt.getString("c_content")); 
					cmt.add(ci);
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			try {
				pi = new PostItem(json.getString("_id"),
						json.getString("p_user"), json.getString("p_date"), json.getString("p_image"),
						json.getString("p_content"), cmt,
						Double.parseDouble(json.getString("p_lati")),
						Double.parseDouble(json.getString("p_long")));
				ta.array1.add(pi);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		TabActivity ta = (TabActivity)ctx;
		if(cnt==0){
			ta.noMoreContentsOne = true;
		}
		ta.switcherOne.showPrevious();
		ta.mLockListViewOne = false;
		ta.adapter1.notifyDataSetChanged();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	
}
