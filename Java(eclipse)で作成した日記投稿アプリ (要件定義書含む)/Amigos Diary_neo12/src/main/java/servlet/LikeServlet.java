package servlet;

import java.io.IOException;

import dao.LikeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Like;
import model.User;

@WebServlet("/LikeServlet")
public class LikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		int diaryId = Integer.parseInt(request.getParameter("diaryId"));
		String action = request.getParameter("action");

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser != null) {
			LikeDAO likeDAO = new LikeDAO();
			if ("like".equals(action)) {
				Like like = new Like(diaryId, loginUser.getUserId());
				likeDAO.insertLike(like);
			} else if ("unlike".equals(action)) {
				likeDAO.deleteLike(diaryId, loginUser.getUserId());
			}
		}

		response.sendRedirect("DiaryDetailServlet?id=" + diaryId);
	}
}