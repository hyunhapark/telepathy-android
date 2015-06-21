package edu.skku.sosil3.sky.telepathy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends Activity {
	
	static final int ActivityCode = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				String id;
				if(preferences.getBoolean("pref_auto_login", true) && !(id = preferences.getString("id", "EMPTY_")).equals("EMPTY_")){
					Intent intent = new Intent(SplashActivity.this, TabActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("Activity", ActivityCode);
					startActivity(intent);
				}
				else{
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
				}
				finish();
			}
		}, 2000);
	}
	
}
