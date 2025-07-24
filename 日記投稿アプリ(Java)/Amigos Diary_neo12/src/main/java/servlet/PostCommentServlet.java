package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Comment;
import model.PostCommentLogic;
import model.User;

@WebServlet("/PostCommentServlet")
public class PostCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		int diaryId = Integer.parseInt(request.getParameter("diaryId"));
		String commentText = request.getParameter("commentText");

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser != null && commentText != null && !commentText.isEmpty()) {
			// Comment オブジェクトの作成時にアイコン画像を渡さない
			Comment comment = new Comment(diaryId, loginUser.getUserId(), loginUser.getUserName(), commentText);
			PostCommentLogic postCommentLogic = new PostCommentLogic();
			boolean isPosted = postCommentLogic.execute(comment);
			if (isPosted) {
				response.sendRedirect("DiaryDetailServlet?id=" + diaryId); // コメント投稿後に詳細画面を再表示
			} else {
				request.setAttribute("commentErrorMsg", "コメントの投稿に失敗しました。");
				request.getRequestDispatcher("DiaryDetailServlet?id=" + diaryId).forward(request, response);
			}
		} else {
			request.setAttribute("commentErrorMsg", "コメントが入力されていません。");
			request.getRequestDispatcher("DiaryDetailServlet?id=" + diaryId).forward(request, response);
		}
	}
}