package edu.skku.sosil3.sky.telepathy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class TabActivity extends Activity implements OnRefreshListener {

	TabHost tabHost;
	ListView ListOne;
	ListView ListTwo;
	PostAdapter adapter1;
	PostAdapter adapter2;
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
	SwipeRefreshLayout refreshLayout;
	
	static String SAMPLEIMG = "photo.png";
	private final static int ACT_CAMERA = 1;
	private final static int ACT_GALLERY = 2;
	private final static int SETTINGS_INFO = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
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
		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.two_swipe);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		date.setText(format.format(new Date()));

		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7beda7")));
		actionBar.setIcon(R.drawable.white);
		
		// refreshLayout.setOnRefreshListener(this); XXX
		
		// 서버 연결, 데이터 수집
		ArrayList<PostItem> array1 = get_newsfeed();
		ArrayList<PostItem> array2 = get_my_posts();
		// 데이터 수집 끝

		int txtSize = Integer.parseInt(preferences.getString("pref_content_text_size", "16"));
		System.out.println(txtSize);
		
		adapter1 = new PostAdapter(this, R.layout.post, array1);
        adapter1.settxtSize(txtSize);
		ListOne.setAdapter(adapter1);
		
		adapter2 = new PostAdapter(this, R.layout.post, array2);
        adapter2.settxtSize(txtSize);
		ListTwo.setAdapter(adapter2);
		
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
		
		TabWidget tabWidget = tabHost.getTabWidget();
		for(int i=0; i<3; i++){
			TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#FFFFFF"));
			tv.setTextSize(16);
		}
		
		tabHost.setCurrentTab(1);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() { // 탭 클릭되었을때 호출되는 메소드
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private ArrayList<PostItem> get_newsfeed() {
		ArrayList<PostItem> array1 = new ArrayList<PostItem>();
		array1.add(new PostItem(1, "조은현", "2015/04/25", "http://sw.skku.ac.kr/image/student/wats/popup/project_sum.jpg", "ㅎㅎㅎ1", null, 100.0, 100.0));
		array1.add(new PostItem(1, "조은현", "2015/04/25", "", "ㅎㅎㅎ2", null, 100.0, 100.0));
		return array1;
	}
	
	private ArrayList<PostItem> get_my_posts() {
		ArrayList<PostItem> array2 = new ArrayList<PostItem>();
		ArrayList<CommentItem> commentarray1 = new ArrayList<CommentItem>();
		ArrayList<CommentItem> commentarray2 = new ArrayList<CommentItem>();
		commentarray1.add(new CommentItem("익명", "2015/04/26", "도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배"));
		commentarray2.add(new CommentItem("김승현", "2015/04/26", "ㅡㅡ?"));
		commentarray2.add(new CommentItem("박현하", "2015/04/26", "ㅇㅅㅇ;;"));
		array2.add(new PostItem(1, "조은현", "2015/04/25", "", "도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배", commentarray1, 100.0, 100.0));
		array2.add(new PostItem(1, "조은현", "2015/04/25", "http://sw.skku.ac.kr/image/student/wats/popup/project_sum.jpg", "ㅎㅎㅎ2", commentarray2, 100.0, 100.0));
		
		return array2;
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
			startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_INFO);
			return true;
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
		Boolean res_anonymity = anonymity.isChecked();
		String res_user;
		String res_date = date.getText().toString();
		String res_content = content.getText().toString();
		
		if(res_anonymity){
			res_user = "익명";
		}
		else{
			res_user = null;
		}
		
		// 서버에 글을 등록
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshLayout.setRefreshing(false);
			}
			
		}, 2000);
	}
}
