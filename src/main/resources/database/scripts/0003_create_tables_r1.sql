CREATE TABLE IF NOT EXISTS `registrations` (
  `registration_id` int(11) NOT NULL AUTO_INCREMENT,
  `consumer_id` int(11) NOT NULL,
  `insert_tmstmp` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(100) NOT NULL,
  PRIMARY KEY (`registration_id`),
  KEY `registrations_FK` (`consumer_id`),
  CONSTRAINT `registrations_FK` FOREIGN KEY (`consumer_id`) REFERENCES `consumer` (`consumer_id`)
);