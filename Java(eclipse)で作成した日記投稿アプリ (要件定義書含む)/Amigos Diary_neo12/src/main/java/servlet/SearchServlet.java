package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SearchResultDiary;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String keyword = request.getParameter("keyword");
		List<SearchResultDiary> searchResults = new ArrayList<>();

		if (keyword == null || keyword.trim().isEmpty()) {
			request.setAttribute("searchMessage", "キーワードを入力してください");
			request.getRequestDispatcher("Main").forward(request, response);
			return;
		}

		UserDAO dao = new UserDAO();
		Connection conn = null;

		try {
			conn = dao.getConnection();

			// diariesテーブルのみから日記情報を検索
			// USER_ID は diaries テーブルにないので、ここでは取得しない
			String sql = "SELECT ID, TITLE, TEXT, CREATED_AT, NAME, ICON_IMAGE FROM diaries WHERE FLAG = 1 AND (title LIKE ? OR text LIKE ?)";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, "%" + keyword + "%");
				pstmt.setString(2, "%" + keyword + "%");
				ResultSet rs = pstmt.executeQuery();

				// ユーザー情報取得用のPreparedStatementを準備
				String userSql = "SELECT USER_ID, ICON_IMAGE FROM users WHERE USER_NAME = ?";
				PreparedStatement userPstmt = conn.prepareStatement(userSql);

				while (rs.next()) {
					SearchResultDiary entry = new SearchResultDiary();
					entry.setDiaryId(rs.getInt("ID"));
					entry.setDiaryTitle(rs.getString("TITLE"));
					entry.setDiaryText(rs.getString("TEXT"));
					entry.setCreatedAt(rs.getTimestamp("CREATED_AT"));
					entry.setUserName(rs.getString("NAME")); // diariesテーブルからユーザー名を取得

					// diariesテーブルのICON_IMAGEは使わず、usersテーブルから取得する
					// String diaryUserName = rs.getString("NAME"); // diariesテーブルのNAMEを使う
					// byte[] diaryIconImage = rs.getBytes("ICON_IMAGE"); // diariesテーブルのICON_IMAGEは使わない

					// diariesテーブルのNAME（投稿者名）を使ってusersテーブルからユーザーIDとアイコン画像を取得
					String diaryUserName = entry.getUserName(); // diariesから取得したユーザー名
					if (diaryUserName != null && !diaryUserName.isEmpty()) {
						userPstmt.setString(1, diaryUserName);
						try (ResultSet userRs = userPstmt.executeQuery()) {
							if (userRs.next()) {
								entry.setUserId(userRs.getInt("USER_ID")); // usersテーブルからUSER_IDを取得
								entry.setUserIconImage(userRs.getBytes("ICON_IMAGE")); // usersテーブルからICON_IMAGEを取得
							}
						}
					}
					// もしusersテーブルに該当ユーザーがいなかった場合、userIdとuserIconImageはnull/0のままになる

					searchResults.add(entry);
				}
				userPstmt.close(); // PreparedStatementを閉じる
			}

			request.setAttribute("searchResults", searchResults);
			request.setAttribute("keyword", keyword);
			request.getRequestDispatcher("/WEB-INF/jsp/searchResult.jsp").forward(request, response);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			request.setAttribute("error", "検索処理中にエラーが発生しました。");
			request.getRequestDispatcher("Main").forward(request, response);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}