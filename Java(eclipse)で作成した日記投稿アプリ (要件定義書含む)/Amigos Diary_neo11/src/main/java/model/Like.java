package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Like implements Serializable {
	private int likeId;
	private int diaryId;
	private int userId;
	private Timestamp likedAt;

	public Like() {
	}

	public Like(int diaryId, int userId) {
		this.diaryId = diaryId;
		this.userId = userId;
	}

	public int getLikeId() {
		return likeId;
	}

	public void setLikeId(int likeId) {
		this.likeId = likeId;
	}

	public int getDiaryId() {
		return diaryId;
	}

	public void setDiaryId(int diaryId) {
		this.diaryId = diaryId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getLikedAt() {
		return likedAt;
	}

	public void setLikedAt(Timestamp likedAt) {
		this.likedAt = likedAt;
	}
}