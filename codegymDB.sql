-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: meeting_room_management
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `booking_attendees`
--

DROP TABLE IF EXISTS `booking_attendees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_attendees` (
  `booking_id` int NOT NULL,
  `user_id` int NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`booking_id`,`user_id`),
  KEY `fk_attendee_user` (`user_id`),
  CONSTRAINT `fk_attendee_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_attendee_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_attendees`
--

LOCK TABLES `booking_attendees` WRITE;
/*!40000 ALTER TABLE `booking_attendees` DISABLE KEYS */;
INSERT INTO `booking_attendees` VALUES (4,4,'INVITED'),(4,5,'INVITED'),(4,6,'INVITED'),(4,7,'ACCEPTED'),(5,5,'INVITED'),(5,7,'ACCEPTED');
/*!40000 ALTER TABLE `booking_attendees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_devices`
--

DROP TABLE IF EXISTS `booking_devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_devices` (
  `booking_id` int NOT NULL,
  `device_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`booking_id`,`device_id`),
  KEY `fk_booking_devices_device` (`device_id`),
  CONSTRAINT `fk_booking_devices_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_booking_devices_device` FOREIGN KEY (`device_id`) REFERENCES `devices` (`device_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_devices`
--

LOCK TABLES `booking_devices` WRITE;
/*!40000 ALTER TABLE `booking_devices` DISABLE KEYS */;
/*!40000 ALTER TABLE `booking_devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `room_id` int DEFAULT NULL,
  `host_user_id` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `description` text,
  `status` varchar(50) DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `fk_bookings_room` (`room_id`),
  KEY `fk_bookings_host` (`host_user_id`),
  CONSTRAINT `fk_bookings_host` FOREIGN KEY (`host_user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_bookings_room` FOREIGN KEY (`room_id`) REFERENCES `meeting_rooms` (`room_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (4,2,2,'Họp dự án SKYLINE','2026-02-12 07:30:00','2026-02-12 11:30:00','Trang phục gọn gàng, đeo thẻ nhân viên khi tham dự cuộc họp','CANCELLED',400000),(5,3,2,'Weekly Team Meeting','2026-02-13 07:30:00','2026-02-13 10:30:00','Họp công ty hàng tuần ','CONFIRMED',300000);
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devices`
--

DROP TABLE IF EXISTS `devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `devices` (
  `device_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `quantity` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devices`
--

LOCK TABLES `devices` WRITE;
/*!40000 ALTER TABLE `devices` DISABLE KEYS */;
INSERT INTO `devices` VALUES (1,'Máy chiếu','PROJECTOR','AVAILABLE',5),(3,'Điều hoà','THIẾT BỊ ĐIỆN','MAINTENANCE',10);
/*!40000 ALTER TABLE `devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meeting_rooms`
--

DROP TABLE IF EXISTS `meeting_rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting_rooms` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `capacity` int DEFAULT NULL,
  `features` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT 'default-room.jpg',
  `price_per_hour` decimal(10,2) DEFAULT '0.00',
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting_rooms`
--

LOCK TABLES `meeting_rooms` WRITE;
/*!40000 ALTER TABLE `meeting_rooms` DISABLE KEYS */;
INSERT INTO `meeting_rooms` VALUES (2,'Phòng họp công ty',10,'Có đầy đủ máy chiếu, phục vụ nước uống','ACTIVE','1770109264528_download.jpg',100000.00,'Phòng 203, Tầng 2, số 48 đường Lê Nin, thành phố Vinh, tỉnh Nghệ An'),(3,'Phòng hội thảo',20,'Máy chiếu, điều hoà, nước uống, máy sưởi, bảng trắng, ghế sofa','MAINTENANCE','1770401078787_download (3).jpg',100000.00,'Phòng 305, Tầng 3, số 48 đường Lê Nin, thành phố Vinh, tỉnh Nghệ An');
/*!40000 ALTER TABLE `meeting_rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_recipients`
--

DROP TABLE IF EXISTS `notification_recipients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification_recipients` (
  `noti_id` int NOT NULL,
  `user_id` int NOT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`noti_id`,`user_id`),
  KEY `fk_recipients_user` (`user_id`),
  CONSTRAINT `fk_recipients_notification` FOREIGN KEY (`noti_id`) REFERENCES `notifications` (`noti_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_recipients_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_recipients`
--

LOCK TABLES `notification_recipients` WRITE;
/*!40000 ALTER TABLE `notification_recipients` DISABLE KEYS */;
INSERT INTO `notification_recipients` VALUES (3,4,0),(3,5,0),(3,6,0),(3,7,1),(4,5,0),(4,7,1);
/*!40000 ALTER TABLE `notification_recipients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `noti_id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `content` text,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`noti_id`),
  KEY `fk_notifications_booking` (`booking_id`),
  CONSTRAINT `fk_notifications_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (3,4,'INVITE','Lời mời họp: Họp dự án SKYLINE','2026-02-09 19:50:36'),(4,5,'INVITE','Lời mời họp: Weekly Team Meeting','2026-02-09 21:48:56');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uq_role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'ROLE_ADMIN'),(1,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_assets`
--

DROP TABLE IF EXISTS `room_assets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_assets` (
  `asset_id` int NOT NULL AUTO_INCREMENT,
  `room_id` int NOT NULL,
  `device_id` int NOT NULL,
  `quantity` int DEFAULT '1',
  PRIMARY KEY (`asset_id`),
  KEY `fk_room_assets_room` (`room_id`),
  KEY `fk_room_assets_device` (`device_id`),
  CONSTRAINT `fk_room_assets_device` FOREIGN KEY (`device_id`) REFERENCES `devices` (`device_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_room_assets_room` FOREIGN KEY (`room_id`) REFERENCES `meeting_rooms` (`room_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_assets`
--

LOCK TABLES `room_assets` WRITE;
/*!40000 ALTER TABLE `room_assets` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_assets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user_roles_role` (`role_id`),
  CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(2,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `fullname` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `department_id` int DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `job_title` varchar(100) DEFAULT NULL,
  `about` text,
  `avatar` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_email` (`email`),
  KEY `fk_users_department` (`department_id`),
  CONSTRAINT `fk_users_department` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,'NGUYEN DINH THIEP','thiepthanhx03@gmail.com','$2a$10$n3G3OYHFzZgSUq8UbFRNE.G5YiP0jMqIS4us6FHm8GxG5h7Ue8WKu','2026-02-04 23:50:34',NULL,'0899073387','Vinh, Nghệ An','OnMeet','Programmer','Deverloper','z6295146552086_4314fdb21b7504c6c0b8fe274bf8d571.jpg'),(3,'NGUYEN DINH THANG','thiepthanhx37@gmail.com','$2a$10$v3J6bdQPb2cgaR7PgamEOelf2gv4G/s7C4ibFYcEobA.I7UjHHwnu','2026-02-06 15:12:00',NULL,'0988822102','Vinh Nghe An','OnMeet','Full Stack Programmer','hi','z5784310133040_dd6b8a46029d60c35674482fdfdfd8b2.jpg'),(4,'NGUYEN DINH DAT','nguyendinhdat@gmail.com','$2a$10$TApZL5Y.QKJHQT07FHDFBeMvr1ZQ2LF4p.T3.EnMb2nzN2WlBYh3y','2026-02-09 19:45:27',NULL,'',NULL,NULL,NULL,NULL,NULL),(5,'NGUYEN DINH THANG','nguyendinhthang@gmail.com','$2a$10$iykKMYFxSLqPZP.iTgFZP.vb7BTY30G71TwNVAN4lk112s6FrXpCK','2026-02-09 19:43:55',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'HOANG THI HUONG','hoangthihuong@gmail.com','$2a$10$43Xr1aFfLctu9m6fCjUgBeixX8aCyonqd01gUc28f6Z.0i5FvPgZK','2026-02-09 19:44:42',NULL,'',NULL,NULL,NULL,NULL,NULL),(7,'NGUYEN DINH THANH','nguyendinhthanh@gmail.com','$2a$10$Rys77HoFZXUfM55zVdS1w.6bfvEF4zv1Rd5rZidElCZxR6mGy7tNi','2026-02-09 19:48:16',NULL,'',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-17 11:00:19
