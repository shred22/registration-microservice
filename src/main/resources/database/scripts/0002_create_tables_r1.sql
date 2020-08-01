CREATE TABLE IF NOT EXISTS `consumer` (
  `consumer_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  `physicalDisability` tinyint(1) NOT NULL DEFAULT '0',
  `address_id` int(11) NOT NULL,
  PRIMARY KEY (`consumer_id`),
  KEY `consumer_FK` (`address_id`),
  CONSTRAINT `consumer_FK` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)
);