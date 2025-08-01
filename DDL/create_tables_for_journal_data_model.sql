-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema journal
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema journal
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `journal` DEFAULT CHARACTER SET utf8 ;
USE `journal` ;

-- -----------------------------------------------------
-- Table `journal`.`journal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `journal`.`journal` ;

CREATE TABLE IF NOT EXISTS `journal`.`journal` (
  `entry_dt` DATE NOT NULL,
  `filename` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`entry_dt`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
