-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema tracking
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tracking
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tracking` DEFAULT CHARACTER SET utf8 ;
USE `tracking` ;

-- -----------------------------------------------------
-- Table `tracking`.`book_category_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tracking`.`book_category_dim` ;

CREATE TABLE IF NOT EXISTS `tracking`.`book_category_dim` (
  `book_category_id` VARCHAR(8) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'ID corresponding to the name of the book category',
  `book_category_name` VARCHAR(20) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name of the cateogry of the book. I like to make sure I am well versed in different areas',
  PRIMARY KEY (`book_category_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracking`.`book_status_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tracking`.`book_status_dim` ;

CREATE TABLE IF NOT EXISTS `tracking`.`book_status_dim` (
  `book_status_id` VARCHAR(10) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'ID Corresponding to the status of the book',
  `book_status_name` VARCHAR(15) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name of the status of the book',
  PRIMARY KEY (`book_status_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracking`.`activity_type_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tracking`.`activity_type_dim` ;

CREATE TABLE IF NOT EXISTS `tracking`.`activity_type_dim` (
  `activity_type_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique identifier for the activity type that time was spent on',
  `activity_title` VARCHAR(30) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Title of activity that I want to keep track of hours under. Usually associated with a goal number of hour I\'m trying to achieve in each category',
  PRIMARY KEY (`activity_type_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracking`.`vehicle_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tracking`.`vehicle_dim` ;

CREATE TABLE IF NOT EXISTS `tracking`.`vehicle_dim` (
  `vehicle_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'ID of the vehicle',
  `make` VARCHAR(15) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Make of the vehicle',
  `model` VARCHAR(12) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Model of the vehicle',
  `year` INT NOT NULL COMMENT 'Year of the vehicle',
  `vin` VARCHAR(20) CHARACTER SET 'utf8mb4' NULL COMMENT 'Vehicle identification number',
  `license` VARCHAR(9) CHARACTER SET 'utf8mb4' NULL COMMENT 'License plate number',
  `reg_exp_dt` DATE NULL COMMENT 'Date the registration expires. This is to help me keep on top of when I need to get the new registration done.',
  PRIMARY KEY (`vehicle_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracking`.`service_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tracking`.`service_dim` ;

CREATE TABLE IF NOT EXISTS `tracking`.`service_dim` (
  `service_id` VARCHAR(6) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID for the car service',
  `service_desc` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name or description of the car service. Som examples might include things like oil change, front wiper blade replacement, wash car, wax car, etc.',
  PRIMARY KEY (`service_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracking`.`car_maintanence_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tracking`.`car_maintanence_log` ;

CREATE TABLE IF NOT EXISTS `tracking`.`car_maintanence_log` (
  `service_entry_id` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for entry. No specific meaning behind automated value',
  `service_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique identifier for type of service performed on car (ie wash, oil change, brakes, etc)',
  `vehicle_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID for vehicle',
  `cost` DOUBLE NULL COMMENT 'Cost to perform service. It was done by myself that labor cost will not be included.',
  `location` VARCHAR(50) CHARACTER SET 'utf8mb4' NULL COMMENT 'Location where service either was or will be performed',
  `mileage` INT NOT NULL COMMENT 'Mileage at which service did occur or should occur',
  `performed_by` VARCHAR(30) CHARACTER SET 'utf8mb4' NULL COMMENT 'Who performed the service. Usually this will be the name of a company or myself',
  `type` VARCHAR(11) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Delineates between either service performed or service planned to be performed',
  PRIMARY KEY (`service_entry_id`),
  INDEX `service_id_idx` (`service_id` ASC) VISIBLE,
  INDEX `vehicle_id_idx` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `fk_service_id`
    FOREIGN KEY (`service_id`)
    REFERENCES `tracking`.`service_dim` (`service_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_id`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `tracking`.`vehicle_dim` (`vehicle_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracking`.`books`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tracking`.`books` ;

CREATE TABLE IF NOT EXISTS `tracking`.`books` (
  `book_id` VARCHAR(5) NOT NULL COMMENT 'Unique ID for the book',
  `book_category_id` VARCHAR(8) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'ID corresponding to the name of the book category',
  `book_status_id` VARCHAR(10) CHARACTER SET 'utf8mb4' NOT NULL,
  `author` VARCHAR(100) CHARACTER SET 'utf8mb4' NULL COMMENT 'Name of the author(s)',
  `finish_dt` DATE NULL,
  `focus_flag` VARCHAR(2) CHARACTER SET 'utf8mb4' NULL,
  `nbr_pages` INT NULL,
  `rating` INT NULL,
  `start_dt` DATE NULL,
  `title` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL,
  PRIMARY KEY (`book_id`),
  INDEX `book_category_id_idx` (`book_category_id` ASC) VISIBLE,
  INDEX `book_status_id_idx` (`book_status_id` ASC) VISIBLE,
  CONSTRAINT `fk_book_category_id`
    FOREIGN KEY (`book_category_id`)
    REFERENCES `tracking`.`book_category_dim` (`book_category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_book_status_id`
    FOREIGN KEY (`book_status_id`)
    REFERENCES `tracking`.`book_status_dim` (`book_status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracking`.`time_tracking_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tracking`.`time_tracking_log` ;

CREATE TABLE IF NOT EXISTS `tracking`.`time_tracking_log` (
  `time_entry_id` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for entry. No specific meaning behind automated value',
  `activity_type_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique identifier for the activity type that time was spent on',
  `activity_desc` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'A description of what specific activities were done within the project',
  `date` DATE NOT NULL COMMENT 'Calendar date of entry. There can be multiple entries with the same date.',
  `minutes_spent` INT NOT NULL COMMENT 'Minutes spent on the activity',
  PRIMARY KEY (`time_entry_id`),
  INDEX `activity_type_id_idx` (`activity_type_id` ASC) VISIBLE,
  CONSTRAINT `fk_activity_type_id`
    FOREIGN KEY (`activity_type_id`)
    REFERENCES `tracking`.`activity_type_dim` (`activity_type_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
