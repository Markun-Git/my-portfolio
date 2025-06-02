package model; // または dto;

import java.sql.Timestamp;

public class SearchResultDiary {
	// 日記に関する情報
	private int diaryId;
	private String diaryTitle;
	private String diaryText; // 本文全体、または抜粋

	// 投稿者（ユーザー）に関する情報
	private String userName;
	private int userId; // ★ここを String から int に変更
	private byte[] userIconImage; // アイコン画像データ（表示判定用）

	private Timestamp createdAt;

	// コンストラクタ
	public SearchResultDiary() {
	}

	// 全フィールドを初期化するコンストラクタ（任意）
	public SearchResultDiary(int diaryId, String diaryTitle, String diaryText,
			String userName, int userId, byte[] userIconImage, Timestamp createdAt) { // ★ここを String から int に変更
		this.diaryId = diaryId;
		this.diaryTitle = diaryTitle;
		this.diaryText = diaryText;
		this.userName = userName;
		this.userId = userId;
		this.userIconImage = userIconImage;
		this.createdAt = createdAt;
	}

	// --- GetterとSetter ---

	public int getDiaryId() {
		return diaryId;
	}

	public void setDiaryId(int diaryId) {
		this.diaryId = diaryId;
	}

	public String getDiaryTitle() {
		return diaryTitle;
	}

	public void setDiaryTitle(String diaryTitle) {
		this.diaryTitle = diaryTitle;
	}

	public String getDiaryText() {
		return diaryText;
	}

	public void setDiaryText(String diaryText) {
		this.diaryText = diaryText;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() { // ★ここを String から int に変更
		return userId;
	}

	public void setUserId(int userId) { // ★ここを String から int に変更
		this.userId = userId;
	}

	public byte[] getUserIconImage() {
		return userIconImage;
	}

	public void setUserIconImage(byte[] userIconImage) {
		this.userIconImage = userIconImage;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}