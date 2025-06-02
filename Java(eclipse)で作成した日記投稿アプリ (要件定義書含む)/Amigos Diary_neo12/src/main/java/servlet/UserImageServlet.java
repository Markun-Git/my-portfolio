package servlet;

import java.io.IOException;
import java.sql.Connection;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

@WebServlet("/UserImageServlet")
public class UserImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idStr = request.getParameter("id");
		if (idStr == null)
			return;

		int userId = Integer.parseInt(idStr);
		byte[] imageData = null;

		try (Connection conn = new UserDAO().getConnection()) {
			User user = new UserDAO().findById(conn, userId);
			if (user != null)
				imageData = user.getIconImage();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (imageData != null) {
			response.setContentType("image/png"); // 必要に応じて変更
			response.getOutputStream().write(imageData);
		}
	}
}
