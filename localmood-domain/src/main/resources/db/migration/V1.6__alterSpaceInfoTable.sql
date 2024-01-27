alter table space_info
    add positive_eval VARCHAR(255) null after opt_serv;

alter table space_info
    add negative_eval VARCHAR(255) null after positive_eval;