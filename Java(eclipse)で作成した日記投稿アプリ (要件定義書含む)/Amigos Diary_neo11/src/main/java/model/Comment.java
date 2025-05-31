package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Comment implements Serializable {
    private int commentId;
    private int diaryId;
    private int userId;
    private String userName;
    private String commentText;
    private Timestamp createdAt;
    private int flag;

    public Comment() {}

    public Comment(int diaryId, int userId, String userName, String commentText) {
        this.diaryId = diaryId;
        this.userId = userId;
        this.userName = userName;
        this.commentText = commentText;
        this.flag = 1; // 初期値
    }

    // ゲッターとセッター
    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getDiaryId() { return diaryId; }
    public void setDiaryId(int diaryId) { this.diaryId = diaryId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getCommentText() { return commentText; }
    public void setCommentText(String commentText) { this.commentText = commentText; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getFlag() { return flag; }
    public void setFlag(int flag) { this.flag = flag; }
}