-- MySQL dump 10.13  Distrib 9.0.1, for Win64 (x86_64)
--
-- Host: localhost    Database: prod_health
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
-- Table structure for table `food_base`
--

DROP TABLE IF EXISTS `food_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_base` (
  `food_id` int NOT NULL COMMENT 'Unique ID associated with a food',
  `calories` int NOT NULL COMMENT 'Number of calories per serving',
  `carbs` int NOT NULL COMMENT 'Number of grams of carbs per serving',
  `fat` int NOT NULL COMMENT 'Number of grams of fat per serving',
  `food_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of food',
  `protein` int NOT NULL COMMENT 'Number of grams of protein per serving',
  `serving_size` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Size of serving',
  PRIMARY KEY (`food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `food_log`
--

DROP TABLE IF EXISTS `food_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_log` (
  `food_log_id` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID for entry since there is nothing else to do a PK',
  `food_id` int NOT NULL COMMENT 'Unique ID associated with a food',
  `calories_ttl` int NOT NULL COMMENT 'Total number of calories consumed',
  `carbs_ttl` int DEFAULT NULL COMMENT 'Total number of grams of carbs consumed',
  `date` date NOT NULL COMMENT 'Date the food was consumed',
  `fat_ttl` int DEFAULT NULL COMMENT 'Total number of grams of fat consumed',
  `protein_ttl` int DEFAULT NULL,
  `servings_nbr` int DEFAULT NULL COMMENT 'Number of servings consumed',
  PRIMARY KEY (`food_log_id`),
  KEY `food_id_idx` (`food_id`),
  CONSTRAINT `fk_food_id` FOREIGN KEY (`food_id`) REFERENCES `food_base` (`food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vitals`
--

DROP TABLE IF EXISTS `vitals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vitals` (
  `date` date NOT NULL COMMENT 'Date of vitals',
  `glucose` int DEFAULT NULL COMMENT 'Blood glucose (usually taken in AM - fasting)',
  `meds` varchar(3) DEFAULT NULL,
  `steps` int DEFAULT NULL COMMENT 'Number of steps taken in a day per my pedometer',
  `weight` double DEFAULT NULL COMMENT 'Body weight (usually taken in AM right after waking)',
  PRIMARY KEY (`date`)
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

-- Dump completed on 2026-01-26  7:18:10
