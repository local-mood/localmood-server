USE localmood_db;

CREATE TABLE `member` (
    `id` BIGINT NOT NULL,

    `nickname` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `profile_img_url` TEXT NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`)
);

CREATE TABLE `curation` (
    `id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,

    `title` VARCHAR(50) NOT NULL,
    `keyword` VARCHAR(255) NOT NULL,
    `privacy` BOOLEAN NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES `member`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `scrap` (
    `id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,
    `curation_id` BIGINT NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES `member`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`curation_id`) REFERENCES `curation`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `space` (
    `id` BIGINT NOT NULL,

    `name` VARCHAR(255) NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `sub_type` VARCHAR(50) NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`)
);

CREATE TABLE `space_info` (
    `id` BIGINT NOT NULL,
    `space_id` BIGINT NOT NULL,

    `purpose` VARCHAR(255) NOT NULL,
    `mood` VARCHAR(255) NOT NULL,
    `music` VARCHAR(255) NOT NULL,
    `interior` VARCHAR(255) NOT NULL,
    `visitor` VARCHAR(255) NOT NULL,
    `opt_serv` VARCHAR(255) NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`space_id`) REFERENCES `space`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `space_menu` (
    `id` BIGINT NOT NULL,
    `space_id` BIGINT NOT NULL,

    `dish` VARCHAR(50) NOT NULL,
    `dish_desc` VARCHAR(255) NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`space_id`) REFERENCES `space`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `curation_space` (
    `id` BIGINT NOT NULL,
    `curation_id` BIGINT NOT NULL,
    `space_id` BIGINT NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`curation_id`) REFERENCES `curation`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`space_id`) REFERENCES `space`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `review` (
    `id` BIGINT NOT NULL,
    `space_id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,

    `purpose` VARCHAR(255) NOT NULL,
    `mood` VARCHAR(255) NOT NULL,
    `music` VARCHAR(255) NOT NULL,
    `interior` VARCHAR(255) NOT NULL,
    `positive_eval` VARCHAR(255) NOT NULL,
    `negative_eval` VARCHAR(255) NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`space_id`) REFERENCES `space`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`member_id`) REFERENCES `member`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `review_img` (
    `id` BIGINT NOT NULL,
    `review_id` BIGINT NOT NULL,
    `space_id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,

    `img_url` TEXT NOT NULL,

    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`review_id`) REFERENCES `review`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`space_id`) REFERENCES `space`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`member_id`) REFERENCES `member`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
