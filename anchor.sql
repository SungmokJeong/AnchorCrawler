CREATE DATABASE test;
USE test;

CREATE TABLE `user` (
	`user_seq`	bigint	NOT NULL,
	`created_at`	datetime(6)	NOT NULL,
	`email`	varchar(512)	NULL,
	`email_verified_yn`	varchar(1)	NULL,
	`password`	varchar(128)	NULL,
	`user_id`	varchar(100)	NULL,
	`username`	varchar(32)	NULL,
	`profile_image_url`	varchar(512)	NULL,
	`role_type`	varchar(20)	NULL,
	`provider_type`	varchar(20)	NULL
);

CREATE TABLE `series` (
	`series_id`	int	NOT NULL,
	`series_name`	varchar(100)	NULL
);

CREATE TABLE `book` (
	`book_id`	int	NOT NULL,
	`book_name`	varchar(100)	NULL,
	`series_id`	int	NULL,
	`series_num`	int	NULL,
	`book_url`	varchar(512)	NULL,
	`isbn`	varchar(32)	NULL,
	`author`	varchar(64)	NULL,
	`created_at`	date	NOT NULL,
	`publisher`	varchar(64)	NULL,
	`price`	int	NULL
);

CREATE TABLE `comment` (
	`comment_id`	bigint	NOT NULL,
	`created_at`	datetime(6)	NOT NULL,
	`content`	varchar(512)	NULL,
	`user_seq`	bigint	NOT NULL,
	`episode_seq`	int	NOT NULL,
	`book_id`	int	NOT NULL
);

CREATE TABLE `like` (
	`user_seq`	bigint	NOT NULL,
	`comment_id`	bigint	NOT NULL,
	`created_at`	datetime(6)	NOT NULL
);

CREATE TABLE `episode` (
	`edisode_num`	int	NOT NULL,
	`book_id`	int	NOT NULL,
	`episode_name`	varchar(100)	NULL
);

CREATE TABLE `refresh_token` (
	`refresh_token_seq`	bigint	NOT NULL,
	`refresh_token`	varchar(256)	NULL,
	`user_seq2`	bigint	NOT NULL
);

ALTER TABLE `user` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`user_seq`
);

ALTER TABLE `series` ADD CONSTRAINT `PK_SERIES` PRIMARY KEY (
	`series_id`
);

ALTER TABLE `book` ADD CONSTRAINT `PK_BOOK` PRIMARY KEY (
	`book_id`
);

ALTER TABLE `comment` ADD CONSTRAINT `PK_COMMENT` PRIMARY KEY (
	`comment_id`
);

ALTER TABLE `like` ADD CONSTRAINT `PK_LIKE` PRIMARY KEY (
	`user_seq`,
	`comment_id`
);

ALTER TABLE `episode` ADD CONSTRAINT `PK_EPISODE` PRIMARY KEY (
	`edisode_num`,
	`book_id`
);

ALTER TABLE `refresh_token` ADD CONSTRAINT `PK_REFRESH_TOKEN` PRIMARY KEY (
	`refresh_token_seq`
);

ALTER TABLE `like` ADD CONSTRAINT `FK_user_TO_like_1` FOREIGN KEY (
	`user_seq`
)
REFERENCES `user` (
	`user_seq`
);

ALTER TABLE `like` ADD CONSTRAINT `FK_comment_TO_like_1` FOREIGN KEY (
	`comment_id`
)
REFERENCES `comment` (
	`comment_id`
);

ALTER TABLE `episode` ADD CONSTRAINT `FK_book_TO_episode_1` FOREIGN KEY (
	`book_id`
)
REFERENCES `book` (
	`book_id`
);

