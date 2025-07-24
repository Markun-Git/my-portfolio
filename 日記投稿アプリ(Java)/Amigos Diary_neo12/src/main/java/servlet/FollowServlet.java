package servlet;

import java.io.IOException;

import dao.FollowDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet("/FollowServlet")
public class FollowServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		int followerId = loginUser.getUserId();
		int followeeId = Integer.parseInt(request.getParameter("followeeId"));
		String action = request.getParameter("action");

		FollowDAO followDao = new FollowDAO();
		String followMsg = null;
		if ("follow".equals(action)) {
			followDao.follow(followerId, followeeId);
			followMsg = "お気に入りユーザーとして登録しました";
		} else if ("unfollow".equals(action)) {
			followDao.unfollow(followerId, followeeId);
			followMsg = "フォローを外しました";
		}
		// メッセージをセッションに一時保存
		session.setAttribute("followMsg", followMsg);

		// プロフィール画面へ戻る
		response.sendRedirect("ProfileServlet?userId=" + followeeId);
	}
}
