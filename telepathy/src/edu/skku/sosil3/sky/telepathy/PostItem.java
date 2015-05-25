package edu.skku.sosil3.sky.telepathy;

import java.util.ArrayList;

public class PostItem {
	int PostId;
	String PostUser;
	String PostDate;
	String PostImage;
	String PostContent;
	ArrayList<CommentItem> PostComments;
	double PostLati;
	double PostLong;
	
	public PostItem(int postId, String postUser, String postDate,
			String postImage, String postContent,
			ArrayList<CommentItem> postComments, double postLati,
			double postLong) {
		super();
		PostId = postId;
		PostUser = postUser;
		PostDate = postDate;
		PostImage = postImage;
		PostContent = postContent;
		PostComments = postComments;
		PostLati = postLati;
		PostLong = postLong;
	}
	
	public int getPostId() {
		return PostId;
	}
	
	public String getPostUser() {
		return PostUser;
	}
	
	public String getPostDate() {
		return PostDate;
	}
	
	public String getPostImage() {
		return PostImage;
	}
	
	public String getPostContent() {
		return PostContent;
	}
	
	public ArrayList<CommentItem> getPostComments() {
		return PostComments;
	}
	
	public double getPostLati() {
		return PostLati;
	}
	
	public double getPostLong() {
		return PostLong;
	}
	
	public double getDistance(double Lati, double Long){
		// 거리 최소값 구함 공식은 http://stackoverflow.com/questions/3715521/how-can-i-calculate-the-distance-between-two-gps-points-in-java
		
		double temp0 = Math.PI / 180;
		double temp1 = (PostLong - Long) * temp0;
		double temp2 = (PostLati - Lati) * temp0;
		double a = Math.pow(Math.sin(temp2 / 2.0), 2) + Math.cos(Lati * temp0) * Math.cos(PostLati * temp0) * Math.pow(Math.sin(temp1 / 2.0), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return 6367 * c;
	}
}