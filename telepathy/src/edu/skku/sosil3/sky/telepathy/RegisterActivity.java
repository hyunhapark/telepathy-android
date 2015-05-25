package edu.skku.sosil3.sky.telepathy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	EditText edit_id;
	EditText edit_pw;
	EditText edit_repw;
	EditText edit_nickname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		edit_id = (EditText) findViewById(R.id.register_id);
		edit_pw = (EditText) findViewById(R.id.register_pw);
		edit_repw = (EditText) findViewById(R.id.register_repw);
		edit_nickname = (EditText) findViewById(R.id.register_nickname);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onRegisterEnd(View v){
		String id = edit_id.getText().toString();
		String pw = edit_pw.getText().toString();
		String repw = edit_repw.getText().toString();
		String nickname = edit_nickname.getText().toString();
		
		if(id.isEmpty()){
			Toast.makeText(this, "ID를 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else if(pw.isEmpty()){
			Toast.makeText(this, "암호를 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else if(!pw.equals(repw)){
			Toast.makeText(this, "재입력한 암호가 다릅니다", Toast.LENGTH_SHORT).show();
		}
		else if(nickname.isEmpty()){
			Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else{
			// 회원가입
			finish();
		}
	}
	
	public void onRegisterCancel(View v){
		finish();
	}
}
