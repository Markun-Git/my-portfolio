package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.GetDiaryListLogic; // GetDiaryListLogicを使用
import model.Mutter;
import model.PaginationInfo; // 追加
import model.PostDiaryLogic;
import model.User;

@WebServlet("/Main")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 20 * 1024 * 1024)
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 1ページあたりの表示件数を定義
	private static final int RECORDS_PER_PAGE = 10; // ★10件表示

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		GetDiaryListLogic getDiaryListLogic = new GetDiaryListLogic(); // ロジッククラスをインスタンス化

		// 1. 総件数を取得
		int totalMutterCount = getDiaryListLogic.getTotalDiaryCount(); // ロジッククラスから総件数を取得

		// 2. 現在のページ番号を取得 (リクエストパラメータから)
		String pageParam = request.getParameter("page");
		int currentPage = 1; // デフォルトは1ページ目
		if (pageParam != null && pageParam.matches("\\d+")) {
			currentPage = Integer.parseInt(pageParam);
		}

		// 3. PaginationInfo オブジェクトを作成
		PaginationInfo paginationInfo = new PaginationInfo(currentPage, totalMutterCount, RECORDS_PER_PAGE);

		// 4. ページングに対応した日記リストを取得
		List<Mutter> mutterList = getDiaryListLogic.execute(paginationInfo); // ページング情報を渡して取得

		// 5. リクエストスコープにセット
		request.setAttribute("mutterList", mutterList);
		request.setAttribute("paginationInfo", paginationInfo); // ページング情報をJSPに渡す

		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String title = request.getParameter("title");
		String text = request.getParameter("text");

		if (title != null && title.length() != 0 && text != null && text.length() != 0) {
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute("loginUser");

			Part imagePart = request.getPart("image");
			byte[] imageData = null;
			if (imagePart != null && imagePart.getSize() > 0) {
				try (java.io.InputStream inputStream = imagePart.getInputStream();
						java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream()) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
					imageData = outputStream.toByteArray();
					System.out.println("読み込んだ画像データ長: " + imageData.length);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// ★削除: アイコン画像関連の処理
			/*
			Part iconPart = request.getPart("iconImage");
			byte[] iconImageData = null;
			if (iconPart != null && iconPart.getSize() > 0) {
				try (java.io.InputStream inputStream = iconPart.getInputStream();
						java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream()) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
					iconImageData = outputStream.toByteArray();
					System.out.println("読み込んだアイコンデータ長: " + iconImageData.length);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			*/
			// ★ここまで削除

			// MutterオブジェクトのコンストラクタからiconImageDataの引数を削除
			// もしくはnullを渡す形にする
			Mutter diary = new Mutter(loginUser.getUserName(), title, text, imageData);
			PostDiaryLogic postDiaryLogic = new PostDiaryLogic();
			postDiaryLogic.execute(diary);

			// POSTで投稿成功後も、ページネーション付きで再表示するためdoGetを呼び出す
			// または、sendRedirect("Main") で GET リクエストを発生させる (こっちが一般的)
			response.sendRedirect("Main");
			return;
		} else {
			request.setAttribute("errorMsg", "タイトルと本文は必須入力です");
		}

		// POSTでエラーがあった場合もページネーション付きで再表示
		// doGetのロジックを再利用
		doGet(request, response);
	}
}