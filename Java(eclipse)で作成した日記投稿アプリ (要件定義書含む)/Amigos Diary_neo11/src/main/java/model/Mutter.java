package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Mutter implements Serializable {
	private int id; // ID
	private int userId; // ★ ユーザーID追加
	private String userName; // ユーザー名
	private String text; // 日記本文
	private Timestamp createdAt; // 投稿日時
	private byte[] image; // メイン画像
	private String title; // 日記タイトル
	// これは一覧表示用なので残す
	private byte[] iconImage; // アイコン画像

	public Mutter() {
	}

	// ★ userId入りコンストラクタを追加（findAllやfindByUserId用） - 変更なし
	public Mutter(int id, int userId, String userName, String title, String text, Timestamp createdAt, byte[] image,
			byte[] iconImage) {
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.title = title;
		this.text = text;
		this.createdAt = createdAt;
		this.image = image;
		this.iconImage = iconImage;
	}

	// 既存（userIdを持たない従来型）- 変更なし
	public Mutter(int id, String userName, String title, String text, Timestamp createdAt, byte[] image,
			byte[] iconImage) {
		this.id = id;
		this.userName = userName;
		this.title = title;
		this.text = text;
		this.createdAt = createdAt;
		this.image = image;
		this.iconImage = iconImage;
	}

	// ★修正: 日記投稿時に使用するコンストラクタからiconImageの引数を削除
	public Mutter(String userName, String title, String text, byte[] image) {
		this.userName = userName;
		this.title = title;
		this.text = text;
		this.image = image;
		this.iconImage = null; // ここで明示的にnullを設定するか、フィールド自体を削除することも検討
	}

	// ゲッター
	public int getId() {
		return id;
	}

	public int getUserId() { // ★ 追加
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getText() {
		return text;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public byte[] getImage() {
		return image;
	}

	public String getTitle() {
		return title;
	}

	public byte[] getIconImage() {
		return iconImage;
	}

	// セッター
	public void setId(int id) {
		this.id = id;
	}

	public void setUserId(int userId) { // ★ 追加
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIconImage(byte[] iconImage) {
		this.iconImage = iconImage;
	}
}
