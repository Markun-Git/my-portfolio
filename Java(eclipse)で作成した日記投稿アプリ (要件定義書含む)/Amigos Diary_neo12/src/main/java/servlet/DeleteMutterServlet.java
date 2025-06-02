package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DeleteDiaryLogic;

@WebServlet("/DeleteMutterServlet") // サーブレット名はそのままに
public class DeleteMutterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("DeleteMutterServlet が呼び出されました");

		String idStr = request.getParameter("id");
		int id = Integer.parseInt(idStr);

		DeleteDiaryLogic logic = new DeleteDiaryLogic();
		logic.execute(id);

		response.sendRedirect("Main"); // 一覧表示サーブレット名は /Main のまま
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}