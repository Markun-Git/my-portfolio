package servlet;

import java.io.IOException;
import java.sql.Connection; // Connectionをインポート
import java.sql.SQLException; // SQLExceptionをインポート

import dao.DiariesDAO;
import dao.UserDAO; // UserDAOをインポート
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String type = request.getParameter("type");

		byte[] imageData = null;
		Connection conn = null; // コネクション変数を定義

		try {
			if ("icon".equals(type)) {
				UserDAO userDAO = new UserDAO(); // UserDAOをインスタンス化
				conn = userDAO.getConnection(); // UserDAOからコネクションを取得
				imageData = userDAO.getIconImageByUserId(conn, id); // UserDAOからユーザーIDでアイコンを取得
			} else { // "main" または type が指定されていない場合 (日記のメイン画像)
				DiariesDAO diariesDAO = new DiariesDAO(); // DiariesDAOをインスタンス化
				conn = diariesDAO.getConnection(); // DiariesDAOからコネクションを取得
				imageData = diariesDAO.getImageById(id); // DiariesDAOから日記IDでメイン画像を取得
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			// エラー処理（例: デフォルト画像を表示するなど）
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500エラーを返す
			return;
		} finally {
			if (conn != null) {
				try {
					conn.close(); // コネクションを閉じる
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		if (imageData != null) {
			response.setContentType("image/jpeg"); // 必要に応じて Content-Type を変更
			response.getOutputStream().write(imageData);
		} else {
			// 画像が見つからない場合、404エラーを返すか、デフォルト画像を表示する
			response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}