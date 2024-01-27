alter table space_info
    add thumbnail_img_url VARCHAR(255) null after opt_serv;

create unique index space_info_thumbnail_img_url_uindex
    on space_info (thumbnail_img_url);