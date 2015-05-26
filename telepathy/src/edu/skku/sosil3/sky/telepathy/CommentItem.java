package edu.skku.sosil3.sky.telepathy;

public class CommentItem {
	String CommentUser;
	String CommentDate;
	String CommentContent;
	
	public CommentItem(String commentUser, String commentDate,
			String commentContent) {
		super();
		CommentUser = commentUser;
		CommentDate = commentDate;
		CommentContent = commentContent;
	}

	public String getCommentUser() {
		return CommentUser;
	}

	public String getCommentDate() {
		return CommentDate;
	}

	public String getCommentContent() {
		return CommentContent;
	}
}