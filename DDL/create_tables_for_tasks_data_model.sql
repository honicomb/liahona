-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema tasks
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tasks
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tasks` DEFAULT CHARACTER SET utf8 ;
USE `tasks` ;

-- -----------------------------------------------------
-- Table `tasks`.`value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`value` ;

CREATE TABLE IF NOT EXISTS `tasks`.`value` (
  `value_id` VARCHAR(7) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID assigned to value',
  `priority` INT NOT NULL COMMENT 'Prioirity of value',
  `value_name` VARCHAR(60) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name/title of value',
  `why` VARCHAR(1000) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'The \"why\" of the value. This is the reason I have the value.',
  PRIMARY KEY (`value_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasks`.`how`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`how` ;

CREATE TABLE IF NOT EXISTS `tasks`.`how` (
  `how_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'The ID associated with the particular \"how.\" This field in combination with the value ID form the unique key in this table.',
  `value_id` VARCHAR(7) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID assigned to value',
  `how_desc` VARCHAR(256) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Description of \"how\"',
  INDEX `value_id_idx` (`value_id` ASC) VISIBLE,
  PRIMARY KEY (`how_id`),
  CONSTRAINT `fk_value_id_how`
    FOREIGN KEY (`value_id`)
    REFERENCES `tasks`.`value` (`value_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasks`.`goal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`goal` ;

CREATE TABLE IF NOT EXISTS `tasks`.`goal` (
  `goal_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'The ID associated with the goal. Will always be tied to a Value ID and usually it will be tied to a How ID',
  `how_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'ID assigned to \"how\"',
  `value_id` VARCHAR(7) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID assigned to value',
  `goal_end_dt` DATE NULL,
  `goal_desc` VARCHAR(256) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Description of the goal. Goals should be specific, measurable, achievable, relevant, and time-bound',
  `goal_start_dt` DATE NULL,
  `short_desc` VARCHAR(30) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Short description of the goal so it can be included in drop down lists',
  PRIMARY KEY (`goal_id`),
  INDEX `how_id_idx` (`how_id` ASC) INVISIBLE,
  INDEX `value_id_idx` (`value_id` ASC) VISIBLE,
  CONSTRAINT `fk_how_id_goal`
    FOREIGN KEY (`how_id`)
    REFERENCES `tasks`.`how` (`how_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_value_id_goal`
    FOREIGN KEY (`value_id`)
    REFERENCES `tasks`.`value` (`value_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasks`.`status_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`status_dim` ;

CREATE TABLE IF NOT EXISTS `tasks`.`status_dim` (
  `status_id` VARCHAR(2) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID associated with a status',
  `status_name` VARCHAR(20) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name or description of the status',
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasks`.`milestone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`milestone` ;

CREATE TABLE IF NOT EXISTS `tasks`.`milestone` (
  `milestone_id` VARCHAR(7) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID assigned to the milestone',
  `goal_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'The unique ID associated with the goal',
  `status_id` VARCHAR(2) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID of the status of the milestone',
  `due_date` DATE NULL,
  `milestone_desc` VARCHAR(256) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'The description of the milestone',
  `status_date` DATE NULL,
  PRIMARY KEY (`milestone_id`),
  INDEX `goal_id_idx` (`goal_id` ASC) VISIBLE,
  INDEX `status_id_idx` (`status_id` ASC) VISIBLE,
  CONSTRAINT `fk_goal_id_milestone`
    FOREIGN KEY (`goal_id`)
    REFERENCES `tasks`.`goal` (`goal_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_status_id_milestone`
    FOREIGN KEY (`status_id`)
    REFERENCES `tasks`.`status_dim` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasks`.`category_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`category_dim` ;

CREATE TABLE IF NOT EXISTS `tasks`.`category_dim` (
  `category_id` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID associated with a category',
  `category_name` VARCHAR(20) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name of the category. Used to be able to filter tasks and other items',
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tasks`.`task_daily`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`task_daily` ;

CREATE TABLE `task_daily` (
   `task_id` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Each task is assigned a unique ID',
   `actual_end_dt` date NULL COMMENT 'The date the task entered a terminal status (Completed, Deleted, Missed)',
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
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4


-- -----------------------------------------------------
-- Table `tasks`.`task_mtl`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`task_mtl` ;

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
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4


-- -----------------------------------------------------
-- Table `tasks`.`owed`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tasks`.`owed` ;

CREATE TABLE IF NOT EXISTS `tasks`.`owed` (
  `owed_id` VARCHAR(6) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Key ID for the entry',
  `category_id` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'ID of the category',
  `status_id` VARCHAR(2) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID associated with a status',
  `assigned_dt` DATE NOT NULL COMMENT 'Date that I assigned or asked',
  `assigned_name` VARCHAR(25) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Person that I assigned of asked',
  `description` VARCHAR(100) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Description of the item that is owed to me',
  `due_date` DATE NULL COMMENT 'Date that follow-up item is due from the person that owes it to me',
  `final_disp_dt` DATE NULL COMMENT 'Date of the final disposition. Not interested in keeping interim dates at this time but do want to keep the date it \"falls of the list.\"',
  `medium` VARCHAR(10) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Medium I used when asking for the item',
  `notes` VARCHAR(2000) CHARACTER SET 'utf8mb4' NULL COMMENT 'More extended notes for anything that is owed to me. Might have multiple date entries as there might be multiple follow-ups',
  PRIMARY KEY (`owed_id`),
  INDEX `status_id_idx` (`status_id` ASC) VISIBLE,
  INDEX `fk_category_id_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_status_id_owed`
    FOREIGN KEY (`status_id`)
    REFERENCES `tasks`.`status_dim` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_category_id_owed`
    FOREIGN KEY (`category_id`)
    REFERENCES `tasks`.`category_dim` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

