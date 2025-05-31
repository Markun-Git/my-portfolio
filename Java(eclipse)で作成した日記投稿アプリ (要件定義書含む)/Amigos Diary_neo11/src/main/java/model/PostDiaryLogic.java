package model;

import dao.DiariesDAO;

public class PostDiaryLogic {
	public void execute(Mutter diary) {
		DiariesDAO dao = new DiariesDAO();
		dao.insert(diary);
	}
}