--ログイン情報テーブルを追加するSQL

CREATE TABLE USERS (
    USER_ID INT AUTO_INCREMENT PRIMARY KEY,
    USER_NAME VARCHAR(20) UNIQUE NOT NULL,
    LOGIN_NAME VARCHAR(20) UNIQUE NOT NULL,
    PASSWORD VARCHAR(10) NOT NULL
);


--会員退会機能を追加するSQL(usersテーブルにFLAGカラムを追加する)

ALTER TABLE users ADD COLUMN FLAG INT DEFAULT 1;


--日記にコメント投稿機能を追加するSQL

CREATE TABLE comments (
    COMMENT_ID INT AUTO_INCREMENT PRIMARY KEY,
    DIARY_ID INT NOT NULL,
    USER_ID INT NOT NULL,
    USER_NAME VARCHAR(50) NOT NULL,
    COMMENT_TEXT TEXT NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FLAG INT NOT NULL DEFAULT 1,
    FOREIGN KEY (DIARY_ID) REFERENCES diaries(ID),
    FOREIGN KEY (USER_ID) REFERENCES users(USER_ID)
);


--いいね！機能を追加するSQL

CREATE TABLE likes (
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    diary_id INT NOT NULL,
    user_id INT NOT NULL,
    liked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (diary_id) REFERENCES diaries(ID),
    FOREIGN KEY (user_id) REFERENCES users(USER_ID),
    UNIQUE KEY unique_like (diary_id, user_id)
);


-- usersテーブル拡張

ALTER TABLE users ADD COLUMN ICON_IMAGE MEDIUMBLOB;
ALTER TABLE users ADD COLUMN PROFILE_TEXT TEXT;


-- followsテーブル新規作成

CREATE TABLE follows (
    follower_id INT NOT NULL,
    followee_id INT NOT NULL,
    PRIMARY KEY (follower_id, followee_id),
    FOREIGN KEY (follower_id) REFERENCES users(USER_ID),
    FOREIGN KEY (followee_id) REFERENCES users(USER_ID)
);
