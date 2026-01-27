-- MySQL dump 10.13  Distrib 9.0.1, for Win64 (x86_64)
--
-- Host: localhost    Database: dev_workout
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
-- Table structure for table `cardio_dim`
--

DROP TABLE IF EXISTS `cardio_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cardio_dim` (
  `cardio_exer_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a cardio exercise',
  `exer_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Type/description of the exercise',
  PRIMARY KEY (`cardio_exer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cardio_log`
--

DROP TABLE IF EXISTS `cardio_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cardio_log` (
  `entry_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'A unique ID assigned to each entry in the table. This will be a sequential number that will auto-generate',
  `date` date NOT NULL COMMENT 'Date exercise activity was done',
  `cardio_exer_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with an exercise',
  `duration` int NOT NULL COMMENT 'Amount of time (minutes) spent on exercise',
  PRIMARY KEY (`entry_id`),
  KEY `fk_cardio_exer_id_idx` (`cardio_exer_id`),
  CONSTRAINT `fk_cardio_exer_id` FOREIGN KEY (`cardio_exer_id`) REFERENCES `cardio_dim` (`cardio_exer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fat_burn_dim`
--

DROP TABLE IF EXISTS `fat_burn_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fat_burn_dim` (
  `fat_burn_exer_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a fat-burning exercise',
  `exer_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Type/description of the exercise',
  PRIMARY KEY (`fat_burn_exer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fat_burn_log`
--

DROP TABLE IF EXISTS `fat_burn_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fat_burn_log` (
  `entry_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'A unique ID assigned to each entry in the table. This will be a sequential number that will auto-generate',
  `date` date NOT NULL COMMENT 'Date exercise activity was done',
  `fat_burn_exer_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a fat-burning exercise',
  `duration` int NOT NULL COMMENT 'Amount of time (minutes) spent on exercise',
  PRIMARY KEY (`entry_id`),
  KEY `fk_fat_burn_exer_id_idx` (`fat_burn_exer_id`),
  CONSTRAINT `fk_fat_burn_exer_id` FOREIGN KEY (`fat_burn_exer_id`) REFERENCES `fat_burn_dim` (`fat_burn_exer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weights_dim`
--

DROP TABLE IF EXISTS `weights_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `weights_dim` (
  `weights_exer_id` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a weight-lifting exercise',
  `exer_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of the exercise',
  `body_part` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Part of the body that the exercise targets',
  PRIMARY KEY (`weights_exer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weights_log`
--

DROP TABLE IF EXISTS `weights_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `weights_log` (
  `entry_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'A unique ID assigned to each entry in the table. This will be a sequential number that will auto-generate',
  `date` date NOT NULL COMMENT 'Date exercise activity was done.',
  `weights_exer_id` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a weight-lifting exercise',
  `weight_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Indicates if the exercise was done with free weights, dumbells, or a machine.',
  `notes` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Notes. Typically will note how weight felt and any recommendations for the next time I hit this body part',
  `rep_nbr` int NOT NULL COMMENT 'Number of repetitions performed in the set',
  `weight` int NOT NULL COMMENT 'Weight used for associated set and rep count',
  PRIMARY KEY (`entry_id`),
  KEY `fk_weight_exer_id_idx` (`weights_exer_id`),
  CONSTRAINT `fk_weights_exer_id` FOREIGN KEY (`weights_exer_id`) REFERENCES `weights_dim` (`weights_exer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-26  7:20:12
