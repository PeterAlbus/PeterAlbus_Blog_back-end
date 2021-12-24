CREATE SCHEMA `blogs` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use blogs;
CREATE TABLE `blogs`.`blog` (
  `blog_id` INT NOT NULL AUTO_INCREMENT,
  `blog_title` VARCHAR(50) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `blogImg` VARCHAR(100) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `blogType` INT NOT NULL,
  `blogDescription` VARCHAR(200) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `blogAuthor` VARCHAR(50) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `blogContent` TEXT CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
  `blogTime` DATE NOT NULL,
  `blogLike` INT NOT NULL,
  `blogViews` INT NOT NULL,
  `isTop` INT NOT NULL,
  PRIMARY KEY (`blog_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;
