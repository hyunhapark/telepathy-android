package edu.skku.sosil3.sky.telepathy;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter {
	Context context;
	int layoutRes;
	ArrayList<PostItem> list;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	int txtSize = 16;

	public PostAdapter(Context context, int layoutRes, ArrayList<PostItem> list) {
		this.context = context;
		this.layoutRes = layoutRes;
		this.list = list;
		inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.icon).showImageForEmptyUri(R.drawable.icon).showImageOnFail(R.drawable.icon).resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
	}

	public void settxtSize(int txtSize){
		this.txtSize = txtSize;
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
		String imageUrl = list.get(position).getPostImage();
		if(imageUrl != null && !imageUrl.isEmpty()){
			image.setVisibility(View.VISIBLE);
			imageLoader.displayImage(imageUrl, image, options);
		}
		else{
			image.setVisibility(View.GONE);
		}
		
		TextView content = (TextView) convertView.findViewById(R.id.post_content);
		content.setText(list.get(position).getPostContent());
		content.setTextSize(txtSize);
		
		if(list.get(position).getPostComments() != null){
			ExpandableListView comments = (ExpandableListView) convertView.findViewById(R.id.post_comments);
			
			ArrayList<ArrayList<CommentItem>> commentlist = new ArrayList<ArrayList<CommentItem>>();
			commentlist.add(list.get(position).getPostComments());
			final CommentAdapter adapter = new CommentAdapter(context, R.layout.comment, commentlist);
			comments.setAdapter(adapter);
			comments.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
				
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					int totalHeight = 0;
					int desiredWidth = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.UNSPECIFIED);
					View view = null;
					view = adapter.getGroupView(0, false, view, parent);
					view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, View.MeasureSpec.UNSPECIFIED));
					view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
					totalHeight += view.getMeasuredHeight();
					
					if((!parent.isGroupExpanded(groupPosition) && 0 == groupPosition) || (parent.isGroupExpanded(groupPosition) && 0 != groupPosition)){
						View listItem = null;
						for(int i = 0; i<adapter.getChildrenCount(0); i++){
							listItem = adapter.getChildView(0, i, false, listItem, parent);
							listItem.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, View.MeasureSpec.UNSPECIFIED));
							listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
							totalHeight += listItem.getMeasuredHeight();
						}
					}
					ViewGroup.LayoutParams params = parent.getLayoutParams();
					params.height = totalHeight + (parent.getDividerHeight() * adapter.getChildrenCount(0));
					parent.setLayoutParams(params);
					parent.requestLayout();
					
					return false;
				}
			});
		}
		
		final CheckBox anonymity = (CheckBox) convertView.findViewById(R.id.post_anonymity);
		final EditText newcomment = (EditText) convertView.findViewById(R.id.post_newcomment);
		Button newcommentfinish = (Button) convertView.findViewById(R.id.post_newcommentfinish);
		newcommentfinish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean res_anonymity = anonymity.isChecked();
				String comment = newcomment.getText().toString();
				newcomment.setText("");
				
				// 댓글을 서버에 전송
			}
		});
		
		
		return convertView;
	}
}