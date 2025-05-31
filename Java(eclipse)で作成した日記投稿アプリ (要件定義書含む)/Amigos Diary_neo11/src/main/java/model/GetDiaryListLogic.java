package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.DiariesDAO;

public class GetDiaryListLogic { // クラス名は変更しない
	// ページング情報を引数に取るexecuteメソッドをオーバーロード（または既存のexecuteを修正）
	public List<Mutter> execute(PaginationInfo paginationInfo) {
		DiariesDAO dao = new DiariesDAO();
		Connection conn = null;
		List<Mutter> diaryList = null;
		try {
			conn = dao.getConnection(); // DiariesDAOにgetConnectionを追加している前提
			diaryList = dao.findAllPaged(conn, paginationInfo); // ページング対応のメソッドを呼び出す
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return diaryList;
	}

	// 総件数を取得する新しいexecuteメソッド（または既存のexecuteを修正）
	// Mainサーブレットで利用するため、コネクションを外から渡せるようにするか、
	// DAOから直接総件数を取得するメソッドを呼び出す形にする
	public int getTotalDiaryCount() { // クラス名に合わせてDiariesCountではなくDiaryCount
		DiariesDAO dao = new DiariesDAO();
		Connection conn = null;
		int count = 0;
		try {
			conn = dao.getConnection(); // DiariesDAOにgetConnectionを追加している前提
			count = dao.getTotalMutterCount(conn); // DAOのメソッド名をMutterCountに合わせる
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}

	// 既存のexecute()メソッドも残す場合 (ページングなしで全件取得)
	// この場合、Mainサーブレットで利用されるのは上記のページングありのexecuteメソッドになる
	public List<Mutter> execute() {
		DiariesDAO dao = new DiariesDAO();
		List<Mutter> diaryList = dao.findAll(); // 既存のfindAll()を呼び出す
		return diaryList;
	}
}