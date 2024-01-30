SET FOREIGN_KEY_CHECKS = 0;

alter table curation
    modify id bigint auto_increment;

alter table curation_space
    modify id bigint auto_increment;

alter table member
    modify id bigint auto_increment;

alter table review
    modify id bigint auto_increment;

alter table review_img
    modify id bigint auto_increment;

alter table scrap
    modify id bigint auto_increment;

alter table space
    modify id bigint auto_increment;

alter table space_info
    modify id bigint auto_increment;

alter table space_menu
    modify id bigint auto_increment;

SET FOREIGN_KEY_CHECKS = 1;