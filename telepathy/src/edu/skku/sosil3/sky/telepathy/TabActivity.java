package edu.skku.sosil3.sky.telepathy;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabActivity extends Activity {

	TabHost tabHost;
	ListView ListOne;
	ListView ListTwo;
	double Lati;
	double Long;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		
		ListOne = (ListView) findViewById(R.id.one_list);
		ListTwo = (ListView) findViewById(R.id.two_list);
		
		// 데이터 수집
		ArrayList<PostItem> array1 = new ArrayList<PostItem>();
		array1.add(new PostItem(1, "조은현", "2015/04/25", "NULL", "ㅎㅎㅎ1", null, 100.0, 100.0));
		array1.add(new PostItem(1, "조은현", "2015/04/25", "NULL", "ㅎㅎㅎ2", null, 100.0, 100.0));
		
		ArrayList<PostItem> array2 = new ArrayList<PostItem>();
		ArrayList<CommentItem> commentarray1 = new ArrayList<CommentItem>();
		ArrayList<CommentItem> commentarray2 = new ArrayList<CommentItem>();
		commentarray1.add(new CommentItem("익명", "2015/04/26", "도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배"));
		commentarray2.add(new CommentItem("김승현", "2015/04/26", "ㅡㅡ?"));
		commentarray2.add(new CommentItem("박현하", "2015/04/26", "ㅇㅅㅇ;;"));
		array2.add(new PostItem(1, "조은현", "2015/04/25", "NULL", "ㅎㅎㅎ1", commentarray1, 100.0, 100.0));
		array2.add(new PostItem(1, "조은현", "2015/04/25", "NULL", "ㅎㅎㅎ2", commentarray2, 100.0, 100.0));
		// 데이터 수집 끝
		
		PostAdapter adapter1 = new PostAdapter(this, R.layout.post, array1);
		ListOne.setAdapter(adapter1);
		
		PostAdapter adapter2 = new PostAdapter(this, R.layout.post, array2);
		ListTwo.setAdapter(adapter2);
		
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
