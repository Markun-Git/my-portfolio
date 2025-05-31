package model;

import dao.CommentDAO;

public class DeleteCommentLogic {
	public boolean execute(int commentId) {
		CommentDAO dao = new CommentDAO();
		return dao.updateFlag(commentId, 0); // FLAG を 0 に更新
	}
}