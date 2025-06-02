package model;

import java.sql.Connection;
import java.sql.SQLException;

import dao.UserDAO;

public class LoginLogic {
	public int execute(User user) {
		UserDAO dao = new UserDAO();
		Connection conn = null;
		int loginResult = 0;

		try {
			conn = dao.getConnection();
			User registeredUser = dao.findUserByLoginName(conn, user.getLoginName()); // Connection を渡す

			if (registeredUser == null) {
				loginResult = 0; // ユーザーが存在しない
			} else if (registeredUser.getPassword().equals(user.getPassword()) && registeredUser.getFlag() == 1) {
				user.setUserId(registeredUser.getUserId());
				user.setUserName(registeredUser.getUserName()); // ログイン成功後は USER_NAME をセット
				user.setLoginName(registeredUser.getLoginName());
				user.setPassword(registeredUser.getPassword());
				user.setFlag(registeredUser.getFlag());
				loginResult = 1; // ログイン成功
			} else {
				loginResult = -1; // パスワードが違う
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			loginResult = -1; // エラーが発生した場合もログイン失敗とする
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return loginResult;
	}
}