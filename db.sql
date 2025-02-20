CREATE DATABASE IF NOT EXISTS `tour_booking` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci */;
USE `tour_booking`;

-- Table structure for table `users`
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `full_name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- Dumping data for table `users`
INSERT INTO `users` VALUES 
(1,'user1','password123','user1@example.com','Nguyen Van A','USER','2024-11-29 23:19:31'),
(2,'admin','admin123','admin@example.com','Admin User','ADMIN','2024-11-29 23:19:31');

-- Table structure for table `tours`
DROP TABLE IF EXISTS `tours`;
CREATE TABLE `tours` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `price` double NOT NULL,
  `duration` int DEFAULT NULL,
  `location` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- Dumping data for table `tours`
INSERT INTO `tours` VALUES 
(1,'Tour Đà Nẵng','Khám phá vẻ đẹp của Đà Nẵng trong 3 ngày.',1500000,3,'Đà Nẵng','2024-11-29 23:19:37'),
(2,'Tour Hà Nội','Trải nghiệm văn hóa Hà Nội trong 2 ngày.',1200000,2,'Hà Nội','2024-11-29 23:19:37'),
(3,'Tour Phú Quốc','Thư giãn tại bãi biển Phú Quốc trong 4 ngày.',2500000,4,'Phú Quốc','2024-11-29 23:19:37');

-- Table structure for table `bookings`
DROP TABLE IF EXISTS `bookings`;
CREATE TABLE `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `tour_id` bigint NOT NULL,
  `booking_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `number_of_people` int NOT NULL,
  `total_price` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `tour_id` (`tour_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`tour_id`) REFERENCES `tours` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- Dumping data for table `bookings`
INSERT INTO `bookings` VALUES 
(1,1,1,'2024-11-29 23:19:42',2,3000000),
(2,2,2,'2024-11-29 23:19:42',1,1200000);

-- Table structure for table `reviews`
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `tour_id` bigint NOT NULL,
  `rating` int DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `tour_id` (`tour_id`),
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`tour_id`) REFERENCES `tours` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- Dumping data for table `reviews`
INSERT INTO `reviews` VALUES 
(1,1,1,5,'Tour rất tuyệt vời!','2024-11-29 23:19:47'),
(2,2,2,4,'Dịch vụ tốt nhưng có một số vấn đề nhỏ.','2024-11-29 23:19:47');
