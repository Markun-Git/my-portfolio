package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowDAO {
	private final String JDBC_URL = "jdbc:mysql://localhost/keg_db";
	private final String DB_USER = "keg_user";
	private final String DB_PASS = "keg_pass";

	// フォロー追加
	public boolean follow(int followerId, int followeeId) {
		String sql = "INSERT INTO follows (follower_id, followee_id) VALUES (?, ?)";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, followerId);
			ps.setInt(2, followeeId);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			// UNIQUE制約違反（すでにフォロー中）は例外で終了
			return false;
		}
	}

	// フォロー解除
	public boolean unfollow(int followerId, int followeeId) {
		String sql = "DELETE FROM follows WHERE follower_id = ? AND followee_id = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, followerId);
			ps.setInt(2, followeeId);
			int count = ps.executeUpdate();
			return count > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// フォローしているか判定
	public boolean isFollowing(int followerId, int followeeId) {
		String sql = "SELECT 1 FROM follows WHERE follower_id = ? AND followee_id = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, followerId);
			ps.setInt(2, followeeId);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// フォロー中ユーザーのIDリスト取得
	public List<Integer> getFolloweeIds(int followerId) {
		List<Integer> ids = new ArrayList<>();
		String sql = "SELECT followee_id FROM follows WHERE follower_id = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, followerId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("followee_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ids;
	}
}
