drop table if exists capability_skills;
drop table if exists resources;
drop table if exists capabilities;
drop table if exists quiz_questions;
drop table if exists user_answers;
drop table if exists users;
drop table if exists quiz;
drop table if exists skills;

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
    skill_id       bigint,
    foreign key (quiz_id) references quiz(quiz_id),
    foreign key (skill_id) references skills(skill_id)
) engine = InnoDB;

create table if not exists users
(
    user_id      bigint auto_increment primary key
) engine = InnoDB;

create table if not exists user_answers
(
    quiz_id             bigint,
    user_id             bigint,
    attempt_number      int,
    answer_json         text,
    primary key (quiz_id, user_id, attempt_number),
    foreign key (quiz_id) references quiz(quiz_id),
    foreign key (user_id) references users(user_id)
) engine = InnoDB;

create table if not exists capabilities (
    capability_id bigint primary key auto_increment,
    title varchar(128),
    description TEXT
) engine = InnoDB;

create table if not exists resources (
    resource_id bigint primary key auto_increment,
    content TEXT,
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