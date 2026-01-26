-- MySQL dump 10.13  Distrib 9.0.1, for Win64 (x86_64)
--
-- Host: localhost    Database: prod_tracking
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity_type_dim`
--

DROP TABLE IF EXISTS `activity_type_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_type_dim` (
  `activity_type_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique identifier for the activity type that time was spent on',
  `activity_title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Title of activity that I want to keep track of hours under. Usually associated with a goal number of hour I\\''m trying to achieve in each category',
  PRIMARY KEY (`activity_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `book_category_dim`
--

DROP TABLE IF EXISTS `book_category_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_category_dim` (
  `book_category_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID corresponding to the name of the book category',
  `book_category_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of the cateogry of the book. I like to make sure I am well versed in different areas',
  PRIMARY KEY (`book_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `book_status_dim`
--

DROP TABLE IF EXISTS `book_status_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_status_dim` (
  `book_status_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID Corresponding to the status of the book',
  `book_status_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of the status of the book',
  PRIMARY KEY (`book_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `book_id` varchar(5) NOT NULL COMMENT 'Unique ID for the book',
  `book_category_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID corresponding to the name of the book category',
  `book_status_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Name of the author(s)',
  `finish_dt` date DEFAULT NULL,
  `focus_flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `nbr_pages` int DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `start_dt` date DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`book_id`),
  KEY `book_category_id_idx` (`book_category_id`),
  KEY `book_status_id_idx` (`book_status_id`),
  CONSTRAINT `fk_book_category_id` FOREIGN KEY (`book_category_id`) REFERENCES `book_category_dim` (`book_category_id`),
  CONSTRAINT `fk_book_status_id` FOREIGN KEY (`book_status_id`) REFERENCES `book_status_dim` (`book_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `car_maintenance_log`
--

DROP TABLE IF EXISTS `car_maintenance_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_maintenance_log` (
  `service_entry_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for entry. No specific meaning behind automated value',
  `service_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique identifier for type of service performed on car (ie wash, oil change, brakes, etc)',
  `vehicle_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID for vehicle',
  `cost` double DEFAULT NULL COMMENT 'Cost to perform service. It was done by myself that labor cost will not be included.',
  `mileage` int NOT NULL COMMENT 'Mileage at which service did occur or should occur',
  `type` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Delineates between either service performed or service planned to be performed',
  `service_provider_id` int DEFAULT NULL,
  `service_date` date DEFAULT NULL,
  PRIMARY KEY (`service_entry_id`),
  KEY `service_id_idx` (`service_id`),
  KEY `vehicle_id_idx` (`vehicle_id`),
  KEY `fk_service_provider_id_idx` (`service_provider_id`),
  CONSTRAINT `fk_service_id` FOREIGN KEY (`service_id`) REFERENCES `service_dim` (`service_id`),
  CONSTRAINT `fk_service_provider_id` FOREIGN KEY (`service_provider_id`) REFERENCES `service_provider_dim` (`service_provider_id`),
  CONSTRAINT `fk_vehicle_id` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle_dim` (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_dim`
--

DROP TABLE IF EXISTS `service_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_dim` (
  `service_id` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID for the car service',
  `service_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name or description of the car service. Som examples might include things like oil change, front wiper blade replacement, wash car, wax car, etc.',
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_provider_dim`
--

DROP TABLE IF EXISTS `service_provider_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_provider_dim` (
  `service_provider_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier associated with the service provider. Will just be an auto-generated incremental number',
  `provider_name` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Who performed the service. Usually this will be the name of a company or myself',
  `location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Location where service either was or will be performed',
  `phone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`service_provider_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `time_tracking_log`
--

DROP TABLE IF EXISTS `time_tracking_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_tracking_log` (
  `time_entry_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for entry. No specific meaning behind automated value',
  `activity_type_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique identifier for the activity type that time was spent on',
  `activity_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'A description of what specific activities were done within the project',
  `date` date NOT NULL COMMENT 'Calendar date of entry. There can be multiple entries with the same date.',
  `minutes_spent` int NOT NULL COMMENT 'Minutes spent on the activity',
  PRIMARY KEY (`time_entry_id`),
  KEY `activity_type_id_idx` (`activity_type_id`),
  CONSTRAINT `fk_activity_type_id` FOREIGN KEY (`activity_type_id`) REFERENCES `activity_type_dim` (`activity_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vehicle_dim`
--

DROP TABLE IF EXISTS `vehicle_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_dim` (
  `vehicle_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID of the vehicle',
  `make` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Make of the vehicle',
  `model` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Model of the vehicle',
  `year` int NOT NULL COMMENT 'Year of the vehicle',
  `vin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Vehicle identification number',
  `license` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'License plate number',
  `reg_exp_dt` date DEFAULT NULL COMMENT 'Date the registration expires. This is to help me keep on top of when I need to get the new registration done.',
  PRIMARY KEY (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-26  7:19:23
