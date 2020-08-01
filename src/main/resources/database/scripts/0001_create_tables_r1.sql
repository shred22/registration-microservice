-- bootdb.address definition

CREATE TABLE IF NOT EXISTS `address` (
  `address_id` int(11) NOT NULL,
  `addressLine1` varchar(100) NOT NULL,
  `addressLine2` varchar(100) DEFAULT NULL,
  `pincode` decimal(10,0) NOT NULL,
  `landmark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`address_id`)
);