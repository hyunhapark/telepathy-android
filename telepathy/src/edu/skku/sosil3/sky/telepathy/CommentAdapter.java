package edu.skku.sosil3.sky.telepathy;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseExpandableListAdapter {
	Context context;
	int layoutRes;
	ArrayList<ArrayList<CommentItem>> list;
	LayoutInflater inflater;

	public CommentAdapter(Context context, int layoutRes, ArrayList<ArrayList<CommentItem>> list) {
		this.context = context;
		this.layoutRes = layoutRes;
		this.list = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.comment, null);
		}
		
		TextView user = (TextView) convertView.findViewById(R.id.comment_user);
		user.setText(list.get(groupPosition).get(childPosition).getCommentUser());

		TextView date = (TextView) convertView.findViewById(R.id.comment_date);
		date.setText(list.get(groupPosition).get(childPosition).getCommentDate());
		
		TextView content = (TextView) convertView.findViewById(R.id.comment_content);
		content.setText(list.get(groupPosition).get(childPosition).getCommentContent());
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return list.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.comment_group, null);
		}
		
		TextView text = (TextView) convertView.findViewById(R.id.comment_group_text);
		text.setText("댓글 목록");
		
		TextView number = (TextView) convertView.findViewById(R.id.comment_group_number);
		number.setText("(" + getChildrenCount(groupPosition) + ")");
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}