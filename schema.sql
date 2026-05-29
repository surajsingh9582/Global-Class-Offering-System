CREATE DATABASE booking_system;

CREATE TABLE IF NOT EXISTS `courses` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `teachers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NULL,
    `email` VARCHAR(255) NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `parent` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NULL,
    `email` VARCHAR(255) NULL
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `offering` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `course_id` BIGINT NULL,
    `teacher` BIGINT NOT NULL,
    `capacity` INT DEFAULT 30,
    `booked_seats` INT DEFAULT 0,
    `version` INT NULL,
    CONSTRAINT `fk_offering_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
    CONSTRAINT `fk_offering_teacher` FOREIGN KEY (`teacher`) REFERENCES `teachers` (`id`)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `session` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `offering_id` BIGINT NULL,
    `start_time` DATETIME(6) NULL,
    `end_time` DATETIME(6) NULL,
    CONSTRAINT `fk_session_offering` FOREIGN KEY (`offering_id`) REFERENCES `offering` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `booking` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `parent_id` BIGINT NULL,
    `offering_id` BIGINT NULL,
    `created_at` DATETIME(6) NOT NULL,
    CONSTRAINT `fk_booking_parent` FOREIGN KEY (`parent_id`) REFERENCES `parent` (`id`),
    CONSTRAINT `fk_booking_offering` FOREIGN KEY (`offering_id`) REFERENCES `offering` (`id`)
) ENGINE=InnoDB;