package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Comment;

public class CommentDAO {
	private final String JDBC_URL = "jdbc:mysql://localhost/keg_db";
	private final String DB_USER = "keg_user";
	private final String DB_PASS = "keg_pass";

	public List<Comment> findByDiaryId(int diaryId) {
		List<Comment> commentList = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = "SELECT COMMENT_ID, USER_ID, USER_NAME, COMMENT_TEXT, CREATED_AT " +
					"FROM comments WHERE DIARY_ID = ? AND FLAG = 1 ORDER BY CREATED_AT DESC";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, diaryId);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setCommentId(rs.getInt("COMMENT_ID"));
				comment.setUserId(rs.getInt("USER_ID"));
				comment.setUserName(rs.getString("USER_NAME"));
				comment.setCommentText(rs.getString("COMMENT_TEXT"));
				comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
				commentList.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return commentList;
	}

	public boolean insert(Comment comment) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = "INSERT INTO comments (DIARY_ID, USER_ID, USER_NAME, COMMENT_TEXT, FLAG) VALUES (?, ?, ?, ?, 1)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, comment.getDiaryId());
			pStmt.setInt(2, comment.getUserId());
			pStmt.setString(3, comment.getUserName());
			pStmt.setString(4, comment.getCommentText());
			int result = pStmt.executeUpdate();
			return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateFlag(int commentId, int flag) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = "UPDATE comments SET FLAG = ? WHERE COMMENT_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, flag);
			pStmt.setInt(2, commentId);
			int result = pStmt.executeUpdate();
			return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Comment findById(int commentId) {
		Comment comment = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = "SELECT COMMENT_ID, DIARY_ID, USER_ID, USER_NAME, COMMENT_TEXT FROM comments WHERE COMMENT_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, commentId);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				comment = new Comment();
				comment.setCommentId(rs.getInt("COMMENT_ID"));
				comment.setDiaryId(rs.getInt("DIARY_ID"));
				comment.setUserId(rs.getInt("USER_ID"));
				comment.setUserName(rs.getString("USER_NAME"));
				comment.setCommentText(rs.getString("COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return comment;
	}

	public boolean update(Comment comment) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = "UPDATE comments SET COMMENT_TEXT = ? WHERE COMMENT_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, comment.getCommentText());
			pStmt.setInt(2, comment.getCommentId());
			int result = pStmt.executeUpdate();
			return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}