set foreign_key_checks = 0;

drop table if exists team_members;
drop table if exists capability_skills;
drop table if exists answer;
drop table if exists user_attempt;
drop table if exists skills;
drop table if exists resources;
drop table if exists quiz_questions;
drop table if exists capabilities;
drop table if exists quiz;
drop table if exists users;
drop table if exists outcomes;
drop table if exists teams;

set foreign_key_checks = 1;

create table if not exists skills (
    skill_id bigint auto_increment primary key,
    name varchar(128)
) engine=InnoDB;

create table if not exists outcomes
(
    outcome_id bigint primary key auto_increment,
    title varchar(128),
    disabled boolean default false
) engine = InnoDB;

create table if not exists capabilities (
    capability_id bigint primary key auto_increment,
    title varchar(128),
    description TEXT,
    outcome_id bigint,
    colour TEXT,
    foreign key (outcome_id) references outcomes(outcome_id)
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

create table if not exists users (
                                     user_id bigint auto_increment primary key ,
                                     email varchar(255) not null unique ,
                                     password varchar(255) not null ,
                                     name varchar(255),
                                     phone varchar(50),
                                     jobrole varchar(100)
);

create table if not exists quiz
(
    quiz_id        bigint auto_increment primary key,
    name           varchar(128),
    description    text,
    time_estimate  int,
    outcome_id bigint,
    foreign key (outcome_id) references outcomes(outcome_id)
) engine = InnoDB;

create table if not exists quiz_questions
(
    question_id    bigint auto_increment primary key,
    quiz_id        bigint,
    capability_id  bigint,
    text           TEXT,
    disabled smallint default 0,
    foreign key (quiz_id) references quiz(quiz_id),
    foreign key (capability_id) references capabilities(capability_id)
) engine = InnoDB;

create table if not exists user_attempt
(
    user_attempt_id     bigint auto_increment primary key,
    quiz_id             bigint,
    user_id             bigint,
    attempt             int,
    complete            int,
    foreign key (quiz_id) references quiz(quiz_id),
    foreign key (user_id) references users(user_id)
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

create table if not exists teams
(
    team_id bigint primary key auto_increment,
    team_name varchar(255),
    team_description TEXT,
    join_code long unique
) engine = InnoDB;

create table if not exists team_members
(
    team_id bigint,
    user_id bigint,
    is_manager boolean default false,
    primary key (team_id, user_id),
    foreign key (team_id) references teams(team_id),
    foreign key (user_id) references users(user_id)
) engine = InnoDB;
