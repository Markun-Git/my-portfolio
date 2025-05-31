package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		UserDAO userDao = new UserDAO();
		Connection conn = null;
		boolean isDeleted = false;

		try {
			conn = userDao.getConnection(); // サーブレットで Connection を取得
			conn.setAutoCommit(false);

			// UserDAO の各メソッドに Connection を渡す
			userDao.updateDiariesFlagByUser(conn, loginUser.getUserId(), 0);
			userDao.updateCommentsFlagByUser(conn, loginUser.getUserId(), 0);
			if (userDao.updateUserFlag(conn, loginUser.getUserId(), 0)) {
				isDeleted = true;
				conn.commit();
				session.invalidate();
				request.setAttribute("message", "退会処理が完了しました。ご利用ありがとうございました。");
				request.getRequestDispatcher("index.jsp").forward(request, response);
			} else {
				conn.rollback();
				request.setAttribute("error", "退会処理に失敗しました。");
				request.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(request, response);
			}

		} catch (SQLException | ClassNotFoundException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			request.setAttribute("error", "退会処理中にエラーが発生しました。");
			request.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(request, response);

		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}