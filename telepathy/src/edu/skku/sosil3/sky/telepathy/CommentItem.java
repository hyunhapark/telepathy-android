package edu.skku.sosil3.sky.telepathy;

public class CommentItem {
	String c_user;
	String c_date;
	String c_content;
	
	public CommentItem(String commentUser, String commentDate,
			String commentContent) {
		super();
		c_user = commentUser;
		c_date = commentDate;
		c_content = commentContent;
	}

	public String getCommentUser() {
		return c_user;
	}

	public String getCommentDate() {
		return c_date;
	}

	public String getCommentContent() {
		return c_content;
	}
}