drop table if exists skills;
drop table if exists quiz;
drop table if exists quiz_questions;
drop table if exists user_answers;
drop table if exists users;

create table if not exists skills (
    skill_id bigint auto_increment primary key,
    name varchar(128)
) engine=InnoDB;

create table if not exists quiz
(
    quiz_id        bigint auto_increment primary key,
    name           varchar(128),
    description    text,
    time_estimate  int
) engine = InnoDB;

create table if not exists quiz_questions
(
    question_id    bigint auto_increment primary key,
    quiz_id        bigint,
    title          varchar(128),
    text           TEXT,
    skill_id       bigint
) engine = InnoDB;

create table if not exists user_answers
(
    quiz_id             bigint,
    user_id             bigint,
    attempt_number      int,
    answer_json         text,
    primary key (quiz_id, user_id, attempt_number)
) engine = InnoDB;

create table if not exists users
(
    user_id      bigint auto_increment primary key
) engine = InnoDB;

