-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema health
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema health
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `health` DEFAULT CHARACTER SET utf8 ;
USE `health` ;

-- -----------------------------------------------------
-- Table `health`.`food_base`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `health`.`food_base` ;

CREATE TABLE IF NOT EXISTS `health`.`food_base` (
  `food_id` INT NOT NULL COMMENT 'Unique ID associated with a food',
  `calories` INT NOT NULL COMMENT 'Number of calories per serving',
  `carbs` INT NOT NULL COMMENT 'Number of grams of carbs per serving',
  `fat` INT NOT NULL COMMENT 'Number of grams of fat per serving',
  `food_name` VARCHAR(40) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name of food',
  `protein` INT NOT NULL COMMENT 'Number of grams of protein per serving',
  `serving_size` VARCHAR(10) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Size of serving',
  PRIMARY KEY (`food_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `health`.`food_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `health`.`food_log` ;

CREATE TABLE IF NOT EXISTS `health`.`food_log` (
  `food_log_id` VARCHAR(4) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique ID for entry since there is nothing else to do a PK',
  `food_id` INT NOT NULL COMMENT 'Unique ID associated with a food',
  `calories_ttl` INT NOT NULL COMMENT 'Total number of calories consumed',
  `carbs_ttl` INT NULL COMMENT 'Total number of grams of carbs consumed',
  `date` DATE NOT NULL COMMENT 'Date the food was consumed',
  `fat_ttl` INT NULL COMMENT 'Total number of grams of fat consumed',
  `protein_ttl` INT NULL,
  `servings_nbr` INT NULL COMMENT 'Number of servings consumed',
  PRIMARY KEY (`food_log_id`),
  INDEX `food_id_idx` (`food_id` ASC) VISIBLE,
  CONSTRAINT `fk_food_id`
    FOREIGN KEY (`food_id`)
    REFERENCES `health`.`food_base` (`food_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `health`.`vitals`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `health`.`vitals` ;

CREATE TABLE IF NOT EXISTS `health`.`vitals` (
  `date` DATE NOT NULL COMMENT 'Date of vitals',
  `allergy_allertec` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Allergy medication (usually pills)',
  `bcomplex` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Super B-complex pill',
  `escitalopram` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Escitalopram (30 mg) - depression medication',
  `fish_oil` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Fish oil (1000 mg)',
  `glucose` INT NULL COMMENT 'Blood glucose (usually taken in AM - fasting)',
  `lisinopril` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Lisinopril (30 mg) - blood pressure medication',
  `msm` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Glucosamine HCI & MSM - for joints',
  `multi_vitamin` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Multi-vitamin pill',
  `nasal_steriod` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Nasal steriod that I take to deal with allergies',
  `ozempic` VARCHAR(3) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Ozempic (2 mg)',
  `steps` INT NULL COMMENT 'Number of steps taken in a day per my pedometer',
  `weight` DOUBLE NULL COMMENT 'Body weight (usually taken in AM right after waking)',
  PRIMARY KEY (`date`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
