package edu.skku.sosil3.sky.telepathy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText edit_id;
	EditText edit_pw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edit_id = (EditText) findViewById(R.id.main_id);
		edit_pw = (EditText) findViewById(R.id.main_pw);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	}public MainActivity() {
		// TODO Auto-generated constructor stub
	}
	
	public void onLogin(View v){
		String id = edit_id.getText().toString();
		String pw = edit_pw.getText().toString();
		Boolean success = true;
		
		if(id.isEmpty()){
			Toast.makeText(this, "ID를 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else if(pw.isEmpty()){
			Toast.makeText(this, "암호를 입력하세요", Toast.LENGTH_SHORT).show();
		}
		else{
			// 로그인 요청
			
			if(success){
				startActivity(new Intent(this, TabActivity.class));
				finish();
			}
			else{
				Toast.makeText(this, "ID나 암호가 잘못되었습니다", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void onRegister(View v){
		startActivity(new Intent(this, RegisterActivity.class));
	}
}
