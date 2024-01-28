alter table member
    add role enum ('ROLE_USER', 'ROLE_ADMIN') null after profile_img_url;
