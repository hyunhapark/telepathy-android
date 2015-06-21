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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	EditText edit_id;
	EditText edit_pw;
	EditText edit_repw;
	ActionBar actionBar;
	
	static int register_state = 0;
	static final int DEFAULT = 0;
	static final int SUCCESS = 1;
	static final int WRONG_ID = 2;
	static final int NETWORK_ERROR = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		edit_id = (EditText) findViewById(R.id.register_id);
		edit_pw = (EditText) findViewById(R.id.register_pw);
		edit_repw = (EditText) findViewById(R.id.register_repw);
		
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7beda7")));
		actionBar.setIcon(R.drawable.white);
	}
	
	public void onRegisterEnd(View v){
		final String id = edit_id.getText().toString();
		final String pw = edit_pw.getText().toString();
		final String repw = edit_repw.getText().toString();
		
		if(id.isEmpty()){
			Toast.makeText(this, "ID를 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else if(pw.isEmpty()){
			Toast.makeText(this, "암호를 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else if(!pw.equals(repw)){
			Toast.makeText(this, "재입력한 암호가 다릅니다", Toast.LENGTH_SHORT).show();
		}
		else{
	         new Thread() {
	            
	            @SuppressWarnings("deprecation")
	            public void run() {
	               register_state = DEFAULT;
	               
	               String uriString = "http://telepathy.hyunha.org/signup"; // 로그인 확인할 url
	               ArrayList<BasicNameValuePair> Parameter = new ArrayList<BasicNameValuePair>();
	               
	               try {
	                  URI uri = new URI(uriString);
	                  HttpClient httpclient = new DefaultHttpClient();
	                  HttpPost httpPost = new HttpPost(uri);
	                  
	                  Parameter.add(new BasicNameValuePair("id",id));
	                  Parameter.add(new BasicNameValuePair("pw",pw));
	                  
	                  httpPost.setEntity(new UrlEncodedFormEntity(Parameter, "UTF-8"));
	                  
	                  // JSON data 얻어내기
	                  HttpResponse httpResponse = httpclient.execute(httpPost);
	                  String responseString = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
	                  Log.d("Register","responseString");
	                  Log.d("Register", responseString);
	                  
	                  // JSON data에서 success 여부 알아내기
	                  JSONObject jsonObject = new JSONObject(responseString);
	                  String success = jsonObject.getString("success");
	                  Log.d("Register","success : "+success);
	                  
	                  // success 여부에 따라 login_state를 설정
	                  if (success.equals("true"))
	                     register_state=SUCCESS;
	                  else if (success.equals("false"))
	                     register_state=WRONG_ID;
	                  
	               } catch (IOException e) {
	                  register_state=NETWORK_ERROR;
	               } catch (JSONException e) {
	                  register_state=NETWORK_ERROR;
	               } catch (Exception e) {
	                  e.printStackTrace();
	               }
	               
	               if (register_state==SUCCESS) { // 로그인 성공
	                  Log.d("Register","comment : SUCCESS");
	                  Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT)
                      .show();
	                  finish();
	                  
	               } else if (register_state==WRONG_ID) { // 로그인 실패 - ID나 암호가 잘못됨
	                  Log.d("Register","comment : WRONG_IDPW");
	                  runOnUiThread(new Runnable(){
	                       public void run(){
	                           Toast.makeText(getApplicationContext(), "중복된 ID이거나 금지된 ID입니다.", Toast.LENGTH_SHORT)
	                           .show();
	                       }
	                  });
	                  
	               } else if (register_state==NETWORK_ERROR) { // 로그인 실패 - 네트워크 오류
	                  Log.d("Register","comment : NETWORK_ERROR");
	                  runOnUiThread(new Runnable(){
	                       public void run(){
	                           Toast.makeText(getApplicationContext(), "네트워크 상태를 확인하십시오.", Toast.LENGTH_SHORT)
	                           .show();
	                       }
	                  });
	                  
	               } else { // 로그인 실패 - 알 수 없는 오류
	                  Log.d("Register","comment : DEFAULT");
	                  runOnUiThread(new Runnable(){
	                       public void run(){
	                           Toast.makeText(getApplicationContext(), "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT)
	                           .show();
	                       }
	                  });
	               }
	            }
	         }.start(); // thread end
			finish();
		}
	}
	
	public void onRegisterCancel(View v){
		finish();
	}
}
