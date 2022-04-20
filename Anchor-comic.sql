CREATE DATABASE anchor;
USE anchor;

DROP TABLE IF EXISTS `series`;

CREATE TABLE `series` (
	`series_id`  int  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`series_name`  varchar(100)  NULL UNIQUE
);

DROP TABLE IF EXISTS `book`;

CREATE TABLE `book` (
	`book_id`  int  NOT  NULL,
	`book_name`  varchar(100)   NULL,
	`series_id`  int  NULL,
	`series_num`  int  NULL,
	`book_url`  varchar(512)  NULL,
	`isbn`   varchar(32)  NULL,
	`author`  varchar(64)   NULL,
	`created_at`  date  NOT NULL,
	`publisher`   varchar(64)  NULL,
	`price`  int  NULL,
	`book_image` varchar(512) NULL
);

ALTER TABLE `book` ADD CONSTRAINT `PK_BOOK` PRIMARY KEY (
	`book_id`
);

ALTER TABLE `book` ADD CONSTRAINT `FK_series_TO_book_1` FOREIGN KEY (
	`series_id`
) REFERENCES `series` (
	`series_id`
);

DROP TABLE IF EXISTS `episode`;

CREATE TABLE `episode` (
	`edisode_num` int NOT NULL,
	`book_id` int NOT NULL,
	`episode_name` varchar(100) NULL
);

ALTER TABLE `episode` ADD CONSTRAINT `PK_EPISODE` PRIMARY KEY (
	`edisode_num`,
	`book_id`
);

ALTER TABLE `episode` ADD CONSTRAINT `FK_book_TO_episode_1` FOREIGN KEY (
	`book_id`
)REFERENCES `book` (
	`book_id`
);
