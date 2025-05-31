package model;

import dao.CommentDAO;

public class PostCommentLogic {
	public boolean execute(Comment comment) {
		CommentDAO dao = new CommentDAO();
		return dao.insert(comment);
	}
}