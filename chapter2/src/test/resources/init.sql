-- CREATE TABLE `customer` (
--     `id` bigint(20) NOT NULL AUTO_INCREMENT,
--     `name` varchar(255) DEFAULT NULL,
--     `contact` varchar(255) DEFAULT NULL,
--     `telephone` varchar(255) DEFAULT NULL,
--     `email` varchar(255) DEFAULT NULL,
--     `remark` text,
--     PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
TRUNCATE customer;
INSERT INTO `demo`.`customer` (`id`, `name`, `contact`, `telephone`, `email`) VALUES ('1', 'customer1', 'Jack', '13512345678', 'jack@gmail.com');
INSERT INTO `demo`.`customer` (`id`, `name`, `contact`, `telephone`, `email`, `remark`) VALUES ('2', 'customer2', 'Rose', '136123456789', 'rose@gmail.com', '');
