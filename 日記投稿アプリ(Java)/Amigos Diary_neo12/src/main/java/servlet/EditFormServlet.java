package servlet;

import java.io.IOException;

import dao.DiariesDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Mutter;

@WebServlet("/EditFormServlet")
public class EditFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		DiariesDAO dao = new DiariesDAO();
		Mutter diary = dao.findById(id);
		request.setAttribute("diary", diary);
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/diaryEdit.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}