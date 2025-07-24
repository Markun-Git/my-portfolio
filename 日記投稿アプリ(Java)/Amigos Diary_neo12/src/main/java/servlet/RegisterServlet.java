package servlet;

import java.io.IOException;
import java.sql.SQLException;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.User;

@WebServlet("/RegisterServlet")
@MultipartConfig // ★これを追加
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String userName = request.getParameter("userName");
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");
		String profileText = request.getParameter("profileText");

		byte[] iconImage = null;
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
			}
		}

		User newUser = new User(userName, loginName, password);
		newUser.setProfileText(profileText);
		newUser.setIconImage(iconImage);

		// ↓↓【ここを追加】↓↓
		UserDAO userDao = new UserDAO();
		java.sql.Connection conn = null;

		try {
			conn = userDao.getConnection();
			if (userDao.insertUser(conn, newUser)) {
				request.setAttribute("registerMsg", "登録が完了しました。ログイン画面からログインしてください。");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			} else {
				request.setAttribute("registerErrorMsg", "登録に失敗しました。入力内容を確認してください。");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			request.setAttribute("registerErrorMsg", "登録処理中にエラーが発生しました。");
			request.getRequestDispatcher("register.jsp").forward(request, response);
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