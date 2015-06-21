package edu.skku.sosil3.sky.telepathy;

public class CommentItem {
	String c_user;
	String c_date;
	String c_content;
	

	public CommentItem(String c_user, String c_date, String c_content) {
		super();
		this.c_user = c_user;
		this.c_date = c_date;
		this.c_content = c_content;
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