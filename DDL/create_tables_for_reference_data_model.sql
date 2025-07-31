-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema reference
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema reference
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `reference` DEFAULT CHARACTER SET utf8 ;
USE `reference` ;

-- -----------------------------------------------------
-- Table `reference`.`shopping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reference`.`shopping` ;

CREATE TABLE IF NOT EXISTS `reference`.`shopping` (
  `shop_id` VARCHAR(8) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique identifier for entry. No specific meaning behind automated value',
  `item_name` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name of short description of item that needs to be purchased',
  `qty` INT NOT NULL COMMENT 'Quantity of item that needs to be purchased',
  `store_name` VARCHAR(50) CHARACTER SET 'utf8mb4' NULL COMMENT 'Name of store (or online location) where item can be purchased',
  PRIMARY KEY (`shop_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reference`.`food_type_dim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reference`.`food_type_dim` ;

CREATE TABLE IF NOT EXISTS `reference`.`food_type_dim` (
  `food_type_id` VARCHAR(4) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'ID of the specifc food type. References \"food_type_dim\" table',
  `food_type` VARCHAR(20) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Kind of food type (ie Indian, Italian)',
  PRIMARY KEY (`food_type_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reference`.`restaurants`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reference`.`restaurants` ;

CREATE TABLE IF NOT EXISTS `reference`.`restaurants` (
  `restaurant_id` VARCHAR(5) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique identifier for restaurant. No special significance is encapsulated in the ID',
  `food_type_id` VARCHAR(4) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'ID of the specific food type. References \"food_type_dim\" table',
  `address` VARCHAR(100) CHARACTER SET 'utf8mb4' NULL COMMENT 'The full address of the restaurant',
  `name` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name of the restaurant',
  `note` VARCHAR(100) CHARACTER SET 'utf8mb4' NULL COMMENT 'Contains any specific notes about the restaurant',
  PRIMARY KEY (`restaurant_id`),
  INDEX `food_type_id_idx` (`food_type_id` ASC) VISIBLE,
  CONSTRAINT `fk_food_type_id`
    FOREIGN KEY (`food_type_id`)
    REFERENCES `reference`.`food_type_dim` (`food_type_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reference`.`gift`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reference`.`gift` ;

CREATE TABLE IF NOT EXISTS `reference`.`gift` (
  `gift_id` VARCHAR(8) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Unique identifier for entry. No specific meaning behind automated value',
  `cost` DOUBLE NULL COMMENT 'Anticipated (or actual) cost of gift',
  `gift_name` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Name or short description of item that needs to be purchased',
  `note` VARCHAR(250) CHARACTER SET 'utf8mb4' NULL COMMENT 'Can hold a text note about the entry',
  `occasion` VARCHAR(30) CHARACTER SET 'utf8mb4' NULL COMMENT 'An optional field where you can list the occasion that the gift would be appropriate for. Doing this because there are some gifts that are good for anniversaries or Valentines day that might not be good for other occasions',
  `store` VARCHAR(50) CHARACTER SET 'utf8mb4' NULL COMMENT 'Store (or online location) where gift can be purchased',
  `whom` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL COMMENT 'Who gift is intended to be given to',
  PRIMARY KEY (`gift_id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
