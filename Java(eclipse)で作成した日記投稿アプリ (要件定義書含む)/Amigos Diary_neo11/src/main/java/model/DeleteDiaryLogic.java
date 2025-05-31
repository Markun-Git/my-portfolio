package model;

import dao.DiariesDAO;

public class DeleteDiaryLogic {
	public void execute(int id) {
		System.out.println("DeleteDiaryLogic: 削除処理を開始します(ID: " + id + ")");
		DiariesDAO dao = new DiariesDAO();
		dao.deleteDiaryById(id);
		System.out.println("DeleteDiaryLogic: 削除処理を実行しました");
	}
}