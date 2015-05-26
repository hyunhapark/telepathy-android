package edu.skku.sosil3.sky.telepathy;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	Context context;
	int layoutRes;
	ArrayList<CommentItem> list;
	LayoutInflater inflater;

	public CommentAdapter(Context context, int layoutRes, ArrayList<CommentItem> list) {
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
		
		TextView user = (TextView) convertView.findViewById(R.id.comment_user);
		user.setText(list.get(position).getCommentUser());

		TextView date = (TextView) convertView.findViewById(R.id.comment_date);
		date.setText(list.get(position).getCommentDate());
		
		TextView content = (TextView) convertView.findViewById(R.id.comment_content);
		content.setText(list.get(position).getCommentContent());
		
		return convertView;
	}
}