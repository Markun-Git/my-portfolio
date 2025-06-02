package servlet;

import java.io.IOException;
import java.util.List;

import dao.DiariesDAO;
import dao.LikeDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Comment;
import model.GetCommentListLogic;
import model.Mutter;
import model.User;

@WebServlet("/DiaryDetailServlet")
public class DiaryDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		DiariesDAO diariesDAO = new DiariesDAO();
		Mutter diary = diariesDAO.findById(id);

		if (diary == null) {
			request.setAttribute("errorMsg", "指定された日記は存在しません。");
			request.getRequestDispatcher("Main").forward(request, response);
			return;
		}

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		request.setAttribute("diary", diary);
		request.setAttribute("loginUser", loginUser);

		// コメントリストを取得
		GetCommentListLogic getCommentListLogic = new GetCommentListLogic();
		List<Comment> commentList = getCommentListLogic.execute(id);
		request.setAttribute("commentList", commentList);

		// いいね！関連の情報を取得
		LikeDAO likeDAO = new LikeDAO();
		int likeCount = likeDAO.countLikesByDiaryId(id);
		boolean isLiked = false;
		if (loginUser != null) {
			isLiked = likeDAO.isLikedByUser(id, loginUser.getUserId());
		}
		request.setAttribute("likeCount", likeCount);
		request.setAttribute("isLiked", isLiked);

		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/diaryDetail.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}