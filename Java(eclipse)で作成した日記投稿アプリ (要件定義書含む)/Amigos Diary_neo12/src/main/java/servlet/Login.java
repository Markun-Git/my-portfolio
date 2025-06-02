package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String loginName = request.getParameter("loginName");
		String pass = request.getParameter("pass");

		UserDAO dao = new UserDAO();
		Connection conn = null;

		try {
			conn = dao.getConnection();
			User registeredUser = dao.findUserByLoginName(conn, loginName);

			if (registeredUser == null) {
				request.setAttribute("loginErrorDetail", "ユーザーが存在しません。");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginResult.jsp"); 
				dispatcher.forward(request, response);
			} else if (!registeredUser.getPassword().equals(pass)) {
				request.setAttribute("loginErrorDetail", "パスワードが間違っています。");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginResult.jsp"); 
				dispatcher.forward(request, response);
			} else if (registeredUser.getFlag() == 0) {
				request.setAttribute("loginErrorDetail", "このアカウントは無効です。");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginResult.jsp");
				dispatcher.forward(request, response);
			} else {
				HttpSession session = request.getSession();
				session.setAttribute("loginUser", registeredUser);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginResult.jsp");
				dispatcher.forward(request, response);
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			request.setAttribute("loginErrorDetail", "ログイン処理中にエラーが発生しました。");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginResult.jsp"); // フォワード先は loginResult.jsp のまま
			dispatcher.forward(request, response);
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