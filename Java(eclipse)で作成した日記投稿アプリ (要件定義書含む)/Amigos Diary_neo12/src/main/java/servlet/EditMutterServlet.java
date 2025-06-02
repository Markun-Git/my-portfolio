package servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dao.DiariesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Mutter;

@WebServlet("/EditMutterServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 20 * 1024 * 1024)
public class EditMutterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		int id = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String text = request.getParameter("text");

		Part imagePart = request.getPart("image");
		byte[] imageData = null;
		if (imagePart != null && imagePart.getSize() > 0) {
			try (InputStream is = imagePart.getInputStream();
					ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
				byte[] data = new byte[1024];
				int nRead;
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				imageData = buffer.toByteArray();
			}
		} else {
			// 新しい画像が選択されなかった場合は、既存の画像データを取得する
			DiariesDAO dao = new DiariesDAO();
			Mutter existingDiary = dao.findById(id);
			if (existingDiary != null) {
				imageData = existingDiary.getImage();
			}
		}

		Mutter diary = new Mutter(id, null, title, text, null, imageData, null); // userName, createdAt, iconImage は更新しない

		DiariesDAO dao = new DiariesDAO();
		dao.updateDiary(diary);

		response.sendRedirect("DiaryDetailServlet?id=" + id);
	}
}