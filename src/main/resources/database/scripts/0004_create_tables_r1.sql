CREATE TABLE IF NOT EXISTS `articles` (
  `article_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`article_id`)
);