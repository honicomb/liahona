-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema workout
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema workout
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `workout` DEFAULT CHARACTER SET utf8 ;
USE `workout` ;

-- -----------------------------------------------------
-- Table `workout`.`exercise_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `workout`.`exercise_dim` ;

CREATE TABLE IF NOT EXISTS `workout`.`exercise_dim` (
  `exercise_id` VARCHAR(6) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID associated with an exercise',
  `exercise_name` VARCHAR(40) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name of the exercise',
  `body_part` VARCHAR(30) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Part of the body that the exercise targets',
  `exercise_type` VARCHAR(10) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Differentiates between cardio and weights. Could possibly add other categories later if I wanted to further delineate.',
  PRIMARY KEY (`exercise_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `workout`.`exercise_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `workout`.`exercise_log` ;

CREATE TABLE IF NOT EXISTS `workout`.`exercise_log` (
  `entry_id` VARCHAR(8) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'A unique ID assigned to each entry in the table.',
  `exercise_id` VARCHAR(6) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID associated with an exercise.',
  `date` DATE NOT NULL COMMENT 'Date exercise activity was done',
  `free_weight_flag` VARCHAR(2) CHARACTER SET 'utf8mb4' NULL COMMENT 'Flag indicating if the movement was done with free weights or not. If no, then it was done with a machine or bodyweight.',
  `notes` VARCHAR(200) CHARACTER SET 'utf8mb4' NULL COMMENT 'Notes. Typically will note how weight felt and any recommendations for the next time I hit this body part',
  `rep_nbr` INT NULL COMMENT 'Number of repititions performed in the set',
  `set_nbr` INT NULL COMMENT 'The number of the set. IT NOT the total number of sets but rather an entry for a particular set which will have a corresponding Rep number and weight amount.',
  `time` INT NULL COMMENT 'Amount of time (minutes) spent on exercise. Not applicable for weight training at this point.',
  `weight` INT NULL COMMENT 'Weight used for associated set and rep count',
  PRIMARY KEY (`entry_id`),
  INDEX `exercise_id_idx` (`exercise_id` ASC) VISIBLE,
  CONSTRAINT `fk_exercise_id`
    FOREIGN KEY (`exercise_id`)
    REFERENCES `workout`.`exercise_dim` (`exercise_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
