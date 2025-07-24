package model;

import java.util.List;

import dao.CommentDAO;

public class GetCommentListLogic {
	public List<Comment> execute(int diaryId) {
		CommentDAO dao = new CommentDAO();
		return dao.findByDiaryId(diaryId);
	}
}