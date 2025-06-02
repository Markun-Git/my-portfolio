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
import model.EditCommentLogic;
import model.User;

@WebServlet("/EditCommentServlet")
public class EditCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int commentId = Integer.parseInt(request.getParameter("commentId"));
		CommentDAO dao = new CommentDAO();
		Comment comment = dao.findById(commentId);
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser != null && comment != null && comment.getUserId() == loginUser.getUserId()) {
			request.setAttribute("editComment", comment);
			request.getRequestDispatcher("WEB-INF/jsp/editComment.jsp").forward(request, response);
		} else {
			response.sendRedirect("DiaryDetailServlet?id=" + request.getParameter("diaryId")); // 権限なし
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		int commentId = Integer.parseInt(request.getParameter("commentId"));
		int diaryId = Integer.parseInt(request.getParameter("diaryId"));
		String commentText = request.getParameter("commentText");

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		Comment comment = new Comment();
		comment.setCommentId(commentId);
		comment.setCommentText(commentText);

		EditCommentLogic editCommentLogic = new EditCommentLogic();
		boolean isEdited = editCommentLogic.execute(comment);

		if (isEdited) {
			response.sendRedirect("DiaryDetailServlet?id=" + diaryId);
		} else {
			request.setAttribute("editErrorMsg", "コメントの編集に失敗しました。");
			request.getRequestDispatcher("WEB-INF/jsp/editComment.jsp?commentId=" + commentId).forward(request,
					response);
		}
	}
}