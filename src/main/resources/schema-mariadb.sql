set foreign_key_checks = 0;

drop table if exists capability_skills;
drop table if exists user_attempt;
drop table if exists answer;
drop table if exists skills;
drop table if exists resources;
drop table if exists capabilities;
drop table if exists quiz_questions;
drop table if exists quiz;
drop table if exists user_info;
drop table if exists outcome; -- Added drop for the new table

set foreign_key_checks = 1;

create table if not exists skills (
    skill_id bigint auto_increment primary key,
    name varchar(128)
) engine=InnoDB;

create table if not exists capabilities (
    capability_id bigint primary key auto_increment,
    title varchar(128),
    description TEXT
) engine = InnoDB;

create table if not exists resources (
    resource_id bigint primary key auto_increment,
    content varchar(255),
    difficulty ENUM('High', 'Medium','Low'),
    capability_id bigint,
    foreign key (capability_id) references capabilities(capability_id)
) engine = InnoDB;

create table if not exists capability_skills (
    capability_id bigint,
    skill_id bigint,
    primary key (capability_id, skill_id),
    foreign key (capability_id) references capabilities(capability_id),
    foreign key (skill_id) references skills(skill_id)
) engine = InnoDB;

create table if not exists user_info
(
    user_id      bigint auto_increment primary key
) engine = InnoDB;

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
    capability_id  bigint,
    text           TEXT,
    foreign key (quiz_id) references quiz(quiz_id),
    foreign key (capability_id) references capabilities(capability_id)
) engine = InnoDB;

create table if not exists user_attempt
(
    user_attempt_id     bigint auto_increment primary key,
    user_id             bigint,
    attempt             int,
    complete            int,
    foreign key (user_id) references user_info(user_id)
    ) engine = InnoDB;

create table if not exists answer
(
    question_id         bigint,
    user_attempt_id     bigint,
    score               int,
    primary key (question_id, user_attempt_id),
    foreign key (question_id) references quiz_questions(question_id),
    foreign key (user_attempt_id) references user_attempt(user_attempt_id)

    ) engine = InnoDB;

create table if not exists outcomes
(
    outcome_id bigint auto_increment primary key,
    title varchar(128)
) engine = InnoDB;
