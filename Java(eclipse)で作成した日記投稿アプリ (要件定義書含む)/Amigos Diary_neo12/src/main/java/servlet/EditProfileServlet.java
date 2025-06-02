package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException; // SQLException を import に追加

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.User;

@WebServlet("/EditProfileServlet")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class EditProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L; // serialVersionUID が定義されていない場合、追加すると良いでしょう。

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ログインユーザー情報をJSPに渡す
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		request.getRequestDispatcher("WEB-INF/jsp/edit_profile.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		String userName = request.getParameter("userName");
		String loginName = request.getParameter("loginName"); // ★追加：ログインネームを取得
		String newPassword = request.getParameter("newPassword"); // ★追加：新しいパスワードを取得
		String confirmPassword = request.getParameter("confirmPassword"); // ★追加：パスワード確認を取得
		String profileText = request.getParameter("profileText");

		// アイコン画像処理
		byte[] iconImage = loginUser.getIconImage(); // デフォルトは現在のアイコン画像
		Part iconPart = request.getPart("iconImage");
		if (iconPart != null && iconPart.getSize() > 0) {
			try (java.io.InputStream inputStream = iconPart.getInputStream();
					java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream()) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				iconImage = outputStream.toByteArray();
			} catch (IOException e) { // IOException の catch を追加
				e.printStackTrace();
				request.setAttribute("editError", "アイコン画像のアップロード中にエラーが発生しました。");
				request.getRequestDispatcher("WEB-INF/jsp/edit_profile.jsp").forward(request, response);
				return;
			}
		}

		UserDAO userDao = new UserDAO();
		Connection conn = null; // Connection オブジェクトを try-catch-finally ブロックの外で宣言
		// String hashedPassword = loginUser.getPassword(); // デフォルトは現在のパスワードを保持
		boolean passwordChanged = false; // ★追加: パスワードが変更されるかどうかを示すフラグ

		try {
			conn = userDao.getConnection();
			conn.setAutoCommit(false); // ★追加：トランザクション開始

			// ★追加：ログインネームの重複チェック
			// ログインネームが変更された場合のみチェック
			if (!loginName.equals(loginUser.getLoginName())) {
				if (userDao.isLoginNameExists(conn, loginName, loginUser.getUserId())) {
					request.setAttribute("editError", "このログインネームは既に使用されています。");
					request.getRequestDispatcher("WEB-INF/jsp/edit_profile.jsp").forward(request, response);
					return; // 重複がある場合はここで処理を終了
				}
			}

			// ★追加：パスワードの更新処理
			if (newPassword != null && !newPassword.isEmpty()) { // 新しいパスワードが入力されている場合のみ
				if (!newPassword.equals(confirmPassword)) {
					request.setAttribute("editError", "新しいパスワードと確認用パスワードが一致しません。");
					request.getRequestDispatcher("WEB-INF/jsp/edit_profile.jsp").forward(request, response);
					return; // パスワード不一致の場合はここで処理を終了
				}
				// TODO: 実際のアプリケーションではパスワードのハッシュ化を実装すること
				// 現状は平文のままですが、セキュリティのためにハッシュ化を強く推奨します。
				// hashedPassword = newPassword; // 新しいパスワードをセット
				passwordChanged = true; // パスワードが変更されることをマーク
			}

			// ユーザー情報を更新
			// パスワードの更新は passwordChanged フラグによって制御する
			boolean result;
			if (passwordChanged) {
				// 新しいパスワードが入力された場合、パスワードも更新対象に含める
				result = userDao.updateUserProfile(conn, loginUser.getUserId(), userName, loginName, newPassword,
						iconImage, profileText);
			} else {
				// パスワードが入力されなかった場合、既存のパスワードをそのまま渡す
				// もしくは、パスワード以外のカラムのみを更新する別のDAOメソッドを用意する方が良い設計
				// 現状の updateUserProfile は全てのカラムを更新するので、既存パスワードを渡す
				result = userDao.updateUserProfile(conn, loginUser.getUserId(), userName, loginName,
						loginUser.getPassword(), iconImage, profileText);
			}

			if (result) {
				// セッションのユーザー情報も更新
				loginUser.setUserName(userName);
				loginUser.setLoginName(loginName);
				// パスワードが変更された場合のみセッションのパスワードも更新
				if (passwordChanged) {
					loginUser.setPassword(newPassword);
				}
				loginUser.setProfileText(profileText);
				loginUser.setIconImage(iconImage);
				session.setAttribute("loginUser", loginUser);

				conn.commit();
				request.setAttribute("editMsg", "会員情報を更新しました");
			} else {
				conn.rollback();
				request.setAttribute("editError", "更新に失敗しました");
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException rollbackE) {
					rollbackE.printStackTrace();
				}
			}
			request.setAttribute("editError", "エラーが発生しました");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		request.getRequestDispatcher("WEB-INF/jsp/edit_profile.jsp").forward(request, response);
	}
}