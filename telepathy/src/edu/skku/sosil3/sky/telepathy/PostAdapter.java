package edu.skku.sosil3.sky.telepathy;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter {
	Context context;
	int layoutRes;
	ArrayList<PostItem> list;
	LayoutInflater inflater;

	public PostAdapter(Context context, int layoutRes, ArrayList<PostItem> list) {
		this.context = context;
		this.layoutRes = layoutRes;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(layoutRes, parent, false);
		}
		
		/*
		ImageView image = (ImageView) convertView.findViewById(R.id.post_image);
		image.setImageResource(list.get(position).MyImage);
		
		TextView title = (TextView) convertView.findViewById(R.id.myitemtitle);
		title.setText(list.get(position).MyTitle);
		
		TextView date = (TextView) convertView.findViewById(R.id.myitemdate);
		date.setText(list.get(position).MyDate);
		
		*/
		return convertView;
	}
}