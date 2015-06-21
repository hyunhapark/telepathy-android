package edu.skku.sosil3.sky.telepathy;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressWarnings("deprecation")
public class PostAdapter extends BaseAdapter {
   Context context;
   int layoutRes;
   ArrayList<PostItem> list;
   LayoutInflater inflater;
   ImageLoader imageLoader;
   DisplayImageOptions options;
   int txtSize = 16;
   
   static int upload_state = 0;
   static final int DEFAULT = 0;
   static final int SUCCESS = 1;
   static final int NETWORK_ERROR = 2;

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
      
      // 댓글 전송하기 버튼
      Button newcommentfinish = (Button) convertView.findViewById(R.id.post_newcommentfinish);
      final int fposition = position;
      newcommentfinish.setOnClickListener(new View.OnClickListener() {
         
         @Override
         public void onClick(View v) {
            
            final String p_id = ((PostItem) getItem(fposition)).getPostId();
            
            Boolean c_anonymity = anonymity.isChecked();
            final String c_user;
            if(c_anonymity) { // 익명 여부 판단
            	  c_user = "익명";
               } else {
                  c_user = TabActivity.id;
               }
            
            final String c_content = newcomment.getText().toString();
            newcomment.setText("");
            
            // 댓글을 서버에 전송
            new Thread() {
                  
                  public void run() {
                     upload_state = DEFAULT;

                     String uriString = Constants.URL_SERVER_HOST+Constants.URI_POST_COMMENT; // 업로드할 url
                     ArrayList<BasicNameValuePair> Parameter = new ArrayList<BasicNameValuePair>();
   
                     try {
                        URI uri = new URI(uriString);
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(uri);
   
                        Parameter.add(new BasicNameValuePair("p_id", p_id));
                        Parameter.add(new BasicNameValuePair("c_user", c_user));
                        Parameter.add(new BasicNameValuePair("c_content", c_content));
                        
                        Log.d("Upload", "Uploading data");
                        Log.d("Upload", "- "+p_id);
                        Log.d("Upload", "- "+c_user);
                        Log.d("Upload", "- "+c_content);
                        
                        httpPost.setEntity(new UrlEncodedFormEntity(Parameter,"UTF-8"));
   
                        // JSON data 얻어내기
                        HttpResponse httpResponse = httpclient.execute(httpPost);
                        String responseString = EntityUtils.toString(
                              httpResponse.getEntity(), HTTP.UTF_8);
                        Log.d("Upload", "responseString");
                        Log.d("Upload", responseString);
   
                        // JSON data에서 success 여부 알아내기
                        JSONObject jsonObject = new JSONObject(responseString);
                        String success = jsonObject.getString("success");
                        Log.d("Upload", "success : " + success);
   
                        upload_state = SUCCESS;
   
                     } catch (IOException e) {
                        upload_state = NETWORK_ERROR;
                     } catch (JSONException e) {
                        upload_state = NETWORK_ERROR;
                     } catch (Exception e) {
                        e.printStackTrace();
                     }
   
                     if (upload_state == SUCCESS) { // 업로드 성공
                        Log.d("Upload", "comment : SUCCESS");
            			((TabActivity)context).refresh_all();
                     } else if (upload_state == NETWORK_ERROR) { // 업로드 실패 - 네트워크 오류
                        Log.d("Upload", "comment : NETWORK_ERROR");
                     } else { // 업로드 실패 - 알 수 없는 오류
                        Log.d("Register", "comment : DEFAULT");
                     }
                  }
               }.start(); // thread end
         }
      });
      
      
      return convertView;
   }
}