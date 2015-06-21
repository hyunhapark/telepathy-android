package edu.skku.sosil3.sky.telepathy;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText edit_id;
	EditText edit_pw;
	ActionBar actionBar;
	static int login_state = 0;
	static final int DEFAULT = 0;
	static final int SUCCESS = 1;
	static final int WRONG_IDPW = 2;
	static final int NETWORK_ERROR = 3;
	
	static final int ActivityCode = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edit_id = (EditText) findViewById(R.id.main_id);
		edit_pw = (EditText) findViewById(R.id.main_pw);
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7beda7")));
		actionBar.setIcon(R.drawable.white);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onLogin(View v){
		final String id = edit_id.getText().toString();
		final String pw = edit_pw.getText().toString();
		
		if(id.isEmpty()){
			Toast.makeText(this, "ID를 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else if(pw.isEmpty()){
			Toast.makeText(this, "암호를 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else{
			// 로그인 요청
	         new Thread() {
	            
	            @SuppressWarnings("deprecation")
	            public void run() {
	               login_state = DEFAULT;
	               
	               String uriString = "http://telepathy.hyunha.org/signin"; // 로그인 확인할 url
	               ArrayList<BasicNameValuePair> Parameter = new ArrayList<BasicNameValuePair>();
	               
	               try {
	                  URI uri = new URI(uriString);
	                  HttpClient httpclient = new DefaultHttpClient();
	                  HttpPost httpPost = new HttpPost(uri);
	                  
	                  Parameter.add(new BasicNameValuePair("id", id));
	                  Parameter.add(new BasicNameValuePair("pw", pw));
	                  
	                  httpPost.setEntity(new UrlEncodedFormEntity(Parameter, "UTF-8"));
	                  
	                  // JSON data 얻어내기
	                  HttpResponse httpResponse = httpclient.execute(httpPost);
	                  String responseString = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
	                  Log.d("responseString", responseString);
	                  
	                  // JSON data에서 success 여부 알아내기
	                  JSONObject jsonObject = new JSONObject(responseString);
	                  String success = jsonObject.getString("success");
	                  Log.d("success",success);
	                  
	                  // success 여부에 따라 login_state를 설정
	                  if (success.equals("true"))
	                     login_state=SUCCESS;
	                  else if (success.equals("false"))
	                     login_state=WRONG_IDPW;
	                  
	               } catch (IOException e) {
	                  login_state=NETWORK_ERROR;
	               } catch (JSONException e) {
	                  login_state=NETWORK_ERROR;
	               } catch (Exception e) {
	                  e.printStackTrace();
	               }
	               
	               if (login_state==SUCCESS) { // 로그인 성공
	                  Log.d("comment","SUCCESS");
	                  Intent intent = new Intent(getApplicationContext(), TabActivity.class);
	                  intent.putExtra("id", id);
	                  intent.putExtra("Activity", ActivityCode);
	                  startActivity(intent);
	                  finish();
	                  
	               } else if (login_state==WRONG_IDPW) { // 로그인 실패 - ID나 암호가 잘못됨
	                  Log.d("comment","WRONG_IDPW");
	                  runOnUiThread(new Runnable(){
	                       public void run(){
	                           Toast.makeText(getApplicationContext(), "ID나 암호가 잘못되었습니다", Toast.LENGTH_SHORT)
	                           .show();
	                       }
	                  });
	                  
	               } else if (login_state==NETWORK_ERROR) { // 로그인 실패 - 네트워크 오류
	                  Log.d("comment","NETWORK_ERROR");
	                  runOnUiThread(new Runnable(){
	                       public void run(){
	                           Toast.makeText(getApplicationContext(), "네트워크 상태를 확인하십시오.", Toast.LENGTH_SHORT)
	                           .show();
	                       }
	                  });
	                  
	               } else { // 로그인 실패 - 알 수 없는 오류
	                  Log.d("comment","DEFAULT");
	                  runOnUiThread(new Runnable(){
	                       public void run(){
	                           Toast.makeText(getApplicationContext(), "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT)
	                           .show();
	                       }
	                  });
	               }
	            }
	         }.start();
		}
	}
	
	public void onRegister(View v){
		startActivity(new Intent(this, RegisterActivity.class));
	}
}
