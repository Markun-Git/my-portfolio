package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import dao.DiariesDAO;
import dao.FollowDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Mutter;
import model.User;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		// パラメータ取得
		String userIdStr = request.getParameter("userId");
		User viewUser;
		boolean isSelf;
		boolean isFollowing = false;

		if (userIdStr == null || userIdStr.isEmpty() || Integer.parseInt(userIdStr) == loginUser.getUserId()) {
			viewUser = loginUser;
			isSelf = true;
		} else {
			int userId = Integer.parseInt(userIdStr);
			UserDAO userDao = new UserDAO();
			try (Connection conn = userDao.getConnection()) {
				User dbUser = userDao.findById(conn, userId);
				if (dbUser != null) {
					viewUser = dbUser;
					isSelf = false;
					// フォロー状態
					FollowDAO followDao = new FollowDAO();
					isFollowing = followDao.isFollowing(loginUser.getUserId(), userId);
				} else {
					// ユーザーがいない場合はTOPへ
					response.sendRedirect("Main");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("Main");
				return;
			}
		}

		List<Mutter> mutterList = new DiariesDAO().findByUserId(viewUser.getUserId());
		request.setAttribute("viewUser", viewUser);
		request.setAttribute("isSelf", isSelf);
		request.setAttribute("isFollowing", isFollowing);
		request.setAttribute("mutterList", mutterList);

		String followMsg = (String) session.getAttribute("followMsg");
		if (followMsg != null) {
			request.setAttribute("followMsg", followMsg);
			session.removeAttribute("followMsg");
		}

		request.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(request, response);
	}
}