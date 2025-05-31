package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Mutter;
import model.PaginationInfo;

public class DiariesDAO {
	private final String JDBC_URL = "jdbc:mysql://localhost/keg_db";
	private final String DB_USER = "keg_user";
	private final String DB_PASS = "keg_pass";

	// 新規追加: コネクションを取得するメソッド
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
	}

	// 新規追加: 全日記の総件数を取得するメソッド
	public int getTotalMutterCount(Connection conn) throws SQLException { // メソッド名を合わせる
		int count = 0;
		String sql = "SELECT COUNT(*) FROM DIARIES WHERE FLAG = 1";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		}
		return count;
	}

	// 新規追加: ページングに対応した日記リストを取得するメソッド
	public List<Mutter> findAllPaged(Connection conn, PaginationInfo paginationInfo) throws SQLException {
		List<Mutter> mutterList = new ArrayList<>();
		String sql = "SELECT d.ID, u.USER_ID, u.USER_NAME AS NAME, d.TITLE, d.TEXT, d.CREATED_AT, d.FLAG, d.IMAGE, u.ICON_IMAGE "
				+ "FROM DIARIES d "
				+ "JOIN users u ON d.NAME = u.USER_NAME "
				+ "WHERE d.FLAG = 1 "
				+ "ORDER BY d.ID DESC " // 新しい順にソート (ID DESCは通常CREATED_AT DESCに相当)
				+ "LIMIT ? OFFSET ?";

		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, paginationInfo.getRecordsPerPage());
			pStmt.setInt(2, paginationInfo.getStartRecord());

			ResultSet rs = pStmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				int userId = rs.getInt("USER_ID");
				String userName = rs.getString("NAME");
				String title = rs.getString("TITLE");
				String text = rs.getString("TEXT");
				Timestamp createdAt = rs.getTimestamp("CREATED_AT");
				byte[] image = rs.getBytes("IMAGE");
				byte[] iconImage = rs.getBytes("ICON_IMAGE");

				Mutter mutter = new Mutter(id, userId, userName, title, text, createdAt, image, iconImage);
				mutterList.add(mutter);
			}
		}
		return mutterList;
	}

	public List<Mutter> findAll() {
		List<Mutter> diaryList = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		String sql = "SELECT d.ID, u.USER_ID, d.NAME, d.TITLE, d.TEXT, d.CREATED_AT, d.FLAG, d.IMAGE, d.ICON_IMAGE " +
				"FROM DIARIES d JOIN users u ON d.NAME = u.USER_NAME " +
				"WHERE d.FLAG = 1 ORDER BY d.ID DESC";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				int userId = rs.getInt("USER_ID");
				String userName = rs.getString("NAME");
				String title = rs.getString("TITLE");
				String text = rs.getString("TEXT");
				Timestamp createdAt = rs.getTimestamp("CREATED_AT");
				byte[] image = rs.getBytes("IMAGE");
				byte[] iconImage = rs.getBytes("ICON_IMAGE");
				Mutter diary = new Mutter(id, userId, userName, title, text, createdAt, image, iconImage);
				diaryList.add(diary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return diaryList;
	}

	public void insert(Mutter diary) {
		// ★修正: ICON_IMAGEカラムにnullを挿入するように変更
		String sql = "INSERT INTO DIARIES (NAME, TITLE, TEXT, FLAG, IMAGE, ICON_IMAGE) VALUES (?, ?, ?, 1, ?, ?)";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, diary.getUserName());
			stmt.setString(2, diary.getTitle());
			stmt.setString(3, diary.getText());
			stmt.setBytes(4, diary.getImage());
			stmt.setNull(5, java.sql.Types.BLOB); // ★ここを修正: ICON_IMAGEにNULLを設定
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteDiaryById(int id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
			String sql = "UPDATE DIARIES SET FLAG = 0 WHERE ID = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateDiary(Mutter diary) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
			String sql = "UPDATE DIARIES SET TITLE = ?, TEXT = ?, IMAGE = ? WHERE ID = ? AND FLAG = 1";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, diary.getTitle());
			stmt.setString(2, diary.getText());
			stmt.setBytes(3, diary.getImage());
			stmt.setInt(4, diary.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Mutter findById(int id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Mutter diary = null;
		try {
			conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
			String sql = "SELECT ID, NAME, TITLE, TEXT, CREATED_AT, IMAGE, ICON_IMAGE FROM DIARIES WHERE ID = ? AND FLAG = 1";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String userName = rs.getString("NAME");
				String title = rs.getString("TITLE");
				String text = rs.getString("TEXT");
				Timestamp createdAt = rs.getTimestamp("CREATED_AT");
				byte[] image = rs.getBytes("IMAGE");
				byte[] iconImage = rs.getBytes("ICON_IMAGE");
				diary = new Mutter(id, userName, title, text, createdAt, image, iconImage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return diary;
	}

	public byte[] getImageById(int id) {
		String sql = "SELECT IMAGE FROM DIARIES WHERE ID = ? AND FLAG = 1";
		byte[] imageData = null;
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					imageData = rs.getBytes("IMAGE");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return imageData;
	}

	public byte[] getIconImageById(int id) {
		String sql = "SELECT ICON_IMAGE FROM DIARIES WHERE ID = ? AND FLAG = 1";
		byte[] iconImageData = null;
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					iconImageData = rs.getBytes("ICON_IMAGE");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return iconImageData;
	}

	public List<Mutter> findByUserId(int userId) {
		List<Mutter> diaryList = new ArrayList<>();
		String sql = "SELECT d.ID, u.USER_ID, u.USER_NAME AS NAME, d.TITLE, d.TEXT, d.CREATED_AT, d.FLAG, d.IMAGE, d.ICON_IMAGE "
				+ "FROM diaries d "
				+ "JOIN users u ON d.NAME = u.USER_NAME "
				+ "WHERE u.USER_ID = ? AND d.FLAG = 1 "
				+ "ORDER BY d.ID DESC";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, userId);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				Mutter m = new Mutter(
						rs.getInt("ID"),
						rs.getInt("USER_ID"),
						rs.getString("NAME"),
						rs.getString("TITLE"),
						rs.getString("TEXT"),
						rs.getTimestamp("CREATED_AT"),
						rs.getBytes("IMAGE"),
						rs.getBytes("ICON_IMAGE"));
				diaryList.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return diaryList;
	}
}