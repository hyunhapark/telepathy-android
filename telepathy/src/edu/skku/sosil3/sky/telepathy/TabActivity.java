package edu.skku.sosil3.sky.telepathy;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class TabActivity extends Activity {

	TabHost tabHost;
	TabWidget tabWidget;
	public ListView ListOne = null;
	public ListView ListTwo = null;
	public PostAdapter adapter1 = null;
	public PostAdapter adapter2 = null;
	CheckBox anonymity;
	TextView date;
	ImageView image;
	EditText content;
	double Lati;
	double Long;
	Dialog dialog;
	Bitmap bmp = null;
	ActionBar actionBar;
	String id;
	Intent intent;
	public SharedPreferences preferences;
	public ArrayList<PostItem> array1;
	public ArrayList<PostItem> array2;
	public double p_lati, p_long;
	private int nf_page = 0;
	private int mp_page = 0;
	
	static String SAMPLEIMG = "photo.png";
	private final static int ACT_CAMERA = 1;
	private final static int ACT_GALLERY = 2;
	private final static int SETTINGS_INFO = 3;
	
	static int upload_state = 0;
	static final int DEFAULT = 0;
	static final int SUCCESS = 1;
	static final int LOCATION_NOT_FOUND = 2;
	static final int NETWORK_ERROR = 3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		intent = getIntent();
		id = intent.getStringExtra("id");
		if(preferences.getBoolean("pref_auto_login", true) && intent.getIntExtra("Activity", 0) == MainActivity.ActivityCode){
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("id", id);
			editor.commit();
		}
		
		ListOne = (ListView) findViewById(R.id.two_list);
		ListTwo = (ListView) findViewById(R.id.three_list);
		anonymity = (CheckBox) findViewById(R.id.one_anonymity);
		date = (TextView) findViewById(R.id.one_date);
		image = (ImageView) findViewById(R.id.one_image);
		content = (EditText) findViewById(R.id.one_content);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		date.setText(format.format(new Date()));

		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(preferences.getString("pref_ui_color", "#7beda7"))));
		actionBar.setIcon(R.drawable.white);

		array1 = new ArrayList<PostItem>();
		array2 = new ArrayList<PostItem>();
		adapter1 = new PostAdapter(this, R.layout.post, array1);
		adapter2 = new PostAdapter(this, R.layout.post, array2);
		int txtSize = Integer.parseInt(preferences.getString("pref_content_text_size", "16"));
        adapter1.settxtSize(txtSize);
        adapter2.settxtSize(txtSize);
        ListOne.setAdapter(adapter1);
        ListTwo.setAdapter(adapter2);

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GPSLocationListener gpsLocationListener = new GPSLocationListener(locationManager);
        p_lati = gpsLocationListener.getLatitude(); // 위도
        p_long = gpsLocationListener.getLongitude(); // 경도
        
        nf_page = 0;
        mp_page = 0;
        
		// 서버 연결, 데이터 수집
		get_newsfeed();
		get_my_posts();
		// 데이터 수집 끝
		
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
		spec.setContent(R.id.tab_fragment1);
		spec.setIndicator("글쓰기");
		// spec.setIndicator("", getResources().getDrawable(R.drawable.)); 이거로 탭에 이미지버튼을 넣을 수 있음
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("tab2");
		spec.setContent(R.id.tab_fragment2);
		spec.setIndicator("뉴스피드");
		// spec.setIndicator("", getResources().getDrawable(R.drawable.));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tab3");
		spec.setContent(R.id.tab_fragment3);
		spec.setIndicator("내가 쓴 글");
		// spec.setIndicator("", getResources().getDrawable(R.drawable.));
		tabHost.addTab(spec);
		
		tabWidget = tabHost.getTabWidget();
		for(int i=0; i<3; i++){
			View tc = tabWidget.getChildAt(i);
			tc.setBackgroundColor(Color.parseColor(preferences.getString("pref_ui_color", "#7beda7")));
			TextView tv = (TextView) tc.findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#FFFFFF"));
			tv.setTextSize(16);
		}
		
		tabHost.setCurrentTab(1);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() { // 탭 클릭되었을때 호출되는 메소드
			@Override
			public void onTabChanged(String tabId) {
			}
		});
	}

	private void get_newsfeed() {
		String url = Constants.URL_SERVER_HOST+Constants.URI_GET_NEWSFEED+"/anonymous/"+p_lati+"/"+p_long+"/"+Constants.DEFAULT_PAGE_SIZE+"/"+nf_page++;
		new GetNewsfeedTask(this, url).execute();
	}
	
	private void get_my_posts() {
		String url = Constants.URL_SERVER_HOST+Constants.URI_GET_MYPOSTS+"/"+id+"/"+p_lati+"/"+p_long+"/"+Constants.DEFAULT_PAGE_SIZE+"/"+mp_page++;
		new GetMypostsTask(this, url).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(adapter1 != null && adapter2 != null){
			int txtSize = Integer.parseInt(preferences.getString("pref_content_text_size", "16"));
			adapter1.settxtSize(txtSize);
			adapter2.settxtSize(txtSize);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.custom_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_INFO);
			return true;
		}
		else if(id == R.id.action_refresh){
			// 새로고침
		}
		else if(id == R.id.action_logout){
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("id", "EMPTY_");
			editor.commit();
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onImage(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View customLayout = View.inflate(this, R.layout.uploadimage, null);
		builder.setView(customLayout);
		dialog = builder.create();
		dialog.show();
	}
	
	public void onClickImg(View v){
		dialog.dismiss();
		switch(v.getId()){
		case R.id.selectcamera:
      		Intent intCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      		File file = new File(Environment.getExternalStorageDirectory(), SAMPLEIMG);
      		intCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
      		startActivityForResult(intCamera, ACT_CAMERA);
      		break;
		case R.id.selectalbum:
    		Intent intAlbum = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      		startActivityForResult(intAlbum, ACT_GALLERY);
    		break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == SETTINGS_INFO){
        	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        	int txtSize = Integer.parseInt(sharedPreferences.getString("pref_content_text_size", "16"));
        	adapter1.settxtSize(txtSize);
        	adapter2.settxtSize(txtSize);
        	adapter1.notifyDataSetChanged();
        	adapter2.notifyDataSetChanged();
    		if(sharedPreferences.getBoolean("pref_auto_login", true) && intent.getIntExtra("Activity", 0) == MainActivity.ActivityCode){
    			SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("id", id);
				editor.commit();
    		}
    		
    		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(sharedPreferences.getString("pref_ui_color", "#7beda7"))));
    		for(int i=0; i<3; i++){
    			View tc = tabWidget.getChildAt(i);
    			tc.setBackgroundColor(Color.parseColor(sharedPreferences.getString("pref_ui_color", "#7beda7")));
    		}
		}
		
	    if(resultCode != RESULT_OK){
  			return;
	    }

  		BitmapFactory.Options option = new BitmapFactory.Options();
  		option.inSampleSize = 4;
  		
	    switch(requestCode){
	    	case ACT_CAMERA:
	    		File file = new File(Environment.getExternalStorageDirectory(), SAMPLEIMG);
	      		bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
	      		file.delete();
				break;
	        case ACT_GALLERY: 
	        	Cursor c = this.getContentResolver().query(data.getData(), null, null, null, null);
	            c.moveToNext();
	            String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
	            Uri uri = Uri.fromFile(new File(path));
	            c.close();
	            bmp = BitmapFactory.decodeFile(uri.getPath(), option);
	        	break;
	    }
	    
	    if(bmp != null){
	    	image.setImageBitmap(bmp);
	    }
	    else{
	    	Toast.makeText(this, "이미지를 불러오는 데에 실패했습니다.", Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void onFinish(View v){
	      Boolean res_anonymity = anonymity.isChecked(); // 익명 여부
	      
	      final String res_user; // 이름
	      final String res_content = content.getText().toString(); // 글
	      final String res_image = ""; // 이미지
	      
	      LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	      GPSLocationListener gpsLocationListener = new GPSLocationListener(locationManager);
	      final double res_latitude = gpsLocationListener.getLatitude(); // 위도
	      final double res_longitude = gpsLocationListener.getLongitude(); // 경도
	      gpsLocationListener.stopGettingLocation();
	      
	      if(res_anonymity) { // 익명 여부 판단
	         res_user = "익명";
	      } else {
	         res_user = id;
	      }
	      
	      // 서버에 글을 등록
	      new Thread() {
	            
	            @SuppressWarnings("deprecation")
	            public void run() {
	               upload_state = DEFAULT;

	            String uriString = "http://telepathy.hyunha.org/post"; // 업로드할 url
	            ArrayList<BasicNameValuePair> Parameter = new ArrayList<BasicNameValuePair>();

	            try {
	               URI uri = new URI(uriString);
	               HttpClient httpclient = new DefaultHttpClient();
	               HttpPost httpPost = new HttpPost(uri);

	               Parameter.add(new BasicNameValuePair("p_user", res_user));
	               Parameter.add(new BasicNameValuePair("p_image", res_image));
	               Parameter.add(new BasicNameValuePair("p_content", res_content));
	               Parameter.add(new BasicNameValuePair("p_lati", Double.toString(res_latitude)));
	               Parameter.add(new BasicNameValuePair("p_long", Double.toString(res_longitude)));
	               
	               Log.d("Upload", "Uploading data");
	               Log.d("Upload", "- "+res_user);
	               Log.d("Upload", "- "+res_image);
	               Log.d("Upload", "- "+res_content);
	               Log.d("Upload", "- "+Double.toString(res_latitude));
	               Log.d("Upload", "- "+Double.toString(res_longitude));
	               
	               httpPost.setEntity(new UrlEncodedFormEntity(Parameter,"UTF-8"));

	               // JSON data 얻어내기
	               HttpResponse httpResponse = httpclient.execute(httpPost);
	               String responseString = EntityUtils.toString(
	                     httpResponse.getEntity(), HTTP.UTF_8);
	               Log.d("Upload", "responseString");
	               Log.d("Upload", responseString);

	               // JSON data에서 success 여부 알아내기
	               JSONObject jsonObject = new JSONObject(responseString);
	               String success = jsonObject.getString("success");
	               Log.d("Upload", "success : " + success);

	               if (res_latitude != GPSLocationListener.DATA_GET_FAILED) upload_state = SUCCESS;
	               else upload_state = LOCATION_NOT_FOUND;

	            } catch (IOException e) {
	               upload_state = NETWORK_ERROR;
	            } catch (JSONException e) {
	               upload_state = NETWORK_ERROR;
	            } catch (Exception e) {
	               e.printStackTrace();
	            }

	            if (upload_state == SUCCESS) { // 업로드 성공
	               Log.d("Upload", "comment : SUCCESS");
	               runOnUiThread(new Runnable() {
	                  public void run() {
	                     Toast.makeText(getApplicationContext(),
	                           "성공적으로 업로드하였습니다.", Toast.LENGTH_SHORT)
	                           .show();
	                  }
	               });
	               startActivity(new Intent(getApplicationContext(), TabActivity.class));
	               finish();
	               
	            } else if (upload_state == LOCATION_NOT_FOUND) { // 업로드 성공 - 좌표 찾기 실패
	               Log.d("Upload", "comment : LOCATION_NOT_FOUND");
	               runOnUiThread(new Runnable() {
	                  public void run() {
	                     Toast.makeText(getApplicationContext(),
	                           "좌표 찾기에 실패하였습니다. 좌표에 대한 정보 없이 업로드합니다.", Toast.LENGTH_SHORT)
	                           .show();
	                  }
	               });
	               startActivity(new Intent(getApplicationContext(), TabActivity.class));
	               finish();

	            } else if (upload_state == NETWORK_ERROR) { // 업로드 실패 - 네트워크 오류
	               Log.d("Upload", "comment : NETWORK_ERROR");
	               runOnUiThread(new Runnable() {
	                  public void run() {
	                     Toast.makeText(getApplicationContext(),
	                           "네트워크 상태를 확인하십시오.", Toast.LENGTH_SHORT)
	                           .show();
	                  }
	               });

	            } else { // 업로드 실패 - 알 수 없는 오류
	               Log.d("Register", "comment : DEFAULT");
	               runOnUiThread(new Runnable() {
	                  public void run() {
	                     Toast.makeText(getApplicationContext(),
	                           "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT)
	                           .show();
	                  }
	               });
	            }
	         }
	      }.start(); // thread end
	   }
}
