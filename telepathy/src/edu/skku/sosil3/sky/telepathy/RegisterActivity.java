package edu.skku.sosil3.sky.telepathy;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		edit_id = (EditText) findViewById(R.id.register_id);
		edit_pw = (EditText) findViewById(R.id.register_pw);
		edit_repw = (EditText) findViewById(R.id.register_repw);
		edit_nickname = (EditText) findViewById(R.id.register_nickname);
		
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7beda7")));
		actionBar.setIcon(R.drawable.white);
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
