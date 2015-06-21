package edu.skku.sosil3.sky.telepathy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText edit_id;
	EditText edit_pw;
	ActionBar actionBar;
	
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
