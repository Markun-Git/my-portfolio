package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Like;

public class LikeDAO {
	private final String JDBC_URL = "jdbc:mysql://localhost/keg_db";
	private final String DB_USER = "keg_user";
	private final String DB_PASS = "keg_pass";

	private Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
	}

	public boolean insertLike(Like like) {
		try (Connection conn = getConnection();
				PreparedStatement pStmt = conn.prepareStatement(
						"INSERT INTO likes (diary_id, user_id) VALUES (?, ?)")) {
			pStmt.setInt(1, like.getDiaryId());
			pStmt.setInt(2, like.getUserId());
			int result = pStmt.executeUpdate();
			return result == 1;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteLike(int diaryId, int userId) {
		try (Connection conn = getConnection();
				PreparedStatement pStmt = conn.prepareStatement(
						"DELETE FROM likes WHERE diary_id = ? AND user_id = ?")) {
			pStmt.setInt(1, diaryId);
			pStmt.setInt(2, userId);
			int result = pStmt.executeUpdate();
			return result == 1;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int countLikesByDiaryId(int diaryId) {
		int count = 0;
		try (Connection conn = getConnection();
				PreparedStatement pStmt = conn.prepareStatement(
						"SELECT COUNT(*) FROM likes WHERE diary_id = ?")) {
			pStmt.setInt(1, diaryId);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return count;
	}

	public boolean isLikedByUser(int diaryId, int userId) {
		try (Connection conn = getConnection();
				PreparedStatement pStmt = conn.prepareStatement(
						"SELECT COUNT(*) FROM likes WHERE diary_id = ? AND user_id = ?")) {
			pStmt.setInt(1, diaryId);
			pStmt.setInt(2, userId);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}