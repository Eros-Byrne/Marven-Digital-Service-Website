drop table if exists skills;

create table if not exists skills (
    id bigint auto_increment primary key,
    name varchar(128)
) engine=InnoDB;