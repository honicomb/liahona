-- MySQL dump 10.13  Distrib 9.0.1, for Win64 (x86_64)
--
-- Host: localhost    Database: prod_tasks
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
-- Table structure for table `category_dim`
--

DROP TABLE IF EXISTS `category_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category_dim` (
  `category_id` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a category',
  `category_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of the category. Used to be able to filter tasks and other items',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal`
--

DROP TABLE IF EXISTS `goal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goal` (
  `goal_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The ID associated with the goal. Will always be tied to a Value ID and usually it will be tied to a How ID',
  `how_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID assigned to "how"',
  `value_id` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID assigned to value',
  `goal_end_dt` date DEFAULT NULL,
  `goal_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Description of the goal. Goals should be specific, measurable, achievable, relevant, and time-bound',
  `goal_start_dt` date DEFAULT NULL,
  `short_desc` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Short description of the goal so it can be included in drop down lists',
  PRIMARY KEY (`goal_id`),
  KEY `how_id_idx` (`how_id`) /*!80000 INVISIBLE */,
  KEY `value_id_idx` (`value_id`),
  CONSTRAINT `fk_how_id_goal` FOREIGN KEY (`how_id`) REFERENCES `how` (`how_id`),
  CONSTRAINT `fk_value_id_goal` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `how`
--

DROP TABLE IF EXISTS `how`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `how` (
  `how_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The ID associated with the particular "how." This field in combination with the value ID form the unique key in this table.',
  `value_id` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID assigned to value',
  `how_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Description of "how"',
  PRIMARY KEY (`how_id`),
  KEY `value_id_idx` (`value_id`),
  CONSTRAINT `fk_value_id_how` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `milestone`
--

DROP TABLE IF EXISTS `milestone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `milestone` (
  `milestone_id` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID assigned to the milestone',
  `goal_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The unique ID associated with the goal',
  `status_id` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID of the status of the milestone',
  `due_date` date DEFAULT NULL,
  `milestone_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The description of the milestone',
  `status_date` date DEFAULT NULL,
  PRIMARY KEY (`milestone_id`),
  KEY `goal_id_idx` (`goal_id`),
  KEY `status_id_idx` (`status_id`),
  CONSTRAINT `fk_goal_id_milestone` FOREIGN KEY (`goal_id`) REFERENCES `goal` (`goal_id`),
  CONSTRAINT `fk_status_id_milestone` FOREIGN KEY (`status_id`) REFERENCES `status_dim` (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `owed`
--

DROP TABLE IF EXISTS `owed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `owed` (
  `owed_id` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Key ID for the entry',
  `category_id` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID of the category',
  `status_id` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a status',
  `assigned_dt` date NOT NULL COMMENT 'Date that I assigned or asked',
  `assigned_name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Person that I assigned of asked',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Description of the item that is owed to me',
  `due_date` date DEFAULT NULL COMMENT 'Date that follow-up item is due from the person that owes it to me',
  `final_disp_dt` date DEFAULT NULL COMMENT 'Date of the final disposition. Not interested in keeping interim dates at this time but do want to keep the date it "falls of the list."',
  `medium` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Medium I used when asking for the item',
  `notes` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'More extended notes for anything that is owed to me. Might have multiple date entries as there might be multiple follow-ups',
  PRIMARY KEY (`owed_id`),
  KEY `status_id_idx` (`status_id`),
  KEY `fk_category_id_idx` (`category_id`),
  CONSTRAINT `fk_category_id_owed` FOREIGN KEY (`category_id`) REFERENCES `category_dim` (`category_id`),
  CONSTRAINT `fk_status_id_owed` FOREIGN KEY (`status_id`) REFERENCES `status_dim` (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status_dim`
--

DROP TABLE IF EXISTS `status_dim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_dim` (
  `status_id` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a status',
  `status_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name or description of the status',
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `task_daily`
--

DROP TABLE IF EXISTS `task_daily`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_daily` (
  `task_id` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Each task is assigned a unique ID',
  `actual_end_dt` date DEFAULT NULL COMMENT 'The date the task entered a terminal status (Completed, Deleted, Missed)',
  `category_id` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a category',
  `status_id` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID associated with the status of the task',
  `actual_min` int DEFAULT NULL COMMENT 'Time in minutes the task actually took to complete',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Text about what the task is',
  `due_dt` date NOT NULL COMMENT 'The date the task was originally planned to be completed',
  `est_min` int DEFAULT NULL COMMENT 'Number of minutes estimated to complete the task',
  `goal_id` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'If the task is linked to a certain goal, then this has the ID of the linked goal',
  `milestone_id` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'If the task has an associated milestone then this will be the milestone ID',
  `notes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Any notes associated with the task',
  `order` int DEFAULT NULL COMMENT 'This is the order of importance with the Priority. For example, if you had 4 "A" priorities, you would have an A1, A2, A3, and A4',
  `priority` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Priority of task. A, B, C, or D followed by a number. A indicates urgent and important, B is not urgent but is important, C is urgent but not important, and D is optional',
  `recur_ind` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'A Y/N indicator to denote if this task recurs on a regular basis',
  `start_dt` date NOT NULL COMMENT 'The date the task was originally planed to start',
  PRIMARY KEY (`task_id`),
  KEY `goal_id_idx` (`goal_id`),
  KEY `milestone_id_idx` (`milestone_id`),
  KEY `status_id_idx` (`status_id`),
  KEY `categor_id_idx` (`category_id`),
  CONSTRAINT `fk_category_id_task_daily` FOREIGN KEY (`category_id`) REFERENCES `category_dim` (`category_id`),
  CONSTRAINT `fk_goal_id_task_daily` FOREIGN KEY (`goal_id`) REFERENCES `goal` (`goal_id`),
  CONSTRAINT `fk_milestone_id_task_daily` FOREIGN KEY (`milestone_id`) REFERENCES `milestone` (`milestone_id`),
  CONSTRAINT `fk_status_id_task_daily` FOREIGN KEY (`status_id`) REFERENCES `status_dim` (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `task_mtl`
--

DROP TABLE IF EXISTS `task_mtl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_mtl` (
  `task_id` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Each task is assigned a unique ID',
  `category_id` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID associated with a category',
  `date_added` date NOT NULL COMMENT 'Date the master task went onto the list. If it was on the list before, was then changed to a daily task, and then re-assigned to the master task list, it will hold the date it was re-assigned',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Text about what the task is',
  `entered_dt` date NOT NULL COMMENT 'Date the master task went onto the list. If it was on the list before, was then changed to a daily task, and then re-assigned to the master task list, it will hold the date it was re-assigned',
  `notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Any notes associated with the task',
  PRIMARY KEY (`task_id`),
  KEY `fk_category_id_idx` (`category_id`),
  CONSTRAINT `fk_category_id_task_mtl` FOREIGN KEY (`category_id`) REFERENCES `category_dim` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `value`
--

DROP TABLE IF EXISTS `value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `value` (
  `value_id` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique ID assigned to value',
  `priority` int NOT NULL COMMENT 'Prioirity of value',
  `value_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name/title of value',
  `why` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The "why" of the value. This is the reason I have the value.',
  PRIMARY KEY (`value_id`)
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

-- Dump completed on 2026-01-26  7:18:57
