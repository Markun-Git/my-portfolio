package model;

import java.io.Serializable;

public class User implements Serializable {
	private int userId;
	private String userName;
	private String loginName;
	private String password;
	private int flag;
	private byte[] iconImage;
	private String profileText;

	public User() {
	}

	// 追加: RegisterServlet で使用するコンストラクタ
	public User(int userId, String userName, String loginName, String password, int flag, byte[] iconImage,
			String profileText) {
		this.userId = userId;
		this.userName = userName;
		this.loginName = loginName;
		this.password = password;
		this.flag = flag;
		this.iconImage = iconImage;
		this.profileText = profileText;
	}

	public User(String userName, String loginName, String password) {
		this.userName = userName;
		this.loginName = loginName;
		this.password = password;
		this.flag = 1;
		this.iconImage = null;
		this.profileText = null;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public byte[] getIconImage() {
		return iconImage;
	}

	public void setIconImage(byte[] iconImage) {
		this.iconImage = iconImage;
	}

	public String getProfileText() {
		return profileText;
	}

	public void setProfileText(String profileText) {
		this.profileText = profileText;
	}
}