package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import dao.FollowDAO;
import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet("/FollowListServlet")
public class FollowListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		FollowDAO followDao = new FollowDAO();
		List<Integer> followeeIds = followDao.getFolloweeIds(loginUser.getUserId());

		// フォロー中ユーザー情報リストを作成
		List<User> followeeUsers = new ArrayList<>();
		UserDAO userDao = new UserDAO();
		try (Connection conn = userDao.getConnection()) {
			for (Integer uid : followeeIds) {
				User user = userDao.findById(conn, uid);
				if (user != null) {
					followeeUsers.add(user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("followeeUsers", followeeUsers);
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/follow_list.jsp");
		dispatcher.forward(request, response);
	}
}
