package edu.skku.sosil3.sky.telepathy;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
		
		
		TextView user = (TextView) convertView.findViewById(R.id.post_user);
		user.setText(list.get(position).getPostUser());

		TextView date = (TextView) convertView.findViewById(R.id.post_date);
		date.setText(list.get(position).getPostDate());
		
		ImageView image = (ImageView) convertView.findViewById(R.id.post_image);
		image.setImageResource(R.drawable.ic_launcher); // 수정 요망 list.get(position).getPostImage()
		
		TextView content = (TextView) convertView.findViewById(R.id.post_content);
		content.setText(list.get(position).getPostContent());
		
		if(list.get(position).getPostComments() != null){
			ListView comments = (ListView) convertView.findViewById(R.id.post_comments);
			CommentAdapter adapter = new CommentAdapter(context, R.layout.comment, list.get(position).getPostComments());
			comments.setAdapter(adapter);
		}
		
		return convertView;
	}
}