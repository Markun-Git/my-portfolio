package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDAO {
	private final String JDBC_URL = "jdbc:mysql://localhost/keg_db";
	private final String DB_USER = "keg_user";
	private final String DB_PASS = "keg_pass";

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
	}

	public User findUserByLoginName(Connection conn, String loginName) throws SQLException {
		User user = null;
		try (PreparedStatement pStmt = conn.prepareStatement(
				"SELECT USER_ID, USER_NAME, LOGIN_NAME, PASSWORD, FLAG, ICON_IMAGE, PROFILE_TEXT FROM users WHERE LOGIN_NAME = ? AND FLAG = 1")) {
			pStmt.setString(1, loginName);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("USER_ID"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setLoginName(rs.getString("LOGIN_NAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setFlag(rs.getInt("FLAG"));
				user.setIconImage(rs.getBytes("ICON_IMAGE")); // 追加
				user.setProfileText(rs.getString("PROFILE_TEXT")); // 追加
			}
		}
		return user;
	}

	public boolean insertUser(Connection conn, User user) throws SQLException {
		try (PreparedStatement pStmt = conn.prepareStatement(
				"INSERT INTO users (USER_NAME, LOGIN_NAME, PASSWORD, ICON_IMAGE, PROFILE_TEXT) VALUES (?, ?, ?, ?, ?)")) {
			pStmt.setString(1, user.getUserName());
			pStmt.setString(2, user.getLoginName());
			pStmt.setString(3, user.getPassword());
			pStmt.setBytes(4, user.getIconImage()); // 追加
			pStmt.setString(5, user.getProfileText()); // 追加
			return pStmt.executeUpdate() == 1;
		}
	}

	public User findById(Connection conn, int userId) throws SQLException {
		User user = null;
		try (PreparedStatement pStmt = conn.prepareStatement(
				"SELECT USER_ID, USER_NAME, LOGIN_NAME, PASSWORD, FLAG, ICON_IMAGE, PROFILE_TEXT FROM users WHERE USER_ID = ? AND FLAG = 1")) {
			pStmt.setInt(1, userId);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("USER_ID"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setLoginName(rs.getString("LOGIN_NAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setFlag(rs.getInt("FLAG"));
				user.setIconImage(rs.getBytes("ICON_IMAGE")); // 追加
				user.setProfileText(rs.getString("PROFILE_TEXT"));// 追加
			}
		}
		return user;
	}

	// Connection を引数として受け取るように変更
	public boolean updateUserFlag(Connection conn, int userId, int flagValue) throws SQLException {
		try (PreparedStatement pStmt = conn.prepareStatement("UPDATE users SET FLAG = ? WHERE USER_ID = ?")) {
			pStmt.setInt(1, flagValue);
			pStmt.setInt(2, userId);
			return pStmt.executeUpdate() > 0;
		}
	}

	public boolean updateDiariesFlagByUser(Connection conn, int userId, int flagValue) throws SQLException {
		String userNameToDelete = null;
		User userToDelete = findById(conn, userId);
		if (userToDelete != null) {
			userNameToDelete = userToDelete.getUserName();
		}

		if (userNameToDelete != null) {
			try (PreparedStatement pStmt = conn.prepareStatement("UPDATE diaries SET FLAG = ? WHERE NAME = ?")) {
				pStmt.setInt(1, flagValue);
				pStmt.setString(2, userNameToDelete);
				return pStmt.executeUpdate() > 0;
			}
		}
		return false; // ユーザーが見つからない場合は false を返す
	}

	// Connection を引数として受け取るように変更
	public boolean updateCommentsFlagByUser(Connection conn, int userId, int flagValue) throws SQLException {
		try (PreparedStatement pStmt = conn.prepareStatement("UPDATE comments SET FLAG = ? WHERE USER_ID = ?")) {
			pStmt.setInt(1, flagValue);
			pStmt.setInt(2, userId);
			return pStmt.executeUpdate() > 0;
		}
	}

	// ★ updateUserProfile メソッドを拡張
	public boolean updateUserProfile(Connection conn, int userId, String userName, String loginName, String password,
			byte[] iconImage, String profileText)
			throws SQLException {
		String sql = "UPDATE users SET USER_NAME = ?, LOGIN_NAME = ?, PASSWORD = ?, ICON_IMAGE = ?, PROFILE_TEXT = ? WHERE USER_ID = ?";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setString(1, userName);
			pStmt.setString(2, loginName);
			pStmt.setString(3, password);
			pStmt.setBytes(4, iconImage);
			pStmt.setString(5, profileText);
			pStmt.setInt(6, userId);
			return pStmt.executeUpdate() > 0;
		}
	}

	// ★ loginName が既に存在するかチェックするメソッドを追加
	public boolean isLoginNameExists(Connection conn, String loginName, int currentUserId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE LOGIN_NAME = ? AND USER_ID <> ? AND FLAG = 1";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setString(1, loginName);
			pStmt.setInt(2, currentUserId);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		}
		return false;
	}

	// ★追加: ユーザーIDからアイコン画像を取得するメソッド
	public byte[] getIconImageByUserId(Connection conn, int userId) throws SQLException {
		String sql = "SELECT ICON_IMAGE FROM users WHERE USER_ID = ?"; // usersテーブルから取得
		byte[] iconImageData = null;
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, userId);
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					iconImageData = rs.getBytes("ICON_IMAGE");
				}
			}
		}
		return iconImageData;
	}
}