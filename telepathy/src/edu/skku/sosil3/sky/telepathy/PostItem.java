package edu.skku.sosil3.sky.telepathy;

import java.util.ArrayList;

public class PostItem {
	int p_id;
	String p_user;
	String p_date;
	String p_image;
	String p_content;
	ArrayList<CommentItem> p_comments;
	double p_lati;
	double p_long;
	
	public PostItem(int postId, String postUser, String postDate,
			String postImage, String postContent,
			ArrayList<CommentItem> postComments, double postLati,
			double postLong) {
		super();
		p_id = postId;
		p_user = postUser;
		p_date = postDate;
		p_image = postImage;
		p_content = postContent;
		p_comments = postComments;
		p_lati = postLati;
		p_long = postLong;
	}
	
	public int getPostId() {
		return p_id;
	}
	
	public String getPostUser() {
		return p_user;
	}
	
	public String getPostDate() {
		return p_date;
	}
	
	public String getPostImage() {
		return p_image;
	}
	
	public String getPostContent() {
		return p_content;
	}
	
	public ArrayList<CommentItem> getPostComments() {
		return p_comments;
	}
	
	public double getPostLati() {
		return p_lati;
	}
	
	public double getPostLong() {
		return p_long;
	}
	
	public double getDistance(double Lati, double Long){
		// 거리 최소값 구함 공식은 http://stackoverflow.com/questions/3715521/how-can-i-calculate-the-distance-between-two-gps-points-in-java
		
		double temp0 = Math.PI / 180;
		double temp1 = (p_long - Long) * temp0;
		double temp2 = (p_lati - Lati) * temp0;
		double a = Math.pow(Math.sin(temp2 / 2.0), 2) + Math.cos(Lati * temp0) * Math.cos(p_lati * temp0) * Math.pow(Math.sin(temp1 / 2.0), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return 6367 * c;
	}
}