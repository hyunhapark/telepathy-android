package edu.skku.sosil3.sky.telepathy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabActivity extends Activity {

	TabHost tabHost;
	ListView ListOne;
	ListView ListTwo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		
		ListOne = (ListView) findViewById(R.id.one_list);
		ListTwo = (ListView) findViewById(R.id.two_list);
		
		
		
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
		spec.setContent(R.id.tab_fragment1);
		spec.setIndicator("1");
		// spec.setIndicator("", getResources().getDrawable(R.drawable.)); 이거로 탭에 이미지버튼을 넣을 수 있음
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("tab2");
		spec.setContent(R.id.tab_fragment2);
		spec.setIndicator("2");
		// spec.setIndicator("", getResources().getDrawable(R.drawable.));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tab3");
		spec.setContent(R.id.tab_fragment3);
		spec.setIndicator("3");
		// spec.setIndicator("", getResources().getDrawable(R.drawable.));
		tabHost.addTab(spec);

		tabHost.setCurrentTab(1);
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() { // 탭 클릭되었을때 호출되는 메소드
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				
			}
		});
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
