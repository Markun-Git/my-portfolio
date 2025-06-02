package servlet;

import java.io.IOException;

import dao.CommentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Comment;
import model.DeleteCommentLogic;
import model.User;

@WebServlet("/DeleteCommentServlet")
public class DeleteCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int commentId = Integer.parseInt(request.getParameter("commentId"));
		int diaryId = Integer.parseInt(request.getParameter("diaryId"));

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		CommentDAO dao = new CommentDAO();
		Comment comment = dao.findById(commentId);

		if (loginUser != null && comment != null && comment.getUserId() == loginUser.getUserId()) {
			DeleteCommentLogic deleteCommentLogic = new DeleteCommentLogic();
			boolean isDeleted = deleteCommentLogic.execute(commentId);
			if (isDeleted) {
				response.sendRedirect("DiaryDetailServlet?id=" + diaryId);
			} else {
				request.setAttribute("deleteErrorMsg", "コメントの削除に失敗しました。");
				request.getRequestDispatcher("DiaryDetailServlet?id=" + diaryId).forward(request, response);
			}
		} else {
			// ログインしていない、コメントが存在しない、または投稿者本人ではない場合
			response.sendRedirect("DiaryDetailServlet?id=" + diaryId); // 詳細画面へリダイレクト
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response); // 基本的にGETリクエストと同じ処理で削除を行う想定
	}
}