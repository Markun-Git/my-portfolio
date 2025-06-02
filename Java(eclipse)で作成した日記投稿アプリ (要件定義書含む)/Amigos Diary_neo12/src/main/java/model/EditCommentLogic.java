package model;

import dao.CommentDAO;

public class EditCommentLogic {
	public boolean execute(Comment comment) {
		CommentDAO dao = new CommentDAO();
		return dao.update(comment);
	}
}